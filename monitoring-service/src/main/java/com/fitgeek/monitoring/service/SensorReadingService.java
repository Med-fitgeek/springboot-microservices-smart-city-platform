package com.fitgeek.monitoring.service;

import com.fitgeek.monitoring.dto.SensorReadingResponse;
import com.fitgeek.shared.events.SensorReadingEvent;

import java.util.List;
import java.util.UUID;

public interface SensorReadingService {

    void handleSensorReading(SensorReadingEvent event);
    List<SensorReadingResponse> getReadings(UUID deviceId);
}
