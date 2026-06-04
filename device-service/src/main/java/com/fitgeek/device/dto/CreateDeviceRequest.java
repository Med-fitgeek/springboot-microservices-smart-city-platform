package com.fitgeek.device.dto;

import com.fitgeek.device.entity.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDeviceRequest(

        @NotBlank(message = "Device name is reuired")
        String name,

        @NotNull(message = "Device type is required")
        DeviceType type,

        @NotBlank(message = "The location is required")
        String location,

        @NotBlank(message = "Firmware version is required")
        String firmwareVersion
) {}