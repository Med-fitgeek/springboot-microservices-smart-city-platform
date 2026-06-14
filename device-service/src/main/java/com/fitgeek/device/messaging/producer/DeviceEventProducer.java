package com.fitgeek.device.messaging.producer;

import com.fitgeek.shared.events.DeviceCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DeviceEventProducer {

    private final KafkaTemplate<String, DeviceCreatedEvent> kafkaTemplate;

    public DeviceEventProducer(KafkaTemplate<String, DeviceCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishDeviceCreated(DeviceCreatedEvent event) {
        kafkaTemplate.send(
                "device-events",
                event.deviceId().toString(),
                event
        );
    }
}