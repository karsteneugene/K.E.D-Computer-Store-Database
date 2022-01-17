package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Models.Cart;
import com.example.computerstoredb.Models.CartItems;
import com.example.computerstoredb.Models.Products;
import com.example.computerstoredb.Navigation;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class SelectedProductController implements Initializable {
    @FXML
    private ImageView img;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField quantityField;

    @FXML
    private Label stockLabel;

    @FXML
    private Label priceLabel;

    private final Navigation navigation = new Navigation();

    public void toProducts(ActionEvent event) throws IOException {
        navigation.toProducts(event);
    }

    public void increaseQuantity() {
        int currentQuantity = Integer.parseInt(quantityField.getText());
        if (currentQuantity == Integer.parseInt(stockLabel.getText())) {
            quantityField.setText(String.valueOf(currentQuantity));
        } else {
            int newQuantity = currentQuantity + 1;
            quantityField.setText(String.valueOf(newQuantity));
        }
    }

    public void decreaseQuantity() {
        int currentQuantity = Integer.parseInt(quantityField.getText());
        if (currentQuantity == 0) {
            quantityField.setText(String.valueOf(currentQuantity));
        } else {
            int newQuantity = currentQuantity - 1;
            quantityField.setText(String.valueOf(newQuantity));
        }
    }

    public void setCart() {
        CartItems cartItem = new CartItems();
        cartItem.setProductId(Products.getInstance().getId());
        cartItem.setName(Products.getInstance().getName());
        cartItem.setQuantity(Integer.parseInt(quantityField.getText()));
        cartItem.setPrice(Integer.parseInt(priceLabel.getText().substring(4)));
        Cart.getInstance().addItem(cartItem);
    }

    public void addToCart() {
        setCart();
    }

    public void buyNow(ActionEvent event) throws IOException {
        setCart();
        navigation.toCart(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.setText(Products.getInstance().getName());
        priceLabel.setText("Rp. " + Products.getInstance().getPrice());
        stockLabel.setText(String.valueOf(Products.getInstance().getStock()));
        try {
            URL imgUrl = new File("src/main/resources/com/example/computerstoredb/images/" + Products.getInstance().getImageSrc()).toURI().toURL();
            Image image = new Image(String.valueOf(imgUrl));
            img.setImage(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        quantityField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantityField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (newValue.equals("")) {
                return;
            } else if (Integer.parseInt(newValue) > Integer.parseInt(stockLabel.getText())) {
                quantityField.setText(String.valueOf(stockLabel.getText()));
            }
            int singlePrice = Products.getInstance().getPrice();
            int newPrice = singlePrice * Integer.parseInt(quantityField.getText());
            priceLabel.setText("Rp. " + newPrice);
        });


    }
}
