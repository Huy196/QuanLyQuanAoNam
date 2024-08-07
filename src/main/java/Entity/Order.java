package Entity;

import Entity.Customer;
import com.example.login.Product;

public class Order {
    private String customer;
    private String product;
    private int quantity;
    private double totalAmount;
    private String status;

    public Order(String customer, String product, int quantity, double totalAmount, String status) {
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = String.valueOf(customer);
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = String.valueOf(product);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Khách hàng: " + customer + ", Sản phẩm: " + product + ", Số lượng: " + quantity +
                ", Tổng tiền: " + totalAmount + " VNĐ, Trạng thái: " + status;
    }


}