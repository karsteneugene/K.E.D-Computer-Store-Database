package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Models.DataAccessor;
import com.example.computerstoredb.Models.Products;
import com.example.computerstoredb.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemController {
    @FXML
    private ImageView img;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Button buyBtn;

    private final Navigation navigation = new Navigation();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    public void toSelectedProduct(ActionEvent actionEvent) throws IOException, SQLException {

        String query = "SELECT * FROM Products WHERE name = '" + nameLabel.getText() + "'";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            Products.getInstance().setId(resultSet.getInt(1));
            Products.getInstance().setCategoryId(resultSet.getInt(2));
            Products.getInstance().setName(resultSet.getString(3));
            Products.getInstance().setPrice(resultSet.getInt(4));
            Products.getInstance().setStock(resultSet.getInt(5));
            Products.getInstance().setImageSrc(resultSet.getString(6));
        }

        navigation.toSelectedProduct(actionEvent);
    }

    public void setData(Products product) throws MalformedURLException {
        nameLabel.setText(product.getName());
        priceLabel.setText("Rp. " + product.getPrice());
        URL url = new File("src/main/resources/com/example/computerstoredb/images/" + product.getImageSrc()).toURI().toURL();
        Image image = new Image(String.valueOf(url));
        img.setImage(image);
        if (product.getStock() == 0) {
            buyBtn.disableProperty().set(true);
        }
    }
}
