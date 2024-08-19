package com.example.login;

import Entity.Product;
import javafx.animation.ScaleTransition;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductHomeUser {
    @FXML
    private Hyperlink facebookLink;

    private HostServices hostServices;

    @FXML
    private FlowPane productContainer;
    @FXML
    private VBox productDisplayArea;

    @FXML
    private Button purchaseButton; // Nút để xử lý việc mua hàng

    private List<Product> cart = new ArrayList<>(); // Giỏ hàng
    private Stage detailStage; // Tham chiếu đến hộp thoại chi tiết sản phẩm

    @FXML
    private void initialize() {
        loadProductsFromFile();

//        purchaseButton.setOnAction(e -> handlePurchase());


    }
    @FXML
    private void handleHome() {
        displayProducts("Trang chủ");
    }

    @FXML
    private void handleShirts() {
        displayProducts("Áo");
    }

    @FXML
    private void handlePants() {
        displayProducts("Quần");
    }

    @FXML
    private void handleSuits() {
        displayProducts("Đồ bộ");
    }

    @FXML
    private void handleAccessories() {
        displayProducts("Phụ Kiện");
    }

    private void displayProducts(String category) {
        productDisplayArea.getChildren().clear();
        // Giả sử bạn có một phương thức để lấy sản phẩm theo danh mục
        // List<Product> products = getProductsByCategory(category);
        // for (Product product : products) {
        //     // Hiển thị sản phẩm
        //     productDisplayArea.getChildren().add(createProductView(product));
        // }

        // Dưới đây là một ví dụ đơn giản để hiển thị tên danh mục
        Label categoryLabel = new Label("Sản phẩm trong danh mục: " + category);
        productDisplayArea.getChildren().add(categoryLabel);
    }



    @FXML
    private void handleFacebookLinkAction() {
        if (hostServices != null) {
            hostServices.showDocument("https://www.facebook.com/profile.php?id=100025115431173&mibextid=ZbWKwL");
        } else {
            System.out.println("HostServices is null");
        }
    }

    @FXML
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
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
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        VBox productBox = new VBox();
        productBox.setSpacing(15);
        productBox.setAlignment(Pos.CENTER);
        productBox.setStyle("-fx-padding: 10; -fx-border-color: black; -fx-border-width: 1; -fx-border-radius: 5; ");

        productBox.setMinWidth(200);
        productBox.setMinHeight(370);
        productBox.setMaxWidth(170);
        productBox.setMaxHeight(370);


        Text nameText = new Text("Tên: " + product.getName());
        nameText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Text priceText = new Text("Giá: $" + product.getPrice());
        priceText.setStyle("-fx-font-size: 14px;");
        priceText.setFill(Color.RED);

//        Text quantityText = new Text("Số lượng: " + product.getQuantity());
//        quantityText.setStyle("-fx-font-size: 14px;");

        Button detailsButton = new Button("Chi tiết");
        detailsButton.setOnAction(e -> showProductDetails(product));
        detailsButton.setStyle("-fx-font-size: 14px; -fx-background-color: #ff7337; -fx-text-fill: white");

        HBox productHBox = new HBox();
        productHBox.setSpacing(10);
        productHBox.getChildren().addAll(imageView, productBox);


        productBox.getChildren().addAll(imageView, nameText, priceText, detailsButton);
        productContainer.getChildren().add(productBox);

//làm phồng
//        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(50), productHBox);
//        scaleUp.setToX(1.1);
//        scaleUp.setToY(1.1);
//
//        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(50), productHBox);
//        scaleDown.setToX(1.0);
//        scaleDown.setToY(1.0);
//
//        productContainer.setOnMouseEntered(event -> scaleUp.play());
//        productContainer.setOnMouseExited(event -> scaleDown.play());

    }

    private void addToCart(Product product, int quantity) {
        for (int i = 0; i < quantity; i++) {
            cart.add(product);
        }
        product.setQuantity(product.getQuantity() - quantity);// số lượng sản phẩm giảm sau mỗi lần mua

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Giỏ hàng");
        alert.setHeaderText(null);
        alert.setContentText(product.getName() + " x" + quantity + " đã được thêm vào giỏ hàng.");
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

    private void buyNow(Product product, ComboBox<String> sizeComboBox, int quantity) {
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
        alert.setContentText("Bạn đã chọn mua ngay " + quantity + " sản phẩm: " + product.getName() +
                ".\nGiá: $" + product.getPrice()*quantity + "\nKích cỡ: " + selectedSize);
        alert.showAndWait();

        // Cập nhật kích cỡ cho sản phẩm
        product.setSizes(Collections.singletonList(selectedSize));
        addToCart(product, quantity); // Thêm sản phẩm vào giỏ hàng
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
        detailBox.setAlignment(Pos.CENTER);

        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        detailBox.setBackground(background);

        detailBox.setMinWidth(450);
        detailBox.setMinHeight(630);
        detailBox.setMaxWidth(970);
        detailBox.setMaxHeight(1700);


        ImageView imageView = new ImageView(product.getImage());
        imageView.setFitWidth(400);
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 5, 0, 5, 5)");

        Text nameText = new Text("Tên: " + product.getName());
        nameText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Text priceText = new Text("Giá: $" + product.getPrice());
        priceText.setStyle("-fx-font-size: 14px; -fx-fill: red");

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

        // Tạo Spinner cho việc chọn số lượng
        Spinner<Integer> quantitySpinner = new Spinner<>(1, product.getQuantity(), 1);
        quantitySpinner.setPrefWidth(100);

        // Tạo các nút
        Button addToCartButton = new Button("Thêm vào giỏ hàng");
        addToCartButton.setOnAction(e -> addToCart(product, quantitySpinner.getValue()));
        addToCartButton.setStyle("-fx-background-color: #ff7337; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 7, 0, 5, 5); -fx-text-fill: white; -fx-font-size: 15");

        Button buyNowButton = new Button("Mua ngay");
        buyNowButton.setOnAction(e -> buyNow(product, sizeComboBox, quantitySpinner.getValue()));
        buyNowButton.setStyle("-fx-background-color: #ff7337;-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 7, 0, 5, 5); -fx-text-fill: white; -fx-font-size: 15");

        // Sử dụng HBox để đặt hai nút trên cùng một hàng
        HBox buttonBox = new HBox(10); // Khoảng cách giữa các nút là 10px
        buttonBox.setAlignment(Pos.CENTER);// Căn giữa các nút trong HBox
        buttonBox.setSpacing(30);
        buttonBox.getChildren().addAll(addToCartButton, buyNowButton);

        // Thêm các thành phần vào VBox chính
        detailBox.getChildren().addAll(imageView, nameText, priceText, quantityText, sizeComboBox, descriptionText, quantitySpinner, buttonBox);

        // Hiển thị hộp thoại
        Scene scene = new Scene(detailBox);
        detailStage.setScene(scene);
        detailStage.setTitle("Chi tiết sản phẩm");
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.show();
    }

}
