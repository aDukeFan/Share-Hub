package ru.luckyskeet.sharehub.request;


import org.mapstruct.Mapper;
import ru.luckyskeet.sharehub.request.dto.RequestDtoIncome;
import ru.luckyskeet.sharehub.request.dto.RequestDtoOutcome;
import ru.luckyskeet.sharehub.request.dto.RequestDtoWithItemList;
import ru.luckyskeet.sharehub.request.model.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    Request toSave(RequestDtoIncome income);

    RequestDtoOutcome toSend(Request request);

    RequestDtoWithItemList toSendAll(Request request);
}
