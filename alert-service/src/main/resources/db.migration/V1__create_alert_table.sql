CREATE TABLE alert
(
    id UUID PRIMARY KEY,

    device_id UUID NOT NULL,

    type VARCHAR(50) NOT NULL,

    alert_severity VARCHAR(50) NOT NULL,

    message VARCHAR(255) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    acknowledged BOOLEAN NOT NULL DEFAULT FALSE
);
