package com.example.winejavasql;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashScreenController {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private StackPane rootPane;

    @FXML
    public void initialize() {
        if (rootPane == null) {
            System.out.println("❌ ERROR: rootPane is null! Check fx:id in SplashScreen.fxml.");
            return;
        }

        if (backgroundImage != null) {
            backgroundImage.setImage(new Image(getClass().getResource("/style/fadeinbackground.jpeg").toExternalForm()));
            backgroundImage.setPreserveRatio(false);
        } else {
        }

        fadeInScreen();
    }

    private void fadeInScreen() {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), rootPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setOnFinished(event -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.play();
        });
        fadeIn.play();
    }

    private void closeSplashScreen() {
        Platform.runLater(() -> {
            if (rootPane.getScene() != null && rootPane.getScene().getWindow() != null) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.close();
            } else {
                System.out.println("❌ ERROR: Cannot close splash screen. Scene or Window is null.");
            }
        });
    }
}
