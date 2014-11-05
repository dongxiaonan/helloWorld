package com.trailblazers.freewheelers.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReserveOrder {

    private Long order_id;
    private Long account_id;
    private List<OrderItem> orderItems;
    private Date reservation_timestamp;
    private OrderStatus status = OrderStatus.NEW;
    private String note = "";

    public ReserveOrder(){
        this.orderItems = new ArrayList<OrderItem>();
    }

    public ReserveOrder(Long account_id, List<OrderItem> orderItems, Date rightNow) {
        this.account_id = account_id;
        this.orderItems = orderItems;
        this.reservation_timestamp = rightNow;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public ReserveOrder setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public ReserveOrder addItemToOrder(Long itemId,Long quantity) {
        OrderItem existingOrderItem = null;
        for(OrderItem orderItem : this.orderItems){
            if(orderItem.getItemId().equals(itemId)){
                existingOrderItem = orderItem;
                break;
            }
        }
        if(existingOrderItem == null){
            orderItems.add(new OrderItem(itemId,quantity));
        } else {
            Long currentQuantity = existingOrderItem.getQuantity();
            orderItems.remove(existingOrderItem);
            orderItems.add(new OrderItem(itemId,currentQuantity + quantity));
        }
        return this;
    }

    public Date getReservation_timestamp() {
        return reservation_timestamp;
    }

    public ReserveOrder setReservation_timestamp(Date reservation_timestamp) {
        this.reservation_timestamp = reservation_timestamp;
        return this;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public ReserveOrder setOrder_id(Long order_id) {
        this.order_id = order_id;
        return this;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public ReserveOrder setAccount_id(Long account_id) {
        this.account_id = account_id;
        return this;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public ReserveOrder setNote(String note) {
        this.note = note;
        return this;
    }

    public String getNote() {
        return note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReserveOrder that = (ReserveOrder) o;

        if (account_id != null ? !account_id.equals(that.account_id) : that.account_id != null) return false;
        if (orderItems != null ? !orderItems.equals(that.orderItems) : that.orderItems != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = account_id != null ? account_id.hashCode() : 0;
        result = 31 * result + (orderItems != null ? orderItems.hashCode() : 0);
        return result;
    }
}
