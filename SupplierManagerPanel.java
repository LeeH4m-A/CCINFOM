import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SupplierManagerPanel extends JPanel {
    private JButton createButton;
    private JButton viewButton;
    private JButton updateButton;
    private JButton deleteButton;

    public SupplierManagerPanel() {
        setLayout(new GridLayout(4, 1, 10, 10));

        // Initialize buttons
        createButton = new JButton("Create Record");
        viewButton = new JButton("View Record");
        updateButton = new JButton("Update Record");
        deleteButton = new JButton("Delete Record");

        // Add functionality to each button
        createButton.addActionListener(e -> createRecord());
        viewButton.addActionListener(e -> viewRecord());
        updateButton.addActionListener(e -> updateRecord());
        deleteButton.addActionListener(e -> deleteRecord());

        // Add buttons to the panel
        add(createButton);
        add(viewButton);
        add(updateButton);
        add(deleteButton);
    }

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/attempt"; // Update with your database URL
    private static final String USER = "root"; // Update with your database user
    private static final String PASSWORD = "root"; // Update with your database password

    // CRUD Operations

    private void createRecord() {
        String supplierId = JOptionPane.showInputDialog("Enter Supplier ID:");
        String productId = JOptionPane.showInputDialog("Enter Product ID:");
        String location = JOptionPane.showInputDialog("Enter Location:");
        String quantity = JOptionPane.showInputDialog("Enter Quantity:");

        if (!isValidInteger(supplierId) || !isValidInteger(productId) || !isValidInteger(quantity)) {
            JOptionPane.showMessageDialog(this, "Supplier ID, Product ID, and Quantity must be numbers.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO suppliers (supplier_id, product_id, location, quantity) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(supplierId));
                stmt.setInt(2, Integer.parseInt(productId));
                stmt.setString(3, location);
                stmt.setInt(4, Integer.parseInt(quantity));

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Record Created Successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating record: " + e.getMessage());
        }
    }

    private void viewRecord() {
        String supplierId = JOptionPane.showInputDialog("Enter Supplier ID to View:");

        if (!isValidInteger(supplierId)) {
            JOptionPane.showMessageDialog(this, "Supplier ID must be a number.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(supplierId));
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String supplierData = "Supplier ID: " + rs.getInt("supplier_id") + "\n"
                            + "Product ID: " + rs.getInt("product_id") + "\n"
                            + "Location: " + rs.getString("location") + "\n"
                            + "Quantity: " + rs.getInt("quantity");

                    JOptionPane.showMessageDialog(this, supplierData);
                } else {
                    JOptionPane.showMessageDialog(this, "No record found with ID: " + supplierId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving record: " + e.getMessage());
        }
    }

    private void updateRecord() {
        String supplierId = JOptionPane.showInputDialog("Enter Supplier ID to Update:");
        String productId = JOptionPane.showInputDialog("Enter Product ID to Update:");
        String quantity = JOptionPane.showInputDialog("Enter New Quantity:");

        if (!isValidInteger(supplierId) || !isValidInteger(productId) || !isValidInteger(quantity)) {
            JOptionPane.showMessageDialog(this, "Supplier ID, Product ID, and Quantity must be numbers.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE suppliers SET quantity = ? WHERE supplier_id = ? AND product_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(quantity));
                stmt.setInt(2, Integer.parseInt(supplierId));
                stmt.setInt(3, Integer.parseInt(productId));

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Record Updated Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "No record found with the given IDs.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating record: " + e.getMessage());
        }
    }

    private void deleteRecord() {
        String supplierId = JOptionPane.showInputDialog("Enter Supplier ID to Delete:");
        String productId = JOptionPane.showInputDialog("Enter Product ID to Delete:");

        if (!isValidInteger(supplierId) || !isValidInteger(productId)) {
            JOptionPane.showMessageDialog(this, "Supplier ID and Product ID must be numbers.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM suppliers WHERE supplier_id = ? AND product_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(supplierId));
                stmt.setInt(2, Integer.parseInt(productId));

                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Record Deleted Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "No record found with the given IDs.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage());
        }
    }

    // Helper method to check if a string is a valid integer
    private boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

