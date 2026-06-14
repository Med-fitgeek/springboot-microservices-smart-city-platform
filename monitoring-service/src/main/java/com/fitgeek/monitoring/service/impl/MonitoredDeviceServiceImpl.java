package com.fitgeek.monitoring.service.impl;

import com.fitgeek.monitoring.dto.MonitoredDeviceResponse;
import com.fitgeek.monitoring.entity.MonitoredDevice;
import com.fitgeek.monitoring.exception.DeviceNotFoundException;
import com.fitgeek.monitoring.mapper.MonitoredDeviceMapper;
import com.fitgeek.monitoring.repository.DeviceMonitoringRepository;
import com.fitgeek.monitoring.service.MonitoredDeviceService;
import com.fitgeek.shared.events.DeviceCreatedEvent;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MonitoredDeviceServiceImpl implements MonitoredDeviceService {

    private final DeviceMonitoringRepository repository;
    private final MonitoredDeviceMapper mapper;

    @Override
    public Page<MonitoredDeviceResponse>
    getMonitoredDevices(Pageable pageable) {

        return repository.findAll(pageable)
                .map(mapper::toDto);
    }

    @Override
    public MonitoredDeviceResponse getMonitoredDeviceById(UUID id) {

        MonitoredDevice device = repository.findById(id)
                .orElseThrow(() ->
                        new DeviceNotFoundException(
                                "Monitored device not found with id: " + id));

        return mapper.toDto(device);
    }

    @Override
    public void handleDeviceCreated(DeviceCreatedEvent event) {

        boolean alreadyExists =
                repository.existsByDeviceId(event.deviceId());

        if (alreadyExists) {
            return;
        }

        MonitoredDevice monitoredDevice =
                new MonitoredDevice();

        monitoredDevice.setId(UUID.randomUUID());
        monitoredDevice.setDeviceId(event.deviceId());
        monitoredDevice.setStatus("ONLINE");
        monitoredDevice.setCreatedAt(event.occurredAt());

        repository.save(monitoredDevice);
    }
}
