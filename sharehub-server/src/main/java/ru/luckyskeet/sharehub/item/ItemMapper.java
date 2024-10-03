package ru.luckyskeet.sharehub.item;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.luckyskeet.sharehub.item.dto.ItemDtoIncome;
import ru.luckyskeet.sharehub.item.dto.ItemDtoOutcomeAvailableRequest;
import ru.luckyskeet.sharehub.item.dto.ItemDtoOutcomeLong;
import ru.luckyskeet.sharehub.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    Item toSave(ItemDtoIncome itemDtoIncome);

    ItemDtoOutcomeAvailableRequest toSend(Item item);

    ItemDtoOutcomeLong toGetById(Item item);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    Item updateItemFromDto(ItemDtoIncome itemDto, @MappingTarget Item item);
}
