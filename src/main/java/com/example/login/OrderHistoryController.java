package com.example.login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderHistoryController implements Initializable {

    @FXML
    private ListView<String> orderHistoryListView; // Hiển thị danh sách các đơn hàng

    @FXML
    private TableView<OrderItem> orderDetailTableView; // Hiển thị chi tiết đơn hàng trong TableView

    @FXML
    private TableColumn<OrderItem, String> productNameColumn;
    @FXML
    private TableColumn<OrderItem, String> priceColumn;
    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;
    @FXML
    private TableColumn<OrderItem, String> totalColumn;
    @FXML
    private TableColumn<OrderItem, String> orderDateColumn;

    @FXML
    private TableColumn<OrderItem, String> statusColumn;

    @FXML
    private Button payButton;
    @FXML
    private Button cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Cấu hình các cột cho TableView
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        // Tải danh sách đơn hàng khi controller được khởi tạo
        refreshOrderList();

        // Xử lý sự kiện chọn đơn hàng từ ListView
        orderHistoryListView.setOnMouseClicked((MouseEvent event) -> {
            String selectedOrder = orderHistoryListView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                ObservableList<OrderItem> orderItems = parseOrderDetails(selectedOrder);
                orderDetailTableView.setItems(orderItems);
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

    private ObservableList<OrderItem> parseOrderDetails(String orderText) {
        ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();
        String[] lines = orderText.split("\n");
        boolean inOrderDetails = false;

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("Chi tiết đơn hàng:")) {
                inOrderDetails = true;
            } else if (line.startsWith("--------------------------------------------------------------------------------")) {
                inOrderDetails = false;
            } else if (inOrderDetails) {
                String[] parts = line.split(", ");
                if (parts.length == 6) {
                    String productName = parts[0].split(": ")[1];
                    String price = parts[1].split(": ")[1];
                    int quantity = Integer.parseInt(parts[2].split(": ")[1]);
                    String total = parts[3].split(": ")[1];
                    String status = parts[4].split(": ")[1];
                    String orderDate = parts[5].split(": ")[1];

                    OrderItem item = new OrderItem(productName, price, quantity, total, status);
                    orderItems.add(item);
                }
            }
        }

        return orderItems;
    }



    @FXML
    private void handlePay() {
        // Xử lý thanh toán
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thanh toán");
        alert.setHeaderText(null);
        alert.setContentText("Đơn hàng đã được thanh toán.");
        alert.showAndWait();
    }

    @FXML
    private void handleCancel() {
        // Xử lý hủy đơn
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Hủy Đơn");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn hủy đơn hàng?");

        ButtonType confirmButton = new ButtonType("Hủy đơn");
        ButtonType cancelButton = new ButtonType("Quay lại", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                // Xử lý hủy đơn
                Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
                confirmationAlert.setTitle("Hủy Đơn");
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setContentText("Đơn hàng đã được hủy.");
                confirmationAlert.showAndWait();
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
