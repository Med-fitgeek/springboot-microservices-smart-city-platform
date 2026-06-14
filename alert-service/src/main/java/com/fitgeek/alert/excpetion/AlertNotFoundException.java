package com.fitgeek.alert.excpetion;

import lombok.Getter;

@Getter
public class AlertNotFoundException extends RuntimeException {

    public AlertNotFoundException(String message) {
        super(message);
    }

}
