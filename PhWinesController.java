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

public class PhWinesController {

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

    @FXML private Button backButton;
    @FXML private Button filterButton;
    @FXML private TextField phInputField;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        setupTable();
        filterButton.setOnAction(e -> loadPhFilteredData());
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
    }

    private void loadPhFilteredData() {
        String phValue = phInputField.getText().trim();
        if (phValue.isEmpty()) {
            errorLabel.setText("❌ Please enter a pH value!");
            return;
        }

        try {
            double pH = Double.parseDouble(phValue);
            ObservableList<Wine> wines = FXCollections.observableArrayList();
            String query = "SELECT * FROM wine WHERE `pH` BETWEEN ? AND ?";

            try (Connection conn = DatabaseConnector.connect();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setDouble(1, pH - 0.05);
                stmt.setDouble(2, pH + 0.05);
                ResultSet rs = stmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    errorLabel.setText("❌ No wines found for pH: " + pH);
                    return;
                }

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
                errorLabel.setText("");

            } catch (SQLException e) {
                errorLabel.setText("❌ Database error!");
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("❌ Invalid pH value! Enter a number.");
        }
    }


    private void closeWindow() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}