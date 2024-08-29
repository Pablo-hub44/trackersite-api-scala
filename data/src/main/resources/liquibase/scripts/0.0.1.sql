-- liquibase formatted sql
-- changeset Jaciel:init

CREATE TABLE trackersite.test (
    test_key VARCHAR(5),
    name VARCHAR(25),
    description VARCHAR(30)
)
