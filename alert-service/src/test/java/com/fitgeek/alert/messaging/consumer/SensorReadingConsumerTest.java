package com.fitgeek.alert.messaging.consumer;

import com.fitgeek.alert.service.AlertService;
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
    private AlertService alertService;

    @InjectMocks
    private SensorReadingConsumer consumer;

    @Test
    void consume_ShouldDelegateToAlertService() {

        // Given
        SensorReadingEvent event = new SensorReadingEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                85.5,
                45.0,
                12.0,
                Instant.now()
        );

        // When
        consumer.consume(event);

        // Then
        verify(alertService)
                .processSensorReading(event);
    }
}