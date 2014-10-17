-- // create_default_accounts_and_item_types
-- Migration SQL that makes the change goes here.
INSERT INTO account (email_address, account_name, password, country, phone_number, enabled)
            VALUES ('admin@freewheelers.com', 'AdminCat','admin', '', '', true);
INSERT INTO account (email_address, account_name, password, country, phone_number, enabled)
            VALUES ('user@freewheelers.com', 'UserCat','user', '', '', true);

INSERT INTO account_role (account_name, role) VALUES ('AdminCat', 'ROLE_ADMIN');
INSERT INTO account_role (account_name, role) VALUES ('UserCat', 'ROLE_USER');

INSERT INTO item_type (name) VALUES ('Frames');
INSERT INTO item_type (name) VALUES ('Accessories');


-- //@UNDO
-- SQL to undo the change goes here.

DELETE FROM account WHERE email_address IN ('admin@freewheelers.com', 'user@freewheelers.com');
DELETE FROM item_type WHERE name IN ('Frames', 'Accessories');
