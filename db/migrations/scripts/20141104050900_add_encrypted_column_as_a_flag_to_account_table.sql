-- // add encrypted column as a flag to account table
-- Migration SQL that makes the change goes here.
ALTER TABLE account
    ADD COLUMN encrypted BOOLEAN
    DEFAULT false;


-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE account
    DROP COLUMN encrypted;

