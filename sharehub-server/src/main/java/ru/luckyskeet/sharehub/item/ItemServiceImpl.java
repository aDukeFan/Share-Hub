package ru.luckyskeet.sharehub.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.luckyskeet.sharehub.booking.BookingMapper;
import ru.luckyskeet.sharehub.booking.BookingRepository;
import ru.luckyskeet.sharehub.booking.model.Booking;
import ru.luckyskeet.sharehub.booking.model.BookingStatus;
import ru.luckyskeet.sharehub.exception.BadRequestException;
import ru.luckyskeet.sharehub.exception.NotFoundException;
import ru.luckyskeet.sharehub.item.dto.CommentDtoIncome;
import ru.luckyskeet.sharehub.item.dto.CommentDtoOutcome;
import ru.luckyskeet.sharehub.item.dto.ItemDtoIncome;
import ru.luckyskeet.sharehub.item.dto.ItemDtoOutcomeLong;
import ru.luckyskeet.sharehub.item.dto.ItemDtoOutcomeAvailableRequest;
import ru.luckyskeet.sharehub.item.model.Comment;
import ru.luckyskeet.sharehub.item.model.Item;
import ru.luckyskeet.sharehub.request.RequestRepository;
import ru.luckyskeet.sharehub.request.model.Request;
import ru.luckyskeet.sharehub.user.UserRepository;
import ru.luckyskeet.sharehub.user.model.User;
import ru.luckyskeet.sharehub.util.Constants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private RequestRepository requestRepository;
    private BookingRepository bookingRepository;
    private CommentMapper commentMapper;
    private ItemMapper itemMapper;
    private BookingMapper bookingMapper;


    @Override
    public ItemDtoOutcomeAvailableRequest create(long userId, ItemDtoIncome itemDtoIncome) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId);
        }
        User owner = optionalUser.get();
        Long requestId = itemDtoIncome.getRequestId();
        if (requestId != null) {
            Optional<Request> requestOptional = requestRepository.findById(requestId);
            if (requestOptional.isEmpty()) {
                throw new NotFoundException("No request with ID " + requestId);
            }
        }
        Item item = itemMapper.toSave(itemDtoIncome)
                .setOwner(owner);
        return itemMapper.toSend(itemRepository.save(item));
    }

    @Override
    public ItemDtoOutcomeAvailableRequest update(long id, long userId, ItemDtoIncome itemDto) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + id);
        }
        if (optionalItem.get().getOwner().getId() != userId) {
            throw new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId);
        }
        Item itemToSave = itemMapper.updateItemFromDto(itemDto, optionalItem.get()).setId(id);
        return itemMapper.toSend(itemRepository.save(itemToSave));
    }

    @Override
    public List<ItemDtoOutcomeLong> getAllItemsByOwner(long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Item> ownerItems = itemRepository.findByOwnerId(userId, pageable);
        List<ItemDtoOutcomeLong> result = new ArrayList<>();
        ownerItems.forEach(item -> result.add(getItemById(item.getId(), item.getOwner().getId())));
        result.sort(Comparator.comparing(ItemDtoOutcomeLong::getId));
        return result;
    }

    @Override
    public List<ItemDtoOutcomeAvailableRequest> findByQuery(String text, Integer from, Integer size) {
        if (text.isEmpty()) {
            return List.of();
        } else {
            Pageable pageable = PageRequest.of(from / size, size);
            return itemRepository
                    .findAllByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableTrue(text, text, pageable)
                    .stream()
                    .map(item -> itemMapper.toSend(item))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ItemDtoOutcomeLong getItemById(long itemId, long userId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + itemId);
        }
        Item item = optionalItem.get();
        ItemDtoOutcomeLong itemDtoOutcomeLong = itemMapper.toGetById(item);
        List<CommentDtoOutcome> comments = commentRepository.findAllByItemId(itemId).stream()
                .map(comment -> commentMapper.toSend(comment))
                .collect(Collectors.toList());
        itemDtoOutcomeLong.getComments().addAll(comments);
        if (item.getOwner().getId() == userId) {
            List<Booking> bookings = bookingRepository.findAllByItemId(itemId);
            Booking nextBooking = bookings.stream()
                    .sorted(Comparator.comparing(Booking::getStart))
                    .filter(booking -> booking.getStatus() != BookingStatus.REJECTED)
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                    .findFirst().orElse(null);
            Booking lastBooking = bookings.stream()
                    .sorted(Comparator.comparing(Booking::getStart).reversed())
                    .filter(booking -> booking.getStatus() != BookingStatus.REJECTED)
                    .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                    .findFirst().orElse(null);
            itemDtoOutcomeLong
                    .setLastBooking(bookingMapper.toOwnerItemShow(lastBooking))
                    .setNextBooking(bookingMapper.toOwnerItemShow(nextBooking));
        }
        return itemDtoOutcomeLong;
    }

    @Override
    public CommentDtoOutcome addComment(long userId, long itemId, CommentDtoIncome commentDtoIncome) {
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndItemIdAndStartIsBeforeAndStatusIsOrStatusIs(userId, itemId,
                        LocalDateTime.now().withNano(0),
                        BookingStatus.APPROVED,
                        BookingStatus.CANCELED);

        if (bookings.isEmpty()) {
            throw new BadRequestException("No bookings");
        }

        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + itemId);
        }
        Item itemToComment = optionalItem.get();

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId);
        }
        User booker = optionalUser.get();

        Comment comment = commentMapper.toSave(commentDtoIncome)
                .setAuthor(booker)
                .setItem(itemToComment);
        return commentMapper.toSend(commentRepository.save(comment));
    }
}
