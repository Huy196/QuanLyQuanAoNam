package com.example.login;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderItem {
    private final StringProperty productName;
    private final StringProperty price;
    private final IntegerProperty quantity;
    private final StringProperty total;
    private final StringProperty status;
    private final StringProperty orderDate;
    private final StringProperty size;  // Thêm thuộc tính size

    public OrderItem(String productName, String price, int quantity, String total, String status, String size) {
        this.productName = new SimpleStringProperty(productName);
        this.price = new SimpleStringProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.total = new SimpleStringProperty(total);
        this.status = new SimpleStringProperty(status);
        this.orderDate = new SimpleStringProperty(getCurrentDateTime());  // Khởi tạo ngày giờ hiện tại
        this.size = new SimpleStringProperty(size);  // Khởi tạo size
    }

    public String getProductName() { return productName.get(); }
    public void setProductName(String productName) { this.productName.set(productName); }
    public StringProperty productNameProperty() { return productName; }

    public String getPrice() { return price.get(); }
    public void setPrice(String price) { this.price.set(price); }
    public StringProperty priceProperty() { return price; }

    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }
    public IntegerProperty quantityProperty() { return quantity; }

    public String getTotal() { return total.get(); }
    public void setTotal(String total) { this.total.set(total); }
    public StringProperty totalProperty() { return total; }

    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
    public StringProperty statusProperty() { return status; }

    public String getOrderDate() { return orderDate.get(); }
    public void setOrderDate(String orderDate) { this.orderDate.set(orderDate); }
    public StringProperty orderDateProperty() { return orderDate; }

    public String getSize() { return size.get(); }  // Getter cho size
    public void setSize(String size) { this.size.set(size); }  // Setter cho size
    public StringProperty sizeProperty() { return size; }

    @Override
    public String toString() {
        return String.format("Sản phẩm: %s, Giá: %s, Kích thước: %s, Số lượng: %d, Tổng cộng: %s, Trạng thái: %s, Ngày giờ: %s",
                getProductName(), getPrice(), getSize(), getQuantity(), getTotal(), getStatus(), getOrderDate());
    }

    // Phương thức để lấy ngày giờ hiện tại
    private String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
