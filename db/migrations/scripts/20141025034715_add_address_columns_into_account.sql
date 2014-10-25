-- // add_address_columns_into_account
-- Migration SQL that makes the change goes here.
ALTER TABLE account
  ADD COLUMN street1 CHARACTER VARYING(255) DEFAULT(''),
  ADD COLUMN street2 CHARACTER VARYING(255) DEFAULT(''),
  ADD COLUMN city CHARACTER VARYING(100) NOT NULL DEFAULT(''),
  ADD COLUMN state_province CHARACTER VARYING(100) DEFAULT(''),
  ADD COLUMN postcode CHARACTER VARYING(10) NOT NULL DEFAULT('');


-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE account
  DROP COLUMN (street1, street2, city,state_province,postcode);

