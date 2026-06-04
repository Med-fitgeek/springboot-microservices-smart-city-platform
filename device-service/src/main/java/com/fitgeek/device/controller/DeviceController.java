package com.fitgeek.device.controller;

import com.fitgeek.device.dto.CreateDeviceRequest;
import com.fitgeek.device.dto.DeviceResponse;
import com.fitgeek.device.entity.Device;
import com.fitgeek.device.mapper.DeviceMapper;
import com.fitgeek.device.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService service;
    private final DeviceMapper mapper;

    @PostMapping
    public ResponseEntity<DeviceResponse> create(
            @Valid @RequestBody CreateDeviceRequest request) {

        DeviceResponse response = service.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getDeviceById(@PathVariable UUID id) {

        DeviceResponse response = service.getDeviceById(id);
        return ResponseEntity.ok(response);
    }
}
