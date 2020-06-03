package com.gestmans.Business.Objects;

import androidx.annotation.NonNull;

public class Menu {
    private String menuName;
    private int menuQuantity;
    private Dish dishDrink;
    private Dish dishFirst;
    private Dish dishSecond;
    private Dish dishDessert;

    public Menu() {

    }

    public Menu(String menuName, int menuQuantity, Dish drink, Dish dishFirst, Dish second, Dish dessert) {
        this.menuName = menuName;
        this.menuQuantity = menuQuantity;
        this.dishDrink = drink;
        this.dishFirst = dishFirst;
        this.dishSecond = second;
        this.dishDessert = dessert;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuQuantity() {
        return menuQuantity;
    }

    public void setMenuQuantity(int menuQuantity) {
        this.menuQuantity = menuQuantity;
    }

    public Dish getDishDrink() {
        return dishDrink;
    }

    public void setDishDrink(Dish dishDrink) {
        this.dishDrink = dishDrink;
    }

    public Dish getDishFirst() {
        return dishFirst;
    }

    public void setDishFirst(Dish dishFirst) {
        this.dishFirst = dishFirst;
    }

    public Dish getDishSecond() {
        return dishSecond;
    }

    public void setDishSecond(Dish dishSecond) {
        this.dishSecond = dishSecond;
    }

    public Dish getDishDessert() {
        return dishDessert;
    }

    public void setDishDessert(Dish dishDessert) {
        this.dishDessert = dishDessert;
    }

    public void addToQuantity() {
        this.menuQuantity++;
    }

    public void removeFromQuantity() {
        if (this.menuQuantity > 1)
            this.menuQuantity--;
    }

    @NonNull
    @Override
    public String toString() {
        // toString all dishes of menu
        StringBuilder sb = new StringBuilder();

        sb.append("--- MENU ---").append("\n");
        sb.append("Name: ").append(getMenuName()).append("\n");
        sb.append("Quantity: ").append(getMenuQuantity()).append("\n");
        sb.append("- Drink -").append("\n");
        sb.append("Name: ").append(getDishDrink().getName()).append("\n");
        sb.append("ID: ").append(getDishDrink().getId()).append("\n");
        sb.append("- First -").append("\n");
        sb.append("Name: ").append(getDishFirst().getName()).append("\n");
        sb.append("ID: ").append(getDishFirst().getId()).append("\n");
        sb.append("- Second -").append("\n");
        sb.append("Name: ").append(getDishSecond().getName()).append("\n");
        sb.append("ID: ").append(getDishSecond().getId()).append("\n");
        sb.append("- Dessert -").append("\n");
        sb.append("Name: ").append(getDishDessert().getName()).append("\n");
        sb.append("ID: ").append(getDishDessert().getId()).append("\n");

        return sb.toString();
    }
}
