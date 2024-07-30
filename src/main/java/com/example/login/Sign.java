package com.example.login;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class Sign extends Login {
    @FXML
    private TextField FullName;
    @FXML
    private TextField Email;
    @FXML
    private PasswordField Password;
    @FXML
    private PasswordField ConfirmPassword;
    @FXML
    private Button signButton;
    @FXML
    private Button backButton;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @FXML
    private void initialize() {
        signButton.setOnAction(actionEvent -> handleSign());
        backButton.setOnAction(actionEvent -> handleLogin());
    }

    private void handleSign() {
        String fullName = FullName.getText();
        String email = Email.getText();
        String password = Password.getText();
        String confirmPassword = ConfirmPassword.getText();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Không được để chống các mục");
        } else if (fullName.length() < 8) {
            showAlert("Tên không được dưới 8 kí tự");
        } else if (password.length() < 8) {
            showAlert("Password không được dưới 8 kí tự");
        } else if (!isValidEmail(email)) {
            showAlert("Địa chỉ email không hợp lệ.");
        } else if (!password.equals(confirmPassword)) {
            showAlert("Mật Khẩu không khớp");
        } else {
            showAlert("Đăng kí thành công");
            Login login = new Sign();
            login.getUsers().add(new String[]{fullName, password, email});
            FullName.clear();
            Password.clear();
            Email.clear();
            ConfirmPassword.clear();


        }
    }

    private void handleLogin() {
        try {
            Main.changeScene("Login.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
    private boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }
}