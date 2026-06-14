package com.fitgeek.monitoring.dto;

import java.time.Instant;
import java.util.UUID;

public record SensorReadingResponse(
        UUID id,
        UUID deviceId,
        Double temperature,
        Double humidity,
        Double batteryLevel,
        Instant occurredAt
) {}
