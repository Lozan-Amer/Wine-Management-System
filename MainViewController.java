package com.example.winejavasql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainViewController {

    @FXML private TableView<Wine> tableView;
    @FXML private TableColumn<Wine, Double> fixedAcidityColumn;
    @FXML private TableColumn<Wine, Double> volatileAcidityColumn;
    @FXML private TableColumn<Wine, Double> citricAcidColumn;
    @FXML private TableColumn<Wine, Double> residualSugarColumn;
    @FXML private TableColumn<Wine, Double> chloridesColumn;
    @FXML private TableColumn<Wine, Double> freeSulfurDioxideColumn;
    @FXML private TableColumn<Wine, Double> totalSulfurDioxideColumn;
    @FXML private TableColumn<Wine, Double> densityColumn;
    @FXML private TableColumn<Wine, Double> pHColumn;
    @FXML private TableColumn<Wine, Double> sulphatesColumn;
    @FXML private TableColumn<Wine, Double> alcoholColumn;
    @FXML private TableColumn<Wine, String> qualityColumn;
    @FXML private TableColumn<Wine, String> colorColumn;

    @FXML private Button showWineButton;
    @FXML private Button columnOptionButton;
    @FXML private Button colorSelectionButton;
    @FXML private Button phFilterButton;
    @FXML private Button columnStatsButton;
    @FXML private Button filterByDateButton;
    @FXML private Button multiThreadingButton;
    @FXML private Button sqlTablesButton;
    @FXML private Button backButton;
    @FXML private ScrollPane tableContainer;

    @FXML
    public void initialize() {
        setupTable();
        setupButtons();
        loadWineData();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
    }

    private void setupButtons() {
        showWineButton.setOnAction(e -> showWineTable());
        columnOptionButton.setOnAction(e -> openColumnSelection());
        colorSelectionButton.setOnAction(e -> openColorSelection());
        phFilterButton.setOnAction(e -> openPhFilter());
        columnStatsButton.setOnAction(e -> openColumnStatistics());
        filterByDateButton.setOnAction(e -> openFilterByDate());
        multiThreadingButton.setOnAction(e -> startMultiThreading());
        sqlTablesButton.setOnAction(e -> openSQLTablesView());
    }

    @FXML
    private void showWineTable() {
        tableContainer.setVisible(true);
        backButton.setVisible(true);
        showWineButton.setVisible(false);
    }

    @FXML
    private void hideWineTable() {
        tableContainer.setVisible(false);
        backButton.setVisible(false);
        showWineButton.setVisible(true);
    }

    @FXML
    private void openSQLTablesView() {
        openNewWindow("/com/example/winejavasql/SQLTablesView.fxml", "SQL Tables");
    }

    @FXML
    private void openColumnSelection() {
        openNewWindow("/com/example/winejavasql/ColumnSelectionView.fxml", "Column Selection");
    }

    @FXML
    private void openColorSelection() {
        openNewWindow("/com/example/winejavasql/WineColorView.fxml", "Wine Color Statistics");
    }

    @FXML
    private void openPhFilter() {
        openNewWindow("/com/example/winejavasql/PhWinesView.fxml", "pH Filtered Wines");
    }

    @FXML
    private void openColumnStatistics() {
        openNewWindow("/com/example/winejavasql/ColumnStatusView.fxml", "Column Statistics");
    }

    @FXML
    private void openFilterByDate() {
        openNewWindow("/com/example/winejavasql/FilterByDateView.fxml", "Filter by Date");
    }

    @FXML
    private void startMultiThreading() {
        new MultiThreadingController().startMultiThreading();
    }


    private void openNewWindow(String fxmlPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            System.err.println("❌ ERROR: Failed to load FXML - " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void loadWineData() {
        ObservableList<Wine> wines = FXCollections.observableArrayList();
        String query = "SELECT * FROM wine.wine";

        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

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
            tableView.setItems(wines);
        } catch (SQLException e) {
            System.err.println("❌ ERROR: Failed to load wine data.");
            e.printStackTrace();
        }
    }
}
