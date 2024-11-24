import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;

public class PurchaseTransactionPanel extends JPanel {
    // Database connection details - Update these with your own
    String url = "jdbc:mysql://localhost:3306/your_database";  // Database URL
    String user = "root";  // Database username
    String password = "password";  // Database password

    private JTextField customerIdField;
    private JTextField productIdField;
    private JTextField branchIdField;
    private JButton processButton;

    public PurchaseTransactionPanel() {
        setLayout(new GridLayout(5, 2, 10, 10));

        // Initialize components
        JLabel customerIdLabel = new JLabel("Customer ID:");
        JLabel productIdLabel = new JLabel("Product ID:");
        JLabel branchIdLabel = new JLabel("Branch ID:");

        customerIdField = new JTextField();
        productIdField = new JTextField();
        branchIdField = new JTextField();
        processButton = new JButton("Process Purchase");

        // Add components to the panel
        add(customerIdLabel);
        add(customerIdField);
        add(productIdLabel);
        add(productIdField);
        add(branchIdLabel);
        add(branchIdField);
        add(new JLabel());  // Empty cell for alignment
        add(processButton);

        // Process button action
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int customerId = Integer.parseInt(customerIdField.getText());
                    int productId = Integer.parseInt(productIdField.getText());
                    int branchId = Integer.parseInt(branchIdField.getText());

                    // Handle the purchase transaction
                    handlePurchase(customerId, productId, branchId);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(PurchaseTransactionPanel.this, "Please enter valid numeric values.");
                }
            }
        });
    }

    // Method to connect to the database
    private Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private void handlePurchase(int customerId, int productId, int branchId) {
        try (Connection connection = connectToDatabase()) {
            // Check if the product is available in the selected branch
            String checkAvailabilityQuery = "SELECT quantity FROM suppliers WHERE supplier_id = ? AND product_id = ?";
            try (PreparedStatement availabilityStmt = connection.prepareStatement(checkAvailabilityQuery)) {
                availabilityStmt.setInt(1, branchId);
                availabilityStmt.setInt(2, productId);
                ResultSet rs = availabilityStmt.executeQuery();

                if (rs.next() && rs.getInt("quantity") > 0) {
                    // If the product is available, create a new receipt
                    String maxReceiptQuery = "SELECT MAX(receipt_id) AS max_receipt FROM receipts";
                    try (PreparedStatement maxReceiptStmt = connection.prepareStatement(maxReceiptQuery)) {
                        ResultSet maxReceiptResult = maxReceiptStmt.executeQuery();
                        int newReceiptId = 1;
                        if (maxReceiptResult.next()) {
                            newReceiptId = maxReceiptResult.getInt("max_receipt") + 1;
                        }

                        // Insert a new record into the receipts table
                        String insertReceiptQuery = "INSERT INTO receipts (receipt_id, customer_id, product_id_purchased, date_of_purchase, branch_id) "
                                                     + "VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertReceiptQuery)) {
                            insertStmt.setInt(1, newReceiptId);
                            insertStmt.setInt(2, customerId);
                            insertStmt.setInt(3, productId);
                            insertStmt.setDate(4, Date.valueOf(LocalDate.now()));  // Current date
                            insertStmt.setInt(5, branchId);
                            insertStmt.executeUpdate();
                        }

                        // Reduce the quantity of the product in the suppliers table
                        String updateQuantityQuery = "UPDATE suppliers SET quantity = quantity - 1 "
                                                     + "WHERE supplier_id = ? AND product_id = ?";
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuantityQuery)) {
                            updateStmt.setInt(1, branchId);
                            updateStmt.setInt(2, productId);
                            updateStmt.executeUpdate();
                        }

                        JOptionPane.showMessageDialog(this, "Purchase successful! Receipt ID: " + newReceiptId);
                    }
                } else {
                    // If product is not available, suggest other branches
                    String findOtherBranchesQuery = "SELECT branches.branch_id, branches.branch_name, branches.location "
                                                   + "FROM branches "
                                                   + "JOIN suppliers ON branches.branch_id = suppliers.supplier_id "
                                                   + "WHERE suppliers.product_id = ? AND branches.branch_id != ?";
                    try (PreparedStatement findBranchesStmt = connection.prepareStatement(findOtherBranchesQuery)) {
                        findBranchesStmt.setInt(1, productId);
                        findBranchesStmt.setInt(2, branchId);
                        ResultSet branchesRs = findBranchesStmt.executeQuery();

                        StringBuilder branchesList = new StringBuilder("The product is not available in this branch. Here are other branches where it is available:\n");
                        while (branchesRs.next()) {
                            branchesList.append("Branch ID: ").append(branchesRs.getInt("branch_id"))
                                        .append(", Name: ").append(branchesRs.getString("branch_name"))
                                        .append(", Location: ").append(branchesRs.getString("location"))
                                        .append("\n");
                        }

                        JOptionPane.showMessageDialog(this, branchesList.toString(), "Product Availability", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing purchase: " + e.getMessage());
        }
    }
}
