-- // encrypt_already_existing_passwords
-- Migration SQL that makes the change goes here.
UPDATE account SET password = md5(password);


-- //@UNDO
-- SQL to undo the change goes here.


