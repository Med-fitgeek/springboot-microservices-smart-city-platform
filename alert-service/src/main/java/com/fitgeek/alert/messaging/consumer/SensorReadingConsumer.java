package com.fitgeek.alert.messaging.consumer;

import com.fitgeek.alert.service.AlertService;
import com.fitgeek.shared.events.SensorReadingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SensorReadingConsumer {

    private final AlertService alertService;

    @KafkaListener(
            topics = "sensor-readings",
            groupId = "alert-group")
    public void consume(SensorReadingEvent event) {

        log.info(
                "Received sensor reading for device {}",
                event.deviceId());

        alertService.processSensorReading(event);
    }
}
