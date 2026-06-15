package com.fitgeek.monitoring.service.impl;

import com.fitgeek.monitoring.dto.SensorReadingResponse;
import com.fitgeek.monitoring.entity.MonitoredDevice;
import com.fitgeek.monitoring.entity.SensorReading;
import com.fitgeek.monitoring.mapper.SensorReadingMapper;
import com.fitgeek.monitoring.repository.MonitoredDeviceRepository;
import com.fitgeek.monitoring.repository.SensorReadingRepository;
import com.fitgeek.shared.events.SensorReadingEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorReadingServiceImplTest {

    @Mock
    private MonitoredDeviceRepository monitoredDeviceRepository;

    @Mock
    private SensorReadingRepository sensorReadingRepository;

    @Mock
    private SensorReadingMapper sensorReadingMapper;

    @InjectMocks
    private SensorReadingServiceImpl service;

    @Test
    void handleSensorReading_ShouldSaveReadingAndUpdateDevice_WhenDeviceExists() {

        // Given
        UUID deviceId = UUID.randomUUID();
        Instant occurredAt = Instant.now();

        SensorReadingEvent event = new SensorReadingEvent(
                UUID.randomUUID(),
                deviceId,
                25.5,
                60.0,
                85.0,
                occurredAt
        );

        MonitoredDevice monitoredDevice = MonitoredDevice.builder()
                .id(UUID.randomUUID())
                .deviceId(deviceId)
                .build();

        when(monitoredDeviceRepository.findByDeviceId(deviceId))
                .thenReturn(Optional.of(monitoredDevice));

        // When
        service.handleSensorReading(event);

        // Then
        ArgumentCaptor<SensorReading> readingCaptor =
                ArgumentCaptor.forClass(SensorReading.class);

        verify(sensorReadingRepository).save(readingCaptor.capture());

        SensorReading savedReading = readingCaptor.getValue();

        assertNotNull(savedReading.getId());
        assertEquals(deviceId, savedReading.getDeviceId());
        assertEquals(event.temperature(), savedReading.getTemperature());
        assertEquals(event.humidity(), savedReading.getHumidity());
        assertEquals(event.batteryLevel(), savedReading.getBatteryLevel());
        assertEquals(occurredAt, savedReading.getOccurredAt());

        assertEquals(event.temperature(),
                monitoredDevice.getLastTemperature());

        assertEquals(event.humidity(),
                monitoredDevice.getLastHumidity());

        assertEquals(event.batteryLevel(),
                monitoredDevice.getLastBatteryLevel());

        assertEquals(occurredAt,
                monitoredDevice.getLastSeenAt());

        verify(monitoredDeviceRepository)
                .save(monitoredDevice);
    }

    @Test
    void handleSensorReading_ShouldSaveReadingOnly_WhenDeviceDoesNotExist() {

        // Given
        UUID deviceId = UUID.randomUUID();

        SensorReadingEvent event = new SensorReadingEvent(
                UUID.randomUUID(),
                deviceId,
                22.0,
                50.0,
                90.0,
                Instant.now()
        );

        when(monitoredDeviceRepository.findByDeviceId(deviceId))
                .thenReturn(Optional.empty());

        // When
        service.handleSensorReading(event);

        // Then
        verify(sensorReadingRepository)
                .save(any(SensorReading.class));

        verify(monitoredDeviceRepository)
                .findByDeviceId(deviceId);

        verify(monitoredDeviceRepository, never())
                .save(any(MonitoredDevice.class));
    }

    @Test
    void getReadings_ShouldReturnMappedResponses() {

        // Given
        UUID deviceId = UUID.randomUUID();

        SensorReading reading1 = SensorReading.builder()
                .id(UUID.randomUUID())
                .deviceId(deviceId)
                .build();

        SensorReading reading2 = SensorReading.builder()
                .id(UUID.randomUUID())
                .deviceId(deviceId)
                .build();

        SensorReadingResponse response1 =
                mock(SensorReadingResponse.class);

        SensorReadingResponse response2 =
                mock(SensorReadingResponse.class);

        when(sensorReadingRepository.findAll())
                .thenReturn(List.of(reading1, reading2));

        when(sensorReadingMapper.toResponse(reading1))
                .thenReturn(response1);

        when(sensorReadingMapper.toResponse(reading2))
                .thenReturn(response2);

        // When
        List<SensorReadingResponse> result =
                service.getReadings(deviceId);

        // Then
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));

        verify(sensorReadingRepository).findAll();

        verify(sensorReadingMapper).toResponse(reading1);
        verify(sensorReadingMapper).toResponse(reading2);
    }

    @Test
    void getReadings_ShouldReturnEmptyList_WhenNoReadingsExist() {

        // Given
        UUID deviceId = UUID.randomUUID();

        when(sensorReadingRepository.findAll())
                .thenReturn(List.of());

        // When
        List<SensorReadingResponse> result =
                service.getReadings(deviceId);

        // Then
        assertTrue(result.isEmpty());

        verify(sensorReadingRepository).findAll();

        verifyNoInteractions(sensorReadingMapper);
    }
}