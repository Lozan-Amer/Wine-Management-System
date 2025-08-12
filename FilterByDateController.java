package com.example.winejavasql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class FilterByDateController {

    @FXML private TableView<WineWithDate> tableView;
    @FXML private TableColumn<WineWithDate, Double> fixedAcidityColumn;
    @FXML private TableColumn<WineWithDate, Double> volatileAcidityColumn;
    @FXML private TableColumn<WineWithDate, Double> citricAcidColumn;
    @FXML private TableColumn<WineWithDate, Double> residualSugarColumn;
    @FXML private TableColumn<WineWithDate, Double> chloridesColumn;
    @FXML private TableColumn<WineWithDate, Double> freeSulfurDioxideColumn;
    @FXML private TableColumn<WineWithDate, Double> totalSulfurDioxideColumn;
    @FXML private TableColumn<WineWithDate, Double> densityColumn;
    @FXML private TableColumn<WineWithDate, Double> pHColumn;
    @FXML private TableColumn<WineWithDate, Double> sulphatesColumn;
    @FXML private TableColumn<WineWithDate, Double> alcoholColumn;
    @FXML private TableColumn<WineWithDate, String> qualityColumn;
    @FXML private TableColumn<WineWithDate, String> colorColumn;
    @FXML private TableColumn<WineWithDate, LocalDate> entryDateColumn; // ✅ שם נכון ב-Java

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Button filterButton;
    @FXML private Button backButton;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        setupTable();
        loadAllData();

        filterButton.setOnAction(e -> filterData());
        backButton.setOnAction(e -> closeWindow());
    }

    private void setupTable() {
        fixedAcidityColumn.setCellValueFactory(new PropertyValueFactory<>("fixedAcidity"));
        volatileAcidityColumn.setCellValueFactory(new PropertyValueFactory<>("volatileAcidity"));
        citricAcidColumn.setCellValueFactory(new PropertyValueFactory<>("citricAcid"));
        residualSugarColumn.setCellValueFactory(new PropertyValueFactory<>("residualSugar"));
        chloridesColumn.setCellValueFactory(new PropertyValueFactory<>("chlorides"));
        freeSulfurDioxideColumn.setCellValueFactory(new PropertyValueFactory<>("freeSulfurDioxide"));
        totalSulfurDioxideColumn.setCellValueFactory(new PropertyValueFactory<>("totalSulfurDioxide"));
        densityColumn.setCellValueFactory(new PropertyValueFactory<>("density"));
        pHColumn.setCellValueFactory(new PropertyValueFactory<>("pH"));
        sulphatesColumn.setCellValueFactory(new PropertyValueFactory<>("sulphates"));
        alcoholColumn.setCellValueFactory(new PropertyValueFactory<>("alcohol"));
        qualityColumn.setCellValueFactory(new PropertyValueFactory<>("quality"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        entryDateColumn.setCellValueFactory(new PropertyValueFactory<>("entryDate"));
    }

    private void loadAllData() {
        loadFilteredData(null, null);
    }

    private void filterData() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        loadFilteredData(startDate, endDate);
    }

    private void loadFilteredData(LocalDate startDate, LocalDate endDate) {
        ObservableList<WineWithDate> wines = FXCollections.observableArrayList();
        String query = "SELECT * FROM wine";

        boolean hasStart = startDate != null;
        boolean hasEnd = endDate != null;

        if (hasStart && hasEnd) {
            query += " WHERE entry_date BETWEEN ? AND ?";
        } else if (hasStart) {
            query += " WHERE entry_date >= ?";
        } else if (hasEnd) {
            query += " WHERE entry_date <= ?";
        }

        query += " ORDER BY entry_date ASC"; // ✅ מיון ב-SQL

        tableView.getItems().clear();

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int paramIndex = 1;
            if (hasStart) stmt.setDate(paramIndex++, java.sql.Date.valueOf(startDate));
            if (hasEnd) stmt.setDate(paramIndex, java.sql.Date.valueOf(endDate));

            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                if (messageLabel != null) {
                    messageLabel.setText("❌ No results found.");
                }
                return;
            }

            if (messageLabel != null) {
                messageLabel.setText("");
            }

            while (rs.next()) {
                wines.add(new WineWithDate(
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
                        rs.getString("color"),
                        rs.getDate("entry_date") != null ? rs.getDate("entry_date").toLocalDate() : null
                ));
            }

            tableView.setItems(wines);

            entryDateColumn.setSortType(TableColumn.SortType.ASCENDING);
            tableView.getSortOrder().clear();
            tableView.getSortOrder().add(entryDateColumn);
            tableView.sort();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void closeWindow() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
