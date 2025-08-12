package com.example.winejavasql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLTablesViewController {

    @FXML private TableView<Wine> lowAlcoholTable;
    @FXML private TableView<Wine> neutralQualityTable;

    // עמודות לטבלת יינות עם אלכוהול נמוך
    @FXML private TableColumn<Wine, Double> laFixedAcidityColumn;
    @FXML private TableColumn<Wine, Double> laVolatileAcidityColumn;
    @FXML private TableColumn<Wine, Double> laCitricAcidColumn;
    @FXML private TableColumn<Wine, Double> laResidualSugarColumn;
    @FXML private TableColumn<Wine, Double> laChloridesColumn;
    @FXML private TableColumn<Wine, Double> laAlcoholColumn;
    @FXML private TableColumn<Wine, String> laQualityColumn;
    @FXML private TableColumn<Wine, String> laColorColumn;

    // עמודות לטבלת יינות באיכות נייטרלית
    @FXML private TableColumn<Wine, Double> nqFixedAcidityColumn;
    @FXML private TableColumn<Wine, Double> nqVolatileAcidityColumn;
    @FXML private TableColumn<Wine, Double> nqCitricAcidColumn;
    @FXML private TableColumn<Wine, Double> nqResidualSugarColumn;
    @FXML private TableColumn<Wine, Double> nqChloridesColumn;
    @FXML private TableColumn<Wine, Double> nqAlcoholColumn;
    @FXML private TableColumn<Wine, String> nqQualityColumn;
    @FXML private TableColumn<Wine, String> nqColorColumn;

    private ObservableList<Wine> lowAlcoholList = FXCollections.observableArrayList();
    private ObservableList<Wine> neutralQualityList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTables();
        loadWineData();
    }

    private void loadTableData(String tableName, ObservableList<Wine> wineList, TableView<Wine> tableView) {
        String query = "SELECT * FROM " + tableName;

        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                wineList.add(new Wine(
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
            tableView.setItems(wineList);
        } catch (SQLException e) {
            System.err.println("❌ ERROR: Failed to load data from " + tableName);
            e.printStackTrace();
        }
    }

    private void setupTables() {
        // טבלת יינות עם אלכוהול נמוך
        laFixedAcidityColumn.setCellValueFactory(new PropertyValueFactory<>("fixedAcidity"));
        laVolatileAcidityColumn.setCellValueFactory(new PropertyValueFactory<>("volatileAcidity"));
        laCitricAcidColumn.setCellValueFactory(new PropertyValueFactory<>("citricAcid"));
        laResidualSugarColumn.setCellValueFactory(new PropertyValueFactory<>("residualSugar"));
        laChloridesColumn.setCellValueFactory(new PropertyValueFactory<>("chlorides"));
        laAlcoholColumn.setCellValueFactory(new PropertyValueFactory<>("alcohol"));
        laQualityColumn.setCellValueFactory(new PropertyValueFactory<>("quality"));
        laColorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));

        // טבלת יינות באיכות נייטרלית
        nqFixedAcidityColumn.setCellValueFactory(new PropertyValueFactory<>("fixedAcidity"));
        nqVolatileAcidityColumn.setCellValueFactory(new PropertyValueFactory<>("volatileAcidity"));
        nqCitricAcidColumn.setCellValueFactory(new PropertyValueFactory<>("citricAcid"));
        nqResidualSugarColumn.setCellValueFactory(new PropertyValueFactory<>("residualSugar"));
        nqChloridesColumn.setCellValueFactory(new PropertyValueFactory<>("chlorides"));
        nqAlcoholColumn.setCellValueFactory(new PropertyValueFactory<>("alcohol"));
        nqQualityColumn.setCellValueFactory(new PropertyValueFactory<>("quality"));
        nqColorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
    }

    private void loadWineData() {
        loadTableData("new_wine_db.low_alcohol_wines", lowAlcoholList, lowAlcoholTable);
        loadTableData("new_wine_db.neutral_quality_wines", neutralQualityList, neutralQualityTable);
    }
}
