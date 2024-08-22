package com.example.login;

import Entity.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.security.auth.callback.Callback;
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
    private void buySelectedProducts(){
        boolean hasSelectedProduct = false;
        // Kiểm tra xem có ít nhất một sản phẩm được chọn
        for (HBox hBox : hboxProductMap.keySet()) {
            CheckBox checkBox = (CheckBox) hBox.getChildren().get(0);
            if (checkBox.isSelected()) {
                hasSelectedProduct = true;
                break;
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
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("purchased_products.txt",true))) {
            for (HBox hBox: new ArrayList<>(hboxProductMap.keySet())) {
                CheckBox checkBox = (CheckBox) hBox.getChildren().get(0);
                Product product = hboxProductMap.get(hBox);
                if (checkBox.isSelected()){
                    int quantity = ((Spinner<Integer>) hBox.getChildren().get(4)).getValue();
                    writer.write(product.toString() + " _ Số lượng: " + quantity + "\n");
                    removeProductFromList(product);
                }
            }
        }catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể lưu thông tin mua hàng: " + e.getMessage());
            alert.showAndWait();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Giỏ hàng");
        alert.setHeaderText(null);
        alert.setContentText("Sản phẩm đã được mua thành công.");
        alert.showAndWait();

        updateTotalCartLabel();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
