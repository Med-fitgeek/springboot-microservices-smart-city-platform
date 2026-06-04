package com.fitgeek.device.messaging.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DeviceEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public DeviceEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
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