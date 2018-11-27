package com.enfiny.tokengenerator.manager.tokengeneratormanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidValueException extends RuntimeException {

    public InvalidValueException(String message) {
        super(message);
    }
}
