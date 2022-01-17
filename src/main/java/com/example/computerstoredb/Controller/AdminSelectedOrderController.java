package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Models.CartItems;
import com.example.computerstoredb.Models.DataAccessor;
import com.example.computerstoredb.Models.Orders;
import com.example.computerstoredb.Navigation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AdminSelectedOrderController implements Initializable {
    @FXML
    private Label userLabel;

    @FXML
    private Label orderDateLabel;

    @FXML
    private TableView<CartItems> adminCartHistoryTableView;

    @FXML
    private TableColumn<CartItems, String> productNameColumn;

    @FXML
    private TableColumn<CartItems, Integer> quantityColumn;

    @FXML
    private TableColumn<CartItems, Integer> totalPriceColumn;

    private final ObservableList<CartItems> items = FXCollections.observableArrayList();

    private final Navigation navigation = new Navigation();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    public void toAdminOrders(ActionEvent event) throws IOException {
        navigation.toAdminOrders(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLabel.setText(Orders.getInstance().getFirstName() + " " + Orders.getInstance().getLastName());
        orderDateLabel.setText(String.valueOf(Orders.getInstance().getOrderDate()));
        adminCartHistoryTableView.setItems(items);
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        String getCartItemsQuery = "SELECT Products.name, Cart_Item.quantity, Cart_Item.price FROM Cart_Item " +
                                    "INNER JOIN Products ON Products.id = Cart_Item.product_id " +
                                    "WHERE Cart_Item.cart_id = " + Orders.getInstance().getCartId();

        Statement getCartItems = null;
        try {
            getCartItems = connection.createStatement();
            ResultSet itemResultSet = getCartItems.executeQuery(getCartItemsQuery);

            while (itemResultSet.next()) {
                CartItems cartItem = new CartItems();
                cartItem.setName(itemResultSet.getString(1));
                cartItem.setQuantity(itemResultSet.getInt(2));
                cartItem.setPrice(itemResultSet.getInt(3));
                items.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
