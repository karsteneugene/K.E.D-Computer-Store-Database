package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Models.*;
import com.example.computerstoredb.Navigation;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class CartController implements Initializable {
    @FXML
    private TableView<CartItems> cartTableView;

    @FXML
    private TableColumn<CartItems, CartItems> deleteColumn;

    @FXML
    private TableColumn<CartItems, String> nameColumn;

    @FXML
    private TableColumn<CartItems, Integer> priceColumn;

    @FXML
    private Label priceLabel;

    @FXML
    private Button placeOrderBtn;

    @FXML
    private TableColumn<CartItems, Integer> quantityColumn;

    private static final ObservableList<CartItems> cartItems = Cart.getInstance().getCart();

    private final Navigation navigation = new Navigation();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    public static void clearCartItems() {
        cartItems.clear();
    }

    public void toPrevious(ActionEvent event) throws IOException {
        if (Categories.getInstance().getId() == 0) {
            navigation.toHome(event);
        } else {
            navigation.toProducts(event);
        }
    }

    public void placeOrder(ActionEvent event) throws SQLException, IOException {
        Cart.getInstance().setTotalPrice(Integer.parseInt(priceLabel.getText().substring(4)));

        Statement statement = connection.createStatement();
        PreparedStatement insertCart, insertCartItems, updateStock;

        String cartQuery = "INSERT INTO Cart(total_price) VALUES(?)";

        String cartIdQuery = "SELECT id FROM Cart";

        String cartItemQuery = "INSERT INTO Cart_Item(cart_id, product_id, quantity, price) VALUES(?, ?, ?, ?)";

        String stockQuery = "UPDATE Products SET stock = ? WHERE id = ? AND ? > 1";

        insertCart = connection.prepareStatement(cartQuery);
        insertCart.setInt(1, Cart.getInstance().getTotalPrice());
        insertCart.executeUpdate();

        Statement getCartIdQuery = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        ResultSet cartIdResultSet = getCartIdQuery.executeQuery(cartIdQuery);

        cartIdResultSet.afterLast();
        if (cartIdResultSet.previous()) {
            Orders.getInstance().setCartId(cartIdResultSet.getInt(1));
            Cart.getInstance().setId(cartIdResultSet.getInt(1));
        }

        for (CartItems cartItem : cartTableView.getItems()) {
            insertCartItems = connection.prepareStatement(cartItemQuery);
            insertCartItems.setInt(1, Cart.getInstance().getId());
            insertCartItems.setInt(2, cartItem.getProductId());
            insertCartItems.setInt(3, cartItem.getQuantity());
            insertCartItems.setInt(4, cartItem.getPrice());
            insertCartItems.executeUpdate();

            String getStockQuery = "SELECT stock FROM Products WHERE id = " + cartItem.getProductId();
            Statement decrease = connection.createStatement();
            ResultSet resultSet = decrease.executeQuery(getStockQuery);

            int currentStock = 0;
            while (resultSet.next()) {
                currentStock = resultSet.getInt(1);
            }
            int newStock = currentStock - cartItem.getQuantity();

            updateStock = connection.prepareStatement(stockQuery);
            updateStock.setInt(1, newStock);
            updateStock.setInt(2, cartItem.getProductId());
            updateStock.setInt(3, currentStock);
            updateStock.executeUpdate();
        }

        navigation.toShippingAddress(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        placeOrderBtn.disableProperty().bind(Bindings.size(cartItems).isEqualTo(0));
        cartTableView.setItems(cartItems);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        quantityColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<CartItems, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<CartItems, Integer> cartItemsIntegerCellEditEvent) {
                int stock = 0;
                int oldQuantity = cartItemsIntegerCellEditEvent.getOldValue();
                int newQuantity = cartItemsIntegerCellEditEvent.getNewValue();
                CartItems cartItem = cartItemsIntegerCellEditEvent.getRowValue();

                String query = "SELECT stock FROM Products WHERE id = " + cartItemsIntegerCellEditEvent.getTableView().getSelectionModel().getSelectedItem().getProductId();

                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    if (resultSet.next()) {
                        stock = resultSet.getInt(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(null);
                alert.setHeaderText(null);
                if (newQuantity > stock) {
                    alert.setContentText("Exceeds item quantity");
                    alert.showAndWait();
                } else {
                    cartItem.setQuantity(newQuantity);
                    cartItem.setPrice((cartItem.getPrice() / oldQuantity) * cartItem.getQuantity());
                    int total = 0;
                    for (CartItems cartItems : cartTableView.getItems()) {
                        total = total + cartItems.getPrice();
                    }
                    priceLabel.setText("Rp. " + total);
                }
                cartItemsIntegerCellEditEvent.getTableView().getColumns().get(0).setVisible(false);
                cartItemsIntegerCellEditEvent.getTableView().getColumns().get(0).setVisible(true);
            }
        });

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        AtomicInteger price = new AtomicInteger();
        for (CartItems cartItem : cartItems) {
            price.set(price.get() + cartItem.getPrice());
        }
        priceLabel.setText("Rp. " + price);

        deleteColumn.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );

        deleteColumn.setCellFactory(param -> new TableCell<CartItems, CartItems>() {
            private final Button deleteBtn = new Button("X");

            @Override
            protected void updateItem(CartItems cartItems, boolean empty) {
                super.updateItem(cartItems, empty);

                if (cartItems == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteBtn);
                deleteBtn.setOnAction(event -> {
                    getTableView().getItems().remove(cartItems);
                    price.set(price.get() - cartItems.getPrice());
                    priceLabel.setText("Rp. " + price);
                        }
                );
            }
        });
    }
}
