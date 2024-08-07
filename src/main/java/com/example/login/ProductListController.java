package com.example.login;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductListController {
    @FXML
    TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, Image> imageColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    @FXML
    private TableColumn<Product, Void> actionColumn;

    private Runnable addProductHandler;

    private ObservableList<Product> products = FXCollections.observableArrayList();
    private Stage stage;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(productTable.getItems().indexOf(cellData.getValue()) + 1).asObject());

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());

        imageColumn.setCellValueFactory(cellData -> cellData.getValue().imageProperty());
        imageColumn.setCellFactory(col -> new TableCell<Product, Image>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Image image, boolean empty) {
                super.updateItem(image, empty);
                if (empty || image == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(image);
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    setGraphic(imageView);
                }
            }
        });
        actionColumn.setCellFactory(col -> new TableCell<Product, Void>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");

            {
                updateButton.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handleEdit(product);
                });

                deleteButton.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    if (showConfirmation("Are you sure you want to delete this product?")) {
                        handleDelete(product);
                    }
                });

                HBox buttons = new HBox(10, updateButton, deleteButton);
                buttons.setPadding(new Insets(5));
                setGraphic(buttons);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : getGraphic());
            }
        });
        productTable.setItems(products);

        List<Product> loadedProducts = loadProductsFromFile();
        products.addAll(loadedProducts);
    }

    @FXML
    private void handleAddProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
            BorderPane root = loader.load();

            AddProductController controller = loader.getController();
            controller.setStage(stage);
            controller.setProducts(products);

            Stage addProductStage = new Stage();
            addProductStage.setScene(new Scene(root));
            addProductStage.setTitle("Add Product");
            addProductStage.initOwner(stage);
            addProductStage.initModality(Modality.WINDOW_MODAL);

            addProductStage.setOnHiding(e -> {
                productTable.refresh();
                saveProductsToFile();

            });

            addProductStage.show();
        } catch (IOException e) {
            showError("Error loading add product window: " + e.getMessage());
        }
    }

    private void handleEdit(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Update_Product.fxml"));
            BorderPane root = loader.load();

            AddProductController controller = loader.getController();
            controller.setStage(stage);
            controller.setProducts(products);
            controller.setProductListController(this); // Thiết lập ProductListController
            controller.setCurrentProduct(product); // Thiết lập sản phẩm hiện tại để chỉnh sửa

            Stage editProductStage = new Stage();
            editProductStage.setScene(new Scene(root));
            editProductStage.setTitle("Edit Product");
            editProductStage.initOwner(stage);
            editProductStage.initModality(Modality.WINDOW_MODAL);

            editProductStage.setOnHiding(e -> {
                refreshTable();
                saveProductsToFile();
            });

            editProductStage.show();
        } catch (IOException e) {
            showError("Error loading edit product window: " + e.getMessage());
        }
    }

    private void handleDelete(Product product) {
        products.remove(product);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private boolean showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().get() == ButtonType.OK;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private List<Product> loadProductsFromFile() {
        List<Product> products = new ArrayList<>();
        File file = new File("project.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Product product = Product.fromString(line);
                    products.add(product);
                }
            } catch (IOException e) {
                showError("Failed to load products: " + e.getMessage());
            }
        }
        return products;
    }

    private void saveProductsToFile() {
        try (FileWriter writer = new FileWriter("project.txt", false)) {
            for (Product product : products) {
                writer.write(product.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            showError("Failed to save products: " + e.getMessage());
        }
    }

    public void refreshTable() {
        productTable.refresh();
    }
}
