package com.example.login;

import Entity.Order;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class OrderListController {

    @FXML
    private ListView<Order> orderListView;

    private ObservableList<Order> orders;

    @FXML
    private void initialize() {
        // Tạo danh sách đơn hàng mẫu
        orders = FXCollections.observableArrayList(
                new Order("HuyenTrangTran", "Sản phẩm A", 2, 50.0, "Chờ thanh toán"),
                new Order("VanDanLe", "Sản phẩm B", 1, 30.0, "Đã thanh toán"),
                new Order("HuyQuocDo", "Sản phẩm C", 5, 75.0, "Hủy đơn"),
                new Order("KhanhBaoNguyen", "Sản phẩm D", 3, 45.0, "Đã thanh toán"),
                new Order("ThomNgocPham", "Sản phẩm E", 7, 95.0, "Hủy đơn")
        );

        // Cập nhật ListView với danh sách đơn hàng
        orderListView.setItems(orders);


        // Tùy chỉnh cách hiển thị trong ListView
        orderListView.setCellFactory(listView -> new ListCell<Order>() {

            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);

                if (empty || order == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("order_item.fxml"));
                        HBox hbox = loader.load();
                        OrderItemController controller = loader.getController();
                        controller.setOrder(order);
                        setGraphic(hbox);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}