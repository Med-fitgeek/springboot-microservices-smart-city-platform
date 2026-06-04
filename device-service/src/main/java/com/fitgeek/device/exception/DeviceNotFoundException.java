package com.fitgeek.device.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DeviceNotFoundException extends RuntimeException {

    private final HttpStatus status;

    public DeviceNotFoundException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}
