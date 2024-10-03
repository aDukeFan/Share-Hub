package ru.luckyskeet.sharehub.booking.dto.validavor;

import org.springframework.context.annotation.Configuration;
import ru.luckyskeet.sharehub.booking.dto.BookingDtoIncome;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Configuration
public class BookingDateValidator implements ConstraintValidator<ValidBookingDate, BookingDtoIncome> {

    @Override
    public boolean isValid(BookingDtoIncome bookingDtoIncome, ConstraintValidatorContext constraintValidatorContext) {
        if (bookingDtoIncome.getStart() != null && bookingDtoIncome.getEnd() != null) {
            return bookingDtoIncome.getStart().isBefore(bookingDtoIncome.getEnd());
        } else {
            return false;
        }
    }
}
