package ru.practicum.huuudrich.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.huuudrich.model.error.ApiError;
import ru.practicum.huuudrich.utils.exception.BadRequestException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("Validation error: " + e.getMessage());
        ApiError apiError = new ApiError();
        e.getBindingResult().getFieldErrors().forEach(fieldError ->
                apiError.setMessage(String.format("Field: %s. Error: %s. Value: %s",
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue())));
        apiError.setReason("For the requested operation the conditions are not met.");
        apiError.setStatus(HttpStatus.FORBIDDEN.toString());
        apiError.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.warn("Data integrity violation: " + e.getMessage());
        String reason = "Data integrity violation.";
        String message = e.getLocalizedMessage();
        ApiError apiError = ApiError.builder()
                .message(message)
                .reason(reason)
                .status(String.valueOf(HttpStatus.CONFLICT))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("EntityNotFoundException: " + e.getMessage());
        ApiError apiError = new ApiError();
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setReason("The required object was not found.");
        apiError.setStatus(HttpStatus.NOT_FOUND.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException e) {
        log.warn("BadRequestException: " + e.getMessage());
        ApiError apiError = new ApiError();
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setReason("Incorrectly made request.");
        apiError.setStatus(HttpStatus.BAD_REQUEST.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}
