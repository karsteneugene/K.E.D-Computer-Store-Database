package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Models.Customer;
import com.example.computerstoredb.Models.DataAccessor;
import com.example.computerstoredb.Navigation;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminAddUsersController implements Initializable {
    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField phoneNoTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button confirmBtn;

    private final Navigation navigation = new Navigation();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    @FXML
    public void toAdminUsers(ActionEvent event) throws IOException {
        navigation.toAdminUsers(event);
    }

    public void addUser(ActionEvent event) throws SQLException, IOException {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String username = usernameTextField.getText();
        String phoneNo = phoneNoTextField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);

        if (!Objects.equals(password, confirmPassword)) {
            alert.setContentText("Passwords are not the same!");
            alert.showAndWait();
        } else {
            String checkUsernameQuery = "SELECT username FROM Users WHERE username = '" + username + "'";

            Statement checkUsername = connection.createStatement();
            ResultSet usernameResultSet = checkUsername.executeQuery(checkUsernameQuery);

            if (usernameResultSet.next()) {
                alert.setContentText("Username is taken!");
                alert.showAndWait();
            } else {
                String insertUserQuery = "INSERT INTO Users(username, password) VALUES(?, ?)";

                PreparedStatement insertUser = connection.prepareStatement(insertUserQuery);
                insertUser.setString(1, username);
                insertUser.setString(2, password);
                insertUser.executeUpdate();

                String getUserIdQuery = "SELECT id FROM Users WHERE username = '" + username + "'";

                Statement getUserId = connection.createStatement();
                ResultSet userIdResultSet = getUserId.executeQuery(getUserIdQuery);

                if (userIdResultSet.next()) {
                    Customer.getInstance().setUser_id(userIdResultSet.getInt(1));

                    String insertCustomerQuery = "INSERT INTO Customer(user_id, first_name, last_name, phone_no) VALUES(?, ?, ?, ?)";

                    PreparedStatement insertCustomer = connection.prepareStatement(insertCustomerQuery);
                    insertCustomer.setInt(1, Customer.getInstance().getUser_id());
                    insertCustomer.setString(2, firstName);
                    insertCustomer.setString(3, lastName);
                    insertCustomer.setString(4, phoneNo);
                    insertCustomer.executeUpdate();

                    Customer.getInstance().setUser_id(0);
                    navigation.toAdminUsers(event);
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmBtn.disableProperty().bind(
                Bindings.isEmpty(firstNameTextField.textProperty())
                        .or(Bindings.isEmpty(lastNameTextField.textProperty()))
                        .or(Bindings.isEmpty(usernameTextField.textProperty()))
                        .or(Bindings.isEmpty(phoneNoTextField.textProperty()))
                        .or(Bindings.isEmpty(passwordField.textProperty()))
                        .or(Bindings.isEmpty(confirmPasswordField.textProperty()))
        );
    }
}
