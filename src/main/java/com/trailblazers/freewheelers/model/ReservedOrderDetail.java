package com.trailblazers.freewheelers.model;

import java.util.Date;
import java.util.List;

public class ReservedOrderDetail  {

    private List<Item> items;
    private Account account;
    private Date reserve_time;
    private OrderStatus status;
    private String note;
    private Long orderId;

    public ReservedOrderDetail(Long orderId, Account account, List<Item> items, Date reserve_time, OrderStatus status, String note){
        this.orderId = orderId;
        this.items = items;
        this.account = account;
        this.reserve_time = reserve_time;
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

    public void addItem(Item item) {
        this.items.add(item);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Date getReserve_time() {
        return reserve_time;
    }

    public void setReserve_time(Date reserve_time) {
        this.reserve_time = reserve_time;
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

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
