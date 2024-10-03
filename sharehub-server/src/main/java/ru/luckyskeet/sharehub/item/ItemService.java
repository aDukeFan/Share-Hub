package ru.luckyskeet.sharehub.item;

import ru.luckyskeet.sharehub.item.dto.CommentDtoIncome;
import ru.luckyskeet.sharehub.item.dto.CommentDtoOutcome;
import ru.luckyskeet.sharehub.item.dto.ItemDtoIncome;
import ru.luckyskeet.sharehub.item.dto.ItemDtoOutcomeLong;
import ru.luckyskeet.sharehub.item.dto.ItemDtoOutcomeAvailableRequest;

import java.util.List;

public interface ItemService {

    ItemDtoOutcomeAvailableRequest create(long userId, ItemDtoIncome itemDtoIncome);

    ItemDtoOutcomeAvailableRequest update(long id, long userId, ItemDtoIncome itemDto);

    ItemDtoOutcomeLong getItemById(long itemId, long userId);

    List<ItemDtoOutcomeLong> getAllItemsByOwner(long userId, Integer from, Integer size);

    List<ItemDtoOutcomeAvailableRequest> findByQuery(String text, Integer from, Integer size);

    CommentDtoOutcome addComment(long userId, long itemId, CommentDtoIncome commentDtoIncome);
}
