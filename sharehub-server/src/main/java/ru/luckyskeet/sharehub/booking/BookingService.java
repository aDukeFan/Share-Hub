package ru.luckyskeet.sharehub.booking;

import ru.luckyskeet.sharehub.booking.dto.BookingDtoIncome;
import ru.luckyskeet.sharehub.booking.dto.BookingDtoOutcomeLong;
import ru.luckyskeet.sharehub.booking.booking_getter.model.BookingGetter;

import java.util.List;

public interface BookingService {

    BookingDtoOutcomeLong createBooking(long clientId, BookingDtoIncome bookingDto);

    BookingDtoOutcomeLong approveBooking(long ownerId, long bookingId, Boolean approved);

    BookingDtoOutcomeLong getBooking(long ownerOrClientId, long bookingId);

    List<BookingDtoOutcomeLong> getAllBookingsById(BookingGetter request);
}
