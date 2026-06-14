package com.fitgeek.monitoring.controller;

import com.fitgeek.monitoring.dto.MonitoredDeviceResponse;
import com.fitgeek.monitoring.service.MonitoredDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/monitoring/devices")
@RequiredArgsConstructor
public class MonitoredDeviceController {

    private final MonitoredDeviceService service;

    @GetMapping
    public ResponseEntity<Page<MonitoredDeviceResponse>> getDevices(
            @PageableDefault(size = 20) Pageable pageable) {

        return ResponseEntity.ok(
                service.getMonitoredDevices(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonitoredDeviceResponse> getDevice(@PathVariable UUID id) {

        return ResponseEntity.ok(
                service.getMonitoredDeviceById(id));
    }
}
