package com.fitgeek.alert.entity;

import com.fitgeek.alert.entity.enums.AlertSeverity;
import com.fitgeek.alert.entity.enums.AlertType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {

    @Id
    private UUID id;

    private UUID deviceId;

    @Enumerated(EnumType.STRING)
    private AlertType type;

    @Enumerated(EnumType.STRING)
    private AlertSeverity severity;

    private String message;

    private Instant createdAt;

    private boolean acknowledged;
}