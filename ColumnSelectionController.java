package com.example.winejavasql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnSelectionController {
    @FXML private Button selectColumnButton;
    @FXML private ComboBox<String> columnDropdown;
    @FXML private TableView<String> tableView;
    @FXML private TableColumn<String, String> dataColumn;
    @FXML
    private Button backButton;

    @FXML
    private void goBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    private final ObservableList<String> columnNames = FXCollections.observableArrayList(
            "fixed acidity", "volatile acidity", "citric acid", "residual sugar",
            "chlorides", "free sulfur dioxide", "total sulfur dioxide", "density",
            "pH", "sulphates", "alcohol", "quality", "color"
    );

    @FXML
    public void initialize() {
        columnDropdown.setItems(columnNames);

        selectColumnButton.setOnAction(e -> columnDropdown.setVisible(true));

        // ✅ בעת בחירה בעמודה - טוען את הנתונים לטבלה
        columnDropdown.setOnAction(e -> {
            String selectedColumn = columnDropdown.getValue();
            if (selectedColumn != null) {
                loadColumnData(selectedColumn);
            }
        });
    }

    private void loadColumnData(String columnName) {
        String query = "SELECT `" + columnName + "` FROM wine";
        ObservableList<String> columnDataList = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                columnDataList.add(rs.getString(1));
            }

            dataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

            tableView.setItems(columnDataList);

        } catch (SQLException e) {
            System.out.println("❌ Error fetching column data: " + e.getMessage());
        }
    }
}
