package com.fitgeek.monitoring.repository;

import com.fitgeek.monitoring.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SensorReadingRepository extends JpaRepository<SensorReading, UUID> {
}
