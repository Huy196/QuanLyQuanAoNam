package Entity;

import javafx.beans.property.*;
import javafx.scene.image.Image;

public class Product {
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty quantity;
    private final ObjectProperty<Image> image;

    public Product(String name, Double price, Integer quantity, Image image) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.image = new SimpleObjectProperty<>(image);
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

    public static Product fromString(String line) {
//         Phân tách chuỗi để tạo đối tượng Product
        String[] parts = line.split(",", 4);
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid product format.");
        }
        String name = parts[0];
        double price = Double.parseDouble(parts[1]);
        int quantity = Integer.parseInt(parts[2]);
        Image image = new Image(parts[3]);

        return new Product(name, price, quantity, image);
    }
}
