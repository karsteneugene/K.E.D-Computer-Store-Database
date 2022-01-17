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

public class NextSignupController implements Initializable {
    @FXML
    private TextField usernameTxtField;

    @FXML
    private PasswordField passwordField, confirmPasswordField;

    @FXML
    private Button doneBtn;

    Navigation navigation = new Navigation();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    public void toSignup(ActionEvent event) throws IOException {
        navigation.toSignup(event);
    }

    public void toLogin(ActionEvent event) throws IOException, SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);

        if (!Objects.equals(passwordField.getText(), confirmPasswordField.getText())) {
            alert.setContentText("Passwords do not match");
            alert.showAndWait();
        } else {
            String username = usernameTxtField.getText();
            String password = passwordField.getText();

            String checkUsernameQuery = "SELECT username from Users WHERE username = '" + username + "'";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(checkUsernameQuery);

            if (resultSet.next()) {
                alert.setContentText("Account exists");
                alert.showAndWait();
            } else {
                Statement statement1 = connection.createStatement();
                PreparedStatement insertUser;
                PreparedStatement insertCustomer;

                String insertUserQuery = "INSERT INTO Users(username, password) VALUES(?, ?)";

                insertUser = connection.prepareStatement(insertUserQuery);
                insertUser.setString(1, username);
                insertUser.setString(2, password);
                insertUser.executeUpdate();

                String userIdQuery = "SELECT id FROM Users WHERE username = '" + username + "'";
                Statement userIdStatement = connection.createStatement();
                ResultSet userIdResultSet = userIdStatement.executeQuery(userIdQuery);
                if (userIdResultSet.next()) {
                    int userId = userIdResultSet.getInt(1);
                    Customer.getInstance().setUser_id(userId);
                }

                String insertCustomerQuery = "INSERT INTO Customer(first_name, last_name, phone_no, user_id) VALUES(?, ?, ?, ?)";

                insertCustomer = connection.prepareStatement(insertCustomerQuery);
                insertCustomer.setString(1, Customer.getInstance().getFirstName());
                insertCustomer.setString(2, Customer.getInstance().getLastName());
                insertCustomer.setString(3, Customer.getInstance().getPhoneNo());
                insertCustomer.setInt(4, Customer.getInstance().getUser_id());
                insertCustomer.executeUpdate();

                alert.setContentText("Account created!");
                alert.showAndWait();
                Customer.getInstance().setFirstName(null);
                Customer.getInstance().setLastName(null);
                Customer.getInstance().setPhoneNo(null);
                navigation.toLogin(event);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        doneBtn.disableProperty().bind(
                Bindings.isEmpty(usernameTxtField.textProperty())
                        .or(Bindings.isEmpty(passwordField.textProperty()))
                        .or(Bindings.isEmpty(confirmPasswordField.textProperty()))
        );
    }
}
