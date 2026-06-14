package com.fitgeek.monitoring.messaging.consumer;

import com.fitgeek.monitoring.service.MonitoredDeviceService;
import com.fitgeek.shared.events.DeviceCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceCreatedConsumer {

    private final MonitoredDeviceService monitoredDeviceService;

    @KafkaListener(
            topics = "device-events",
            groupId = "monitoring-group")
    public void consume(DeviceCreatedEvent event) {

        log.info("Received DeviceCreatedEvent for device {}",
                event.deviceId());

        monitoredDeviceService.handleDeviceCreated(event);
    }
}