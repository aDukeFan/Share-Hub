package ru.luckyskeet.sharehub.booking.dto;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.luckyskeet.sharehub.booking.model.BookingStatus;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Setter
@Getter
@EqualsAndHashCode
public class BookerDtoForOwnerItem {

    long id;
    long bookerId;
    String bookerName;
    LocalDateTime start;
    LocalDateTime end;
    BookingStatus status;
}
