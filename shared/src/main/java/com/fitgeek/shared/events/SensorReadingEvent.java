package com.fitgeek.shared.events;

import java.time.Instant;
import java.util.UUID;

public record SensorReadingEvent(
        UUID eventId,
        UUID deviceId,
        Double temperature,
        Double humidity,
        Double batteryLevel,
        Instant occurredAt
) {
}
