package com.trailblazers.freewheelers.model;

import java.util.Date;

public class ReserveOrder {

    private Long order_id;
    private Long account_id;
    private Long item_id;
    private Date reservation_timestamp;
    private OrderStatus status = OrderStatus.NEW;
    private String note = "";

    public ReserveOrder(){}

    public ReserveOrder(Long account_id, Long item_id, Date rightNow) {
        this.account_id = account_id;
        this.item_id = item_id;
        this.reservation_timestamp = rightNow;
    }

    public Long getItem_id() {
        return item_id;
    }

    public ReserveOrder setItem_id(Long item_id) {
        this.item_id = item_id;
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

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
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
        if (item_id != null ? !item_id.equals(that.item_id) : that.item_id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = account_id != null ? account_id.hashCode() : 0;
        result = 31 * result + (item_id != null ? item_id.hashCode() : 0);
        return result;
    }
}
