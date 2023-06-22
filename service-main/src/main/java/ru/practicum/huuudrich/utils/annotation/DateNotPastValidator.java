package ru.practicum.huuudrich.utils.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateNotPastValidator implements ConstraintValidator<DateNotPast, LocalDateTime> {

    @Override
    public void initialize(DateNotPast constraint) {
    }

    @Override
    public boolean isValid(LocalDateTime date, ConstraintValidatorContext context) {
        LocalDateTime nowPlusTwo = LocalDateTime.now().plusHours(2);
        if (date == null) {
            return true;
        }
        return !date.isBefore(nowPlusTwo);
    }
}

