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

public class OrderHistoryController implements Initializable {

    @FXML
    private ListView<String> orderHistoryListView; // Hiển thị danh sách các đơn hàng

    @FXML
    private Button payButton;
    @FXML
    private Button cancelButton;

    @FXML
    private Label customerNameLabel;
    @FXML
    private Label customerEmailLabel;
    @FXML
    private Label customerAddressLabel;
    @FXML
    private Label customerPhoneLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Tải thông tin khách hàng từ user_info.txt
        loadCustomerInfo();
        // Tải danh sách đơn hàng khi controller được khởi tạo
        refreshOrderList();
        // Xử lý sự kiện chọn đơn hàng từ ListView
        orderHistoryListView.setOnMouseClicked((MouseEvent event) -> {
            String selectedOrder = orderHistoryListView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                // Xử lý hiển thị các nút dựa trên trạng thái của đơn hàng
                if (selectedOrder.contains("Chờ thanh toán")) {
                    payButton.setVisible(true);
                    cancelButton.setVisible(true);
                } else {
                    payButton.setVisible(false);
                    cancelButton.setVisible(false);
                }
            }
        });
    }

    // Phương thức tải thông tin khách hàng từ file user_info.txt
    private void loadCustomerInfo() {
        try (BufferedReader reader = new BufferedReader(new FileReader("user_info.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Tên đăng nhập:")) {
                    customerNameLabel.setText("Tên khách hàng: " + line.split(":")[1].trim());
                } else if (line.startsWith("Email:")) {
                    customerEmailLabel.setText("Email: " + line.split(":")[1].trim());
                } else if (line.startsWith("SĐT:")) {
                    customerPhoneLabel.setText("SĐT: " + line.split(":")[1].trim());
                } else if (line.startsWith("Địa chỉ:")) {
                    customerAddressLabel.setText("Địa chỉ: " + line.split(":")[1].trim());
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải thông tin khách hàng.");
            e.printStackTrace();
        }
    }

    // Tải danh sách đơn hàng từ file orders.txt
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

    @FXML
    private void handlePay() {
        // Xử lý thanh toán
        String selectedOrder = orderHistoryListView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null && selectedOrder.contains("Chờ thanh toán")) {
            // Cập nhật trạng thái đơn hàng
            String updatedOrder = selectedOrder.replace("Chờ thanh toán", "Đã thanh toán");
            int selectedIndex = orderHistoryListView.getSelectionModel().getSelectedIndex();
            orderHistoryListView.getItems().set(selectedIndex, updatedOrder);

            // Lưu danh sách đơn hàng đã cập nhật vào file
            saveOrdersToFile();

            showAlert(Alert.AlertType.INFORMATION, "Thanh toán", "Đơn hàng đã được thanh toán.");

            payButton.setVisible(false);
            cancelButton.setVisible(false);
        } else {
            showAlert(Alert.AlertType.WARNING, "Không Có Đơn Hàng", "Vui lòng chọn đơn hàng hợp lệ để thanh toán.");
        }
    }

    @FXML
    private void handleCancel() {
        // Xử lý hủy đơn
        String selectedOrder = orderHistoryListView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null && selectedOrder.contains("Chờ thanh toán")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Hủy Đơn");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn hủy đơn hàng này?");

            ButtonType confirmButton = new ButtonType("Hủy đơn");
            ButtonType cancelButton = new ButtonType("Quay lại", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(confirmButton, cancelButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == confirmButton) {
                    // Cập nhật trạng thái đơn hàng
                    String updatedOrder = selectedOrder.replace("Chờ thanh toán", "Đã hủy");
                    int selectedIndex = orderHistoryListView.getSelectionModel().getSelectedIndex();
                    orderHistoryListView.getItems().set(selectedIndex, updatedOrder);

                    // Lưu danh sách đơn hàng đã cập nhật vào file
                    saveOrdersToFile();

                    showAlert(Alert.AlertType.INFORMATION, "Hủy Đơn", "Đơn hàng đã được hủy.");
                    payButton.setVisible(false);
                    this.cancelButton.setVisible(false);
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Không Có Đơn Hàng", "Vui lòng chọn đơn hàng hợp lệ để hủy.");
        }
    }

    @FXML
    private void saveOrdersToFile() {
        // Cập nhật thông tin khách hàng và danh sách đơn hàng vào file orders.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt"))) {
            // Lưu danh sách đơn hàng
            for (String order : orderHistoryListView.getItems()) {
                // Lưu thông tin khách hàng
                writer.write("Thông tin khách hàng:");
                writer.newLine();
                writer.write("Tên: " + customerNameLabel.getText().split(":")[1].trim());
                writer.newLine();
                writer.write("Email: " + customerEmailLabel.getText().split(":")[1].trim());
                writer.newLine();
                writer.write("SĐT: " + customerPhoneLabel.getText().split(":")[1].trim());
                writer.newLine();
                writer.write("Địa chỉ: " + customerAddressLabel.getText().split(":")[1].trim());
//                writer.newLine();
//                writer.write("--------------------------------------------------------------------------------");
                writer.newLine();

                // Lưu chi tiết đơn hàng
//                writer.write("Chi tiết đơn hàng:");
                writer.newLine();
                writer.write(order); // Đơn hàng đã cập nhật
                writer.newLine();
                writer.write("--------------------------------------------------------------------------------");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể lưu thông tin khách hàng và danh sách đơn hàng.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
