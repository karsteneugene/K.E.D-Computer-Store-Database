package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Models.Customer;
import com.example.computerstoredb.Models.DataAccessor;
import com.example.computerstoredb.Navigation;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    private TextField firstNameTxtField, lastNameTxtField, phoneNumberTxtField;

    @FXML
    private Button nextBtn;

    Navigation navigation = new Navigation();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    public void toLogin(ActionEvent event) throws IOException {
        Customer.getInstance().setFirstName(null);
        Customer.getInstance().setLastName(null);
        Customer.getInstance().setPhoneNo(null);
        navigation.toLogin(event);
    }

    public void toNextSignup(ActionEvent event) throws IOException {
        Customer.getInstance().setFirstName(firstNameTxtField.getText());
        Customer.getInstance().setLastName(lastNameTxtField.getText());
        Customer.getInstance().setPhoneNo(phoneNumberTxtField.getText());
        navigation.toNextSignup(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstNameTxtField.setText(Customer.getInstance().getFirstName());
        lastNameTxtField.setText(Customer.getInstance().getLastName());
        phoneNumberTxtField.setText(Customer.getInstance().getPhoneNo());
        nextBtn.disableProperty().bind(
                Bindings.isEmpty(firstNameTxtField.textProperty())
                        .or(Bindings.isEmpty(lastNameTxtField.textProperty()))
                        .or(Bindings.isEmpty(phoneNumberTxtField.textProperty()))
        );
    }
}
