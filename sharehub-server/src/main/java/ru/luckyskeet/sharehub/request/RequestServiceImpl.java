package ru.luckyskeet.sharehub.request;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.luckyskeet.sharehub.exception.NotFoundException;
import ru.luckyskeet.sharehub.item.ItemMapper;
import ru.luckyskeet.sharehub.item.ItemRepository;
import ru.luckyskeet.sharehub.item.dto.ItemDtoOutcomeAvailableRequest;
import ru.luckyskeet.sharehub.item.model.Item;
import ru.luckyskeet.sharehub.request.dto.RequestDtoIncome;
import ru.luckyskeet.sharehub.request.dto.RequestDtoOutcome;
import ru.luckyskeet.sharehub.request.dto.RequestDtoWithItemList;
import ru.luckyskeet.sharehub.request.model.Request;
import ru.luckyskeet.sharehub.user.UserRepository;
import ru.luckyskeet.sharehub.user.model.User;
import ru.luckyskeet.sharehub.util.Constants;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private RequestRepository requestRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private RequestMapper requestMapper;
    private ItemMapper itemMapper;

    @Override
    public RequestDtoOutcome create(long requesterId, RequestDtoIncome income) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + requesterId));
        Request request = requestMapper.toSave(income).setRequester(requester);
        return requestMapper.toSend(requestRepository.save(request));
    }

    @Override
    public List<RequestDtoWithItemList> getAllByRequesterId(long requesterId) {
        userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + requesterId));
        List<Request> requests = requestRepository.findAllByRequesterId(requesterId);
        if (requests.isEmpty()) {
            return List.of();
        }
        List<RequestDtoWithItemList> requestDtoWithItemLists = requests.stream()
                .map(request -> requestMapper.toSendAll(request))
                .sorted(Comparator.comparing(RequestDtoWithItemList::getCreated).reversed())
                .collect(Collectors.toList());
        requestDtoWithItemLists.forEach(this::setItemsDto);
        return requestDtoWithItemLists;

    }

    @Override
    public RequestDtoWithItemList getByRequestId(long userId, long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + userId));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("No request with Id " + requestId));
        RequestDtoWithItemList requestDtoWithItemList = requestMapper.toSendAll(request);
        return setItemsDto(requestDtoWithItemList);
    }

    @Override
    public List<RequestDtoWithItemList> getAllWithParams(long userId, Integer from, Integer size) {
        if (from == null || size == null) {
            return List.of();
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + userId));
        Pageable pageable = PageRequest.of(from / size, size, Sort.Direction.DESC, "created");
        return requestRepository.findAll(pageable).stream()
                .filter(request -> request.getRequester().getId() != userId)
                .map(request -> requestMapper.toSendAll(request)).map(this::setItemsDto)
                .collect(Collectors.toList());
    }

    private RequestDtoWithItemList setItemsDto(RequestDtoWithItemList requestDtoWithItemList) {
        List<Item> items = itemRepository.findByRequestId(requestDtoWithItemList.getId());
        List<ItemDtoOutcomeAvailableRequest> itemDtoForRequests = items.stream()
                .filter(Item::getAvailable)
                .sorted(Comparator.comparing(Item::getId))
                .map(item -> itemMapper.toSend(item))
                .collect(Collectors.toList());
        requestDtoWithItemList.getItems().addAll(itemDtoForRequests);
        return requestDtoWithItemList;
    }
}
