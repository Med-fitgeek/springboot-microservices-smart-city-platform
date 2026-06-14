package com.fitgeek.monitoring.controller;

import com.fitgeek.monitoring.dto.SensorReadingResponse;
import com.fitgeek.monitoring.service.SensorReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/monitoring/readings")
@RequiredArgsConstructor
public class SensorReadingController {

    private final SensorReadingService service;

    @GetMapping("/{deviceId}")
    public ResponseEntity<List<SensorReadingResponse>> getReadings(@PathVariable UUID deviceId) {

        return ResponseEntity.ok(
                service.getReadings(deviceId));
    }
}
