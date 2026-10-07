package com.isslab.se_form_backend.helper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSaveGradeException extends RuntimeException {
    public InvalidSaveGradeException(String message) {
        super(message);
    }
}
