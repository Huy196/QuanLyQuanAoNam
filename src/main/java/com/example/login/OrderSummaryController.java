package com.example.login;

import Entity.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderSummaryController {

    @FXML
    private TableView<OrderItem> orderTableView;

    @FXML
    private TableColumn<OrderItem, String> productNameColumn;

    @FXML
    private TableColumn<OrderItem, String> priceColumn;

    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;

    @FXML
    private TableColumn<OrderItem, String> totalColumn;

    @FXML
    private TableColumn<OrderItem, String> statusColumn;

    @FXML
    private Label customerNameLabel;

    @FXML
    private Label customerEmailLabel;

    @FXML
    private Label customerPhoneLabel;

    @FXML
    private Label customerAddressLabel;

    @FXML
    private Label purchaseDateLabel;  // Label để hiển thị ngày giờ mua hàng

    @FXML
    private Button totalAmountLabel; // Label để hiển thị tổng tiền

    @FXML
    private TableColumn<OrderItem, String> sizeColumn;

    @FXML
    private Button closeButton;

    private ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();
    private Stage stage;

    @FXML
    private void initialize() {
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        sizeColumn.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());  // Gán giá trị kích cỡ
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        orderTableView.setItems(orderItems);
    }

    public void setInvoiceDetails(Customer customer, List<OrderItem> items) {
        customerNameLabel.setText("Tên Khách Hàng: " + customer.getName());
        customerEmailLabel.setText("Email: " + customer.getEmail());
        customerPhoneLabel.setText("SĐT: " + customer.getPhoneNumber());
        customerAddressLabel.setText("Địa Chỉ: " + customer.getAddress());

        orderItems.setAll(items);

        double total = items.stream().mapToDouble(item -> Double.parseDouble(item.getTotal().replace("$", ""))).sum();
        totalAmountLabel.setText("Tổng: $" + String.format("%.2f", total)); // Sử dụng totalAmountLabel

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String currentDateTime = LocalDateTime.now().format(formatter);
        purchaseDateLabel.setText("Ngày giờ mua hàng: " + currentDateTime);

    }

    @FXML
    private void completeOrder() {
        try {
            // Lưu đơn hàng vào orders.txt
            saveOrderToFile();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể lưu đơn hàng", "Có lỗi xảy ra khi lưu đơn hàng.");
        }

        // Đóng giỏ hàng sau khi hoàn thành
        handleClose();
    }


    private void saveOrderToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt", true))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String currentDateTime = LocalDateTime.now().format(formatter);

            // Lưu thông tin khách hàng và đơn hàng
            writer.write("Ngày giờ mua hàng: " + currentDateTime);
            writer.newLine();
            writer.write("Khách Hàng: " + customerNameLabel.getText().replace("Tên Khách Hàng: ", ""));
            writer.newLine();
            writer.write("Email: " + customerEmailLabel.getText().replace("Email: ", ""));
            writer.newLine();
            writer.write("SĐT: " + customerPhoneLabel.getText().replace("SĐT: ", ""));
            writer.newLine();
            writer.write("Địa Chỉ: " + customerAddressLabel.getText().replace("Địa Chỉ: ", ""));
            writer.newLine();
            writer.write("Danh Sách Đơn Hàng:");
            writer.newLine();

            // Ghi thông tin các mặt hàng
            for (OrderItem item : orderItems) {
                writer.write(String.format(" Sản phẩm: %s, Giá: %s, Số lượng: %d, Tổng: %s, Trạng thái: %s, Ngày giờ: %s",
                        item.getProductName(), item.getPrice(), item.getQuantity(), item.getTotal(), item.getStatus(), item.getOrderDate()));
                writer.newLine();
            }

            writer.write("----------------------------------------------------");
            writer.newLine();
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();

    }

    @FXML
    private void handleClose() {
        if (stage != null) {
            stage.close();
        }
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
