package com.fitgeek.monitoring.service.impl;

import com.fitgeek.monitoring.dto.MonitoredDeviceResponse;
import com.fitgeek.monitoring.entity.MonitoredDevice;
import com.fitgeek.monitoring.exception.DeviceNotFoundException;
import com.fitgeek.monitoring.mapper.MonitoredDeviceMapper;
import com.fitgeek.monitoring.repository.MonitoredDeviceRepository;
import com.fitgeek.shared.events.DeviceCreatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonitoredDeviceServiceImplTest {

    @Mock
    private MonitoredDeviceRepository repository;

    @Mock
    private MonitoredDeviceMapper mapper;

    @InjectMocks
    private MonitoredDeviceServiceImpl service;

    @Test
    void getMonitoredDevices_ShouldReturnMappedPage() {

        // Given
        Pageable pageable = PageRequest.of(0, 10);

        MonitoredDevice device = MonitoredDevice.builder()
                .id(UUID.randomUUID())
                .deviceId(UUID.randomUUID())
                .status("ONLINE")
                .createdAt(Instant.now())
                .build();

        MonitoredDeviceResponse response = mock(MonitoredDeviceResponse.class);

        Page<MonitoredDevice> devices =
                new PageImpl<>(List.of(device), pageable, 1);

        when(repository.findAll(pageable)).thenReturn(devices);
        when(mapper.toResponse(device)).thenReturn(response);

        // When
        Page<MonitoredDeviceResponse> result =
                service.getMonitoredDevices(pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(response, result.getContent().getFirst());

        verify(repository).findAll(pageable);
        verify(mapper).toResponse(device);
    }

    @Test
    void getMonitoredDeviceById_ShouldReturnResponse_WhenDeviceExists() {

        // Given
        UUID id = UUID.randomUUID();

        MonitoredDevice device = MonitoredDevice.builder()
                .id(id)
                .build();

        MonitoredDeviceResponse expectedResponse =
                mock(MonitoredDeviceResponse.class);

        when(repository.findById(id)).thenReturn(Optional.of(device));
        when(mapper.toResponse(device)).thenReturn(expectedResponse);

        // When
        MonitoredDeviceResponse result =
                service.getMonitoredDeviceById(id);

        // Then
        assertEquals(expectedResponse, result);

        verify(repository).findById(id);
        verify(mapper).toResponse(device);
    }

    @Test
    void getMonitoredDeviceById_ShouldThrowException_WhenDeviceDoesNotExist() {

        // Given
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        // When / Then
        DeviceNotFoundException exception = assertThrows(
                DeviceNotFoundException.class,
                () -> service.getMonitoredDeviceById(id)
        );

        assertEquals(
                "Monitored device not found with id: " + id,
                exception.getMessage()
        );

        verify(repository).findById(id);
        verifyNoInteractions(mapper);
    }

    @Test
    void handleDeviceCreated_ShouldSaveMonitoredDevice_WhenDeviceDoesNotExist() {

        // Given
        UUID deviceId = UUID.randomUUID();
        Instant occurredAt = Instant.now();

        DeviceCreatedEvent event = new DeviceCreatedEvent(
                UUID.randomUUID(),
                deviceId,
                "Temperature Sensor",
                "Paris",
                occurredAt
        );

        when(repository.existsByDeviceId(deviceId)).thenReturn(false);

        // When
        service.handleDeviceCreated(event);

        // Then
        ArgumentCaptor<MonitoredDevice> captor =
                ArgumentCaptor.forClass(MonitoredDevice.class);

        verify(repository).save(captor.capture());

        MonitoredDevice savedDevice = captor.getValue();

        assertNotNull(savedDevice.getId());
        assertEquals(deviceId, savedDevice.getDeviceId());
        assertEquals("ONLINE", savedDevice.getStatus());
        assertEquals(occurredAt, savedDevice.getCreatedAt());

        verify(repository).existsByDeviceId(deviceId);
    }

    @Test
    void handleDeviceCreated_ShouldNotSave_WhenDeviceAlreadyExists() {

        // Given
        UUID deviceId = UUID.randomUUID();

        DeviceCreatedEvent event = new DeviceCreatedEvent(
                UUID.randomUUID(),
                deviceId,
                "Temperature Sensor",
                "Paris",
                Instant.now()
        );

        when(repository.existsByDeviceId(deviceId)).thenReturn(true);

        // When
        service.handleDeviceCreated(event);

        // Then
        verify(repository).existsByDeviceId(deviceId);
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }
}