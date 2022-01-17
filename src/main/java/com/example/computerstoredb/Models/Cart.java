package com.example.computerstoredb.Models;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cart {
    private static Cart instance = new Cart();
    private int id;
    private int totalPrice;
    private ObservableList<CartItems> items = FXCollections.observableArrayList();

    public Cart() {
    }

    public Cart(int id, int totalPrice) {
        this.id = id;
        this.totalPrice = totalPrice;
    }

    public static Cart getInstance() {
        return instance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ObservableList<CartItems> getCart() {
        return items;
    }

    public void addItem(CartItems cartItems) {
        items.add(cartItems);
    }
}
