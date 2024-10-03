package ru.luckyskeet.sharehub.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.luckyskeet.sharehub.item.dto.CommentDtoIncome;
import ru.luckyskeet.sharehub.item.dto.CommentDtoOutcome;
import ru.luckyskeet.sharehub.item.dto.ItemDtoIncome;
import ru.luckyskeet.sharehub.item.dto.ItemDtoOutcomeAvailableRequest;
import ru.luckyskeet.sharehub.item.dto.ItemDtoOutcomeLong;
import ru.luckyskeet.sharehub.util.Constants;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private ItemService service;

    @PostMapping
    public ItemDtoOutcomeAvailableRequest create(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                                 @RequestBody ItemDtoIncome itemDtoIncome) {
        return service.create(userId, itemDtoIncome);
    }

    @PatchMapping("/{id}")
    public ItemDtoOutcomeAvailableRequest update(@PathVariable long id,
                                                 @RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                                 @RequestBody ItemDtoIncome itemDto) {
        return service.update(id, userId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemDtoOutcomeLong getItemById(@PathVariable long id,
                                          @RequestHeader(Constants.X_SHARER_USER_ID) long userId) {
        return service.getItemById(id, userId);
    }

    @GetMapping
    public List<ItemDtoOutcomeLong> getAllByOwner(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "100") @Min(1) Integer size) {
        return service.getAllItemsByOwner(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDtoOutcomeAvailableRequest> findByQuery(@RequestParam String text,
                                                            @RequestParam(required = false, defaultValue = "0") Integer from,
                                                            @RequestParam(required = false, defaultValue = "100") Integer size) {
        return service.findByQuery(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOutcome addComment(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                        @PathVariable long itemId,
                                        @RequestBody CommentDtoIncome commentDtoIncome) {
        return service.addComment(userId, itemId, commentDtoIncome);
    }
}
