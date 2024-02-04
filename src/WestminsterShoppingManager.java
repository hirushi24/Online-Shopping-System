import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class WestminsterShoppingManager implements ShoppingManager {
    private static final int MAX_PRODUCTS = 50;
    public static List<Product> inventory;
    private static final String FILE_PATH = "CW.txt";

    // Constructor initializes the inventory and loads existing inventory from file
    public WestminsterShoppingManager() {
        this.inventory = new ArrayList<>();
        loadInventoryFromFile();
    }

    // Implementing methods from the interface
    @Override
    public void displayMenu() {
        // Display the shopping system menu
        System.out.println("-----------------------------------------------------------");
        System.out.println("------------------- Shopping System Menu ------------------");
        System.out.println("             1.Add Product to Inventory");
        System.out.println("             2. Remove Product from Inventory");
        System.out.println("             3. Display Inventory");
        System.out.println("             4. Save Inventory to File");
        System.out.println("             5. Open the GUI. Exiting the system");
        System.out.println("----------------------------------------------------------");

        Scanner scanner = new Scanner(System.in);

        int choice = 0;
        try {
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number between 1 and 5.");
            scanner.nextLine(); // Consume the invalid input
        }
        switch (choice) {
            case 1:
                addProductToInventory(getProductFromUserInput());
                break;
            case 2:
                System.out.print("Enter Product ID to remove: ");
                String productIdToRemove = scanner.next();
                removeProductFromInventory(productIdToRemove);
                break;
            case 3:
                displayInventory();
                break;
            case 4:
                saveInventoryToFile();
                break;
            case 5:
                System.out.println("Open the GUI. Exiting the system");
                WestminsterShoppingGUI shoppingGUI = new WestminsterShoppingGUI();
                shoppingGUI.createTableModel();
                break;
        }

        // Recursively call the menu to allow for multiple operations
        displayMenu();
    }

    @Override
    public void addProductToInventory(Product product) { // Add a product to the inventory
        if (inventory.size() < MAX_PRODUCTS) {
            inventory.add(product);
            System.out.println("Product added to inventory: " + product.getProductName());
        } else {
            System.out.println("Cannot add more products. Maximum limit reached.");
        }
    }

    @Override
    public void removeProductFromInventory(String productId) {// Remove a product from the inventory based on its ID
        Iterator<Product> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getProductId().equals(productId)) {
                String productType = (product instanceof Electronics) ? "Electronics" : "Clothing";
                iterator.remove();
                System.out.println("Product deleted: " + product.getProductName() +
                        " | Type: " + productType);
                System.out.println("Total products left in the system: " + inventory.size());
                return;
            }
        }
        System.out.println("Product with ID " + productId + " not found in inventory.");
    }

    @Override
    public void displayInventory() {// Display the entire inventory, ordered by Product ID
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("------ Product List (Ordered by Product ID) ------");
            List<Product> sortedInventory = new ArrayList<>(inventory);
            Collections.sort(sortedInventory, Comparator.comparing(Product::getProductId));

            for (Product product : sortedInventory) {
                displayProductInfo(product);
                System.out.println("Type: " + (product instanceof Electronics ? "Electronics" : "Clothing"));
                System.out.println("------------------------");
            }
        }
    }

    // Display detailed information about a product
    private void displayProductInfo(Product product) {
        // Display product details
        System.out.println("Product ID: " + product.getProductId());
        System.out.println("Product Name: " + product.getProductName());
        System.out.println("Available Items: " + product.getAvailableItems());
        System.out.println("Price: " + product.getPrice() + "£");
        // Additional attributes specific to Electronics or Clothing can be displayed here
    }


    // Get user input and create a product based on the entered type
    private Product getProductFromUserInput() {  // Prompt user for product type and create corresponding product
        Scanner scanner = new Scanner(System.in);
        Product product = null;

        while (product == null) {
            System.out.println(" ");
            System.out.print("Enter Product Type (Electronics to 'E' or Clothing to 'C'): ");
            String productType = scanner.next();

            if ("E".equalsIgnoreCase(productType)) {
                product = createElectronicsProduct(scanner);
            } else if ("C".equalsIgnoreCase(productType)) {
                product = createClothingProduct(scanner);
            } else {
                System.out.println("Invalid Product Type. Please enter either 'Electronics' or 'Clothing'.");
            }
        }

        return product;
    }


    // Create an Electronics product based on user input
    private Electronics createElectronicsProduct(Scanner scanner) {   // Prompt user for Electronics product details and create the product
        try {
            System.out.print("Enter Product ID: ");
            String productId = scanner.next();

            System.out.print("Enter Product Name: ");
            String productName = scanner.next();

            System.out.print("Enter Available Items: ");
            int availableItems = scanner.nextInt();

            System.out.print("Enter Price (£): ");
            double price = scanner.nextDouble();

            System.out.print("Enter Brand: ");
            String brand = scanner.next();

            System.out.print("Enter Warranty Period (in months): ");
            int warrantyPeriod = scanner.nextInt();

            return new Electronics(productId, productName, availableItems, price, brand, warrantyPeriod);
        } catch (java.util.InputMismatchException e) {
            System.out.println(" ");
            System.out.println("Invalid input. Please enter valid values as follows:");
            System.out.println("Product ID: Alphanumeric");
            System.out.println("Product Name: Alphanumeric");
            System.out.println("Available Items: Numeric");
            System.out.println("Price: Numeric");
            System.out.println("Brand: Alphanumeric");
            System.out.println("Warranty Period: Numeric");
            scanner.nextLine(); // Consume the invalid input
            return null;
        }
    }

    // Create a Clothing product based on user input
    private Clothing createClothingProduct(Scanner scanner) {   // Prompt user for Clothing product details and create the product
        try {
            System.out.print("Enter Product ID: ");
            String productId = scanner.next();

            System.out.print("Enter Product Name: ");
            String productName = scanner.next();

            System.out.print("Enter Available Items: ");
            int availableItems = scanner.nextInt();

            System.out.print("Enter Price (£): ");
            double price = scanner.nextDouble();

            System.out.print("Enter Size: ");
            String size = scanner.next();

            System.out.print("Enter Color: ");
            String color = scanner.next();

            return new Clothing(productId, productName, availableItems, price, size, color);
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid values as follows:");
            System.out.println("Product ID: Alphanumeric");
            System.out.println("Product Name: Alphanumeric");
            System.out.println("Available Items: Numeric");
            System.out.println("Price: Numeric");
            System.out.println("Size: Alphanumeric");
            System.out.println("Color: Alphanumeric");
            scanner.nextLine(); // Consume the invalid input
            return null;
        }
    }


    // Method to save the inventory to a file
    public void saveInventoryToFile() {      // Save the current inventory to a file, appending to existing content
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            File file = new File(FILE_PATH);

            // If the file doesn't exist, write a header or any initialization info
            if (!file.exists()) {
                writer.println("ProductID,ProductName,AvailableItems,Price");
            }

            // Append all the product details to the existing file
            for (Product product : inventory) {
                writer.println("Product Type: " + (product instanceof Electronics ? "Electronics" : "Clothing"));
                writer.println(product.toString());
                writer.println();
            }
            System.out.println("Inventory saved to file successfully.");
        } catch (IOException e) {
            System.err.println("Error saving inventory to file: " + e.getMessage());
        }
    }


    // Method to load the inventory from a file
    private void loadInventoryFromFile() {     // Load inventory from a file if it exists
        try (Scanner scanner = new Scanner(new FileReader(FILE_PATH))) {
            while (scanner.hasNextLine()) {
                // Read product information from the file
                String productInfo = scanner.nextLine();
                if (!productInfo.isEmpty()) {
                    // Parse the product information and add it to the inventory
                    Product product = parseProductFromString(productInfo);
                    if (product != null) {
                        inventory.add(product);
                    }
                }
            }
            System.out.println("Inventory loaded from file successfully.");
        } catch (FileNotFoundException e) {
            // The file doesn't exist (first run), ignore this exception
        } catch (IOException e) {
            System.err.println("Error loading inventory from file: " + e.getMessage());
        }
    }

    // Parse a product from a string representation read from a file
    private Product parseProductFromString(String productInfo) {
        // Parse product information from a string and create a product
        String[] parts = productInfo.split(",");

        if (parts.length >= 4) {
            // Extract attributes from the string
            String productId = parts[0].trim();
            String productName = parts[1].trim();
            int availableItems = Integer.parseInt(parts[2].trim());
            double price = Double.parseDouble(parts[3].trim());

            // Create and return a new Product object
            return new Product(productId, productName, availableItems, price) {
                @Override
                public void displayInfo() {

                }
            };
        } else {
            // Return null if parsing fails
            return null;
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        shoppingManager.displayMenu();
    }
}
