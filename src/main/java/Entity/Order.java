package Entity;

public class Order {

    private Customer customer;
    private Product product;
    private int quantity;
    private double totalAmount;
    private String status;

    public Order(Customer customer, Product product, int quantity, double totalAmount, String status) {
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
//        return totalAmount;
        return product.getPrice()* quantity;
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
        return "Khách hàng: " + customer.getName() + ", Sản phẩm: " + product.getName() + ", Số lượng: " + quantity +
                ", Tổng tiền: " + totalAmount + " VNĐ, Trạng thái: " + status;
    }


}
