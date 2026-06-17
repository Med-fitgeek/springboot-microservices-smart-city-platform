package com.fitgeek.alert.controller;

import com.fitgeek.alert.dto.AlertResponse;
import com.fitgeek.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService service;

    @GetMapping
    public ResponseEntity<List<AlertResponse>> getAlerts() {

        return ResponseEntity.ok(service.getAlerts());
    }

    @GetMapping("/active")
    public ResponseEntity<List<AlertResponse>> getActiveAlerts() {

        return ResponseEntity.ok(service.getActiveAlerts());
    }

    @PatchMapping("/{id}/acknowledge")
    public ResponseEntity<AlertResponse> acknowledge(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                service.acknowledge(id));
    }
}
