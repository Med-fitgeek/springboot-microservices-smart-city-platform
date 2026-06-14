package com.fitgeek.alert.repository;

import com.fitgeek.alert.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {

    List<Alert> findByAcknowledgedFalse();
    List<Alert> findByDeviceId(UUID deviceId);
}