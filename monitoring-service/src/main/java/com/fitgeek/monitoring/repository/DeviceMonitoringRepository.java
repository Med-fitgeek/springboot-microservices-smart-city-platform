package com.fitgeek.monitoring.repository;

import com.fitgeek.monitoring.entity.MonitoredDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceMonitoringRepository extends JpaRepository<MonitoredDevice, UUID> {

    Boolean existsByDeviceId(UUID deviceId);
}
