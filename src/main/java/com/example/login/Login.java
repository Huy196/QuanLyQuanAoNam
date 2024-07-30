package com.example.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Login {
    @FXML
    private TextField Username;
    @FXML
    private PasswordField Password;
    @FXML
    private Button loginButton;
    @FXML
    private Button signButton;

    private static List<String[]> user = new ArrayList<>();

    @FXML
    private void initialize() {
        listUser();
        loginButton.setOnAction(actionEvent -> handleLogin());
        signButton.setOnAction(actionEvent -> handleSign());
    }

    public void listUser() {
        user.add(new String[]{"doquochuy", "123456789"});
        user.add(new String[]{"levandan1", "987654321"});
        user.add(new String[]{"huyentrang", "111111111"});
        user.add(new String[]{"vungocanh", "333333333"});
        user.add(new String[]{"tiencong1", "555555555"});
    }

    private void handleLogin() {
        String username = Username.getText();
        String password = Password.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Tên đăng nhập và mật khẩu ko đc để trống");
        } else if (username.length() < 8) {
            showAlert("Tên đăng nhập phải trên 8 kí tự");
        } else if (password.length() < 8) {
            showAlert("Mật khẩu phải trên 8 kí tự");
        } else {
            boolean isValid = false;
            for (String[] newUser : user) {
                if (newUser[0].equals(username) && newUser[1].equals(password)) {
                    isValid = true;
                    break;
                }
            }
            if (isValid) {
                showAlert("Đăng nhập thành công");
                Username.clear();
                Password.clear();
                showUserList();
            } else {
                showAlert("Đăng nhập thất bại !");
            }
        }
    }

    private void handleSign() {
        try {
            Main.changeScene("Sign.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String mesage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mesage);
        alert.show();
    }
    protected List<String[]> getUsers() {
        return user;
    }

    protected void showUserList(){
        for (String[] user : user){
            System.out.println(user);
        }
    }
}

