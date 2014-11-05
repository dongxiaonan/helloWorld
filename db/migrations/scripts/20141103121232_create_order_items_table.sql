-- // create_order_items_table
-- Migration SQL that makes the change goes here.
CREATE TABLE orderItems
(
  orderId BIGINT NOT NULL REFERENCES reserve_order,
  itemId BIGINT NOT NULL REFERENCES item,
  quantity INT NOT NULL,
  PRIMARY KEY (orderId,itemId)
);


-- //@UNDO
-- SQL to undo the change goes here.
DROP TABLE orderItems;

