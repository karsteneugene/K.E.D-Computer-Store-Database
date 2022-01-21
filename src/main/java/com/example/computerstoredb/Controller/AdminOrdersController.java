package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Models.DataAccessor;
import com.example.computerstoredb.Models.Orders;
import com.example.computerstoredb.Navigation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AdminOrdersController implements Initializable {
    @FXML
    private TextField filterTextField;

    @FXML
    private TableView<Orders> adminHistoryTableView;

    @FXML
    private TableColumn<Orders, String> nameColumn;

    @FXML
    private TableColumn<Orders, String> addressColumn;

    @FXML
    private TableColumn<Orders, Date> orderDateColumn;

    @FXML
    private TableColumn<Orders, Integer> subtotalColumn;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private final ObservableList<Orders> ordersHistories = FXCollections.observableArrayList();

    private final Navigation navigation = new Navigation();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    public void toAdminHome(ActionEvent event) throws IOException {
        navigation.toAdmin(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adminHistoryTableView.setItems(ordersHistories);
        nameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getFirstName() + " "
                + p.getValue().getLastName()));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        subtotalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        String getOrdersQuery = "SELECT Orders.customer_id, Orders.shipping_address_id, Orders.cart_id, " +
                "Customer.first_name, Customer.last_name, Shipping_Address.address, Orders.order_date, Cart.total_price " +
                "FROM Orders " +
                "INNER JOIN Customer ON Customer.id = Orders.customer_id " +
                "INNER JOIN Shipping_Address ON Shipping_Address.id = Orders.shipping_address_id " +
                "INNER JOIN Cart ON Cart.id = Orders.cart_id";

        Statement getOrders = null;
        try {
            getOrders = connection.createStatement();
            ResultSet resultSet = getOrders.executeQuery(getOrdersQuery);

            while (resultSet.next()) {
                Orders order = new Orders();
                order.setCustomerId(resultSet.getInt(1));
                order.setShippingAddressId(resultSet.getInt(2));
                order.setCartId(resultSet.getInt(3));
                order.setFirstName(resultSet.getString(4));
                order.setLastName(resultSet.getString(5));
                order.setAddress(resultSet.getString(6));
                order.setOrderDate(resultSet.getDate(7));
                order.setTotalPrice(resultSet.getInt(8));
                ordersHistories.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adminHistoryTableView.setRowFactory(adminHistoryTableView -> {
            TableRow<Orders> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        if (adminHistoryTableView.getSelectionModel().isEmpty()) {
                            return;
                        } else {
                            Orders.getInstance().setCartId(adminHistoryTableView.getSelectionModel().getSelectedItem().getCartId());
                            Orders.getInstance().setFirstName(adminHistoryTableView.getSelectionModel().getSelectedItem().getFirstName());
                            Orders.getInstance().setLastName(adminHistoryTableView.getSelectionModel().getSelectedItem().getLastName());
                            Orders.getInstance().setOrderDate(adminHistoryTableView.getSelectionModel().getSelectedItem().getOrderDate());
                            if (mouseEvent.getClickCount() == 2) {
                                URL url = null;
                                try {
                                    url = new File("src/main/resources/com/example/computerstoredb/admin-selected-order-view.fxml").toURI().toURL();
                                    root = FXMLLoader.load(url);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                                scene = new Scene(root);
                                stage.setScene(scene);
                                stage.show();
                            }
                        }
                    }
                }
            });
            return row;
        });
    }
}
