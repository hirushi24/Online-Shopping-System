import java.util.List;

// Interface for shopping manager
interface ShoppingManager {
    void displayMenu();
    void addProductToInventory(Product product);
    void removeProductFromInventory(String productId);
    void displayInventory();

}