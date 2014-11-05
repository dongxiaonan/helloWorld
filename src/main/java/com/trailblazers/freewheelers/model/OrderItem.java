package com.trailblazers.freewheelers.model;

public class OrderItem {

    private final Long itemId;
    private final Long quantity;

    public OrderItem(Long itemId,Long quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        if (itemId != null ? !itemId.equals(orderItem.itemId) : orderItem.itemId != null) return false;
        if (quantity != null ? !quantity.equals(orderItem.quantity) : orderItem.quantity != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemId != null ? itemId.hashCode() : 0;
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }
}
