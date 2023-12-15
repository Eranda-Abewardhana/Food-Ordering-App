package com.example.wavesoffood.Database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
@DatabaseTable(tableName = "dishes")
public class DishDTO  {
    public DishDTO() {
    }

    public DishDTO(int id, String name, String description, boolean inStock, boolean isVeg, double price, int qty) {
        Id = id;
        this.name = name;
        this.description = description;
        this.inStock = inStock;
        this.isVeg = isVeg;
        this.price = price;
        this.qty = qty;
    }

    @DatabaseField(generatedId = true)
    private int Id;
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
    private int qty;

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

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

}
