package com.fitgeek.device.mapper;

import com.fitgeek.device.dto.DeviceResponse;
import com.fitgeek.device.entity.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {
    public DeviceResponse toResponse(Device device) {

        return new DeviceResponse(
                device.getId(),
                device.getName(),
                device.getType(),
                device.getStatus(),
                device.getLocation(),
                device.getFirmwareVersion(),
                device.getCreatedAt()
        );
    }
}