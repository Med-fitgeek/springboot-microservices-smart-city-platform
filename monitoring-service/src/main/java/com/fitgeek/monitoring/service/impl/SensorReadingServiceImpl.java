package com.fitgeek.monitoring.service.impl;

import com.fitgeek.monitoring.dto.SensorReadingResponse;
import com.fitgeek.monitoring.entity.SensorReading;
import com.fitgeek.monitoring.mapper.SensorReadingMapper;
import com.fitgeek.monitoring.repository.MonitoredDeviceRepository;
import com.fitgeek.monitoring.repository.SensorReadingRepository;
import com.fitgeek.monitoring.service.SensorReadingService;
import com.fitgeek.shared.events.SensorReadingEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SensorReadingServiceImpl implements SensorReadingService {

    private final MonitoredDeviceRepository monitoredDeviceRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final SensorReadingMapper sensorReadingMapper;

    @Override
    @Transactional
    public void handleSensorReading(SensorReadingEvent event) {

        SensorReading reading =
                SensorReading.builder()
                        .id(UUID.randomUUID())
                        .deviceId(event.deviceId())
                        .temperature(event.temperature())
                        .humidity(event.humidity())
                        .batteryLevel(event.batteryLevel())
                        .occurredAt(event.occurredAt())
                        .build();

        sensorReadingRepository.save(reading);

        monitoredDeviceRepository
                .findByDeviceId(event.deviceId())
                .ifPresent(device -> {

                    device.setLastTemperature(event.temperature());
                    device.setLastHumidity(event.humidity());
                    device.setLastBatteryLevel(event.batteryLevel());
                    device.setLastSeenAt(event.occurredAt());
                    monitoredDeviceRepository.save(device);
                });
    }

    @Override
    public List<SensorReadingResponse> getReadings(UUID deviceId) {
        return sensorReadingRepository.findAll().stream()
                .map(sensorReadingMapper::toResponse)
                .toList();
    }
}
