package com.fitgeek.alert.service.impl;

import com.fitgeek.alert.dto.AlertResponse;
import com.fitgeek.alert.entity.Alert;
import com.fitgeek.alert.entity.enums.AlertSeverity;
import com.fitgeek.alert.entity.enums.AlertType;
import com.fitgeek.alert.excpetion.AlertNotFoundException;
import com.fitgeek.alert.mapper.AlertMapper;
import com.fitgeek.alert.messaging.producer.AlertEventProducer;
import com.fitgeek.alert.repository.AlertRepository;
import com.fitgeek.alert.service.AlertService;
import com.fitgeek.shared.events.AlertCreatedEvent;
import com.fitgeek.shared.events.SensorReadingEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;
    private final AlertEventProducer alertEventProducer;

    @Override
    @Transactional
    public List<AlertResponse> getAlerts() {

        return alertRepository.findAll()
                .stream()
                .map(alertMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<AlertResponse> getActiveAlerts() {

        return alertRepository.findByAcknowledgedFalse()
                .stream()
                .map(alertMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public AlertResponse acknowledge(UUID id) {

        Alert alert =
                alertRepository.findById(id)
                        .orElseThrow(
                                () -> new AlertNotFoundException(
                                        "Alert not found"));

        alert.setAcknowledged(true);

        return alertMapper.toResponse(
                alertRepository.save(alert));
    }

    @Override
    public void processSensorReading(SensorReadingEvent event) {
        checkTemperature(event);
        checkBattery(event);
    }

    private void checkTemperature(
            SensorReadingEvent event) {

        if (event.temperature() > 80) {

            Alert alert =
                    Alert.builder()
                            .id(UUID.randomUUID())
                            .deviceId(event.deviceId())
                            .type(AlertType.HIGH_TEMPERATURE)
                            .severity(AlertSeverity.HIGH)
                            .message(
                                    "Temperature exceeded threshold: "
                                            + event.temperature())
                            .createdAt(Instant.now())
                            .acknowledged(false)
                            .build();

            Alert saved = alertRepository.save(alert);

            alertEventProducer.publish(
                    new AlertCreatedEvent(
                            UUID.randomUUID(),
                            saved.getId(),
                            saved.getDeviceId(),
                            saved.getType().name(),
                            saved.getSeverity().name(),
                            saved.getMessage(),
                            Instant.now()));

        }
    }

    private void checkBattery(
            SensorReadingEvent event) {

        if (event.batteryLevel() < 15) {

            Alert alert =
                    Alert.builder()
                            .id(UUID.randomUUID())
                            .deviceId(event.deviceId())
                            .type(AlertType.LOW_BATTERY)
                            .severity(AlertSeverity.MEDIUM)
                            .message(
                                    "Battery level critical: "
                                            + event.batteryLevel())
                            .createdAt(Instant.now())
                            .acknowledged(false)
                            .build();

            Alert saved = alertRepository.save(alert);

            alertEventProducer.publish(
                    new AlertCreatedEvent(
                            UUID.randomUUID(),
                            saved.getId(),
                            saved.getDeviceId(),
                            saved.getType().name(),
                            saved.getSeverity().name(),
                            saved.getMessage(),
                            Instant.now()));

        }
    }
}
