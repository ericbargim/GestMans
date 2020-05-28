package com.gestmans.Business.Objects;

import android.os.Environment;

import androidx.annotation.NonNull;

public class Menu {
    private String drinkId;
    private String drinkName;
    private String firstId;
    private String firstName;
    private String secondId;
    private String secondName;
    private String dessertId;
    private String dessertName;

    public Menu() {

    }

    public Menu(String drinkId, String drinkName, String firstId, String firstName, String secondId, String secondName, String dessertId, String dessertName) {
        this.drinkId = drinkId;
        this.drinkName = drinkName;
        this.firstId = firstId;
        this.firstName = firstName;
        this.secondId = secondId;
        this.secondName = secondName;
        this.dessertId = dessertId;
        this.dessertName = dessertName;
    }

    public String getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(String drinkId) {
        this.drinkId = drinkId;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public String getFirstId() {
        return firstId;
    }

    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondId() {
        return secondId;
    }

    public void setSecondId(String secondId) {
        this.secondId = secondId;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getDessertId() {
        return dessertId;
    }

    public void setDessertId(String dessertId) {
        this.dessertId = dessertId;
    }

    public String getDessertName() {
        return dessertName;
    }

    public void setDessertName(String dessertName) {
        this.dessertName = dessertName;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("--- MENU ---").append("\n");
        sb.append("- Drink -").append("\n");
        sb.append("Name: ").append(getDrinkName()).append("\n");
        sb.append("ID: ").append(getDrinkId()).append("\n");
        sb.append("- First -").append("\n");
        sb.append("Name: ").append(getFirstName()).append("\n");
        sb.append("ID: ").append(getFirstId()).append("\n");
        sb.append("- Second -").append("\n");
        sb.append("Name: ").append(getSecondName()).append("\n");
        sb.append("ID: ").append(getSecondId()).append("\n");
        sb.append("- Dessert -").append("\n");
        sb.append("Name: ").append(getDessertName()).append("\n");
        sb.append("ID: ").append(getDessertId()).append("\n");

        return sb.toString();
    }
}
