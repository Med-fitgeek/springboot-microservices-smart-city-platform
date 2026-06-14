package com.fitgeek.shared.events;

import java.time.Instant;
import java.util.UUID;

public record AlertCreatedEvent(
        UUID eventId,
        UUID alertId,
        UUID deviceId,
        String type,
        String severity,
        String message,
        Instant occurredAt
) {
}
