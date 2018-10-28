package com.zebrapd.usermanagement.error.exception;

import com.zebrapd.usermanagement.dto.ValidationExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionDto handleValidationException(ValidationException e) {
        return new ValidationExceptionDto(e.getMessage());
    }

    @ExceptionHandler(CanNotRemoveClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionDto handleCanNotRemoveClientException(CanNotRemoveClientException e) {
        return new ValidationExceptionDto(e.getMessage());
    }

}
