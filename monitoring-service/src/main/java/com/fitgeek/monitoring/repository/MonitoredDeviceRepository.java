package com.fitgeek.monitoring.repository;

import com.fitgeek.monitoring.entity.MonitoredDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MonitoredDeviceRepository extends JpaRepository<MonitoredDevice, UUID> {

    Optional<MonitoredDevice> findByDeviceId(UUID id);
    Boolean existsByDeviceId(UUID deviceId);
}
