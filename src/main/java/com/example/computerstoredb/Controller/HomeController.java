package com.example.computerstoredb.Controller;

import com.example.computerstoredb.UserInterface;
import com.example.computerstoredb.Models.Categories;
import com.example.computerstoredb.Models.DataAccessor;
import com.example.computerstoredb.Navigation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Label userLabel;
    @FXML
    private ListView<Categories> categoriesListView;


    private final ObservableList<Categories> categories = FXCollections.observableArrayList();

    private final Navigation navigation = new Navigation();
    private final UserInterface userInterface = new UserInterface();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    public void toLogin(ActionEvent event) throws IOException {
        navigation.toLogin(event);
    }

    public void toCart(ActionEvent event) throws IOException {
        navigation.toCart(event);
    }

    public void toHistory(ActionEvent event) throws IOException {
        navigation.toHistory(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userInterface.load(userLabel, categoriesListView, categories, connection);
    }
}
