package com.fitgeek.monitoring.messaging.consumer;

import com.fitgeek.monitoring.entity.DeviceMonitoring;
import com.fitgeek.monitoring.repository.DeviceMonitoringRepository;
import com.fitgeek.shared.events.DeviceCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeviceCreatedConsumer {

    private final DeviceMonitoringRepository repository;

    @KafkaListener(
            topics = "device-events",
            groupId = "monitoring-group")
    public void consume(
            DeviceCreatedEvent event) {

        DeviceMonitoring monitoring =
                new DeviceMonitoring();

        monitoring.setId(UUID.randomUUID());

        monitoring.setDeviceId(
                event.deviceId());

        monitoring.setStatus("ONLINE");

        monitoring.setCreatedAt(
                Instant.now());

        repository.save(monitoring);
    }
}