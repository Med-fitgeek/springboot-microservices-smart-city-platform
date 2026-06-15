package com.fitgeek.device.service.impl;

import com.fitgeek.device.dto.CreateDeviceRequest;
import com.fitgeek.device.dto.DeviceResponse;
import com.fitgeek.device.entity.Device;
import com.fitgeek.device.entity.enums.DeviceStatus;
import com.fitgeek.device.entity.enums.DeviceType;
import com.fitgeek.device.exception.DeviceNotFoundException;
import com.fitgeek.device.mapper.DeviceMapper;
import com.fitgeek.device.messaging.producer.DeviceEventProducer;
import com.fitgeek.device.repository.DeviceRepository;
import com.fitgeek.shared.events.DeviceCreatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceImplTest {

    @Mock
    private DeviceRepository repository;

    @Mock
    private DeviceMapper mapper;

    @Mock
    private DeviceEventProducer deviceEventProducer;

    @InjectMocks
    private DeviceServiceImpl service;

    @Test
    void register_ShouldSaveDevicePublishEventAndReturnResponse() {

        // Given
        CreateDeviceRequest request = new CreateDeviceRequest(
                "Temperature Sensor",
                DeviceType.POLLUTION_SENSOR,
                "Paris",
                "1.0.0"
        );

        UUID deviceId = UUID.randomUUID();

        Device savedDevice = Device.builder()
                .id(deviceId)
                .name(request.name())
                .type(request.type())
                .location(request.location())
                .firmwareVersion(request.firmwareVersion())
                .status(DeviceStatus.ACTIVE)
                .createdAt(Instant.now())
                .build();

        DeviceResponse expectedResponse = mock(DeviceResponse.class);

        when(repository.save(any(Device.class))).thenReturn(savedDevice);
        when(mapper.toResponse(savedDevice)).thenReturn(expectedResponse);

        // When
        DeviceResponse result = service.register(request);

        // Then
        assertEquals(expectedResponse, result);

        ArgumentCaptor<Device> deviceCaptor = ArgumentCaptor.forClass(Device.class);
        verify(repository).save(deviceCaptor.capture());

        Device capturedDevice = deviceCaptor.getValue();

        assertEquals(request.name(), capturedDevice.getName());
        assertEquals(request.type(), capturedDevice.getType());
        assertEquals(request.location(), capturedDevice.getLocation());
        assertEquals(request.firmwareVersion(), capturedDevice.getFirmwareVersion());
        assertEquals(DeviceStatus.ACTIVE, capturedDevice.getStatus());
        assertNotNull(capturedDevice.getId());
        assertNotNull(capturedDevice.getCreatedAt());

        ArgumentCaptor<DeviceCreatedEvent> eventCaptor =
                ArgumentCaptor.forClass(DeviceCreatedEvent.class);

        verify(deviceEventProducer).publishDeviceCreated(eventCaptor.capture());

        DeviceCreatedEvent publishedEvent = eventCaptor.getValue();

        assertEquals(deviceId, publishedEvent.deviceId());
        assertEquals(savedDevice.getName(), publishedEvent.name());
        assertNotNull(publishedEvent.eventId());
        assertNotNull(publishedEvent.occurredAt());

        verify(mapper).toResponse(savedDevice);
    }

    @Test
    void getDeviceById_ShouldReturnDevice_WhenDeviceExists() {

        // Given
        UUID deviceId = UUID.randomUUID();

        Device device = Device.builder()
                .id(deviceId)
                .build();

        DeviceResponse expectedResponse = mock(DeviceResponse.class);

        when(repository.findById(deviceId)).thenReturn(Optional.of(device));
        when(mapper.toResponse(device)).thenReturn(expectedResponse);

        // When
        DeviceResponse result = service.getDeviceById(deviceId);

        // Then
        assertEquals(expectedResponse, result);

        verify(repository).findById(deviceId);
        verify(mapper).toResponse(device);
    }

    @Test
    void getDeviceById_ShouldThrowException_WhenDeviceDoesNotExist() {

        // Given
        UUID deviceId = UUID.randomUUID();

        when(repository.findById(deviceId)).thenReturn(Optional.empty());

        // When / Then
        DeviceNotFoundException exception = assertThrows(
                DeviceNotFoundException.class,
                () -> service.getDeviceById(deviceId)
        );

        assertEquals("Device not found", exception.getMessage());

        verify(repository).findById(deviceId);
        verifyNoInteractions(mapper);
    }

    @Test
    void deactivateDevice_ShouldDeactivateAndReturnResponse() {

        // Given
        UUID deviceId = UUID.randomUUID();

        Device device = Device.builder()
                .id(deviceId)
                .status(DeviceStatus.ACTIVE)
                .build();

        Device deactivatedDevice = Device.builder()
                .id(deviceId)
                .status(DeviceStatus.INACTIVE)
                .build();

        DeviceResponse expectedResponse = mock(DeviceResponse.class);

        when(repository.findById(deviceId)).thenReturn(Optional.of(device));
        when(repository.save(device)).thenReturn(deactivatedDevice);
        when(mapper.toResponse(deactivatedDevice)).thenReturn(expectedResponse);

        // When
        DeviceResponse result = service.deactivateDevice(deviceId);

        // Then
        assertEquals(expectedResponse, result);

        assertEquals(DeviceStatus.INACTIVE, device.getStatus());

        verify(repository).findById(deviceId);
        verify(repository).save(device);
        verify(mapper).toResponse(deactivatedDevice);
    }

    @Test
    void deactivateDevice_ShouldThrowException_WhenDeviceDoesNotExist() {

        // Given
        UUID deviceId = UUID.randomUUID();

        when(repository.findById(deviceId)).thenReturn(Optional.empty());

        // When / Then
        DeviceNotFoundException exception = assertThrows(
                DeviceNotFoundException.class,
                () -> service.deactivateDevice(deviceId)
        );

        assertEquals("Device not found", exception.getMessage());

        verify(repository).findById(deviceId);
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }
}