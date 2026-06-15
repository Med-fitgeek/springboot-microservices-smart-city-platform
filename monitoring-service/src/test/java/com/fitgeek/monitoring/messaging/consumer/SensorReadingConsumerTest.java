package com.fitgeek.monitoring.messaging.consumer;

import com.fitgeek.monitoring.service.SensorReadingService;
import com.fitgeek.shared.events.SensorReadingEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SensorReadingConsumerTest {

    @Mock
    private SensorReadingService service;

    @InjectMocks
    private SensorReadingConsumer consumer;

    @Test
    void consume_ShouldDelegateToService() {

        // Given
        SensorReadingEvent event = new SensorReadingEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                24.5,
                58.0,
                87.0,
                Instant.now()
        );

        // When
        consumer.consume(event);

        // Then
        verify(service).handleSensorReading(event);
    }
}