-- // create_country
-- Migration SQL that makes the change goes here.
CREATE TABLE country
(
      country_id SERIAL PRIMARY KEY,
      country_name CHARACTER VARYING(25) NOT NULL
);


-- //@UNDO
-- SQL to undo the change goes here.
DROP TABLE country;

