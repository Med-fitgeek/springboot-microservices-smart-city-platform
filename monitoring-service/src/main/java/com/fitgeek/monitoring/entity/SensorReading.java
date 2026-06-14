package com.fitgeek.monitoring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sensor_readings")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SensorReading {

    @Id
    private UUID id;
    private UUID deviceId;
    private Double temperature;
    private Double humidity;
    private Double batteryLevel;
    private Instant occurredAt;
}
