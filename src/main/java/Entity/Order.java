package Entity;

import javafx.beans.property.*;

public class Order {

    private final StringProperty customerName;
    private final StringProperty productName;
    private final DoubleProperty price;
    private final IntegerProperty quantity;
    private final DoubleProperty totalAmount;
    private final StringProperty status;

    public Order(Customer customer, Product product, int quantity, String status) {
        this.customerName = new SimpleStringProperty(customer.getName());
        this.productName = new SimpleStringProperty(product.getName());
        this.price = new SimpleDoubleProperty(product.getPrice());
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalAmount = new SimpleDoubleProperty(product.getPrice() * quantity);
        this.status = new SimpleStringProperty(status);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public StringProperty customerNameProperty() {
        return customerName;
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
        setTotalAmount(price.get() * quantity); // Cập nhật lại tổng số tiền khi số lượng thay đổi
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public DoubleProperty totalAmountProperty() {
        return totalAmount;
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
        return String.format("Khách hàng: %s, Sản phẩm: %s, Số lượng: %d, Tổng tiền: %.2f VNĐ, Trạng thái: %s",
                customerName.get(), productName.get(), quantity.get(), totalAmount.get(), status.get());
    }
}
