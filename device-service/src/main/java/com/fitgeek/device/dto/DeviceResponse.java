package com.fitgeek.device.dto;

import com.fitgeek.device.entity.enums.DeviceStatus;
import com.fitgeek.device.entity.enums.DeviceType;

import java.time.Instant;
import java.util.UUID;

public record DeviceResponse(
        UUID id,
        String name,
        DeviceType type,
        DeviceStatus status,
        String location,
        String firmwareVersion,
        Instant createdAt
) {}
