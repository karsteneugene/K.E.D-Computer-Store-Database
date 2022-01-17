module com.example.computerstoredb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.example.computerstoredb to javafx.fxml;
    exports com.example.computerstoredb;
    exports com.example.computerstoredb.Models;
    opens com.example.computerstoredb.Models to javafx.fxml;
    exports com.example.computerstoredb.Controller;
    opens com.example.computerstoredb.Controller to javafx.fxml;
}