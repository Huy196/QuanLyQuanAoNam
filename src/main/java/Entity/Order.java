package Entity;

import javafx.beans.property.*;

public class Order {
    private final StringProperty customer;
    private final StringProperty product;
    private final IntegerProperty quantity;
    private final DoubleProperty totalAmount;
    private final StringProperty status;

    public Order(String customer, String product, int quantity, double totalAmount, String status) {
        this.customer = new SimpleStringProperty(customer);
        this.product = new SimpleStringProperty(product);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.status = new SimpleStringProperty(status);
    }

    public String getCustomer() {
        return customer.get();
    }

    public StringProperty customerProperty() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer.set(customer);
    }

    public String getProduct() {
        return product.get();
    }

    public StringProperty productProperty() {
        return product;
    }

    public void setProduct(String product) {
        this.product.set(product);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public DoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    @Override
    public String toString() {
        return "Khách hàng: " + customer.get() + ", Sản phẩm: " + product.get() +
                ", Số lượng: " + quantity.get() + ", Tổng tiền: " + totalAmount.get() + " VNĐ, Trạng thái: " + status.get();
    }
}
