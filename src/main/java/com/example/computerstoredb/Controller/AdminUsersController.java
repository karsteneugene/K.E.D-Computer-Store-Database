package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Models.Customer;
import com.example.computerstoredb.Models.DataAccessor;
import com.example.computerstoredb.Navigation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminUsersController implements Initializable {
    @FXML
    private TableView<Customer> adminUsersTableView;

    @FXML
    private TableColumn<Customer, String> usernameColumn;

    @FXML
    private TableColumn<Customer, String> firstNameColumn;

    @FXML
    private TableColumn<Customer, String> lastNameColumn;

    @FXML
    private TableColumn<Customer, String> phoneNumberColumn;

    @FXML
    private Button deleteUserBtn;

    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    private final Navigation navigation = new Navigation();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    @FXML
    public void toAdminHome(ActionEvent event) throws IOException {
        navigation.toAdmin(event);
    }

    public void toAddUser(ActionEvent event) throws IOException {
        navigation.toAdminAddUsers(event);
    }

    public void deleteUser() throws SQLException {
        Customer customer = adminUsersTableView.getSelectionModel().getSelectedItem();

        String deleteCustomerQuery = "DELETE FROM Customer WHERE id = ?";

        PreparedStatement deleteCustomer = connection.prepareStatement(deleteCustomerQuery);
        deleteCustomer.setInt(1, customer.getId());
        deleteCustomer.executeUpdate();

        String deleteUserQuery = "DELETE FROM Users WHERE id = ?";

        PreparedStatement deleteUser = connection.prepareStatement(deleteUserQuery);
        deleteUser.setInt(1, customer.getUser_id());
        deleteUser.executeUpdate();

        customers.remove(customer);

        adminUsersTableView.getColumns().get(0).setVisible(false);
        adminUsersTableView.getColumns().get(0).setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteUserBtn.disableProperty().bind(adminUsersTableView.getSelectionModel().selectedItemProperty().isNull());
        adminUsersTableView.setItems(customers);

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Customer, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Customer, String> customerStringCellEditEvent) {
                String oldUsername = customerStringCellEditEvent.getOldValue();
                String newUsername = customerStringCellEditEvent.getNewValue();
                Customer customer = customerStringCellEditEvent.getRowValue();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(null);
                alert.setHeaderText(null);

                if (Objects.equals(oldUsername, newUsername)) {
                    return;
                } else if (Objects.equals(newUsername, "")) {
                    alert.setContentText("Cannot leave field blank!");
                    alert.showAndWait();
                } else {

                    String checkUsernameQuery = "SELECT username FROM Users WHERE username = '" + newUsername + "'";

                    Statement statement = null;
                    try {
                        statement = connection.createStatement();
                        ResultSet usernameResultSet = statement.executeQuery(checkUsernameQuery);

                        if (usernameResultSet.next()) {
                            alert.setContentText("Username already exists!");
                            alert.showAndWait();
                        } else {
                            customer.setUsername(newUsername);

                            String updateUsernameQuery = "UPDATE Users SET username = ? WHERE id = ?";

                            PreparedStatement updateUsername = connection.prepareStatement(updateUsernameQuery);
                            updateUsername.setString(1, newUsername);
                            updateUsername.setInt(2, customer.getUser_id());
                            updateUsername.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                customerStringCellEditEvent.getTableView().getColumns().get(0).setVisible(false);
                customerStringCellEditEvent.getTableView().getColumns().get(0).setVisible(true);
            }
        });

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Customer, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Customer, String> customerStringCellEditEvent) {
                String oldFirstName = customerStringCellEditEvent.getOldValue();
                String newFirstName = customerStringCellEditEvent.getNewValue();
                Customer customer = customerStringCellEditEvent.getRowValue();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(null);
                alert.setHeaderText(null);

                if (Objects.equals(oldFirstName, newFirstName)) {
                    return;
                } else if (Objects.equals(newFirstName, "")) {
                    alert.setContentText("Cannot leave field blank!");
                    alert.showAndWait();
                } else {
                    try {
                        customer.setFirstName(newFirstName);

                        String updateFirstNameQuery = "UPDATE Customer SET first_name = ? WHERE id = ?";

                        PreparedStatement updateFirstName = connection.prepareStatement(updateFirstNameQuery);
                        updateFirstName.setString(1, newFirstName);
                        updateFirstName.setInt(2, customer.getId());
                        updateFirstName.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                customerStringCellEditEvent.getTableView().getColumns().get(0).setVisible(false);
                customerStringCellEditEvent.getTableView().getColumns().get(0).setVisible(true);
            }
        });

        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Customer, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Customer, String> customerStringCellEditEvent) {
                String oldLastName = customerStringCellEditEvent.getOldValue();
                String newLastName = customerStringCellEditEvent.getNewValue();
                Customer customer = customerStringCellEditEvent.getRowValue();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(null);
                alert.setHeaderText(null);

                if (Objects.equals(oldLastName, newLastName)) {
                    return;
                } else if (Objects.equals(newLastName, "")) {
                    alert.setContentText("Cannot leave field blank!");
                    alert.showAndWait();
                } else {
                    try {
                        customer.setLastName(newLastName);

                        String updateLastNameQuery = "UPDATE Customer SET last_name = ? WHERE id = ?";

                        PreparedStatement updateLastName = connection.prepareStatement(updateLastNameQuery);
                        updateLastName.setString(1, newLastName);
                        updateLastName.setInt(2, customer.getId());
                        updateLastName.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                customerStringCellEditEvent.getTableView().getColumns().get(0).setVisible(false);
                customerStringCellEditEvent.getTableView().getColumns().get(0).setVisible(true);
            }
        });

        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        phoneNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneNumberColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Customer, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Customer, String> customerStringCellEditEvent) {
                String oldPhoneNo = customerStringCellEditEvent.getOldValue();
                String newPhoneNo = customerStringCellEditEvent.getNewValue();
                Customer customer = customerStringCellEditEvent.getRowValue();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(null);
                alert.setHeaderText(null);

                if (Objects.equals(oldPhoneNo, newPhoneNo)) {
                    return;
                } else if (Objects.equals(newPhoneNo, "")) {
                    alert.setContentText("Cannot leave field blank!");
                    alert.showAndWait();
                } else {

                    String checkPhoneNoQuery = "SELECT phone_no FROM Customer WHERE phone_no = '" + newPhoneNo + "'";

                    Statement statement = null;
                    try {
                        statement = connection.createStatement();
                        ResultSet phoneNoResultSet = statement.executeQuery(checkPhoneNoQuery);

                        if (phoneNoResultSet.next()) {
                            alert.setContentText("Phone number already exists!");
                            alert.showAndWait();
                        } else {
                            customer.setPhoneNo(newPhoneNo);

                            String updatePhoneNoQuery = "UPDATE Customer SET phone_no = ? WHERE id = ?";

                            PreparedStatement updatePhoneNo = connection.prepareStatement(updatePhoneNoQuery);
                            updatePhoneNo.setString(1, newPhoneNo);
                            updatePhoneNo.setInt(2, customer.getId());
                            updatePhoneNo.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                customerStringCellEditEvent.getTableView().getColumns().get(0).setVisible(false);
                customerStringCellEditEvent.getTableView().getColumns().get(0).setVisible(true);
            }
        });

        String getUsersQuery = "SELECT Customer.id, Customer.user_id, Users.username, Users.password, Customer.first_name, Customer.last_name, Customer.phone_no FROM Customer " +
                                "INNER JOIN Users ON Users.id = Customer.user_id " +
                                "WHERE Users.username != 'admin'";

        Statement getUsers = null;
        try {
            getUsers = connection.createStatement();
            ResultSet usersResultSet = getUsers.executeQuery(getUsersQuery);

            while (usersResultSet.next()) {
                Customer customer = new Customer();
                customer.setId(usersResultSet.getInt(1));
                customer.setUser_id(usersResultSet.getInt(2));
                customer.setUsername(usersResultSet.getString(3));
                customer.setPassword(usersResultSet.getString(4));
                customer.setFirstName(usersResultSet.getString(5));
                customer.setLastName(usersResultSet.getString(6));
                customer.setPhoneNo(usersResultSet.getString(7));
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
