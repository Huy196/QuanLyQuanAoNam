package com.example.login;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeController {
    @FXML
    private AnchorPane contentArea;

    @FXML
    private TextField text;

    @FXML
    private ImageView imageView;

    // Phương thức xử lý sự kiện
    @FXML
    private void showDashboard() {
        // Mã để hiển thị giao diện bảng điều khiển
        System.out.println("Đang hiển thị Bảng điều khiển");
    }

    @FXML
    private void showProducts() {
        // Mã để hiển thị giao diện sản phẩm
        System.out.println("Đang hiển thị Sản phẩm");


    }

    @FXML
    private void showOrders() {
        // Mã để hiển thị giao diện đơn hàng
        System.out.println("Đang hiển thị Đơn hàng");
    }

    @FXML
    private void showEmployees() {
        // Mã để hiển thị giao diện nhân viên
        System.out.println("Đang hiển thị Nhân viên");
    }

    @FXML
    private void showInvoices() {
        // Mã để hiển thị giao diện hóa đơn
        System.out.println("Đang hiển thị Hóa đơn");
    }

    @FXML
    private void showInvoiceDetails() {
        // Mã để hiển thị chi tiết hóa đơn
        System.out.println("Đang hiển thị Chi tiết hóa đơn");
    }

    @FXML
    private void logout() {
        // Xác nhận đăng xuất
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có muốn đăng xuất?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Đăng xuất");
        alert.setHeaderText(null);

        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            // Mã để xử lý đăng xuất
            System.out.println("Đang đăng xuất...");
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.close(); // Đóng cửa sổ hiện tại
        }
    }
}

