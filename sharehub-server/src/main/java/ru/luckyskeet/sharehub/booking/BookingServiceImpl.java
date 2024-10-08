package ru.luckyskeet.sharehub.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.luckyskeet.sharehub.booking.booking_getter.model.BookingGetter;
import ru.luckyskeet.sharehub.booking.booking_getter.model.BookingGetterState;
import ru.luckyskeet.sharehub.booking.booking_getter.model.BookingGetterType;
import ru.luckyskeet.sharehub.booking.dto.BookingDtoIncome;
import ru.luckyskeet.sharehub.booking.dto.BookingDtoOutcomeLong;
import ru.luckyskeet.sharehub.booking.model.Booking;
import ru.luckyskeet.sharehub.booking.model.BookingStatus;
import ru.luckyskeet.sharehub.exception.BadRequestException;
import ru.luckyskeet.sharehub.exception.BookingTimeException;
import ru.luckyskeet.sharehub.exception.NotFoundException;
import ru.luckyskeet.sharehub.item.ItemRepository;
import ru.luckyskeet.sharehub.item.model.Item;
import ru.luckyskeet.sharehub.user.UserRepository;
import ru.luckyskeet.sharehub.user.model.User;
import ru.luckyskeet.sharehub.util.Constants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;
    private BookingMapper bookingMapper;

    @Override
    public BookingDtoOutcomeLong createBooking(long bookerId, BookingDtoIncome bookingDtoIncome) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + bookerId));
        Long itemId = bookingDtoIncome.getItemId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + itemId));
        if (!item.getAvailable()) {
            throw new BookingTimeException("item is unavailable");
        }
        if (item.getOwner().getId() == bookerId) {
            throw new NotFoundException("Owner can't booking item");
        }

        Booking booking = bookingMapper.toSave(bookingDtoIncome)
                .setItem(item)
                .setBooker(booker);
        return bookingMapper.toSendLong(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOutcomeLong approveBooking(long ownerId, long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_BOOKING_WITH_SUCH_ID + bookingId));
        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + ownerId);
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new BadRequestException("Booking is already APPROVED");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingMapper.toSendLong(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOutcomeLong getBooking(long ownerOrClientId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_BOOKING_WITH_SUCH_ID + bookingId));
        if (booking.getBooker().getId() == ownerOrClientId
                || booking.getItem().getOwner().getId() == ownerOrClientId) {
            return bookingMapper.toSendLong(booking);
        } else {
            throw new NotFoundException("Bad ID: " + ownerOrClientId);
        }
    }

    @Override
    public List<BookingDtoOutcomeLong> getAllBookingsById(BookingGetter getter) {
        long userId = getter.getUserId();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + userId);
        }
        String state = getter.getState();
        Integer from = getter.getFrom();
        Integer size = getter.getSize();
        Pageable pageable = PageRequest.of(from / size, size);
        BookingGetterType type = getter.getType();
        if (type == BookingGetterType.BOOKER) {
            return findAllByBookerWithState(userId, pageable, getter.getState())
                    .stream()
                    .map(booking -> bookingMapper.toSendLong(booking))
                    .collect(Collectors.toList());
        } else if (type == BookingGetterType.OWNER) {
            return findAllByOwnerWithState(userId, pageable, state).stream()
                    .map(booking -> bookingMapper.toSendLong(booking))
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    private List<Booking> findAllByOwnerWithState(long userId, Pageable pageable, String state) {
        switch (BookingGetterState.valueOf(state)) {
            case ALL:
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId, pageable);
            case CURRENT:
                return bookingRepository
                        .findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                                LocalDateTime.now().withNano(0),
                                LocalDateTime.now().withNano(0),
                                pageable);
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(userId,
                        LocalDateTime.now().withNano(0),
                        pageable);
            case FUTURE:
                return bookingRepository
                        .findAllByItemOwnerIdAndStartIsAfterAndStatusIsOrStatusIsOrderByStartDesc(userId,
                                LocalDateTime.now().withNano(0),
                                BookingStatus.APPROVED,
                                BookingStatus.WAITING,
                                pageable);
            case WAITING:
                return bookingRepository
                        .findAllByItemOwnerIdAndStatusIsOrderByStartDesc(userId,
                                BookingStatus.WAITING,
                                pageable);
            case REJECTED:
                return bookingRepository
                        .findAllByItemOwnerIdAndStatusIsOrderByStartDesc(userId,
                                BookingStatus.REJECTED,
                                pageable);
            default:
                return List.of();
        }
    }

    private List<Booking> findAllByBookerWithState(long userId, Pageable pageable, String state) {
        switch (BookingGetterState.valueOf(state)) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId, pageable);
            case CURRENT:
                return bookingRepository
                        .findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                                LocalDateTime.now().withNano(0),
                                LocalDateTime.now().withNano(0),
                                pageable);
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(userId,
                        LocalDateTime.now().withNano(0),
                        pageable);
            case FUTURE:
                return bookingRepository
                        .findAllByBookerIdAndStartIsAfterAndStatusIsOrStatusIsOrderByStartDesc(userId,
                                LocalDateTime.now().withNano(0),
                                BookingStatus.APPROVED,
                                BookingStatus.WAITING,
                                pageable);
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusIsOrderByStartDesc(userId,
                        BookingStatus.WAITING,
                        pageable);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusIsOrderByStartDesc(userId,
                        BookingStatus.REJECTED,
                        pageable);
            default:
                return List.of();
        }
    }
}
