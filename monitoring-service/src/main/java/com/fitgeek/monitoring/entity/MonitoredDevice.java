package com.fitgeek.monitoring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "monitoring_devices")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitoredDevice {

    @Id
    private UUID id;
    private UUID deviceId;
    private String status;
    private Double lastTemperature;
    private Double lastHumidity;
    private Double lastBatteryLevel;
    private Instant createdAt;
    private Instant lastSeenAt;
}
