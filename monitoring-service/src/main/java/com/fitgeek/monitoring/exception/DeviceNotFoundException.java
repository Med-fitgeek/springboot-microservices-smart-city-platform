package com.fitgeek.monitoring.exception;

import lombok.Getter;

@Getter
public class DeviceNotFoundException extends RuntimeException {

    public DeviceNotFoundException(String message) {
        super(message);
    }

}
