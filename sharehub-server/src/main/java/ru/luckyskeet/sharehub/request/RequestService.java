package ru.luckyskeet.sharehub.request;

import ru.luckyskeet.sharehub.request.dto.RequestDtoIncome;
import ru.luckyskeet.sharehub.request.dto.RequestDtoOutcome;
import ru.luckyskeet.sharehub.request.dto.RequestDtoWithItemList;

import java.util.List;

public interface RequestService {
    RequestDtoOutcome create(long bookerId, RequestDtoIncome income);

    List<RequestDtoWithItemList> getAllByRequesterId(long requesterId);

    RequestDtoWithItemList getByRequestId(long userId, long requestId);

    List<RequestDtoWithItemList> getAllWithParams(long userId, Integer from, Integer size);
}
