package com.fitgeek.monitoring.service;

import com.fitgeek.monitoring.dto.MonitoredDeviceResponse;
import com.fitgeek.shared.events.DeviceCreatedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MonitoredDeviceService {
    Page<MonitoredDeviceResponse>
    getMonitoredDevices(Pageable pageable);
    MonitoredDeviceResponse getMonitoredDeviceById(UUID id);
    void handleDeviceCreated(DeviceCreatedEvent event);
}
