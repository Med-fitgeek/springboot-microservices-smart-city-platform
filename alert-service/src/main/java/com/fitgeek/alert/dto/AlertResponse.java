package com.fitgeek.alert.dto;

import com.fitgeek.alert.entity.enums.AlertSeverity;
import com.fitgeek.alert.entity.enums.AlertType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.time.Instant;
import java.util.UUID;

public record AlertResponse(
        UUID id,
        UUID deviceId,
        AlertType type,
        AlertSeverity severity,
        String message,
        Instant createdAt,
        boolean acknowledged
){}
