package com.example.computerstoredb.Models;

public class Products {
    private static Products instance = new Products();
    private int id;
    private int categoryId;
    private String name;
    private int price;
    private int stock;
    private String imageSrc;
    private float rating;

    public Products() {
    }

    public Products(int id, int categoryId, String name, int price, int stock, String image, float rating) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imageSrc = image;
        this.rating = rating;
    }

    public static Products getInstance() { return instance; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() { return stock; }

    public void setStock(int stock) { this.stock = stock; }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
