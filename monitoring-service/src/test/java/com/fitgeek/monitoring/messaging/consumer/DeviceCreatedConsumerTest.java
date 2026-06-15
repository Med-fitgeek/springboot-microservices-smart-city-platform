package com.fitgeek.monitoring.messaging.consumer;

import com.fitgeek.monitoring.service.MonitoredDeviceService;
import com.fitgeek.shared.events.DeviceCreatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeviceCreatedConsumerTest {

    @Mock
    private MonitoredDeviceService monitoredDeviceService;

    @InjectMocks
    private DeviceCreatedConsumer consumer;

    @Test
    void consume_ShouldDelegateToService() {

        // Given
        DeviceCreatedEvent event = new DeviceCreatedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Temperature Sensor",
                "Paris",
                Instant.now()
        );

        // When
        consumer.consume(event);

        // Then
        verify(monitoredDeviceService)
                .handleDeviceCreated(event);
    }
}