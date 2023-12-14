package com.example.wavesoffood.Database;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class DishDTO implements Serializable {
    @DatabaseField(generatedId = true)
    private int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public boolean isVeg() {
        return isVeg;
    }

    public void setVeg(boolean veg) {
        isVeg = veg;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @DatabaseField
    private String name;
    @DatabaseField
    private String description;
    @DatabaseField
    private boolean inStock;
    @DatabaseField
    private boolean isVeg;
    @DatabaseField
    private double price;
}
