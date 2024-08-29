-- changeset Jaciel 23/05/24
-- change table recordings, rename column events and chante type data column events

ALTER TABLE trackersite.recordings rename column events to events_reference;
ALTER TABLE trackersite.recordings ALTER COLUMN events_reference type char(36) using events_reference::char(36);
