package com.fitgeek.device.service;

import com.fitgeek.device.dto.CreateDeviceRequest;
import com.fitgeek.device.dto.DeviceResponse;
import com.fitgeek.device.entity.Device;

import java.util.UUID;

public interface DeviceService {

    DeviceResponse register(CreateDeviceRequest request);
    DeviceResponse getDeviceById(UUID id);
}
