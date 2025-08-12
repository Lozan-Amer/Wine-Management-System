/*package com.example.winejavasql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WineTable {
    public ObservableList<Wine> loadWineData() {
        ObservableList<Wine> wineList = FXCollections.observableArrayList();
        String query = "SELECT `fixed acidity`, `volatile acidity`, `citric acid`, `residual sugar`, " +
                "`chlorides`, `free sulfur dioxide`, `total sulfur dioxide`, `density`, `pH`, " +
                "`sulphates`, `alcohol`, `quality`, `color` FROM wine";

        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Wine wine = new Wine(
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
                );

                wineList.add(wine);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wineList;
    }
}
*/