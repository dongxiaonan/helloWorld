-- // add_default_countries
-- Migration SQL that makes the change goes here.
INSERT INTO country (country_name)
    VALUES ('United Kingdom');
INSERT INTO country (country_name)
    VALUES ('United States Of America');
INSERT INTO country (country_name)
    VALUES ('Germany');
INSERT INTO country (country_name)
    VALUES ('France');
INSERT INTO country (country_name)
    VALUES ('Canada');
INSERT INTO country (country_name)
    VALUES ('Italy');


-- //@UNDO
-- SQL to undo the change goes here.

DELETE FROM country WHERE country_name IN ('UK','USA','Germany','France','Canada','Italy');
