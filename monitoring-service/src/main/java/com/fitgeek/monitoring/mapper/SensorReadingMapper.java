package com.fitgeek.monitoring.mapper;

import com.fitgeek.monitoring.dto.SensorReadingResponse;
import com.fitgeek.monitoring.entity.SensorReading;
import org.springframework.stereotype.Component;

@Component
public class SensorReadingMapper {

    public SensorReadingResponse toResponse(SensorReading sensorReading) {
        return new SensorReadingResponse(
                sensorReading.getId(),
                sensorReading.getDeviceId(),
                sensorReading.getTemperature(),
                sensorReading.getHumidity(),
                sensorReading.getBatteryLevel(),
                sensorReading.getOccurredAt()
        );
    }
}
