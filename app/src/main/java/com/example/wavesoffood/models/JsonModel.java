package com.example.wavesoffood.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.List;

public class JsonModel {
    private About about;
    private List<MenuCategory> menu;

    // Constructors, getters, and setters

    // You can use Jackson annotations to map JSON keys to Java fields
    @JsonProperty("About")
    public About getAbout() {
        return about;
    }

    public void setAbout(About about) {
        this.about = about;
    }

    @JsonProperty("Menu")
    public List<MenuCategory> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuCategory> menu) {
        this.menu = menu;
    }


public static class About {
    private String name;
    private String ratings;
    private String locality;
    private String areaName;
    private String city;
    private List<String> cuisines;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<String> cuisines) {
        this.cuisines = cuisines;
    }


    // Constructors, getters, and setters
}

    public static class MenuCategory {
    private String title;
    private List<Dish> dishes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
// Constructors, getters, and setters
}

    public static class Dish implements Serializable {
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
// Constructors, getters, and setters
    }
}