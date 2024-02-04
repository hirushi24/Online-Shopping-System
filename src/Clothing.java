import java.io.Serializable;

// Clothing class extends Product and implements Serializable
class Clothing extends Product implements Serializable {
    // Instance variables specific to Clothing
    private String size;
    private String color;

    // Constructor for creating a Clothing object
    public Clothing(String productId, String productName, int availableItems, double price, String size, String color) {
        // Call the constructor of the superclass (Product)
        super(productId, productName, availableItems, price);
        this.size = size;
        this.color = color;
    }

    // Getters and setters specific to Clothing
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // Display information method (overrides the displayInfo method in the superclass)
    @Override
    public void displayInfo() {
        System.out.println("Clothing - " + getProductName() + " | Size: " + size + " | Color: " + color);
    }

    // Override toString method to provide a formatted string representation of the object
    @Override
    public String toString() {
        return super.toString() +
                String.format("\nSize: %s\nColor: %s", size, color);
    }
}