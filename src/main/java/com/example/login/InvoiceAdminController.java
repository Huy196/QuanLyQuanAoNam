package com.example.login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceAdminController {

    @FXML
    private ListView<String> orderListView;
    @FXML
    private Button confirmButton;
    @FXML
    private Button rejectButton;

    @FXML
    public void initialize() {
        // Đọc dữ liệu từ tệp orders.txt và hiển thị lên ListView
        List<String> orders = readOrdersFromFile("orders.txt");
        ObservableList<String> observableOrders = FXCollections.observableArrayList(orders);
        orderListView.setItems(observableOrders);
    }

    private List<String> readOrdersFromFile(String fileName) {
        List<String> orders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                orders.add(line); // Thêm mỗi dòng vào danh sách
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @FXML
    private void handleConfirm() {
        // Xử lý xác nhận đơn hàng
        showAlert("Xác Nhận Đơn Hàng", "Đơn hàng đã được xác nhận.");
        closeWindow();
    }

    @FXML
    private void handleReject() {
        // Xử lý từ chối đơn hàng
        showAlert("Từ Chối Đơn Hàng", "Đơn hàng đã bị từ chối.");
        closeWindow();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}
