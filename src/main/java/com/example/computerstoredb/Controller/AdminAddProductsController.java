package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Models.Categories;
import com.example.computerstoredb.Models.DataAccessor;
import com.example.computerstoredb.Navigation;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AdminAddProductsController implements Initializable {
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField stockTextField;

    @FXML
    private ChoiceBox<Categories> categoryChoiceBox;

    @FXML
    private Button confirmBtn;

    private final ObservableList<Categories> categories = FXCollections.observableArrayList();

    private final Navigation navigation = new Navigation();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    @FXML
    void toAdminProducts(ActionEvent event) throws IOException {
        navigation.toAdminProducts(event);
    }

    public void addProduct(ActionEvent event) throws IOException, SQLException {
        int id = categoryChoiceBox.getValue().getId();
        String name = nameTextField.getText();
        int stock = Integer.parseInt(stockTextField.getText());
        int price = Integer.parseInt(priceTextField.getText());

        String checkDupQuery = "SELECT name FROM Products WHERE name = '" + name + "'";
        String addProductQuery = "INSERT INTO Products(categories_id, name, price, stock, image) VALUES(?, ?, ?, ?, ?)";

        Statement statement = connection.createStatement();
        ResultSet dupResultSet = statement.executeQuery(checkDupQuery);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);

        if (dupResultSet.next()) {
            alert.setContentText("Product is already registered!");
            alert.showAndWait();
        } else {
            PreparedStatement addProduct = connection.prepareStatement(addProductQuery);
            addProduct.setInt(1, id);
            addProduct.setString(2, name);
            addProduct.setInt(3, price);
            addProduct.setInt(4, stock);
            addProduct.setString(5, "default.jpg");
            addProduct.executeUpdate();
        }

        navigation.toAdminProducts(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmBtn.disableProperty().bind(
                Bindings.isEmpty(nameTextField.textProperty())
                        .or(Bindings.isEmpty(priceTextField.textProperty()))
                        .or(Bindings.isEmpty(stockTextField.textProperty()))
                        .or(categoryChoiceBox.valueProperty().isNull())
        );
        categoryChoiceBox.setItems(categories);
        stockTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                stockTextField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (newValue.equals("")) {
                return;
            }
        });
        priceTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                priceTextField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (newValue.equals("")) {
                return;
            }
        });

        String getCategoriesQuery = "SELECT * FROM Categories";

        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet categoriesResultSet = statement.executeQuery(getCategoriesQuery);

            while (categoriesResultSet.next()) {
                Categories category = new Categories();
                category.setId(categoriesResultSet.getInt(1));
                category.setName(categoriesResultSet.getString(2));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
