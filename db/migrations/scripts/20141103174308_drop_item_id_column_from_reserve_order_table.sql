-- // drop_item_id_column_from_reserve_order_table
-- Migration SQL that makes the change goes here.
ALTER TABLE reserve_order
    DROP COLUMN item_id;


-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE reserve_order
    ADD COLUMN item_id BIGINT NOT NULL DEFAULT '';

