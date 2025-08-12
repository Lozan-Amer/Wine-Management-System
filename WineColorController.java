package com.example.winejavasql;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WineColorController {
    @FXML private Label whiteWineLabel;
    @FXML private Label redWineLabel;
    @FXML private Label totalWineLabel;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        loadWineStatistics();
        backButton.setOnAction(e -> backToMain());
    }

    private void loadWineStatistics() {
        String queryWhite = "SELECT COUNT(*) FROM wine WHERE color = 'white'";
        String queryRed = "SELECT COUNT(*) FROM wine WHERE color = 'red'";
        String queryTotal = "SELECT COUNT(*) FROM wine";

        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement()) {

            try (ResultSet rsWhite = stmt.executeQuery(queryWhite)) {
                if (rsWhite.next()) {
                    whiteWineLabel.setText("White Wines: " + rsWhite.getInt(1));
                }
            }

            try (ResultSet rsRed = stmt.executeQuery(queryRed)) {
                if (rsRed.next()) {
                    redWineLabel.setText("Red Wines: " + rsRed.getInt(1));
                }
            }

            try (ResultSet rsTotal = stmt.executeQuery(queryTotal)) {
                if (rsTotal.next()) {
                    totalWineLabel.setText("Total Wines: " + rsTotal.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void backToMain() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
