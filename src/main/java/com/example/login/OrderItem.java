package com.example.login;

import javafx.beans.property.*;

public class OrderItem {

    private final StringProperty productName;
    private final DoubleProperty price;
    private final IntegerProperty quantity;
    private final DoubleProperty total;
    private final StringProperty status;  // Thêm thuộc tính trạng thái

    public OrderItem(String productName, double price, int quantity, double total, String status) {
        this.productName = new SimpleStringProperty(productName);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.total = new SimpleDoubleProperty(total);
        this.status = new SimpleStringProperty(status);  // Khởi tạo thuộc tính trạng thái
    }

    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getTotal() {
        return total.get();
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

    public DoubleProperty totalProperty() {
        return total;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("Tên Sản Phẩm: %s, Giá: $%.2f, Số lượng: %d, Tổng: $%.2f, Trạng thái: %s",
                productName.get(), price.get(), quantity.get(), total.get(), status.get());
    }
}
