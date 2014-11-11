-- // migrate_order_data_from_reserve_order_to_orderItem
-- Migration SQL that makes the change goes here.
INSERT INTO orderItems (orderId, itemId, quantity)
    SELECT ro.order_id,ro.item_id,coalesce(1) from reserve_order ro
    WHERE ro.item_id in (SELECT item.item_id from item);

-- //@UNDO
-- SQL to undo the change goes here.
UPDATE reserve_order
SET item_id =
(SELECT itemId FROM orderItems
  WHERE reserve_order.order_id=orderItems.orderId);

