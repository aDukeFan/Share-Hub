package ru.luckyskeet.sharehub.booking;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.luckyskeet.sharehub.booking.dto.BookerDtoForOwnerItem;
import ru.luckyskeet.sharehub.booking.dto.BookingDtoIncome;
import ru.luckyskeet.sharehub.booking.dto.BookingDtoOutcomeLong;
import ru.luckyskeet.sharehub.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking toSave(BookingDtoIncome income);

    BookingDtoOutcomeLong toSendLong(Booking booking);

    @Mapping(target = "bookerId", source = "booker.id")
    @Mapping(target = "bookerName", source = "booker.name")
    BookerDtoForOwnerItem toOwnerItemShow(Booking booking);
}
