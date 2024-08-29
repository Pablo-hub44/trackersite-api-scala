-- changeset jaciel: 13/05/24
-- creation of tables, users, applications, customers and recordings

CREATE TABLE trackersite.users
(
    key        CHAR(36) PRIMARY KEY NOT NULL,
    name       VARCHAR(30)          NOT NULL,
    last_name  VARCHAR(30)          NOT NULL,
    username   VARCHAR(20)          NOT NULL,
    created_at TIMESTAMP            NOT NULL,
    updated_at TIMESTAMP            NOT NULL
);

CREATE TABLE trackersite.customers
(
    key      CHAR(36) PRIMARY KEY NOT NULL,
    active   BOOLEAN,
    user_key CHAR(36)             NOT NULL REFERENCES trackersite.users (key)
);

CREATE TABLE trackersite.applications
(
    key        CHAR(36) PRIMARY KEY NOT NULL,
    name       VARCHAR(20)          NOT NULL,
    active     BOOLEAN              NOT NULL DEFAULT true,
    created_at TIMESTAMP            NOT NULL,
    updated_at TIMESTAMP            NOT NULL,
    user_key   CHAR(36)             NOT NULL REFERENCES trackersite.users (key)
);

CREATE TABLE trackersite.recordings
(
    key             CHAR(36) PRIMARY KEY NOT NULL,
    events          TEXT[]               NOT NULL,
    created_at      TIMESTAMP            NOT NULL,
    updated_at      TIMESTAMP            NOT NULL,
    application_key CHAR(36)             NOT NULL REFERENCES trackersite.applications (key)
);
