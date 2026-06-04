package com.fitgeek.device.dto;

import com.fitgeek.device.entity.DeviceStatus;
import com.fitgeek.device.entity.DeviceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

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
