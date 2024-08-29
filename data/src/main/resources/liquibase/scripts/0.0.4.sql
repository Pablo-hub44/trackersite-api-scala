-- changeset Jaciel 21/05/24
-- change table customers, add column created_at and updated_at

ALTER TABLE trackersite.customers ADD COLUMN created_at TIMESTAMP NOT NULL;
ALTER TABLE trackersite.customers ADD COLUMN updated_at TIMESTAMP NOT NULL ;
