module com.example.winejavasql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;  // ✅ הוספת מודול java.sql
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.winejavasql to javafx.fxml;
    exports com.example.winejavasql;
}
