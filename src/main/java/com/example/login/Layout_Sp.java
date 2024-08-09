package com.example.login;

import com.example.login.Login;
import com.example.login.Main;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;

import java.lang.reflect.GenericArrayType;

public class Layout_Sp {
    public static com.example.login.Main Main;
    @FXML
    private TextField userName;

    @FXML
    private TextField password;
    private static String[] userInfo;

    @FXML
    private Button signButton;
    @FXML
    private Button backButton;

    @FXML
    private void handleBack() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có muốn đăng xuất?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Đăng xuất");
        alert.setHeaderText(null);
        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                Main.changeScene("Login.fxml");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayUserInfo() {
        if (userInfo != null) {
            System.out.println("Username: " + userInfo[0]);
            System.out.println("Password: " + userInfo[1]);
            System.out.println("Email: " + userInfo[2]);
            System.out.println("SĐT: " + userInfo[3]);
            System.out.println("Role: " + userInfo[4]);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

}
