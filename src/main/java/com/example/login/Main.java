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
    public void start(Stage primaryStage){
    Main.primaryStage = primaryStage;
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css.css")).toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeScene(String fxml) throws IOException {
        try {
            Parent pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(fxml)));
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("css.css")).toExternalForm());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
