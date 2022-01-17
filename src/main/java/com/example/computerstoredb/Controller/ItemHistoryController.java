package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Models.Cart;
import com.example.computerstoredb.Models.CartItems;
import com.example.computerstoredb.Models.DataAccessor;
import com.example.computerstoredb.Navigation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ItemHistoryController implements Initializable {
    @FXML
    private TableView<CartItems> itemHistoryTableView;

    @FXML
    private TableColumn<CartItems, String> nameColumn;

    @FXML
    private TableColumn<CartItems, Integer> priceColumn;

    @FXML
    private TableColumn<CartItems, Integer> quantityColumn;

    private final ObservableList<CartItems> items = FXCollections.observableArrayList();

    private final Navigation navigation = new Navigation();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    public void toPrevious(ActionEvent event) throws IOException {
        navigation.toHistory(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        itemHistoryTableView.setItems(items);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        String query = "SELECT Products.name, Cart_Item.quantity, Cart_Item.price FROM Cart_Item " +
                        "INNER JOIN Products ON Products.id = Cart_Item.product_id " +
                        "WHERE Cart_Item.cart_id = " + Cart.getInstance().getId();

        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                CartItems cartItem = new CartItems();
                cartItem.setName(resultSet.getString(1));
                cartItem.setQuantity(resultSet.getInt(2));
                cartItem.setPrice(resultSet.getInt(3));
                items.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
