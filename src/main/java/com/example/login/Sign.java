package com.example.login;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.lang.annotation.Inherited;
import java.util.regex.Pattern;

public class Sign extends Login {
    @FXML
    private TextField FullName;
    @FXML
    private TextField Email;
    @FXML
    private TextField PhoneNumber;
    @FXML
    private PasswordField Password;
    @FXML
    private PasswordField ConfirmPassword;
    @FXML
    private Button signButton;
    @FXML
    private Button backButton;
    @FXML
    private TextField DiaChi;

    private static final String EMAIL_REGEX = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";
    private static final String PHONE_REGEX = "^(0|84)(2(0[3-9]|1[0-6|8|9]|2[0-2|5-9]|3[2-9]|4[0-9]|5[1|2|4-9]|6[0-3|9]|7[0-7]|8[0-9]|9[0-4|6|7|9])|3[2-9]|5[5|6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])([0-9]{7})$";


    @FXML
    private void initialize() {
        signButton.setOnAction(actionEvent -> handleSign());
        backButton.setOnAction(actionEvent -> handleLogin());


    }

    public void handleSign() {
        String fullName = FullName.getText();
        String email = Email.getText();
        String phoneNumber = PhoneNumber.getText();
        String password = Password.getText();
        String confirmPassword = ConfirmPassword.getText();
        String diaChi = DiaChi.getText();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phoneNumber.isEmpty() || diaChi.isEmpty()) {
            showAlert("Không được để chống các mục");
        } else if (fullName.length() < 8) {
            showAlert("Tên không được dưới 8 kí tự");
        } else if (!isValidEmail(email)) {
            showAlert("Địa chỉ email không hợp lệ.");
        } else if (!isValidPhoneNumber(phoneNumber)) {
            showAlert("Số điện thoại không hợp lệ.");
        } else if (password.length() < 8) {
            showAlert("Password không được dưới 8 kí tự");
        } else if (!password.equals(confirmPassword)) {
            showAlert("Mật Khẩu không khớp");
        } else {
            showAlert("Đăng kí thành công");
            Login login = new Sign();
            login.getUsers().add(new String[]{fullName, password, email, phoneNumber,diaChi,User});

            FullName.clear();
            Password.clear();
            Email.clear();
            PhoneNumber.clear();
            ConfirmPassword.clear();
            DiaChi.clear();

            handleLogin();
        }
    }

    @FXML
    public void handleLogin() {
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

    private boolean isValidPhoneNumber(String phoneNumber) {
        return Pattern.matches(PHONE_REGEX, phoneNumber);
    }

}