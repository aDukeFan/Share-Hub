package ru.luckyskeet.sharehub.item;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
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

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Validated
public class ItemController {

    private ItemServiceClient client;

    @PostMapping
    public ItemDtoOutcomeAvailableRequest create(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                                 @Valid @RequestBody ItemDtoIncome itemDtoIncome) {
        return client.create(userId, itemDtoIncome);
    }

    @PatchMapping("/{id}")
    public ItemDtoOutcomeAvailableRequest update(@PathVariable long id,
                                                 @RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                                 @RequestBody ItemDtoIncome itemDto) {
        return client.update(id, userId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemDtoOutcomeLong getItemById(@PathVariable long id,
                                          @RequestHeader(Constants.X_SHARER_USER_ID) long userId) {
        return client.getItemById(id, userId);
    }

    @GetMapping
    public List<ItemDtoOutcomeLong> getAllByOwner(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                                  @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(required = false, defaultValue = "100") @Min(1) Integer size) {
        return client.getAllItemsByOwner(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDtoOutcomeAvailableRequest> findByQuery(@RequestParam String text,
                                                            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                            @RequestParam(required = false, defaultValue = "100") @Min(1) Integer size) {
        return client.findByQuery(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOutcome addComment(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                        @PathVariable long itemId,
                                        @Valid @RequestBody CommentDtoIncome commentDtoIncome) {
        return client.addComment(userId, itemId, commentDtoIncome);
    }
}
