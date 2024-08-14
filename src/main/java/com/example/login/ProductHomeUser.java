package com.example.login;

import Entity.Product;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductHomeUser {
    @FXML
    private VBox productContainer;
//    private ObservableList<String> productList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        loadProductsFromFile();
//        productListView.setItems(productList);
    }
    private void loadProductsFromFile() {
//        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("project.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product product = Product.fromString(line);
                addProductToDisplay(product);
            }
        } catch (IOException e) {
            System.err.println("Failed to load products: " + e.getMessage());
        }
    }

    private void addProductToDisplay(Product product) {

        ImageView imageView = new ImageView(product.getImage());
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        // Tạo các phần tử giao diện cho sản phẩm
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

        productBox.getChildren().addAll(imageView, nameText, priceText, quantityText,detailsButton);
        productContainer.getChildren().add(productBox);
    }

    private void showProductDetails(Product product) {
        Stage detailStage = new Stage();
        VBox detailBox = new VBox();
        detailBox.setSpacing(10);
        detailBox.setPadding(new Insets(20));

        // Tạo ImageView và thiết lập hình ảnh sản phẩm
        ImageView imageView = new ImageView(product.getImage());
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        // Tạo Text để hiển thị tên, giá và số lượng
        Text nameText = new Text("Tên: " + product.getName());
        nameText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Text priceText = new Text("Giá: $" + product.getPrice());
        priceText.setStyle("-fx-font-size: 14px;");

        Text quantityText = new Text("Số lượng: " + product.getQuantity());
        quantityText.setStyle("-fx-font-size: 14px;");

        // Thêm các phần tử vào VBox
        detailBox.getChildren().addAll(imageView, nameText, priceText, quantityText);

        // Tạo Scene và hiển thị cửa sổ chi tiết
        Scene scene = new Scene(detailBox);
        detailStage.setScene(scene);
        detailStage.setTitle("Chi tiết sản phẩm");
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.show();
    }
}
