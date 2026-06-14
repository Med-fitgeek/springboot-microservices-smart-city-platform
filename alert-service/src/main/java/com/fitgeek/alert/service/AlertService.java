package com.fitgeek.alert.service;

import com.fitgeek.alert.dto.AlertResponse;
import com.fitgeek.shared.events.SensorReadingEvent;

import java.util.List;
import java.util.UUID;

public interface AlertService {

    List<AlertResponse> getAlerts();
    List<AlertResponse> getActiveAlerts();
    AlertResponse acknowledge(UUID id);
    void processSensorReading(SensorReadingEvent event);

}
