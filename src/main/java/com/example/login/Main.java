package com.example.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {

        try {
            Main.primaryStage = primaryStage;
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
            root.getStylesheets().add(getClass().getResource("css.css").toExternalForm());
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeScene(String fxml) throws Exception {
        Parent pane = FXMLLoader.load(Main.class.getResource(fxml));
        primaryStage.setScene(new Scene(pane));
        pane.getStylesheets().add(Main.class.getResource("css.css").toExternalForm());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
