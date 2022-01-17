package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Models.DataAccessor;
import com.example.computerstoredb.Models.Orders;
import com.example.computerstoredb.Models.ShippingAddress;
import com.example.computerstoredb.Navigation;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ShippingAddressController implements Initializable {
    @FXML
    private TextField addressTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField countryTextField;

    @FXML
    private TextField stateTextField;

    @FXML
    private TextField zipcodeTextField;

    @FXML
    private Button confirmBtn;

    private final Navigation navigation = new Navigation();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    public void confirmOrder(ActionEvent event) throws SQLException, IOException {
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddress(addressTextField.getText());
        shippingAddress.setCity(cityTextField.getText());
        shippingAddress.setState(stateTextField.getText());
        shippingAddress.setZipcode(zipcodeTextField.getText());
        shippingAddress.setCountry(countryTextField.getText());

        Statement statement = connection.createStatement();
        PreparedStatement insertShippingAddress;

        String query = "INSERT INTO Shipping_Address(address, city, state, zipcode, country) VALUES(?, ?, ?, ?, ?)";

        insertShippingAddress = connection.prepareStatement(query);
        insertShippingAddress.setString(1, shippingAddress.getAddress());
        insertShippingAddress.setString(2, shippingAddress.getCity());
        insertShippingAddress.setString(3, shippingAddress.getState());
        insertShippingAddress.setString(4, shippingAddress.getZipcode());
        insertShippingAddress.setString(5, shippingAddress.getCountry());
        insertShippingAddress.executeUpdate();

        String shippingIdQuery = "SELECT id FROM Shipping_Address";

        Statement getShippingIdQuery = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = getShippingIdQuery.executeQuery(shippingIdQuery);

        resultSet.afterLast();
        if (resultSet.previous()) {
            Orders.getInstance().setShippingAddressId(resultSet.getInt(1));
        }

        Date dateNow = Date.valueOf(LocalDate.now());
        Orders.getInstance().setOrderDate(dateNow);

        String ordersQuery = "INSERT INTO Orders(customer_id, shipping_address_id, cart_id, order_date) VALUES(?, ?, ?, ?)";

        PreparedStatement insertOrders = connection.prepareStatement(ordersQuery);
        insertOrders.setInt(1, Orders.getInstance().getCustomerId());
        insertOrders.setInt(2, Orders.getInstance().getShippingAddressId());
        insertOrders.setInt(3, Orders.getInstance().getCartId());
        insertOrders.setDate(4, Orders.getInstance().getOrderDate());
        insertOrders.executeUpdate();

        CartController.clearCartItems();
        navigation.toProducts(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmBtn.disableProperty().bind(
                Bindings.isEmpty(addressTextField.textProperty())
                        .or(Bindings.isEmpty(cityTextField.textProperty()))
                        .or(Bindings.isEmpty(countryTextField.textProperty()))
                        .or(Bindings.isEmpty(stateTextField.textProperty()))
                        .or(Bindings.isEmpty(zipcodeTextField.textProperty()))
        );
    }
}
