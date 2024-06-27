package pl.coherentsolutions.core.product;

import java.io.Serializable;

public class Product implements Serializable {
    private final String name;
    private final String brand;
    private final Double price;
    private final Double size;
    private final Boolean isAvailable;
    private final String category;

    public Product(String name, String brand, Double price, Double size, String category, Boolean isAvailable) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.size = size;
        this.category = category;
        this.isAvailable = isAvailable;
    }

    public String getBrand() {
        return brand;
    }

    public Double getPrice() {
        return price;
    }

    public Double getSize() {
        return size;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", size=" + size +
                ", isAvailable=" + isAvailable +
                ", category='" + category + '\'' +
                '}';
    }
}
