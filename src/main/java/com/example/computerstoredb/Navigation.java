package com.example.computerstoredb;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Navigation {

    public void toLogin(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/login-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void toSignup(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/signup-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void toNextSignup(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/next-signup-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void toAdmin(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/admin-home-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    public void toAdminProducts(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/admin-products-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 576);
        stage.setScene(scene);
        stage.show();
    }

    public void toAdminAddProducts(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/admin-add-products-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    public void toAdminOrders(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/admin-orders-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 576);
        stage.setScene(scene);
        stage.show();
    }

    public void toAdminUsers(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/admin-users-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 576);
        stage.setScene(scene);
        stage.show();
    }

    public void toAdminAddUsers(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/admin-add-users-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    public void toHome(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/home-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 576);
        stage.setScene(scene);
        stage.show();
    }

    public void toProducts(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/product-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 576);
        stage.setScene(scene);
        stage.show();
    }

    public void toSelectedProduct(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/selected-product-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 576);
        stage.setScene(scene);
        stage.show();
    }

    public void toHistory(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/history-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 576);
        stage.setScene(scene);
        stage.show();
    }

    public void toCart(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/cart-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 576);
        stage.setScene(scene);
        stage.show();
    }

    public void toShippingAddress(ActionEvent event) throws IOException {
        URL url = new File("src/main/resources/com/example/computerstoredb/shipping-address-view.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
}
