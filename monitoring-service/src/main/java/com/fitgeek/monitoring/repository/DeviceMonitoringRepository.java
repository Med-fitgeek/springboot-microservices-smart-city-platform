package com.fitgeek.monitoring.repository;

import com.fitgeek.monitoring.entity.DeviceMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceMonitoringRepository extends JpaRepository<DeviceMonitoring, UUID> {
}
