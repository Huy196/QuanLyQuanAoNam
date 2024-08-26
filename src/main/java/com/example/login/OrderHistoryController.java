package com.example.login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

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

                // Xử lý hiển thị các nút dựa trên trạng thái của đơn hàng
                if (orderItems.isEmpty()) {
                    payButton.setVisible(false);
                    cancelButton.setVisible(false);
                } else {
                    String status = orderItems.get(0).getStatus(); // Giả sử tất cả các mục đơn hàng có trạng thái giống nhau
                    if ("Chờ thanh toán".equals(status)) {
                        payButton.setVisible(true);
                        cancelButton.setVisible(true);
                    } else {
                        payButton.setVisible(false);
                        cancelButton.setVisible(false);
                    }
                }
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
        // Lấy đơn hàng đã chọn từ ListView
        String selectedOrder = orderHistoryListView.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            ObservableList<OrderItem> allOrderItems = orderDetailTableView.getItems();
            boolean hasPendingPayment = false;

            // Kiểm tra xem đơn hàng có sản phẩm nào ở trạng thái "Chờ thanh toán" không
            for (OrderItem orderItem : allOrderItems) {
                if ("Chờ thanh toán".equals(orderItem.getStatus())) {
                    hasPendingPayment = true;
                    break;
                }
            }

            if (hasPendingPayment) {
                // Cập nhật trạng thái sản phẩm trong TableView
                for (OrderItem orderItem : allOrderItems) {
                    if ("Chờ thanh toán".equals(orderItem.getStatus())) {
                        orderItem.setStatus("Đã thanh toán");
                    }
                }

                // Cập nhật trạng thái đơn hàng trong ListView
                String updatedOrder = selectedOrder.replace("Chờ thanh toán", "Đã thanh toán");

                // Cập nhật trạng thái trong file
                updateOrderStatusInFile(selectedOrder, "Đã thanh toán");

                // Cập nhật ListView
                int selectedIndex = orderHistoryListView.getSelectionModel().getSelectedIndex();
                orderHistoryListView.getItems().set(selectedIndex, updatedOrder);

                showAlert(Alert.AlertType.INFORMATION, "Thanh toán", "Đơn hàng đã được thanh toán.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Không Có Đơn Hàng", "Không có sản phẩm nào cần thanh toán.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Không Có Đơn Hàng", "Vui lòng chọn một đơn hàng để thanh toán.");
        }
    }
    @FXML
    private void handleCancel() {
        // Lấy đơn hàng đã chọn từ ListView
        String selectedOrder = orderHistoryListView.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            ObservableList<OrderItem> allOrderItems = orderDetailTableView.getItems();
            boolean hasPendingCancellation = false;

            // Kiểm tra nếu có sản phẩm nào trong đơn hàng cần hủy
            for (OrderItem orderItem : allOrderItems) {
                if ("Chờ thanh toán".equals(orderItem.getStatus())) {
                    hasPendingCancellation = true;
                    break;
                }
            }

            if (hasPendingCancellation) {
                // Hiển thị hộp thoại xác nhận hủy đơn
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác Nhận Hủy Đơn");
                alert.setHeaderText(null);
                alert.setContentText("Bạn có chắc chắn muốn hủy đơn hàng đã chọn?");

                ButtonType confirmButton = new ButtonType("Hủy đơn");
                ButtonType cancelButton = new ButtonType("Quay lại", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(confirmButton, cancelButton);

                alert.showAndWait().ifPresent(response -> {
                    if (response == confirmButton) {
                        // Cập nhật trạng thái sản phẩm trong TableView
                        for (OrderItem orderItem : allOrderItems) {
                            if ("Chờ thanh toán".equals(orderItem.getStatus())) {
                                orderItem.setStatus("Đã hủy");
                            }
                        }

                        // Cập nhật trạng thái đơn hàng trong ListView
                        String updatedOrder = selectedOrder.replace("Chờ thanh toán", "Đã hủy");

                        // Cập nhật trạng thái trong file
                        updateOrderStatusInFile(selectedOrder, "Đã hủy");

                        // Cập nhật ListView
                        int selectedIndex = orderHistoryListView.getSelectionModel().getSelectedIndex();
                        orderHistoryListView.getItems().set(selectedIndex, updatedOrder);

                        showAlert(Alert.AlertType.INFORMATION, "Hủy Đơn", "Đơn hàng đã được hủy.");
                    }
                });
            } else {
                showAlert(Alert.AlertType.WARNING, "Không Có Đơn Hàng", "Không có sản phẩm nào cần hủy.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Không Có Đơn Hàng", "Vui lòng chọn một đơn hàng để hủy.");
        }
    }
    private void updateOrderStatusInFile(String selectedOrder, String newStatus) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("orders.txt"));
            StringBuilder fileContent = new StringBuilder();
            String line;
            boolean isInSelectedOrder = false;

            // Đọc từng dòng trong file
            while ((line = reader.readLine()) != null) {
                // Khi bắt đầu xử lý đơn hàng được chọn
                if (line.equals("--------------------------------------------------------------------------------")) {
                    isInSelectedOrder = false;  // Reset biến kiểm soát khi gặp dấu phân cách
                }

                if (line.equals(selectedOrder.split("\n")[0])) {
                    isInSelectedOrder = true; // Xác định bắt đầu xử lý đúng đơn hàng
                }

                // Nếu đúng đơn hàng và dòng chứa "Trạng thái:" thì cập nhật trạng thái
                if (isInSelectedOrder && line.contains("Trạng thái:")) {
                    line = line.replace("Chờ thanh toán", newStatus)
                            .replace("Đã thanh toán", newStatus)
                            .replace("Đã hủy", newStatus);
                    isInSelectedOrder = false;  // Sau khi thay đổi trạng thái, không cần kiểm tra tiếp trong đơn hàng này
                }

                fileContent.append(line).append("\n");
            }
            reader.close();

            // Ghi nội dung cập nhật vào file
            BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt"));
            writer.write(fileContent.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật trạng thái đơn hàng.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
