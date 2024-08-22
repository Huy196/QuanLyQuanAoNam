package com.example.login;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class UserInfoController {
    @FXML
    private VBox userInfoVBox;
    @FXML
    private Button closeButton;
    @FXML
    private Button Sign_in;

    private static final String USER_INFO_FILE = "user_info.txt";

    @FXML
    public void initialize() {
        loadUserInfo();
    }

    private void loadUserInfo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_INFO_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Tạo Label cho mỗi dòng thông tin người dùng
                Label userInfoLabel = new Label(line);
                userInfoVBox.getChildren().add(userInfoLabel);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Label errorLabel = new Label("Không thể đọc thông tin người dùng.");
            userInfoVBox.getChildren().add(errorLabel);
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSign() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận đăng xuất");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn đăng xuất không?");
        ButtonType buttonTypeOk = new ButtonType("LOG OUT");
        ButtonType buttonTypeCancel = new ButtonType("CANCEL");

        alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeOk) {
            try {
                Main.changeScene("Login.fxml");
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
