package com.gestmans.Business.Objects;

import androidx.annotation.NonNull;

public class Dish {
    private String id;
    private String name;
    private String dishType;
    private int quantity;

    public Dish() {

    }

    public Dish(String id, String name, String dishType, int quantity) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
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

    public String getDishType() {
        return dishType;
    }

    public void setDishType(String dishType) {
        this.dishType = dishType;
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
        StringBuilder sb = new StringBuilder();

        sb.append("--- DISH ---").append("\n");
        sb.append("ID: ").append(getId()).append("\n");
        sb.append("Name: ").append(getName()).append("\n");
        sb.append("Dish type: ").append(getDishType()).append("\n");
        sb.append("Quantity: ").append(getQuantity()).append("\n");

        return sb.toString();
    }
}
