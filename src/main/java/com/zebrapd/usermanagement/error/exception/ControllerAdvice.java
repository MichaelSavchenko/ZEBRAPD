package com.zebrapd.usermanagement.error.exception;

import com.zebrapd.usermanagement.dto.ValidationExceptionDto;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ValidationException.class)
    public ValidationExceptionDto handleValidationException(ValidationException e){
        return new ValidationExceptionDto(e.getMessage());
    }

}
