package com.example.login;

import Entity.Product;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductHomeUser {

    @FXML
    private HBox productContainer;

    @FXML
    private Button purchaseButton; // Nút để xử lý việc mua hàng

    private List<Product> cart = new ArrayList<>(); // Giỏ hàng
    private Stage detailStage; // Tham chiếu đến hộp thoại chi tiết sản phẩm

    @FXML
    private void initialize() {
        loadProductsFromFile();
//        purchaseButton.setOnAction(e -> handlePurchase());
    }

    private void loadProductsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("project.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product product = Product.fromString(line);
                addProductToDisplay(product);
            }
        } catch (IOException e) {
            System.err.println("Không thể tải sản phẩm: " + e.getMessage());
        }
    }
    @FXML
    private void handleCartClick() {
        // Xử lý sự kiện khi nhấp vào biểu tượng giỏ hàng
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Giỏ hàng");
        alert.setHeaderText(null);
        alert.setContentText("Giỏ hàng được mở!");
        alert.showAndWait();
    }

    private void addProductToDisplay(Product product) {
        ImageView imageView = new ImageView(product.getImage());
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        VBox productBox = new VBox();
        productBox.setSpacing(10);
        productBox.setStyle("-fx-padding: 10; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5;");

        Text nameText = new Text("Tên: " + product.getName());
        nameText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Text priceText = new Text("Giá: $" + product.getPrice());
        priceText.setStyle("-fx-font-size: 14px;");

        Text quantityText = new Text("Số lượng: " + product.getQuantity());
        quantityText.setStyle("-fx-font-size: 14px;");

        Button detailsButton = new Button("Chi tiết");
        detailsButton.setOnAction(e -> showProductDetails(product));

        HBox productHBox = new HBox();
        productHBox.setSpacing(10);
        productHBox.getChildren().addAll(imageView, productBox);

        productBox.getChildren().addAll(imageView, nameText, priceText, quantityText, detailsButton);
        productContainer.getChildren().add(productBox);
    }

    private void addToCart(Product product) {
        cart.add(product);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Giỏ hàng");
        alert.setHeaderText(null);
        alert.setContentText(product.getName() + " đã được thêm vào giỏ hàng.");
        alert.showAndWait();

        if (detailStage != null) {
            detailStage.close(); // Đóng hộp thoại chi tiết sau khi thêm vào giỏ hàng
        }
    }

    private void handlePurchase() {
        if (cart.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Giỏ hàng trống");
            alert.setHeaderText(null);
            alert.setContentText("Bạn không có sản phẩm nào trong giỏ hàng.");
            alert.showAndWait();
            return;
        }

        // Tại đây, bạn có thể triển khai logic để xử lý việc mua hàng

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mua hàng");
        alert.setHeaderText(null);
        alert.setContentText("Bạn đã mua thành công " + cart.size() + " sản phẩm.");
        alert.showAndWait();

        cart.clear(); // Xóa giỏ hàng sau khi mua
    }

    private void buyNow(Product product, ComboBox<String> sizeComboBox) {
        String selectedSize = sizeComboBox.getValue();
        if (selectedSize == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Lỗi chọn kích cỡ");
            alert.setHeaderText(null);
            alert.setContentText("Bạn cần chọn kích cỡ trước khi mua hàng.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mua ngay");
        alert.setHeaderText(null);
        alert.setContentText("Bạn đã chọn mua ngay sản phẩm: " + product.getName() +
                ".\nGiá: $" + product.getPrice() + "\nKích cỡ: " + selectedSize);
        alert.showAndWait();

        // Cập nhật kích cỡ cho sản phẩm
        product.setSizes(Collections.singletonList(selectedSize));
        cart.add(product); // Thêm sản phẩm vào giỏ hàng
        handlePurchase(); // Xử lý mua hàng

        if (detailStage != null) {
            detailStage.close(); // Đóng hộp thoại chi tiết sau khi mua ngay
        }
    }


    private void showProductDetails(Product product) {

        detailStage = new Stage(); // Lưu tham chiếu đến hộp thoại chi tiết
        VBox detailBox = new VBox();
        detailBox.setSpacing(10);
        detailBox.setPadding(new Insets(20));

        ImageView imageView = new ImageView(product.getImage());
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        Text nameText = new Text("Tên: " + product.getName());
        nameText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Text priceText = new Text("Giá: $" + product.getPrice());
        priceText.setStyle("-fx-font-size: 14px;");

        Text quantityText = new Text("Số lượng: " + product.getQuantity());
        quantityText.setStyle("-fx-font-size: 14px;");

        Text descriptionText = new Text("Mô tả: " + (product.getDescription() != null ? product.getDescription() : "Chưa có mô tả"));
        descriptionText.setStyle("-fx-font-size: 14px;");

        // Tạo ComboBox cho việc chọn kích cỡ
        ComboBox<String> sizeComboBox = new ComboBox<>();
        List<String> sizes = product.getSizes();
        if (sizes == null || sizes.isEmpty()) {
            sizeComboBox.getItems().addAll("S", "M", "L"); // Giá trị mặc định nếu sizes rỗng
        } else {
            sizeComboBox.getItems().addAll(sizes);
        }
        sizeComboBox.setPromptText("Chọn kích cỡ");


        Button addToCartButton = new Button("Thêm vào giỏ hàng");
        addToCartButton.setOnAction(e -> addToCart(product));

        Button buyNowButton = new Button("Mua ngay");
        buyNowButton.setOnAction(e -> buyNow(product,sizeComboBox));

        detailBox.getChildren().addAll(imageView, nameText, priceText, quantityText,sizeComboBox,descriptionText, addToCartButton, buyNowButton);

        Scene scene = new Scene(detailBox);
        detailStage.setScene(scene);
        detailStage.setTitle("Chi tiết sản phẩm");
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.show();
    }
}
