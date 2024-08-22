package com.example.login;

import Entity.Product;
import javafx.animation.ScaleTransition;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Optional;

public class ProductHomeUser {

    private HostServices hostServices;

    @FXML
    private FlowPane productContainer;
    @FXML
    private VBox productDisplayArea;

    @FXML
    private ImageView nguoiDung; // Thêm ImageView

    @FXML
    private TextField searchProduct; // TextField để nhập từ khóa tìm kiếm

    @FXML
    private Button searchButton; // Nút "Tìm kiếm"



    private List<Product> cart = new ArrayList<>(); // Giỏ hàng
    private List<Product> productList = new ArrayList<>();
    private Stage detailStage; // Tham chiếu đến hộp thoại chi tiết sản phẩm
    private String[] currentUser = {"doquochuy", "123456789", "doquochuy@gmail.com", "0743278462", "admin"};

    @FXML
    private void initialize() {
        loadProductsFromFile();
        searchButton.setOnAction(event -> handleSearch());
        displayAllProducts();
        nguoiDung.setOnMouseClicked(mouseEvent -> showUserInfo());
    }

    private void showUserInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông tin người dùng");
        alert.setHeaderText("Thông tin chi tiết");
        alert.setContentText(
                "Username: " + currentUser[0] + "\n" +
                        "Email: " + currentUser[2] + "\n" +
                        "SĐT: " + currentUser[3] + "\n" +
                        "Role: " + currentUser[4]
        );
        alert.showAndWait();
    }




    // Phương thức để tải sản phẩm từ file vào danh sách productList
    private void loadProductsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("project.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product product = Product.fromString(line);
                productList.add(product); // Thêm sản phẩm vào danh sách
            }
            displayAllProducts(); // Hiển thị tất cả sản phẩm sau khi load xong
        } catch (IOException e) {
            System.err.println("Không thể tải sản phẩm: " + e.getMessage());
        }
    }


    private void displayAllProducts() {
        productContainer.getChildren().clear(); // Xóa các sản phẩm cũ trên giao diện
        for (Product product : productList) {
            addProductToDisplay(product); // Thêm từng sản phẩm vào giao diện
        }
    }




    private void handleSearch() {
        String searchText = searchProduct.getText().toLowerCase(); // Lấy từ khóa tìm kiếm

        // Lọc danh sách sản phẩm dựa trên từ khóa
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(searchText)) {
                filteredList.add(product);
            }
        }

        // Kiểm tra nếu danh sách lọc trống
        if (filteredList.isEmpty()) {
            // Hiển thị thông báo alert nếu không tìm thấy sản phẩm
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Không có sản phẩm phù hợp với từ khóa tìm kiếm!");
            alert.showAndWait();
            searchProduct.clear();
            displayAllProducts();
        }else {
            // Nếu tìm thấy sản phẩm, cập nhật giao diện với các sản phẩm đã lọc
            productContainer.getChildren().clear(); // Xóa các sản phẩm cũ khỏi màn hình
            for (Product product : filteredList) {
                addProductToDisplay(product); // Hiển thị các sản phẩm đã lọc
            }
        }
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
    }

    private void addToCart(Product product,  int quantity) {
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

        Text descriptionText = new Text("Mô tả: " + (product.getDescription() != null ? product.getDescription() : " \n Phong cách---> " + "Hàn Quốc\n" +
                "Chiều dài áo---> " + "Dài\n" +
                "Xuất xứ---> " + "Việt Nam\n" +
                "Chất liệu---> " + "Cotton\n"
                ));
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
        HBox buttonBox = new HBox(10); // Khoảng cách giữa các nút là  10px
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
