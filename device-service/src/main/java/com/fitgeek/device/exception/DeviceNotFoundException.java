package com.fitgeek.device.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DeviceNotFoundException extends RuntimeException {

    public DeviceNotFoundException(String message) {
        super(message);
    }

}
