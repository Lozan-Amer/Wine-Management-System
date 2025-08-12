package com.example.winejavasql;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MultiThreadingController {

    public void startMultiThreading() {
        Stage stage = new Stage();
        stage.setTitle("Wine Data & Statistics");

        // יצירת Labels להצגת מספר היינות
        Label whiteWineCountLabel = new Label("White: ...");
        Label redWineCountLabel = new Label("Red: ...");

        VBox statisticsBox = new VBox(15, new Label("Wine Counts:"), whiteWineCountLabel, redWineCountLabel);
        statisticsBox.setPrefWidth(400);
        statisticsBox.setPadding(new Insets(20));
        statisticsBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

        // טבלת הנתונים
        TableView<Wine> wineTableView = new TableView<>();
        setupTable(wineTableView);
        wineTableView.setPrefWidth(900);

        // שימוש ב-SplitPane כדי לחלק את המסך לשניים
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(statisticsBox, wineTableView);
        splitPane.setDividerPositions(0.3);

        Scene scene = new Scene(splitPane, 1300, 700);
        stage.setScene(scene);
        stage.show();

        // הרצת השאילתות במקביל
        new Thread(() -> loadWineCounts(whiteWineCountLabel, redWineCountLabel)).start();
        new Thread(() -> loadWineTable(wineTableView)).start();
    }

    private void setupTable(TableView<Wine> tableView) {
        tableView.getColumns().addAll(
                createColumn("Fixed Acidity", "fixedAcidity"),
                createColumn("Volatile Acidity", "volatileAcidity"),
                createColumn("Citric Acid", "citricAcid"),
                createColumn("Residual Sugar", "residualSugar"),
                createColumn("Chlorides", "chlorides"),
                createColumn("Free Sulfur Dioxide", "freeSulfurDioxide"),
                createColumn("Total Sulfur Dioxide", "totalSulfurDioxide"),
                createColumn("Density", "density"),
                createColumn("pH", "pH"),
                createColumn("Sulphates", "sulphates"),
                createColumn("Alcohol", "alcohol"),
                createColumn("Quality", "quality"),
                createColumn("Color", "color")
        );
    }

    private TableColumn<Wine, ?> createColumn(String title, String property) {
        TableColumn<Wine, Object> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    private void loadWineCounts(Label whiteWineCountLabel, Label redWineCountLabel) {
        String query = "SELECT color, COUNT(*) AS count FROM wine GROUP BY color";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            int whiteCount = 0, redCount = 0;
            while (rs.next()) {
                if ("white".equalsIgnoreCase(rs.getString("color"))) {
                    whiteCount = rs.getInt("count");
                } else if ("red".equalsIgnoreCase(rs.getString("color"))) {
                    redCount = rs.getInt("count");
                }
            }

            int finalWhiteCount = whiteCount, finalRedCount = redCount;
            Platform.runLater(() -> {
                whiteWineCountLabel.setText("White: " + finalWhiteCount);
                redWineCountLabel.setText("Red: " + finalRedCount);
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadWineTable(TableView<Wine> tableView) {
        ObservableList<Wine> wines = FXCollections.observableArrayList();
        String query = "SELECT `fixed acidity`, `volatile acidity`, `citric acid`, `residual sugar`, " +
                "`chlorides`, `free sulfur dioxide`, `total sulfur dioxide`, `density`, `pH`, " +
                "`sulphates`, `alcohol`, `quality`, `color` FROM wine ORDER BY entry_date ASC, color ASC";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                wines.add(new Wine(
                        rs.getDouble("fixed acidity"),
                        rs.getDouble("volatile acidity"),
                        rs.getDouble("citric acid"),
                        rs.getDouble("residual sugar"),
                        rs.getDouble("chlorides"),
                        rs.getDouble("free sulfur dioxide"),
                        rs.getDouble("total sulfur dioxide"),
                        rs.getDouble("density"),
                        rs.getDouble("pH"),
                        rs.getDouble("sulphates"),
                        rs.getDouble("alcohol"),
                        rs.getString("quality"),
                        rs.getString("color")
                ));
            }

            Platform.runLater(() -> tableView.setItems(wines));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
