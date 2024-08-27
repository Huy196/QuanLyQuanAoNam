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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    @FXML
    public void refreshOrderList() {
        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            ObservableList<String> orders = FXCollections.observableArrayList();
            StringBuilder currentOrder = new StringBuilder();
            boolean inOrderDetails = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.equals("--------------------------------------------------------------------------------")) {
                    if (currentOrder.length() > 0) {
                        orders.add(currentOrder.toString().trim());
                        currentOrder.setLength(0);
                    }
                    inOrderDetails = false;
                } else if (line.startsWith("Chi tiết đơn hàng:")) {
                    inOrderDetails = true;
                    currentOrder.append(line).append("\n ");
                } else if (inOrderDetails) {
                    currentOrder.append(line).append("\n");
                }
            }

            if (currentOrder.length() > 0) {
                orders.add(currentOrder.toString().trim());
            }

            orderHistoryListView.setItems(orders);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải đơn hàng.");
        }
    }
    @FXML
    private void handlePay() {
        String selectedOrder = orderHistoryListView.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            // Cập nhật trạng thái trong ListView
            String updatedOrder = selectedOrder.replace("Chờ thanh toán", "Đã thanh toán");
            int selectedIndex = orderHistoryListView.getSelectionModel().getSelectedIndex();
            orderHistoryListView.getItems().set(selectedIndex, updatedOrder);

            // Lưu danh sách đơn hàng vào tệp
            saveOrdersToFile();

            showAlert(Alert.AlertType.INFORMATION, "Thanh toán", "Đơn hàng đã được thanh toán.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Không Có Đơn Hàng", "Vui lòng chọn một đơn hàng để thanh toán.");
        }
    }

    @FXML
    private void handleCancel() {
        String selectedOrder = orderHistoryListView.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác Nhận Hủy Đơn");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn hủy đơn hàng đã chọn?");

            ButtonType confirmButton = new ButtonType("Hủy đơn");
            ButtonType cancelButton = new ButtonType("Quay lại", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(confirmButton, cancelButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == confirmButton) {
                    // Cập nhật trạng thái trong ListView
                    String updatedOrder = selectedOrder.replace("Chờ thanh toán", "Đã hủy");
                    int selectedIndex = orderHistoryListView.getSelectionModel().getSelectedIndex();
                    orderHistoryListView.getItems().set(selectedIndex, updatedOrder);

                    // Lưu danh sách đơn hàng vào tệp
                    saveOrdersToFile();

                    showAlert(Alert.AlertType.INFORMATION, "Hủy Đơn", "Đơn hàng đã được hủy.");
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Không Có Đơn Hàng", "Vui lòng chọn một đơn hàng để hủy.");
        }
    }
    @FXML
    private void saveOrdersToFile() {
        File file = new File("orders.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Duyệt qua từng đơn hàng trong ListView và ghi vào file với định dạng yêu cầu
            for (String order : orderHistoryListView.getItems()) {
                // Tách các thông tin đơn hàng từ String order
                String[] orderLines = order.split("\n");

                for (String line : orderLines) {
                    // Ghi từng dòng của đơn hàng vào file
                    writer.write(line);
                    writer.newLine();
                }

                // Ghi dấu phân cách sau mỗi đơn hàng
                writer.write("--------------------------------------------------------------------------------");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể lưu danh sách đơn hàng.");
        }
    }




    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}