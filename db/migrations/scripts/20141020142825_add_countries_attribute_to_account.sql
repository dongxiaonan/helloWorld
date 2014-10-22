-- // add_countries_attribute_to_account
-- Migration SQL that makes the change goes here.
ALTER TABLE account
    ADD COLUMN country_id INTEGER;

-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE account
    DROP COLUMN country_id;

