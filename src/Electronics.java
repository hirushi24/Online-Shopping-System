import java.io.Serializable;

// Electronics class represents electronic products and extends the Product class
class Electronics extends Product implements Serializable {
    // Additional attributes specific to Electronics
    private String brand;
    private int warrantyPeriod;

    // Constructor for creating an Electronics object
    public Electronics(String productId, String productName, int availableItems, double price, String brand, int warrantyPeriod) {
        super(productId, productName, availableItems, price);
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    // Getters and setters specific to Electronics
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    // Display information method
    @Override
    public void displayInfo() {
        System.out.println("Electronics - " + getProductName() + " | Brand: " + brand + " | Warranty: " + warrantyPeriod + " months");
    }

    @Override
    public String toString() {
        return super.toString() +
                String.format("\nBrand: %s\nWarranty Period: %d months", brand, warrantyPeriod);
    }
}
