package com.example.login;

import Entity.Customer;
import Entity.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartController {

    @FXML
    private ListView<HBox> cartListView;

    @FXML
    private Button buyButton;
    private Stage stage;
    private List<Product> cart = new ArrayList<>();
    private Map<HBox, Product> hboxProductMap = new HashMap<>();
    @FXML
    private Label totalCartLabel;

    @FXML
    private void initialize() {
        loadCart();
        updateTotalCartLabel();
    }

    private void loadCart() {
        try (BufferedReader reader = new BufferedReader(new FileReader("cart.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product product = Product.fromString(line);
                cart.add(product);
                addProductToListView(product);
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể đọc giỏ hàng: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void addProductToListView(Product product) {
        ImageView imageView = new ImageView(product.getImage());
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label("Tên: " + product.getName());
        Label priceLabel = new Label("Giá: $" + product.getPrice());

        Label totalLabel = new Label();
        updateTotalLabel(totalLabel, product);

        Spinner<Integer> quantitySpinner = new Spinner<>(1, product.getQuantity(),1);
        quantitySpinner.setEditable(true);
        quantitySpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            product.setQuantity(newValue);
            updateTotalLabel(totalLabel, product);
            updateCartFile();
            updateTotalCartLabel();
        });
        CheckBox selectCheckBox = new CheckBox();
        selectCheckBox.setSelected(false);
        selectCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            updateTotalCartLabel();
        });

        Button removeButton = new Button("Xóa");

        removeButton.setOnAction(e -> {
            removeProductFromList(product);
            updateTotalCartLabel();
        });
        HBox hbox = new HBox(30);
        hbox.getChildren().addAll(selectCheckBox,imageView, nameLabel, priceLabel, quantitySpinner, removeButton);
        cartListView.getItems().add(hbox);

        hboxProductMap.put(hbox, product);
    }

    private void updateTotalLabel(Label totalLabel, Product product) {
        double total = product.getQuantity() * product.getPrice();
        totalLabel.setText("Tổng: $" + String.format("%.2f", total));
    }

    private void updateTotalCartLabel() {
        double total = 0.0;
        for (HBox hbox : hboxProductMap.keySet()) {
            CheckBox checkBox = (CheckBox) hbox.getChildren().get(0); // Lấy CheckBox từ HBox
            Product product = hboxProductMap.get(hbox);
            if (checkBox.isSelected()) { // Nếu sản phẩm được chọn
                total += product.getPrice() * product.getQuantity();
            }
        }
        totalCartLabel.setText("Tổng giỏ hàng: $" + String.format("%.2f", total));
    }

    private void removeProductFromList(Product product) {
        HBox hboxToRemove = null;
        for (Map.Entry<HBox, Product> entry : hboxProductMap.entrySet()) {
            if (entry.getValue().equals(product)) {
                hboxToRemove = entry.getKey();
                break;
            }
        }

        if (hboxToRemove != null) {
            // Xóa sản phẩm khỏi danh sách và ListView
            cart.remove(product);
            cartListView.getItems().remove(hboxToRemove);
            hboxProductMap.remove(hboxToRemove);

            // Cập nhật file giỏ hàng
            updateCartFile();
        }
    }

    private void updateCartFile() {
        try (FileWriter writer = new FileWriter("cart.txt", false)) {
            for (Product product : cart) {
                writer.write(product.toString() + "\n");
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể cập nhật giỏ hàng: " + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML

    private void buySelectedProducts() {
        boolean hasSelectedProduct = false;
        List<OrderItem> orderItems = new ArrayList<>();
        Customer customer = getCustomerInformation();

        // Kiểm tra xem có ít nhất một sản phẩm được chọn và chuẩn bị dữ liệu
        for (HBox hBox : hboxProductMap.keySet()) {
            CheckBox checkBox = (CheckBox) hBox.getChildren().get(0);
            if (checkBox.isSelected()) {
                hasSelectedProduct = true;
                Product product = hboxProductMap.get(hBox);
                int quantity = ((Spinner<Integer>) hBox.getChildren().get(4)).getValue();
                double total = product.getPrice() * quantity;

                // Thay đổi trạng thái mặc định ở đây
                String status = "Chờ xác nhận"; // Trạng thái mặc định

                orderItems.add(new OrderItem(product.getName(), product.getPrice(), quantity, total, status));

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("purchased_products.txt", true))) {
                    writer.write(product.toString() + " _ Số lượng: " + quantity + "\n");
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Không thể lưu thông tin mua hàng: " + e.getMessage());
                    alert.showAndWait();
                }
                removeProductFromList(product);
            }
        }

        if (!hasSelectedProduct) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn ít nhất một sản phẩm để mua.");
            alert.showAndWait();
            return;
        }

        // Hiển thị hóa đơn
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("order_summary.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            OrderSummaryController controller = loader.getController();
            controller.setInvoiceDetails(customer, orderItems);

            stage.setTitle("Hóa Đơn");
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể hiển thị hóa đơn: " + e.getMessage());
            alert.showAndWait();
        }

        // Lưu chi tiết đơn hàng vào order.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt", true))) {
            writer.write("Thông tin khách hàng:\n");
            writer.write("Tên: " + customer.getName() + "\n");
            writer.write("Email: " + customer.getEmail() + "\n");
            writer.write("SĐT: " + customer.getPhoneNumber() + "\n");
            writer.write("Địa chỉ: " + customer.getAddress() + "\n\n");
            writer.write("Chi tiết đơn hàng:\n");

            for (OrderItem item : orderItems) {
                writer.write(item.toString() + "\n");
            }

            writer.write("--------------------------------------------------------------------------------\n");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể lưu thông tin đơn hàng: " + e.getMessage());
            alert.showAndWait();
        }

        // Cập nhật giao diện và xóa giỏ hàng
        cartListView.getItems().clear(); // Xóa tất cả các mục trong ListView
        cart.clear(); // Xóa tất cả các sản phẩm trong danh sách giỏ hàng
        hboxProductMap.clear(); // Xóa tất cả các ánh xạ trong Map
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Giỏ hàng");
        alert.setHeaderText(null);
        alert.setContentText("Sản phẩm đã được mua thành công.");
        alert.showAndWait();

        updateTotalCartLabel();

        // Đóng giỏ hàng
        if (stage != null) {
            stage.close();
        }
    }


    private Customer getCustomerInformation() {
        String filePath = "user_info.txt";
        String name = "";
        String email = "";
        String phoneNumber = "";
        String address = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Tên đăng nhập: ")) {
                    name = line.substring("Tên đăng nhập: ".length()).trim();
                } else if (line.startsWith("Email: ")) {
                    email = line.substring("Email: ".length()).trim();
                } else if (line.startsWith("SĐT: ")) {
                    phoneNumber = line.substring("SĐT: ".length()).trim();
                } else if (line.startsWith("Địa chỉ: ")) {
                    address = line.substring("Địa chỉ: ".length()).trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể đọc thông tin khách hàng: " + e.getMessage());
            alert.showAndWait();
        }

        return new Customer(name, email, address, phoneNumber);
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
