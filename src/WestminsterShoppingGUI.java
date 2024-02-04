import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class WestminsterShoppingGUI {

    private JFrame frame;
    private JComboBox<String> productTypeComboBox;
    private JTable productTable;
    private JTextArea productDetailsTextArea;
    private JButton addToCartButton;
    private JButton viewShoppingCartButton;


    // Lists to store inventory, shopping cart, and purchase history
    public static List<Product> inventory;
    private List<Product> shoppingCart;
    private Map<String, Integer> purchaseHistory;


    public WestminsterShoppingGUI() {
        // Initialize lists
        inventory = new ArrayList<>();
        shoppingCart = new ArrayList<>();
        purchaseHistory = new HashMap<>();

        // Create the main frame
        frame = new JFrame("Westminster Shopping System");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        productTypeComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});
        productTable = new JTable();
        productTable.setBounds(200, 30, 700, 400);
        productDetailsTextArea = new JTextArea();
        addToCartButton = new JButton("Add to Cart Shopping Cart");
        viewShoppingCartButton = new JButton("Shopping Cart");

        // Layout
        frame.setLayout(new BorderLayout());

        // Add productTable and productDetailsTextArea to the CENTER region with a GridLayout
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(new JScrollPane(productTable));
        centerPanel.add(productDetailsTextArea);

        // Layout
        frame.add(centerPanel, BorderLayout.CENTER);

        // Create top panel with productTypeComboBox and viewShoppingCartButton
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Select Product Type:"));
        topPanel.add(productTypeComboBox);
        topPanel.add(viewShoppingCartButton);

        frame.add(topPanel, BorderLayout.NORTH);

        // Create bottom panel with addToCartButton
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(addToCartButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Event handlers
        productTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProductTable((String) productTypeComboBox.getSelectedItem());
            }
        });

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });

        viewShoppingCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewShoppingCart();
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }


    // Method to update the product table based on the selected product type
    private void updateProductTable(String productType) {
        List<Product> products = fetchProductsByType(productType);
        DefaultTableModel tableModel = createTableModel(products); // Pass the products to createTableModel
        productTable.setModel(tableModel);

        // Clear the selection to prevent errors
        productTable.clearSelection();

        // Add a selection listener to update product details when a row is selected
        ListSelectionModel selectionModel = productTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    String productId = (String) productTable.getValueAt(selectedRow, 0);
                    for (Product product : products) { // Loop through the filtered products
                        if (product.getProductId().equals(productId)) {
                            displayProductDetails(product);
                        }
                    }
                }
            }
        });
    }


    // Method to create a table model based on the list of products
    private DefaultTableModel createTableModel(List<Product> products) {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Category");
        tableModel.addColumn("Price   £" );
        tableModel.addColumn("Info");

        Collections.sort(products); // Sort the filtered products
        for (Product product : products) {
            String[] rowData = {
                    product.getProductId(),
                    product.getProductName(),
                    getProductCategory(product),
                    String.valueOf(product.getPrice()),
                    getProductInfo(product)
            };

            // Highlight low availability products and add them to the shopping cart
            if (product.getAvailableItems() < 3) {
                for (int i = 0; i < rowData.length; i++) {
                    rowData[i] = "<html><font color='red'>" + rowData[i] + "</font></html>";
                }

                // If the product has low availability, add it to the shopping cart
                shoppingCart.add(product);
            }

            tableModel.addRow(rowData);
        }

        return tableModel;
    }


    // Method to display detailed information about a selected product
    private void displayProductDetails(Product product) {
        if (product != null) {
            String details = "Product Details:\n";
            details += "Product ID: " + (product.getProductId() != null ? product.getProductId() : "") + "\n";
            details += "Name: " + (product.getProductName() != null ? product.getProductName() : "") + "\n";
            details += "Category: " + getProductCategory(product) + "\n";

            // Check for null before concatenating the price
            details += "Price: £" + product.getPrice() + "\n";

            details += "Info: " + (getProductInfo(product) != null ? getProductInfo(product) : "") + "\n";

            productDetailsTextArea.setText(details);
        } else {
            // Handle the case when selectedProduct is null (optional)
            productDetailsTextArea.setText("No product selected.");
        }
    }


    // Method to determine the category of a product
    private String getProductCategory(Product product) {
        if (product instanceof Electronics) {
            return "Electronics";
        } else if (product instanceof Clothing) {
            return "Clothing";
        }
        return "";
    }


    // Method to fetch products based on the selected product type
    private List<Product> fetchProductsByType(String productType) {
        List<Product> products = new ArrayList<>();
        for (Product product : WestminsterShoppingManager.inventory) {
            if ("All".equals(productType) || productType.equals(getProductCategory(product))) {
                products.add(product);
            }
        }
        return products;
    }


    // Method to create a table model for the entire inventory
    public DefaultTableModel createTableModel() {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Category");
        tableModel.addColumn("Price");
        tableModel.addColumn("Info");

        Collections.sort(WestminsterShoppingManager.inventory);
        for (Product product : WestminsterShoppingManager.inventory) {
            String[] rowData = {
                    product.getProductId(),
                    product.getProductName(),
                    getProductCategory(product),
                    String.valueOf(product.getPrice()),
                    getProductInfo(product)
            };
            tableModel.addRow(rowData);
        }

        return tableModel;
    }


    // Method to get additional information about a product (specific to Electronics or Clothing)
    private String getProductInfo(Product product) {
        if (product instanceof Electronics) {
            Electronics electronics = (Electronics) product;
            return "Brand: " + electronics.getBrand() + ", Warranty: " + electronics.getWarrantyPeriod() + " months";
        } else if (product instanceof Clothing) {
            Clothing clothing = (Clothing) product;
            return "Size: " + clothing.getSize() + ", Color: " + clothing.getColor();
        }
        return "";
    }


    // Method to add the selected product to the shopping cart
    public void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String productId = (String) productTable.getValueAt(selectedRow, 0);
            for (Product product : WestminsterShoppingManager.inventory) {
                if (product.getProductId().equals(productId)) {
                    shoppingCart.add(product);
                    break;
                }

            }

            JOptionPane.showMessageDialog(frame, "Product added to the shopping cart: " );
        }
    }


    // Method to view the contents of the shopping cart, calculate discounts, and display the final total
    private void viewShoppingCart() {
        DecimalFormat df = new DecimalFormat("#.##");

        // Create a table model for the shopping cart
        DefaultTableModel cartTableModel = new DefaultTableModel();
        cartTableModel.addColumn("Product");
        cartTableModel.addColumn("Quantity");
        cartTableModel.addColumn("Price   £");

        double totalCost = 0.0;

        // Map to keep track of product quantities in the cart
        Map<Product, Integer> productQuantities = new HashMap<>();

        for (Product product : shoppingCart) {
            // Update quantity in the map
            productQuantities.put(product, productQuantities.getOrDefault(product, 0) + 1);
        }

        // Iterate through products in the shopping cart
        for (Map.Entry<Product, Integer> entry : productQuantities.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            // Display product, quantity, and price in the table
            cartTableModel.addRow(new Object[]{product.getProductName(), quantity, product.getPrice() * quantity});

            // Update total cost
            totalCost += product.getPrice() * quantity;
        }

        // Calculate and display discounts
        int discount = calculateDiscount();

        // Display total cost before any discounts
        String cartDetails = "Shopping Cart:\n";
        cartDetails += "\nTotal :" + totalCost +"£";

        // Check for first purchase discount
        double firstPurchaseDiscount = 0.0;
        String username = JOptionPane.showInputDialog(frame, "Enter your username:");
        if (username != null && !purchaseHistory.containsKey(username)) {
            Product firstProduct = shoppingCart.get(0); // Get the first product in the cart
            firstPurchaseDiscount = firstProduct.getPrice() * 0.1; // Apply discount to the price of the first product
            cartDetails += "\nFirst Purchase Discount (10%): " + df.format(firstPurchaseDiscount) + "£";
            purchaseHistory.put(username, 1);
        }

        // Check for category discount
        Map<String, Integer> categoryCounts = new HashMap<>();
        for (Product product : shoppingCart) {
            categoryCounts.merge(getProductCategory(product), 1, Integer::sum);
        }

        double categoryDiscount = 0.0;
        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            String category = entry.getKey();
            int count = entry.getValue();

            if (count >= 3) {
                double categoryTotalCost = shoppingCart.stream()
                        .filter(product -> getProductCategory(product).equals(category))
                        .mapToDouble(product -> product.getPrice() * productQuantities.get(product))
                        .sum();

                categoryDiscount = categoryTotalCost * 0.2; // 20% discount for three or more products of the same category
                cartDetails += "\nThree items in same category total after discount(20%): "+ df.format(categoryDiscount) + "£";
                break; // Apply discount only once even if multiple categories qualify
            }
        }

        if (username != null && !purchaseHistory.containsKey(username)) {
            Product firstProduct = shoppingCart.get(0); // Get the first product in the cart
            firstPurchaseDiscount = firstProduct.getPrice() * 0.1; // Apply discount to the price of the first product
            cartDetails += "\nFirst Purchase Discount (10%): " + df.format(firstPurchaseDiscount) + "£";
            purchaseHistory.put(username, 1);
        }

// Calculate and display final total after applying discounts
        double discountedCost = totalCost - categoryDiscount - firstPurchaseDiscount;
        cartDetails += "\nFinal Total : " + df.format(discountedCost) + "£";

        // Create a new table with shopping cart details and display it
        JTable cartTable = new JTable(cartTableModel);
        JScrollPane scrollPane = new JScrollPane(cartTable);

        JOptionPane.showMessageDialog(frame, new Object[]{scrollPane, cartDetails}, "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);

    }

    // Placeholder method to calculate discounts
        private int calculateDiscount() {
        int i = 0;
        return i;
    }

    // Main method for testing
    public static void main(String[] args) {
       WestminsterShoppingGUI w1 = new WestminsterShoppingGUI();
    }


}
