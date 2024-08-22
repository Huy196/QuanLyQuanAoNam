package com.example.login;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserInfoController {
    @FXML
    private VBox userInfoVBox;
    @FXML
    private Button closeButton;
    @FXML
    private Button Sign_in;

    private static final String USER_INFO_FILE = "user_info.txt";
    private Map<String, TextField> fieldMap = new HashMap<>();
    @FXML
    public void initialize() {
        loadUserInfo();
    }

    private void loadUserInfo() {

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_INFO_FILE))) {

            userInfoVBox.setSpacing(10);
            userInfoVBox.setPadding(new Insets(10));

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10); // Khoảng cách giữa các cột
            gridPane.setVgap(10); // Khoảng cách giữa các hàng
            gridPane.setPadding(new Insets(10));

            String line;
            int rowIndex = 0;
            gridPane.getChildren().clear();
            while ((line = reader.readLine()) != null) {
                // Tách tên trường và giá trị
                String[] parts = line.split(": ", 2);
                if (parts.length == 2) {
                    // Tạo Label cho tên trường
                    Label fieldLabel = new Label(parts[0] + ":");
                    fieldLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                    // Tạo TextField cho giá trị trường
                    TextField valueField = new TextField(parts[1]);
                    valueField.setStyle("-fx-font-size: 14px;");

                    // Thêm Label và TextField vào GridPane ở hàng hiện tại
                    gridPane.add(fieldLabel, 0, rowIndex);
                    gridPane.add(valueField, 1, rowIndex);

                    // Lưu TextField vào fieldMap để dễ dàng cập nhật sau này
                    fieldMap.put(parts[0], valueField);

                    // Tăng chỉ số hàng để thêm các thành phần vào hàng tiếp theo
                    rowIndex++;
                }
            }
            userInfoVBox.getChildren().clear();
            userInfoVBox.getChildren().add(gridPane);

            Button updateButton = new Button("Lưu");
            updateButton.setOnAction(e -> saveUpdatedUserInfo());
            userInfoVBox.getChildren().add(updateButton);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Không thể đọc thông tin người dùng.");
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
    private void saveUpdatedUserInfo() {
        try (FileWriter fileWriter = new FileWriter("user_info.txt", false);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            for (Map.Entry<String, TextField> entry : fieldMap.entrySet()) {
                String fieldName = entry.getKey();
                TextField valueField = entry.getValue();

                printWriter.println(fieldName + ": " + valueField.getText());
            }
            showSuccessMessage("Cập nhật thành công !");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Lỗi !");
        }
    }


    private void showErrorMessage(String message) {
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        userInfoVBox.getChildren().add(errorLabel);
    }
    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
