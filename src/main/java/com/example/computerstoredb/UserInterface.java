package com.example.computerstoredb;

import com.example.computerstoredb.Models.Categories;
import com.example.computerstoredb.Models.Customer;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class UserInterface {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void load(Label label, ListView<Categories> listView, ObservableList<Categories> list, Connection connection) {
        label.setText(String.format("%s %s", Customer.getInstance().getFirstName(), Customer.getInstance().getLastName()));
        listView.setItems(list);

        String query = "SELECT * FROM Categories";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Categories category = new Categories(resultSet.getInt(1), resultSet.getString(2));
                list.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (listView.getSelectionModel().isEmpty()) {
                    return;
                } else {
                    Categories.getInstance().setId(listView.getSelectionModel().getSelectedItem().getId());
                    Categories.getInstance().setName(listView.getSelectionModel().getSelectedItem().getName());
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        if (mouseEvent.getClickCount() == 2) {
                            URL url = null;
                            try {
                                url = new File("src/main/resources/com/example/computerstoredb/product-view.fxml").toURI().toURL();
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
    }
}
