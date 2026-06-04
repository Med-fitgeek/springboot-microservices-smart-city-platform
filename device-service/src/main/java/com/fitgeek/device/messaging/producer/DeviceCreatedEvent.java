package com.fitgeek.device.messaging.producer;

import java.time.Instant;
import java.util.UUID;

public record DeviceCreatedEvent(
        UUID eventId,
        UUID deviceId,
        String name,
        String type,
        Instant occurredAt
) {}