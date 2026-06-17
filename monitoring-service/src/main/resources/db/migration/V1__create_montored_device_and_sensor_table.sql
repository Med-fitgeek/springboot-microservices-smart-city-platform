CREATE TABLE monitored_devices
(
    id UUID PRIMARY KEY,

    device_id UUID NOT NULL,

    status VARCHAR(50) NOT NULL,

    last_temperature DOUBLE PRECISION,

    last_humidity DOUBLE PRECISION,

    last_battery_level DOUBLE PRECISION,

    created_at TIMESTAMP NOT NULL,

    last_seen_at TIMESTAMP NOT NULL
);

CREATE TABLE sensor_readings
(
    id UUID PRIMARY KEY,

    device_id UUID NOT NULL,

    temperature DOUBLE PRECISION,

    humidity DOUBLE PRECISION,

    battery_level DOUBLE PRECISION,

    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_sensor_device
    ON sensor_readings(device_id);