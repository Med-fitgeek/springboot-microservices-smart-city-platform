package com.fitgeek.alert.service.impl;

import com.fitgeek.alert.dto.AlertResponse;
import com.fitgeek.alert.entity.Alert;
import com.fitgeek.alert.entity.enums.AlertSeverity;
import com.fitgeek.alert.entity.enums.AlertType;
import com.fitgeek.alert.excpetion.AlertNotFoundException;
import com.fitgeek.alert.mapper.AlertMapper;
import com.fitgeek.alert.messaging.producer.AlertEventProducer;
import com.fitgeek.alert.repository.AlertRepository;
import com.fitgeek.shared.events.AlertCreatedEvent;
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
class AlertServiceImplTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private AlertMapper alertMapper;

    @Mock
    private AlertEventProducer alertEventProducer;

    @InjectMocks
    private AlertServiceImpl service;

    @Test
    void getAlerts_ShouldReturnMappedAlerts() {

        Alert alert = Alert.builder().build();

        AlertResponse response = mock(AlertResponse.class);

        when(alertRepository.findAll())
                .thenReturn(List.of(alert));

        when(alertMapper.toResponse(alert))
                .thenReturn(response);

        List<AlertResponse> result = service.getAlerts();

        assertEquals(1, result.size());
        assertEquals(response, result.getFirst());

        verify(alertRepository).findAll();
        verify(alertMapper).toResponse(alert);
    }

    @Test
    void getActiveAlerts_ShouldReturnMappedAlerts() {

        Alert alert = Alert.builder()
                .acknowledged(false)
                .build();

        AlertResponse response = mock(AlertResponse.class);

        when(alertRepository.findByAcknowledgedFalse())
                .thenReturn(List.of(alert));

        when(alertMapper.toResponse(alert))
                .thenReturn(response);

        List<AlertResponse> result = service.getActiveAlerts();

        assertEquals(1, result.size());
        assertEquals(response, result.getFirst());

        verify(alertRepository).findByAcknowledgedFalse();
        verify(alertMapper).toResponse(alert);
    }

    @Test
    void acknowledge_ShouldMarkAlertAsAcknowledged() {

        UUID id = UUID.randomUUID();

        Alert alert = Alert.builder()
                .id(id)
                .acknowledged(false)
                .build();

        AlertResponse expectedResponse =
                mock(AlertResponse.class);

        when(alertRepository.findById(id))
                .thenReturn(Optional.of(alert));

        when(alertRepository.save(alert))
                .thenReturn(alert);

        when(alertMapper.toResponse(alert))
                .thenReturn(expectedResponse);

        AlertResponse result = service.acknowledge(id);

        assertTrue(alert.isAcknowledged());
        assertEquals(expectedResponse, result);

        verify(alertRepository).findById(id);
        verify(alertRepository).save(alert);
        verify(alertMapper).toResponse(alert);
    }

    @Test
    void acknowledge_ShouldThrowException_WhenAlertNotFound() {

        UUID id = UUID.randomUUID();

        when(alertRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                AlertNotFoundException.class,
                () -> service.acknowledge(id)
        );

        verify(alertRepository).findById(id);

        verify(alertRepository, never())
                .save(any());

        verifyNoInteractions(alertMapper);
    }

    @Test
    void processSensorReading_ShouldCreateTemperatureAlert() {

        UUID deviceId = UUID.randomUUID();

        SensorReadingEvent event =
                new SensorReadingEvent(
                        UUID.randomUUID(),
                        deviceId,
                        85.0,
                        50.0,
                        80.0,
                        Instant.now());

        when(alertRepository.save(any(Alert.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.processSensorReading(event);

        ArgumentCaptor<Alert> alertCaptor =
                ArgumentCaptor.forClass(Alert.class);

        verify(alertRepository).save(alertCaptor.capture());

        Alert savedAlert = alertCaptor.getValue();

        assertEquals(deviceId, savedAlert.getDeviceId());
        assertEquals(
                AlertType.HIGH_TEMPERATURE,
                savedAlert.getType());

        assertEquals(
                AlertSeverity.HIGH,
                savedAlert.getSeverity());

        assertFalse(savedAlert.isAcknowledged());

        verify(alertEventProducer)
                .publish(any(AlertCreatedEvent.class));
    }

    @Test
    void processSensorReading_ShouldCreateBatteryAlert() {

        UUID deviceId = UUID.randomUUID();

        SensorReadingEvent event =
                new SensorReadingEvent(
                        UUID.randomUUID(),
                        deviceId,
                        30.0,
                        50.0,
                        10.0,
                        Instant.now());

        when(alertRepository.save(any(Alert.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.processSensorReading(event);

        ArgumentCaptor<Alert> alertCaptor =
                ArgumentCaptor.forClass(Alert.class);

        verify(alertRepository).save(alertCaptor.capture());

        Alert savedAlert = alertCaptor.getValue();

        assertEquals(deviceId, savedAlert.getDeviceId());

        assertEquals(
                AlertType.LOW_BATTERY,
                savedAlert.getType());

        assertEquals(
                AlertSeverity.MEDIUM,
                savedAlert.getSeverity());

        assertFalse(savedAlert.isAcknowledged());

        verify(alertEventProducer)
                .publish(any(AlertCreatedEvent.class));
    }

    @Test
    void processSensorReading_ShouldDoNothing_WhenThresholdsAreNotExceeded() {

        SensorReadingEvent event =
                new SensorReadingEvent(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        50.0,
                        50.0,
                        70.0,
                        Instant.now());

        service.processSensorReading(event);

        verify(alertRepository, never())
                .save(any());

        verifyNoInteractions(alertEventProducer);
    }

    @Test
    void processSensorReading_ShouldCreateTwoAlerts_WhenBothThresholdsExceeded() {

        SensorReadingEvent event =
                new SensorReadingEvent(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        90.0,
                        50.0,
                        5.0,
                        Instant.now());

        when(alertRepository.save(any(Alert.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.processSensorReading(event);

        verify(alertRepository, times(2))
                .save(any(Alert.class));

        verify(alertEventProducer, times(2))
                .publish(any(AlertCreatedEvent.class));
    }
}