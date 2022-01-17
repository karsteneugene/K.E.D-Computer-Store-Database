package com.example.computerstoredb.Controller;

import com.example.computerstoredb.Models.Categories;
import com.example.computerstoredb.Models.DataAccessor;
import com.example.computerstoredb.Models.Products;
import com.example.computerstoredb.Navigation;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminProductsController implements Initializable {
    @FXML
    private TextField categoryTextField;

    @FXML
    private Button categoryAddBtn;

    @FXML
    private Button categoryUpdateBtn;

    @FXML
    private Button categoryDeleteBtn;

    @FXML
    private ListView<Categories> categoriesListView;

    @FXML
    private TableView<Products> productsTableView;

    @FXML
    private TableColumn<Products, String> nameColumn;

    @FXML
    private TableColumn<Products, Integer> stockColumn;

    @FXML
    private TableColumn<Products, Integer> priceColumn;

    @FXML
    private TableColumn<Products, String> imageSrcColumn;

    @FXML
    private Button updateImgBtn;

    @FXML
    private Button deleteProductBtn;

    private Stage stage;

    private final ObservableList<Categories> categories = FXCollections.observableArrayList();
    private final ObservableList<Products> products = FXCollections.observableArrayList();

    private final Navigation navigation = new Navigation();
    private final FileChooser fileChooser = new FileChooser();

    private final DataAccessor dataAccessor = new DataAccessor();
    private final Connection connection = dataAccessor.getConnection();

    public void toAdminHome(ActionEvent event) throws IOException {
        navigation.toAdmin(event);
    }

    public void addCategory(ActionEvent event) throws SQLException, IOException {
        String newCategory = categoryTextField.getText();

        String checkQuery = "SELECT name FROM Categories WHERE name = '" + newCategory + "'";

        Statement checkCategory = connection.createStatement();
        ResultSet categoryResultSet = checkCategory.executeQuery(checkQuery);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        if (categoryResultSet.next()) {
            alert.setContentText("Category already exists");
            alert.showAndWait();
        } else {
            String insertQuery = "INSERT INTO Categories(name) VALUES(?)";

            PreparedStatement insertCategory = connection.prepareStatement(insertQuery);
            insertCategory.setString(1, newCategory);

            int insertStatus = insertCategory.executeUpdate();
            if (insertStatus == 0) {
                alert.setContentText("Something went wrong");
                alert.showAndWait();
            } else {
                alert.setContentText("Category successfully added!");
                alert.showAndWait();

                navigation.toAdminProducts(event);
            }
        }
    }

    public void updateCategory(ActionEvent event) throws SQLException, IOException {
        String newName = categoryTextField.getText();
        int selectedId = categoriesListView.getSelectionModel().getSelectedItem().getId();

        String checkQuery = "SELECT name FROM Categories WHERE name = '" + newName + "'";

        Statement checkCategory = connection.createStatement();
        ResultSet categoryResultSet = checkCategory.executeQuery(checkQuery);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        if (categoryResultSet.next()) {
            alert.setContentText("Category already exists");
            alert.showAndWait();
        } else {
            String updateQuery = "UPDATE Categories SET name = ? WHERE id = ?";

            PreparedStatement updateCategory = connection.prepareStatement(updateQuery);
            updateCategory.setString(1, newName);
            updateCategory.setInt(2, selectedId);

            int updateStatus = updateCategory.executeUpdate();
            if (updateStatus == 0) {
                alert.setContentText("Something went wrong");
                alert.showAndWait();
            } else {
                alert.setContentText("Category successfully updated!");
                alert.showAndWait();

                navigation.toAdminProducts(event);
            }
        }
    }

    public void deleteCategory(ActionEvent event) throws SQLException, IOException {
        int selectedId = categoriesListView.getSelectionModel().getSelectedItem().getId();

        String deleteQuery = "DELETE FROM Categories WHERE id = ?";

        PreparedStatement deleteCategory;
        deleteCategory = connection.prepareStatement(deleteQuery);
        deleteCategory.setInt(1, selectedId);
        int deleteStatus = deleteCategory.executeUpdate();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        if (deleteStatus == 0) {
            alert.setContentText("Something went wrong");
            alert.showAndWait();
        } else {
            alert.setContentText("Category successfully deleted!");
            alert.showAndWait();

            navigation.toAdminProducts(event);
        }
    }

    public void toAddProduct(ActionEvent event) throws IOException {
        navigation.toAdminAddProducts(event);
    }

    public void updateImage() throws SQLException {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
        );
        Products product = productsTableView.getSelectionModel().getSelectedItem();
        String projectDir = System.getProperty("user.dir");
        fileChooser.setInitialDirectory(new File(projectDir + "/src/main/resources/com/example/computerstoredb/images"));
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        } else {
            String imgSrc = file.getName();
            product.setImageSrc(imgSrc);

            String updateImageQuery = "UPDATE Products SET image = ? WHERE id = ?";

            PreparedStatement updateImage = connection.prepareStatement(updateImageQuery);
            updateImage.setString(1, imgSrc);
            updateImage.setInt(2, product.getId());
            updateImage.executeUpdate();
        }
        productsTableView.getColumns().get(0).setVisible(false);
        productsTableView.getColumns().get(0).setVisible(true);
    }

    public void deleteProduct() throws SQLException {
        Products product = productsTableView.getSelectionModel().getSelectedItem();

        String deleteQuery = "DELETE FROM Products WHERE id = ?";

        PreparedStatement deleteProduct = connection.prepareStatement(deleteQuery);
        deleteProduct.setInt(1, product.getId());
        int deleteStatus = deleteProduct.executeUpdate();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        if (deleteStatus == 0) {
            alert.setContentText("Something went wrong!");
            alert.showAndWait();
        } else {
            products.remove(product);
        }
        productsTableView.getColumns().get(0).setVisible(false);
        productsTableView.getColumns().get(0).setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryAddBtn.disableProperty().bind(Bindings.isEmpty(categoryTextField.textProperty()));
        categoryUpdateBtn.disableProperty().bind(categoriesListView.getSelectionModel().selectedItemProperty().isNull());
        categoryDeleteBtn.disableProperty().bind(categoriesListView.getSelectionModel().selectedItemProperty().isNull());
        updateImgBtn.disableProperty().bind(productsTableView.getSelectionModel().selectedItemProperty().isNull());
        deleteProductBtn.disableProperty().bind(productsTableView.getSelectionModel().selectedItemProperty().isNull());
        categoriesListView.setItems(categories);
        productsTableView.setItems(products);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Products, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Products, String> productsStringCellEditEvent) {
                String oldName = productsStringCellEditEvent.getOldValue();
                String newName = productsStringCellEditEvent.getNewValue();
                Products product = productsStringCellEditEvent.getRowValue();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(null);
                alert.setHeaderText(null);
                if (Objects.equals(oldName, newName)) {
                    return;
                } else if (Objects.equals(newName, "")) {
                    alert.setContentText("Cannot leave field blank!");
                    alert.showAndWait();
                } else {

                    String checkNameQuery = "SELECT name FROM Products WHERE name = '" + newName + "'";

                    Statement statement = null;
                    try {
                        statement = connection.createStatement();
                        ResultSet nameResultSet = statement.executeQuery(checkNameQuery);

                        if (nameResultSet.next()) {
                            alert.setContentText("Product name already exists!");
                            alert.showAndWait();
                            product.setName(oldName);
                        } else {
                            product.setName(newName);

                            String updateNameQuery = "UPDATE Products SET name = ? WHERE id = ?";

                            PreparedStatement updateName = connection.prepareStatement(updateNameQuery);
                            updateName.setString(1, newName);
                            updateName.setInt(2, product.getId());
                            updateName.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                productsStringCellEditEvent.getTableView().getColumns().get(0).setVisible(false);
                productsStringCellEditEvent.getTableView().getColumns().get(0).setVisible(true);
            }
        });

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        priceColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Products, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Products, Integer> productsIntegerCellEditEvent) {
                int oldPrice = productsIntegerCellEditEvent.getOldValue();
                int newPrice = productsIntegerCellEditEvent.getNewValue();
                Products product = productsIntegerCellEditEvent.getRowValue();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(null);
                alert.setHeaderText(null);
                if (newPrice == oldPrice) {
                    return;
                } else if (newPrice <= 0) {
                    alert.setContentText("Price cannot be less than or equal to 0!");
                    alert.showAndWait();
                } else {
                    product.setPrice(newPrice);

                    String updatePriceQuery = "UPDATE Products SET price = ? WHERE id = ?";

                    try {
                        PreparedStatement updatePrice = connection.prepareStatement(updatePriceQuery);
                        updatePrice.setInt(1, newPrice);
                        updatePrice.setInt(2, product.getId());
                        updatePrice.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                productsIntegerCellEditEvent.getTableView().getColumns().get(0).setVisible(false);
                productsIntegerCellEditEvent.getTableView().getColumns().get(0).setVisible(true);
            }
        });

        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        stockColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Products, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Products, Integer> productsIntegerCellEditEvent) {
                int oldStock = productsIntegerCellEditEvent.getOldValue();
                int newStock = productsIntegerCellEditEvent.getNewValue();
                Products product = productsIntegerCellEditEvent.getRowValue();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(null);
                alert.setHeaderText(null);
                if (newStock == oldStock) {
                    return;
                } else if (newStock <= 0) {
                    alert.setContentText("Stock cannot be less than or equal to 0!");
                    alert.showAndWait();
                } else {
                    product.setStock(newStock);

                    String updateStockQuery = "UPDATE Products SET stock = ? WHERE id = ?";

                    try {
                        PreparedStatement updateStock = connection.prepareStatement(updateStockQuery);
                        updateStock.setInt(1, newStock);
                        updateStock.setInt(2, product.getId());
                        updateStock.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                productsIntegerCellEditEvent.getTableView().getColumns().get(0).setVisible(false);
                productsIntegerCellEditEvent.getTableView().getColumns().get(0).setVisible(true);
            }
        });

        imageSrcColumn.setCellValueFactory(new PropertyValueFactory<>("imageSrc"));

        categoriesListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (categoriesListView.getSelectionModel().isEmpty()) {
                    return;
                } else {
                    products.clear();
                    int categoryId = categoriesListView.getSelectionModel().getSelectedItem().getId();
                    String getProductQuery = "SELECT * FROM Products WHERE categories_id = " + categoryId;

                    Statement statement1 = null;
                    try {
                        statement1 = connection.createStatement();
                        ResultSet productsResultSet = statement1.executeQuery(getProductQuery);

                        while (productsResultSet.next()) {
                            Products product = new Products();
                            product.setId(productsResultSet.getInt(1));
                            product.setCategoryId(productsResultSet.getInt(2));
                            product.setName(productsResultSet.getString(3));
                            product.setPrice(productsResultSet.getInt(4));
                            product.setStock(productsResultSet.getInt(5));
                            product.setImageSrc(productsResultSet.getString(6));
                            products.add(product);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    productsTableView.getColumns().get(0).setVisible(false);
                    productsTableView.getColumns().get(0).setVisible(true);
                }
            }
        });

        String getCategoryQuery = "SELECT * FROM Categories";

        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet categoriesResultSet = statement.executeQuery(getCategoryQuery);

            while (categoriesResultSet.next()) {
                Categories category = new Categories();
                category.setId(categoriesResultSet.getInt(1));
                category.setName(categoriesResultSet.getString(2));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
