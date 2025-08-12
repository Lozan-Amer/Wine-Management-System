package com.example.winejavasql;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // טעינת מסך הפתיחה
            FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("/com/example/winejavasql/SplashScreen.fxml"));
            Scene splashScene = new Scene(splashLoader.load(), 800, 600);

            Stage splashStage = new Stage();
            splashStage.setScene(splashScene);
            splashStage.initStyle(StageStyle.UNDECORATED); // ללא מסגרת
            splashStage.setFullScreen(true); // מסך מלא
            splashStage.setFullScreenExitHint(""); //
            splashStage.show();

            // השהיית מסך הפתיחה למשך 3 שניות, ואז מעבר לחלון הראשי
            PauseTransition delay = new PauseTransition(Duration.seconds(5));
            delay.setOnFinished(event -> {
                splashStage.close();
                showMainStage();
            });
            delay.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMainStage() {
        try {
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/com/example/winejavasql/MainView.fxml"));
            Scene mainScene = new Scene(mainLoader.load(), 1250, 750);
            mainScene.getStylesheets().add(getClass().getResource("/style/styles.css").toExternalForm());

            Stage mainStage = new Stage();
            mainStage.setScene(mainScene);
            mainStage.setTitle("Wine Database Management");
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
