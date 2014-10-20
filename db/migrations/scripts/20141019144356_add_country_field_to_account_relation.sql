-- // add_country_field_to_account_relation
-- Migration SQL that makes the change goes here.
 ALTER TABLE account
    ADD COLUMN country CHARACTER VARYING(25) NOT NULL DEFAULT('');

-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE account
    DROP COLUMN country;
