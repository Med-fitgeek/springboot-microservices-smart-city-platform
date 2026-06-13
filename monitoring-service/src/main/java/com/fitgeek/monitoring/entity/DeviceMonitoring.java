package com.fitgeek.monitoring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "device_monitoring")
@Getter
@Setter
public class DeviceMonitoring {

    @Id
    private UUID id;

    private UUID deviceId;

    private String status;

    private Instant createdAt;
}
