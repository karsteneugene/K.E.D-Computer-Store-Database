package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Models.Products;
import com.example.computerstoredb.UserInterface;
import com.example.computerstoredb.Models.Categories;
import com.example.computerstoredb.Models.DataAccessor;
import com.example.computerstoredb.Navigation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProductController implements Initializable {
    @FXML
    private Label userLabel, categoryLabel;

    @FXML
    private ListView<Categories> categoriesListView;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    private final ObservableList<Categories> categories = FXCollections.observableArrayList();
    private List<Products> products = new ArrayList<>();

    private final Navigation navigation = new Navigation();
    private final UserInterface userInterface = new UserInterface();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    public void toLogin(ActionEvent event) throws IOException {
        navigation.toLogin(event);
    }

    public void toHistory(ActionEvent event) throws  IOException {
        navigation.toHistory(event);
    }

    public void toCart(ActionEvent event) throws IOException {
        navigation.toCart(event);
    }

    private List<Products> getProducts() throws SQLException {
        List<Products> products = new ArrayList<>();
        Products product;

        String query = "SELECT * FROM Products WHERE categories_id = " + Categories.getInstance().getId();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            product = new Products();
            product.setId(resultSet.getInt(1));
            product.setCategoryId(resultSet.getInt(2));
            product.setName(resultSet.getString(3));
            product.setPrice(resultSet.getInt(4));
            product.setStock(resultSet.getInt(5));
            product.setImageSrc(resultSet.getString(6));
            products.add(product);
        }

        return products;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryLabel.setText(Categories.getInstance().getName());

        userInterface.load(userLabel, categoriesListView, categories, connection);

        try {
            products.addAll(getProducts());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int column = 0;
        int row = 1;
        try {
            for (Products product : products) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                URL fxmlUrl = new File("src/main/resources/com/example/computerstoredb/item-view.fxml").toURI().toURL();
                fxmlLoader.setLocation(fxmlUrl);
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(product);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
