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
    String User = "User";
    @FXML
    private Button loginButton;
    @FXML
    private Button signButton;
    @FXML
    private TextField textField;
    @FXML
    private Label messageLable;

    public Login(TextField UserName, TextField Password) {
        TextField username = null;
        this.Username = username;
        PasswordField password = null;
        this.Password = password;
    }

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

    void handleLogin() {
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
            String[] loggedInUser = null;
            for (String[] newUser : user) {
                if (newUser[0].equals(username) && newUser[1].equals(password)) {
                    isValid = true;
                    loggedInUser = newUser;
                    break;
                }
            }
            if (isValid) {
                messageLable.setText("Đăng nhập thành công");
                Username.clear();
                Password.clear();
                if (loggedInUser != null) {
                    showUserList(loggedInUser);
                }
                try {
                    if ("admin".equals(loggedInUser[4])) {
                        Main.changeScene("View_Home.fxml");
                    }else {
                        Main.changeScene("ShoppingCar.fxml");
                    }
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

    void handleSign() {
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

    protected void showUserList(String[] loggedInUser) {
        String newUser = loggedInUser[4];
        if (newUser.equals("admin")) {
            System.out.println("Username: \"" + loggedInUser[0] + ",\t\t" + " Password: " + loggedInUser[1] + "\t\t," + "Email: " + loggedInUser[2] + ",\t\t" + "SĐT : " + loggedInUser[3] + ",\t\t" + " Role: " + loggedInUser[4]);

        } else {
            System.out.println("Username: \"" + loggedInUser[0] + ",\t" + " Password: " + loggedInUser[1] + "\t\t," + "Email: " + loggedInUser[2] + ",\t\t" + "SĐT : " + loggedInUser[3] + ",\t\t" + " Role: " + loggedInUser[4]);
        }
    }
}

