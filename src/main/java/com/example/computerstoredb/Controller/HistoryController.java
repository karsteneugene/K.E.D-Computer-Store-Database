package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Models.*;
import com.example.computerstoredb.Navigation;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {
    @FXML
    private TableView<Orders> historyTableView;

    @FXML
    private TableColumn<Orders, Date> orderDateColumn;

    @FXML
    private TableColumn<Orders, Integer> subtotalColumn;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private final ObservableList<Orders> orders = FXCollections.observableArrayList();

    private final Navigation navigation = new Navigation();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    public void toPrevious(ActionEvent event) throws IOException {
        if (Categories.getInstance().getId() == 0) {
            navigation.toHome(event);
        } else {
            navigation.toProducts(event);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        historyTableView.setItems(orders);
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        subtotalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        String query = "SELECT Orders.customer_id, Orders.cart_id, Orders.order_date, Cart.total_price FROM Orders " +
                            "INNER JOIN Cart ON Cart.id = Orders.cart_id " +
                            "INNER JOIN Customer ON Customer.id = Orders.customer_id " +
                            "WHERE Orders.customer_id = " + Customer.getInstance().getId();

        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Orders order = new Orders();
                order.setCartId(resultSet.getInt(2));
                order.setOrderDate(resultSet.getDate(3));
                order.setTotalPrice(resultSet.getInt(4));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        historyTableView.setRowFactory(historyTableView -> {
            TableRow<Orders> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        if (historyTableView.getSelectionModel().isEmpty()) {
                            return;
                        } else {
                            Cart.getInstance().setId(historyTableView.getSelectionModel().getSelectedItem().getCartId());
                            if (mouseEvent.getClickCount() == 2) {
                                URL url = null;
                                try {
                                    url = new File("src/main/resources/com/example/computerstoredb/item-history-view.fxml").toURI().toURL();
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
