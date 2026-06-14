package com.fitgeek.alert.messaging.producer;

import com.fitgeek.shared.events.AlertCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertEventProducer {

    private final KafkaTemplate<String, AlertCreatedEvent> kafkaTemplate;

    public void publish(AlertCreatedEvent event) {

        kafkaTemplate.send(
                "alert-events",
                event.alertId().toString(),
                event);
    }
}
