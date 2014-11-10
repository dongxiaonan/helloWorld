package com.trailblazers.freewheelers.model;

import java.math.BigDecimal;

public class Item implements Comparable<Item> {

    private Long itemId;
    private String name;
    private BigDecimal price;
    private String description;
    private ItemType type;
    private Long quantity;

    public Long getItemId() {
        return itemId;
    }
    public Item setItemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }
    public String getName() {
        return name;
    }
    public Item setName(String name) {
        this.name = name;
        return this;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public Item setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
    public String getDescription() {
        return description;
    }
    public Item setDescription(String description) {
        this.description = description;
        return this;
    }
    public ItemType getType() {
        return type;
    }
    public Item setType(ItemType type) {
        this.type = type;
        return this;
    }
    public Long getQuantity() {
        return quantity;
    }
    public Item setQuantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (description != null ? !description.equals(item.description) : item.description != null) return false;
        if (itemId != null ? !itemId.equals(item.itemId) : item.itemId != null) return false;
        if (name != null ? !name.equals(item.name) : item.name != null) return false;
        if (price != null ? !price.equals(item.price) : item.price != null) return false;
        if (type != item.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemId != null ? itemId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Item item) {
        return this.getName().compareToIgnoreCase(item.getName()) ;
    }
}
