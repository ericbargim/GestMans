package com.gestmans.Business.Objects;

import androidx.annotation.NonNull;

public class Dish {
    private String id;
    private String name;
    private int quantity;

    public Dish() {

    }

    public Dish(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addToQuantity() {
        this.quantity++;
    }

    public void removeFromQuantity() {
        if (this.quantity > 1)
        this.quantity--;
    }

    @NonNull
    @Override
    public String toString() {
        return "Id: " + getId() + " - Name: " + getName() + " - Quantity: " + getQuantity();
    }
}
