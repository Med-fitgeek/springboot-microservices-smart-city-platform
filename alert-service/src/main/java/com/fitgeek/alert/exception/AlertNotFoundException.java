package com.fitgeek.alert.exception;

import lombok.Getter;

@Getter
public class AlertNotFoundException extends RuntimeException {

    public AlertNotFoundException(String message) {
        super(message);
    }

}
