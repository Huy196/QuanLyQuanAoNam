package com.example.login;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryController {

    @FXML
    private ListView<String> orderListView; // Sử dụng ListView để hiển thị đơn hàng

    @FXML
    private Button closeButton; // Nút đóng cửa sổ

    @FXML
    public void initialize() {
        loadOrderHistory();
    }

    private void loadOrderHistory() {
        List<String> orderList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            StringBuilder orderDetails = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                // Thêm dòng vào thông tin đơn hàng hiện tại
                orderDetails.append(line).append("\n");

                // Kiểm tra xem có dấu phân cách không
                if (line.trim().equals("--------------------------------------------------------------------------------")) {
                    orderList.add(orderDetails.toString()); // Thêm thông tin đơn hàng vào danh sách
                    orderDetails.setLength(0); // Xóa nội dung của StringBuilder để chuẩn bị cho đơn hàng tiếp theo
                }
            }

            // Thêm đơn hàng cuối cùng nếu không có phân cách cuối cùng
            if (orderDetails.length() > 0) {
                orderList.add(orderDetails.toString());
            }
        } catch (IOException e) {
            orderList.add("Không thể đọc lịch sử đơn hàng.");
        }

        // Hiển thị các đơn hàng trong ListView
        orderListView.getItems().addAll(orderList);
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
