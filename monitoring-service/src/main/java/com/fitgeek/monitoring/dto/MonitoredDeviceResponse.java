package com.fitgeek.monitoring.dto;

import java.time.Instant;
import java.util.UUID;

public record MonitoredDeviceResponse(
        UUID id,
        UUID deviceId,
        String status,
        Instant createdAt
){}
