package com.example.computerstoredb.Models;

public class CartItems {
    private int id;
    private int cartId;
    private int productId;
    private String name;
    private int quantity;
    private int price;

    public CartItems() {
    }

    public CartItems(int id, int cartId, int productId, String name, int quantity, int price) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCartId() { return cartId; }

    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }
}
