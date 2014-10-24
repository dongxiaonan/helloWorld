-- // create account
-- Migration SQL that makes the change goes here.
CREATE TABLE account
(
      account_id SERIAL PRIMARY KEY,
      account_name CHARACTER VARYING(255) NOT NULL,
      email_address CHARACTER VARYING(255) NOT NULL UNIQUE,
      password CHARACTER VARYING(255) NOT NULL,
      street1 CHARACTER VARYING(255) NOT NULL DEFAULT(''),
      street2 CHARACTER VARYING(255) DEFAULT(''),
      city CHARACTER VARYING(100) NOT NULL DEFAULT(''),
      state_province CHARACTER VARYING(100) DEFAULT(''),
      postcode CHARACTER VARYING(10) NOT NULL DEFAULT(''),
      phone_number CHARACTER VARYING(32) NOT NULL,
      enabled BOOLEAN NOT NULL
);

-- //@UNDO
-- SQL to undo the change goes here.

DROP TABLE account;
