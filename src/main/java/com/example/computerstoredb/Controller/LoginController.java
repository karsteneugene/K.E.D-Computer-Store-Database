package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Application;
import com.example.computerstoredb.Models.Customer;
import com.example.computerstoredb.Models.DataAccessor;
import com.example.computerstoredb.Models.Orders;
import com.example.computerstoredb.Navigation;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField usernameTxtField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label signupTxt;

    @FXML
    private Button loginBtn;

    private final Navigation navigation = new Navigation();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    public void toHome(ActionEvent event) throws IOException, SQLException {
        String username = usernameTxtField.getText();
        String password = passwordField.getText();

        String query = "SELECT id, username, password FROM Users WHERE username = '" + username + "'";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        if (resultSet.next()) {
            if (Objects.equals(resultSet.getString(3), password)) {
                int id = resultSet.getInt(1);

                String nameQuery = "SELECT id, first_name, last_name FROM Customer WHERE user_id = " + id;

                Statement statement1 = connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(nameQuery);

                if (resultSet1.next()) {
                    Orders.getInstance().setCustomerId(resultSet1.getInt(1));
                    Customer.getInstance().setId(resultSet1.getInt(1));
                    Customer.getInstance().setFirstName(resultSet1.getString(2));
                    Customer.getInstance().setLastName(resultSet1.getString(3));
                }

                if (Objects.equals(username, "admin")) {
                    navigation.toAdmin(event);
                } else {
                    navigation.toHome(event);
                }
            } else {
                alert.setContentText("Credentials incorrect");
                alert.showAndWait();
            }
        } else {
            alert.setContentText("Credentials incorrect");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginBtn.disableProperty().bind(
                Bindings.isEmpty(usernameTxtField.textProperty())
                        .or(Bindings.isEmpty(passwordField.textProperty()))
        );

        signupTxt.setOnMouseClicked((mouseEvent) -> {
            Customer.getInstance().setFirstName("");
            Customer.getInstance().setLastName("");
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("signup-view.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 600, 400);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });
    }
}