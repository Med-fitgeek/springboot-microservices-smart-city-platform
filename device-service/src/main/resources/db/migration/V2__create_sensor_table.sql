CREATE TABLE devices
(
    id UUID PRIMARY KEY,

    name VARCHAR(255) NOT NULL,

    type VARCHAR(50) NOT NULL,

    status VARCHAR(50) NOT NULL,

    location VARCHAR(255) NOT NULL,

    firmware_version VARCHAR(100) NOT NULL,

    created_at TIMESTAMP NOT NULL
);

CREATE TABLE monitoring_devices
(
    id UUID PRIMARY KEY,

    deviceId UUID NOT NULL,

    status VARCHAR(50) NOT NULL,

    Instant TIMESTAMP NOT NULL
);

CREATE TABLE sensor_readings (
    id UUID PRIMARY KEY,
    device_id UUID NOT NULL,
    temperature DOUBLE PRECISION,
    humidity DOUBLE PRECISION,
    battery_level DOUBLE PRECISION,
    occurred_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_sensor_device
    ON sensor_readings(device_id);