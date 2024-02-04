import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

// Abstract class representing a product
abstract class Product implements Serializable,Comparable<Product> {
    // Instance variables for product details
    private String productId;
    private String productName;
    private int availableItems;
    private double price;

    // Constructor for creating a Product object
    public Product(String productId, String productName, int availableItems, double price) {
        this.productId = productId;
        this.productName = productName;
        this.availableItems = availableItems;
        this.price = price;
    }

    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Abstract method to be implemented by subclasses to display product information
    public abstract void displayInfo();

    // Abstract method to be implemented by subclasses if additional information needs to be retrieved
    public Object getInfo() {
        Object o = null;
        return null;
    }

    // Implementation of the Comparable interface to enable sorting of products
    @Override
    public int compareTo(Product otherProduct) {
        // Assuming getProductId() returns a String
        return this.getProductName().compareTo(otherProduct.getProductName());
    }

    // Override toString method to provide a formatted string representation of the object
    @Override
    public String toString() {
        return String.format("Product ID: %s\nProduct Name: %s\nAvailable Items: %d\nPrice: %.2f",
                getProductId(), getProductName(), getAvailableItems(), getPrice());
    }
}