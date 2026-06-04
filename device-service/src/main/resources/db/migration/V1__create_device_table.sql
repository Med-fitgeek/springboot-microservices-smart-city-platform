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