package com.trailblazers.freewheelers.model;

import java.util.Date;
import java.util.List;

public class ReservedOrderDetail  {

    private List<Item> items;
    private Account account;
    private Date reserveTime;
    private OrderStatus status;
    private String note;
    private Long orderId;

    public ReservedOrderDetail(Long orderId, Account account, List<Item> items, Date reserveTime, OrderStatus status, String note){
        this.orderId = orderId;
        this.items = items;
        this.account = account;
        this.reserveTime = reserveTime;
        this.status = status;
        this.note = note;
    }

    public ReservedOrderDetail() {
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> item) {
        this.items = item;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Date getReserveTime() {
        return reserveTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public OrderStatus[] getStatusOptions() {
        return OrderStatus.values();
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
