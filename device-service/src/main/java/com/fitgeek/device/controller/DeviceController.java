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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@EnableMethodSecurity
public class DeviceController {

    private final DeviceService service;

    @PostMapping({"", "/"})
    @PreAuthorize("hasRole('CITY_ADMIN') or hasRole('TECHNICIAN')")
    public ResponseEntity<DeviceResponse> create(
            @Valid @RequestBody CreateDeviceRequest request) {

        DeviceResponse response = service.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('CITY_ADMIN', 'TECHNICIAN', 'OPERATOR', 'VIEWER')"
    )
    public ResponseEntity<DeviceResponse> getDeviceById(@PathVariable UUID id) {

        DeviceResponse response = service.getDeviceById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('CITY_ADMIN')")
    public ResponseEntity<DeviceResponse> deactivate(@PathVariable UUID id) {

        DeviceResponse response = service.deactivateDevice(id);
        return ResponseEntity.ok(response);
    }



}
