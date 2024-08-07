package com.example.login;

import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    @FXML
    private TextField textField;
    @FXML
    private Label messageLable;

    private static List<String[]> user = new ArrayList<>();

    public Login() {
    }

    @FXML
    private void initialize() {
        listUser();
        loginButton.setOnAction(actionEvent -> handleLogin());
        signButton.setOnAction(actionEvent -> handleSign());
        if (textField != null) {
            textField.setManaged(false);
            textField.setVisible(false);

            textField.textProperty().bindBidirectional(Password.textProperty());
        }
    }

    public void listUser() {
        user.add(new String[]{"doquochuy", "123456789", "doquochuy@gmail.com", "0743278462", "admin"});
        user.add(new String[]{"levandan1", "987654321", "lavandan1@gmail.com", "0108738465", "user"});
        user.add(new String[]{"huyentrang", "111111111", "huyentrang@gmail.com", "0829746352", "user"});
        user.add(new String[]{"vungocanh", "333333333", "vungocanh@gmail.com", "0849284736", "user"});
        user.add(new String[]{"tiencong1", "555555555", "tiencong1@gmail.com", "9473625174", "user"});
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
                messageLable.setText("Đăng nhập thành công");
                Username.clear();
                Password.clear();
                showUserList();
                try {
                    Main.changeScene("Layout.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                messageLable.setText("sai mật khẩu");

                messageLable.setStyle("-fx-text-fill: red");
                Password.clear();
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

    protected void showUserList() {
        for (String[] user : user) {
            if (user[3].equals("admin")) {
                System.out.println("Username: \"" + user[0] + ",\t" + " Password: " + user[1] + "\t," + "Email: " + user[2] + ",\t\t" + user[3] + ",\t\t" + " Role: " + user[4]);
            } else {
                System.out.println("Username: \"" + user[0] + ",\t" + " Password: " + user[1] + "\t," + "Email: " + user[2] + ",\t\t" + "SĐT : " + user[3] + ",\t\t" + " Role: " + user[4]);
            }
        }
    }
}

