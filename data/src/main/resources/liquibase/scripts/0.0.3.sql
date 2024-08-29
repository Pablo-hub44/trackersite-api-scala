-- changeset Jaciel 13/05/24
-- change table users, add column email

ALTER TABLE trackersite.users ADD COLUMN email VARCHAR(25) NOT NULL
