package com.fitgeek.device.service.impl;

import com.fitgeek.device.dto.CreateDeviceRequest;
import com.fitgeek.device.dto.DeviceResponse;
import com.fitgeek.device.entity.Device;
import com.fitgeek.device.entity.DeviceStatus;
import com.fitgeek.device.exception.DeviceNotFoundException;
import com.fitgeek.device.mapper.DeviceMapper;
import com.fitgeek.device.messaging.producer.DeviceEventProducer;
import com.fitgeek.device.repository.DeviceRepository;
import com.fitgeek.device.service.DeviceService;
import com.fitgeek.shared.events.DeviceCreatedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository repository;
    private final DeviceMapper mapper;
    private final DeviceEventProducer deviceEventProducer;

    @Override
    @Transactional
    public DeviceResponse register(CreateDeviceRequest request) {

        Device device = Device.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .type(request.type())
                .location(request.location())
                .firmwareVersion(request.firmwareVersion())
                .status(DeviceStatus.ACTIVE)
                .createdAt(Instant.now())
                .build();

        Device savedDevice = repository.save(device);

        DeviceCreatedEvent event = new DeviceCreatedEvent(
                UUID.randomUUID(),
                savedDevice.getId(),
                savedDevice.getName(),
                savedDevice.getLocation(),
                Instant.now()
        );

        deviceEventProducer.publishDeviceCreated(event);

        return mapper.toResponse(savedDevice);
    }

    @Override
    @Transactional()
    public DeviceResponse getDeviceById(UUID id) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found"));

        return mapper.toResponse(device);
    }

    @Override
    public DeviceResponse deactivateDevice(UUID id) {

        Device device = repository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found"));

        device.setStatus(DeviceStatus.INACTIVE);
        Device savedDevice = repository.save(device);
        return mapper.toResponse(savedDevice);
    }
}
