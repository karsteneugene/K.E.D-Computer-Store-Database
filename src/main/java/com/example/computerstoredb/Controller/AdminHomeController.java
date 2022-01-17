package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Navigation;
import javafx.event.ActionEvent;

import java.io.IOException;

public class AdminHomeController {
    private final Navigation navigation = new Navigation();

    public void toLogin(ActionEvent event) throws IOException {
        navigation.toLogin(event);
    }

    public void toAdminProducts(ActionEvent event) throws IOException {
        navigation.toAdminProducts(event);
    }

    public void toAdminOrders(ActionEvent event) throws IOException {
        navigation.toAdminOrders(event);
    }

    public void toAdminUsers(ActionEvent event) throws IOException {
        navigation.toAdminUsers(event);
    }
}
