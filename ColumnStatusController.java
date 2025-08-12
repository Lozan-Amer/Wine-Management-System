package com.example.winejavasql;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ColumnStatusController {
    @FXML private Button selectColumnButton;
    @FXML private ComboBox<String> columnDropdown;
    @FXML private Label maxValueLabel;
    @FXML private Label minValueLabel;
    @FXML private Button backButton;

    @FXML
    private void showColumnDropdown() {
        columnDropdown.setVisible(true);
    }

    private final Map<String, String> columnMappings = new HashMap<>() {{
        put("fixed acidity", "fixed acidity");
        put("volatile acidity", "volatile acidity");
        put("citric acid", "citric acid");
        put("residual sugar", "residual sugar");
        put("chlorides", "chlorides");
        put("free sulfur dioxide", "free sulfur dioxide");
        put("total sulfur dioxide", "total sulfur dioxide");
        put("density", "density");
        put("pH", "pH");
        put("sulphates", "sulphates");
        put("alcohol", "alcohol");
    }};

    @FXML
    public void initialize() {
        columnDropdown.setItems(FXCollections.observableArrayList(columnMappings.keySet()));

        selectColumnButton.setOnAction(e -> columnDropdown.setVisible(true));

        // ✅ חישוב מינימום ומקסימום בעת בחירה בעמודה
        columnDropdown.setOnAction(e -> calculateMinMax());

        backButton.setOnAction(e -> goBackToMain());
    }

    @FXML
    private void calculateMinMax() {
        String selectedColumn = columnDropdown.getValue();
        if (selectedColumn != null) {
            String databaseColumn = columnMappings.get(selectedColumn);
            String query = "SELECT MIN(`" + databaseColumn + "`), MAX(`" + databaseColumn + "`) FROM wine";

            try (Connection conn = DatabaseConnector.connect();
                 PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    minValueLabel.setText("Min " + selectedColumn + ": " + rs.getDouble(1));
                    maxValueLabel.setText("Max " + selectedColumn + ": " + rs.getDouble(2));
                }
            } catch (SQLException e) {
                System.out.println("❌ Error fetching min/max: " + e.getMessage());
            }
        }
    }

    @FXML
    private void goBackToMain() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
