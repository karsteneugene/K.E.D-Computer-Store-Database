package com.example.computerstoredb.Models;

public class ShippingAddress {
    private int id;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private String country;

    public ShippingAddress() {
    }

    public ShippingAddress(int id, String address, String city, String state, String zipcode, String country) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
