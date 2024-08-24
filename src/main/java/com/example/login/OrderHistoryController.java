package com.example.login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderHistoryController implements Initializable {

    @FXML
    private ListView<String> orderHistoryListView; // Hiển thị danh sách chi tiết đơn hàng

    @FXML
    private TextArea orderDetailTextArea; // Hiển thị chi tiết đơn hàng khi chọn

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load orders when the controller is initialized
        refreshOrderList();

        // Handle the selection of an order from the ListView
        orderHistoryListView.setOnMouseClicked((MouseEvent event) -> {
            String selectedOrder = orderHistoryListView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                orderDetailTextArea.setText(selectedOrder);
            }
        });
    }
    @FXML
    public void refreshOrderList() {
        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            ObservableList<String> orders = FXCollections.observableArrayList();
            StringBuilder currentOrder = new StringBuilder();
            boolean inOrderDetails = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Khi gặp dấu phân cách "--------", hoàn tất đơn hàng hiện tại và thêm vào danh sách
                if (line.equals("--------------------------------------------------------------------------------")) {
                    if (currentOrder.length() > 0) {
                        orders.add(currentOrder.toString().trim()); // Thêm đơn hàng vào danh sách
                        currentOrder.setLength(0); // Reset builder cho đơn hàng tiếp theo
                    }
                    inOrderDetails = false; // Reset biến kiểm soát khi gặp dấu phân cách
                } else if (line.startsWith("Chi tiết đơn hàng:")) {
                    inOrderDetails = true; // Khi gặp "Chi tiết đơn hàng:", bắt đầu đọc chi tiết đơn hàng
                    currentOrder.append(line).append("\n ");
                } else if (inOrderDetails) {
                    currentOrder.append(line).append("\n"); // Kết hợp từng dòng chi tiết đơn hàng thành 1 dòng
//                } else {
//                    // Đưa phần thông tin khách hàng vào `currentOrder`
//                    currentOrder.append(line).append(" ");
                }
            }

            // Thêm đơn hàng cuối cùng (nếu có)
            if (currentOrder.length() > 0) {
                orders.add(currentOrder.toString().trim());
            }

            // Hiển thị danh sách đơn hàng trong ListView
            orderHistoryListView.setItems(orders);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải đơn hàng.");
        }
    }
    private String extractOrderDetails(String orderText) {
        StringBuilder orderDetails = new StringBuilder();
        String[] lines = orderText.split("\n");
        boolean inOrderDetails = false;

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("Chi tiết đơn hàng:")) {
                inOrderDetails = true;
            } else if (line.startsWith("--------------------------------------------------------------------------------")) {
                inOrderDetails = false;
            } else if (inOrderDetails) {
                orderDetails.append(line).append("\n");
            }
        }

        return orderDetails.length() > 0 ? orderDetails.toString().trim() : null;
    }

//    @FXML
//    public void refreshOrderList() {
//        refreshOrderList();
//    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
