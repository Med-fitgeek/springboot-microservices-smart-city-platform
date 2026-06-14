package com.fitgeek.monitoring.mapper;

import com.fitgeek.monitoring.dto.MonitoredDeviceResponse;
import com.fitgeek.monitoring.entity.MonitoredDevice;
import org.springframework.stereotype.Component;

@Component
public class MonitoredDeviceMapper {

    public MonitoredDeviceResponse toResponse(MonitoredDevice monitoredDevice) {

        return new MonitoredDeviceResponse(
                monitoredDevice.getId(),
                monitoredDevice.getDeviceId(),
                monitoredDevice.getStatus(),
                monitoredDevice.getCreatedAt()
        );
    }
}
