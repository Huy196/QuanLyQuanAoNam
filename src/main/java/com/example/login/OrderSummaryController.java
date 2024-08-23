package com.example.login;

import Entity.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.util.List;

public class OrderSummaryController {

    @FXML
    private TableView<OrderItem> orderTableView;

    @FXML
    private TableColumn<OrderItem, String> productNameColumn;

    @FXML
    private TableColumn<OrderItem, Double> priceColumn;

    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;

    @FXML
    private TableColumn<OrderItem, Double> totalColumn;

    @FXML
    private TableColumn<OrderItem, String> statusColumn;  // Thêm cột trạng thái

    @FXML
    private Label customerNameLabel;

    @FXML
    private Label customerEmailLabel;

    @FXML
    private Label customerPhoneLabel;

    @FXML
    private Label customerAddressLabel;

    @FXML
    private Button totalAmountButton;

    @FXML
    private Button payOrderButton;

    @FXML
    private Button cancelOrderButton;

    @FXML
    private Button closeButton;

    private ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();
    private Stage stage;

    @FXML
    private void initialize() {
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
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
        double total = items.stream().mapToDouble(OrderItem::getTotal).sum();
        totalAmountButton.setText("Tổng: $" + String.format("%.2f", total));

        // Kiểm tra trạng thái của tất cả các mục đơn hàng
        boolean allPaid = items.stream().allMatch(item -> "Đã Thanh Toán".equals(item.getStatus()));
        boolean allCancelled = items.stream().allMatch(item -> "Đã hủy".equals(item.getStatus()));

        // Vô hiệu hóa nút thanh toán nếu tất cả các mục đã được thanh toán hoặc đã hủy
        payOrderButton.setDisable(allPaid || allCancelled);

        // Vô hiệu hóa nút hủy đơn nếu tất cả các mục đã được thanh toán hoặc đã hủy
        cancelOrderButton.setDisable(allPaid || allCancelled);
    }

    @FXML
    private void handlePayOrder() {
        // Xử lý logic thanh toán cho đơn hàng
        for (OrderItem item : orderItems) {
            item.setStatus("Đã Thanh Toán");
        }
        orderTableView.refresh();

        // Vô hiệu hóa các nút khi đã thanh toán
        payOrderButton.setDisable(true);
        cancelOrderButton.setDisable(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thanh Toán Thành Công");
        alert.setHeaderText(null);
        alert.setContentText("Đơn hàng đã được thanh toán thành công.");
        alert.showAndWait();
    }

    @FXML
    private void handleCancelOrder() {
        // Xác nhận hủy đơn hàng
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận hủy đơn hàng");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn hủy đơn hàng này không?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            // Cập nhật trạng thái đơn hàng thành "Đã hủy"
            cancelOrder();
        }
    }

    private void cancelOrder() {
        for (OrderItem item : orderItems) {
            item.setStatus("Đã hủy");
        }
        orderTableView.refresh();

        // Vô hiệu hóa các nút khi đã hủy
        payOrderButton.setDisable(true);
        cancelOrderButton.setDisable(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Đơn hàng đã được hủy.");
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
