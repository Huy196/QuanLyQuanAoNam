package Entity;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Product implements Serializable {
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty quantity;
    private final ObjectProperty<Image> image;
    private int selectedQuantity;
    private String description;
    private List<String> sizes;

    public Product(String name, Double price, Integer quantity, Image image) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.image = new SimpleObjectProperty<>(image);
    }


    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSizes() {
        return sizes != null ? sizes : new ArrayList<>();
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public Image getImage() {
        return image.get();
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    public void setImage(Image image) {
        this.image.set(image);
    }

    @Override
    public String toString() {
        return getName() + "," + getPrice() + "," + getQuantity() + "," + getImage().getUrl();
    }

    public static Product fromString(String data) {
        // Example data format: "name,price,quantity,imagePath,description,size"
        String[] parts = data.split(",");
        return new Product(
                parts[0],
                Double.parseDouble(parts[1]),
                Integer.parseInt(parts[2]),
                new Image(parts[3])
        );

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return
                quantity == product.quantity &&
                Objects.equals(name, product.name) &&
                Objects.equals(image, product.image) &&
                Objects.equals(description, product.description) &&
                Objects.equals(sizes, product.sizes);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name, price, quantity, image, description, sizes);
    }

    public ImageView getImageView() {
        ImageView imageView = new ImageView(String.valueOf(image));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        return imageView;
    }


}
