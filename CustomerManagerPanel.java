import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ProductManagerPanel extends JPanel {
    private JButton createButton, viewButton, updateButton, deleteButton;

    public ProductManagerPanel() {
        setLayout(new GridLayout(4, 1, 10, 10));

        createButton = new JButton("Create Product");
        viewButton = new JButton("View Product");
        updateButton = new JButton("Update Product");
        deleteButton = new JButton("Delete Product");

        createButton.addActionListener(e -> createProduct());
        viewButton.addActionListener(e -> viewProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());

        add(createButton);
        add(viewButton);
        add(updateButton);
        add(deleteButton);
    }

    private static final String URL = "jdbc:mysql://localhost:3306/attempt";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private void createProduct() {
        String productId = JOptionPane.showInputDialog("Enter Product ID:");
        String gameId = JOptionPane.showInputDialog("Enter Game ID:");
        String console = JOptionPane.showInputDialog("Enter Console Name:");
        String price = JOptionPane.showInputDialog("Enter Price:");

        if (!isValidInteger(productId) || !isValidDouble(price)) {
            JOptionPane.showMessageDialog(this, "Product ID must be a number, and Price must be a valid amount.");
            return;
        }

        if (!isGameIdExists(gameId)) {
            JOptionPane.showMessageDialog(this, "Game ID does not exist in the Games table. Please provide a valid Game ID.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO products (product_id, game_id, console, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(productId));
                stmt.setString(2, gameId);
                stmt.setString(3, console);
                stmt.setDouble(4, Double.parseDouble(price));

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Product Created Successfully!");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, "Product ID already exists. Please choose a different ID.");
            } else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error creating product: " + e.getMessage());
            }
        }
    }

    private void viewProduct() {
        String productId = JOptionPane.showInputDialog("Enter Product ID to View:");

        if (!isValidInteger(productId)) {
            JOptionPane.showMessageDialog(this, "Product ID must be a number.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM products WHERE product_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(productId));
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String data = "Product ID: " + rs.getInt("product_id") + "\n" +
                                  "Game ID: " + rs.getString("game_id") + "\n" +
                                  "Console: " + rs.getString("console") + "\n" +
                                  "Price: " + rs.getDouble("price");
                    JOptionPane.showMessageDialog(this, data);
                } else {
                    JOptionPane.showMessageDialog(this, "No Product found with ID: " + productId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving product: " + e.getMessage());
        }
    }

    private void updateProduct() {
        String productId = JOptionPane.showInputDialog("Enter Product ID to Update:");
        String gameId = JOptionPane.showInputDialog("Enter New Game ID:");
        String console = JOptionPane.showInputDialog("Enter New Console Name:");
        String price = JOptionPane.showInputDialog("Enter New Price:");

        if (!isValidInteger(productId) || !isValidDouble(price)) {
            JOptionPane.showMessageDialog(this, "Product ID must be a number, and Price must be a valid amount.");
            return;
        }

        if (!isGameIdExists(gameId)) {
            JOptionPane.showMessageDialog(this, "Game ID does not exist in the Games table. Please provide a valid Game ID.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE products SET game_id = ?, console = ?, price = ? WHERE product_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, gameId);
                stmt.setString(2, console);
                stmt.setDouble(3, Double.parseDouble(price));
                stmt.setInt(4, Integer.parseInt(productId));

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Product Updated Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "No Product found with ID: " + productId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating product: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        String productId = JOptionPane.showInputDialog("Enter Product ID to Delete:");

        if (!isValidInteger(productId)) {
            JOptionPane.showMessageDialog(this, "Product ID must be a number.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM products WHERE product_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(productId));

                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Product Deleted Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "No Product found with ID: " + productId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting product: " + e.getMessage());
        }
    }

    private boolean isGameIdExists(String gameId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT COUNT(*) FROM games WHERE game_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, gameId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
