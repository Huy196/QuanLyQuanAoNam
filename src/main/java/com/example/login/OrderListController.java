package com.example.login;

import Entity.Customer;
import Entity.Order;

import Entity.Product;
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
                new Order(new Customer("HuyenTrangTran", "huyentrangtran@gmail.com"),
                        new Product("Sản phẩm A", 450.000, 50, null),
                        4, 0, "Chờ thanh toán"),
                new Order(new Customer("VanDanLe", "vandandl@gmail.com"),
                        new Product("Sản phẩm B", 300.000, 50, null),
                        3, 0, "Đã thanh toán"),

                new Order(new Customer("HuyQuocDo", "huyquocdo@gmail.com"),
                        new Product("Sản phẩm C", 140.000, 50, null),
                        5, 0, "Chờ thanh toán"),

                new Order(new Customer("KhanhBaoNguyen", "khanhbaonguyen@gmail.com"),
                        new Product("Sản phẩm D", 400.000, 30, null),
                        3, 0, "Đã thanh toán"),

                new Order(
                        new Customer("ThomNgocPham", "thomngocpham@gmail.com"),
                        new Product("Sản phẩm E", 95.000, 100, null),
                        7, 0, "Hủy đơn"
                ),
                new Order(
                        new Customer("MinhTuanNguyen", "minhtuannguyen@gmail.com"),
                        new Product("Sản phẩm G", 100.000, 70, null),
                        7, 0, "Chờ thanh toán")
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