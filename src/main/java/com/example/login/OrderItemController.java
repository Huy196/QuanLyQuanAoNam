package com.example.login;

import Entity.Order;
import Entity.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class OrderItemController {

    @FXML
    private Label productLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private Label statusLabel;  // Thêm để hiển thị trạng thái đơn hàng

    @FXML
    private Button paidButton;

    @FXML
    private Button cancelButton;

    private Order order;  // Đúng kiểu dữ liệu là Order

    private static final String FILE_HOADON = "project.txt";

    @FXML
    private void initialize() {
        // Xử lý logic cho các nút, nếu cần
        paidButton.setOnAction(event -> handlePaidAction());
        cancelButton.setOnAction(event -> handleCancelAction());
    }

    public void setOrder(Order order) {
        this.order = order;  // Cập nhật biến order
        productLabel.setText(order.getProductName());
        quantityLabel.setText("Số lượng: " + order.getQuantity());
        totalAmountLabel.setText("Tổng tiền: $" + order.getTotalAmount());
        // Cập nhật trạng thái nút nếu cần
        statusLabel.setText(order.getStatus());

        paidButton.setVisible(order.getStatus().equals("Chờ thanh toán"));
        cancelButton.setVisible(order.getStatus().equals("Chờ thanh toán"));

//        order.statusProperty().addListener((observable, oldValue, newValue) -> {
//            updateButtonStatus();  // Cập nhật trạng thái các nút khi trạng thái đơn hàng thay đổi
//        });

    }

    private void handlePaidAction() {
        if (order != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn chuyển trạng thái đơn hàng thành Đã thanh toán?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                order.setStatus("Đã thanh toán");
                System.out.println("Đã thanh toán");
                statusLabel.setText(order.getStatus()); // cập nhật mới
                updateButtonStatus();

                //ẩn nút
                paidButton.setVisible(false);
                cancelButton.setVisible(false);
            }
        }
    }

    private void handleCancelAction() {
        if (order != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn chuyển trạng thái đơn hàng thành Hủy đơn?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                order.setStatus("Hủy đơn");
                System.out.println("Hủy đơn");
                statusLabel.setText(order.getStatus()); //chuyển đổi trạng thái
                updateButtonStatus();

                //ẩn nút
                paidButton.setVisible(false);
                cancelButton.setVisible(false);
            }
        }
    }

    private void updateButtonStatus() {
        if (order != null) {
            switch (order.getStatus()) {
                case "Đã thanh toán", "Hủy đơn" -> {
                    paidButton.setDisable(true);
                    cancelButton.setDisable(true);
                }
                default -> {
                    paidButton.setDisable(false);
                    cancelButton.setDisable(false);
                }
            }
        } else {
            paidButton.setDisable(false);
            cancelButton.setDisable(false);
        }
    }

}
