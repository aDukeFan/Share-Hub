package ru.luckyskeet.sharehub.booking.dto;


import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.luckyskeet.sharehub.item.dto.ItemDtoOutcomeShort;
import ru.luckyskeet.sharehub.user.dto.UserDto;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Setter
@Getter
@EqualsAndHashCode
public class BookingDtoOutcomeLong {

    long id;
    UserDto booker;
    ItemDtoOutcomeShort item;
    LocalDateTime start;
    LocalDateTime end;
    BookingStatus status;

}
