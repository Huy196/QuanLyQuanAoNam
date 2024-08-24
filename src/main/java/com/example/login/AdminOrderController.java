package com.example.login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminOrderController implements Initializable {

    @FXML
    private ListView<String> orderHistoryListView;

    @FXML
    private TextArea orderDetailTextArea;

    @FXML
    private Button backButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private ObservableList<String> orders = FXCollections.observableArrayList();
    private String selectedOrder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadOrders();

        orderHistoryListView.setOnMouseClicked((MouseEvent event) -> {
            selectedOrder = orderHistoryListView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                // Hiển thị chi tiết đơn hàng được chọn
                displayOrderDetails(selectedOrder);
                updateButtonStates();
            }
        });
    }

    private void loadOrders() {
        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            StringBuilder currentOrder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.equals("--------------------------------------------------------------------------------")) {
                    if (currentOrder.length() > 0) {
                        orders.add(currentOrder.toString().trim());
                        currentOrder.setLength(0);
                    }
                } else {
                    currentOrder.append(line).append("\n");
                }
            }

            // Thêm đơn hàng cuối cùng nếu có
            if (currentOrder.length() > 0) {
                orders.add(currentOrder.toString().trim());
            }

            orderHistoryListView.setItems(orders);

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải đơn hàng.");
            e.printStackTrace();
        }
    }

    private void displayOrderDetails(String order) {
        // Hiển thị chi tiết đơn hàng được chọn trong TextArea
        orderDetailTextArea.setText(order);
    }

    private void updateButtonStates() {
        if (selectedOrder != null && selectedOrder.contains("Chờ xác nhận")) {
            confirmButton.setDisable(false);
            cancelButton.setDisable(false);
        } else {
            confirmButton.setDisable(true);
            cancelButton.setDisable(true);
        }
    }

    @FXML
    public void handleConfirmButton() {
        handleOrderUpdate("Chờ xác nhận", "Chờ thanh toán", "Đơn hàng đã được xác nhận.");
    }

    @FXML
    public void handleCancelButton() {
        handleOrderUpdate("Chờ xác nhận", "Hủy", "Đơn hàng đã bị hủy.");
    }

    private void handleOrderUpdate(String oldStatus, String newStatus, String notificationMessage) {
        if (selectedOrder == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng chọn một đơn hàng để tiếp tục.");
            return;
        }

        if (showConfirmationDialog(newStatus.equals("Xác nhận") ? "Xác nhận" : "Hủy",
                "Bạn có chắc chắn muốn " + newStatus.toLowerCase() + " đơn hàng này không?")) {

            String updatedOrder = selectedOrder.replace(oldStatus, newStatus);
            if (updateOrderStatusInFile(selectedOrder, updatedOrder)) {
                orders.set(orders.indexOf(selectedOrder), updatedOrder);
                displayOrderDetails(updatedOrder);
                updateButtonStates();

                sendNotificationToUser(notificationMessage, updatedOrder);
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật đơn hàng.");
            }
        }
    }

    private boolean updateOrderStatusInFile(String originalOrder, String updatedOrder) {
        File inputFile = new File("orders.txt");
        File tempFile = new File("orders_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            StringBuilder currentOrder = new StringBuilder();
            boolean orderUpdated = false;

            while ((line = reader.readLine()) != null) {
                // Khi gặp dấu phân cách, kiểm tra đơn hàng hiện tại
                if (line.equals("--------------------------------------------------------------------------------")) {
                    if (currentOrder.toString().trim().equals(originalOrder.trim())) {
                        writer.write(updatedOrder + "\n");
                        orderUpdated = true;
                    } else {
                        writer.write(currentOrder.toString().trim() + "\n");
                    }
                    writer.write("--------------------------------------------------------------------------------\n");
                    currentOrder.setLength(0); // Reset builder cho đơn hàng tiếp theo
                } else {
                    // Không xóa khoảng trắng mà giữ nguyên định dạng như tệp ban đầu
                    currentOrder.append(line).append("\n");
                }
            }

            // Nếu đơn hàng cuối cùng chưa được cập nhật và khớp với originalOrder
            if (!orderUpdated && currentOrder.toString().trim().equals(originalOrder.trim())) {
                writer.write(updatedOrder + "\n");
                writer.write("--------------------------------------------------------------------------------\n");
                orderUpdated = true;
            }

            return orderUpdated;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Xóa file gốc và thay thế bằng file tạm
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
            }
        }
    }

    private void sendNotificationToUser(String message, String orderDetails) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_notifications.txt", true))) {
            writer.write("Thông báo: " + message + "\n");
            writer.write(orderDetails);
            writer.write("\n--------------------------------------------------------------------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
