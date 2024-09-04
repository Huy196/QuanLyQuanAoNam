package com.example.login;

import Entity.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AddProductController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;
    @FXML
    private ImageView imageView;
    private ProductListController productListController;
    private Product currentProduct;
    private Stage stage;
    private ObservableList<Product> products;
    private static final String FILE_PATH = "project.txt";

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setProducts(ObservableList<Product> products) {
        this.products = products;
    }

    @FXML //lấy image
    public void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            } catch (Exception e) {
                showError("Error loading image");
            }
        }
    }

    @FXML
    public void handleAdd() {
        try {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            Image image = imageView.getImage();

            if (name.isEmpty() || price <= 0 || quantity <= 0 || image == null) {
                throw new IllegalArgumentException("Please fill out all fields correctly.");
            }
            if (products == null) {
                throw new IllegalStateException("Product list is not initialized.");
            }
            Product newProduct = new Product(name, price, quantity, image);
            products.addAll(newProduct);
            saveProductsToFile();

            showAlert("Product added successfully!");
            Stage stage1 = (Stage) nameField.getScene().getWindow();
            stage1.close();

        } catch (NumberFormatException e) {
            showError(e.getMessage());
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (IllegalStateException e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String mesage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mesage);
        alert.show();
    }

    private void saveProductsToFile() { //lưu dữ liệu product để tránh bị mất khi thoát
        try (FileWriter writer = new FileWriter(FILE_PATH, false)) {
            for (Product product : products) {
                writer.write(product.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            showError("Failed to save products: " + e.getMessage());
        }
    }

    @FXML //update sản phầm
    public void handAddOrUpdate() {
        try {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            Image image = imageView.getImage();

            if (name.isEmpty() || price <= 0 || quantity <= 0 || image == null) {
                throw new IllegalArgumentException("Please fill out all fields correctly.");
            }
            if (products == null) {
                throw new IllegalStateException("Product list is not initialized.");
            }
            currentProduct.setName(name);
            currentProduct.setPrice(price);
            currentProduct.setQuantity(quantity);
            currentProduct.setImage(image);
            saveProductsToFile();
            showAlert("Product saved successfully!");

            Stage stage1 = (Stage) nameField.getScene().getWindow();
            stage1.close();

        } catch (NumberFormatException e) {
            showError(e.getMessage());
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (IllegalStateException e) {
            showError(e.getMessage());
        }
    }

    public void setProductListController(ProductListController productListController) { //thiết lập giá trị tham chiếu từ bên ngoài
    }

    public void setCurrentProduct(Product product) {
        this.currentProduct = product;
        if (product != null) {
            nameField.setText(product.getName());
            priceField.setText(String.valueOf(product.getPrice()));
            quantityField.setText(String.valueOf(product.getQuantity()));
            imageView.setImage(product.getImage()); // Hiển thị hình ảnh sản phẩm
        }
    }
}
