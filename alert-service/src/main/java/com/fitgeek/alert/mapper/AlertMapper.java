package com.fitgeek.alert.mapper;

import com.fitgeek.alert.dto.AlertResponse;
import com.fitgeek.alert.entity.Alert;
import org.springframework.stereotype.Component;

@Component
public class AlertMapper {

    public AlertResponse toResponse( Alert alert) {
        return new AlertResponse(
                alert.getId(),
                alert.getDeviceId(),
                alert.getType(),
                alert.getSeverity(),
                alert.getMessage(),
                alert.getCreatedAt(),
                alert.isAcknowledged()
        );
    }
}
