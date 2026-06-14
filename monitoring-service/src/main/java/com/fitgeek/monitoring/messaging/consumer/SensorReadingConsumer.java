package com.fitgeek.monitoring.messaging.consumer;

import com.fitgeek.monitoring.service.SensorReadingService;
import com.fitgeek.shared.events.SensorReadingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SensorReadingConsumer {

    private final SensorReadingService service;

    @KafkaListener(
            topics = "sensor-readings",
            groupId = "monitoring-group")
    public void consume(SensorReadingEvent event) {

        log.info("Received SensorReadingEvent for device {}",
                event.deviceId());
        service.handleSensorReading(event);
    }
}
