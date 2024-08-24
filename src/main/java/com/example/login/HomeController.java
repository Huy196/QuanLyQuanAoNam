package com.example.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    @FXML
    private AnchorPane contentArea;

    @FXML
    private TextField text;

    @FXML
    private ImageView imageView;

    // Phương thức xử lý sự kiện
    @FXML
    private void showHome() {
        // Mã để hiển thị giao diện bảng điều khiển
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home_Page.fxml"));
            Parent dashboardView = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(dashboardView);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Có lỗi khi hiển thị giao diện  Trang chủ.");
        } finally {
            System.out.println("Đang hiển thị Trang chủ");
        }
    }

    @FXML
    private void showProducts() {
        try {
            // Tải nội dung của ProductList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuProduct.fxml"));
            Parent productListView = loader.load();

            // Thay thế nội dung hiện tại của contentArea bằng ProductList.fxml
            contentArea.getChildren().clear();
            contentArea.getChildren().add(productListView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showOrders() {
        // Mã để hiển thị giao diện đơn hàng

        try {
            // Tải nội dung của order_list.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("order_list.fxml"));
            Parent orderListView = loader.load();

            // Thay thế nội dung hiện tại của contentArea bằng order_list.fxml
            contentArea.getChildren().clear();
            contentArea.getChildren().add(orderListView);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Có lỗi khi hiển thị giao diện Đơn hàng.");
        } finally {
            System.out.println("Đang hiển thị  Đơn hàng ");
        }
    }


    @FXML
    private void showEmployees() {
        // Mã để hiển thị giao diện nhân viên
        System.out.println("Đang hiển thị Nhân viên");
    }

    @FXML
    private void showCustomer() {
        // Mã để hiển thị giao diện khách hàng
        System.out.println("Đang hiển thị Khách hàng");
    }

    @FXML
    private void showInvoiceDetails() {
        try {
            // Tải nội dung của order_list.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_order.fxml"));
            Parent orderListView = loader.load();

            // Thay thế nội dung hiện tại của contentArea bằng order_list.fxml
            contentArea.getChildren().clear();
            contentArea.getChildren().add(orderListView);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Có lỗi khi hiển thị giao diện Đơn hàng.");
        } finally {
            System.out.println("Đang hiển thị  Đơn hàng ");
        }
    }

    @FXML
        private void logout () {
            // Xác nhận đăng xuất
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có muốn đăng xuất?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Đăng xuất");
            alert.setHeaderText(null);


            if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                System.out.println("Đang đăng xuất...");
                try {
                    Main.changeScene("Login.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }

