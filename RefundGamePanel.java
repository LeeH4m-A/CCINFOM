import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RefundGamePanel extends JPanel {
    private JTextField receiptIdField, customerIdField, productIdField;
    private JButton refundButton;
    private JLabel messageLabel;
    
    public RefundGamePanel() {
        setLayout(new GridLayout(5, 2, 10, 10));

        // Initialize fields and labels
        JLabel receiptIdLabel = new JLabel("Receipt ID:");
        receiptIdField = new JTextField();
        
        JLabel customerIdLabel = new JLabel("Customer ID:");
        customerIdField = new JTextField();
        
        JLabel productIdLabel = new JLabel("Product ID:");
        productIdField = new JTextField();

        refundButton = new JButton("Refund Game");
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);

        // Add components to the panel
        add(receiptIdLabel);
        add(receiptIdField);
        add(customerIdLabel);
        add(customerIdField);
        add(productIdLabel);
        add(productIdField);
        add(refundButton);
        add(messageLabel);

        // Set button action
        refundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRefund();
            }
        });
    }

    private void handleRefund() {
        String receiptId = receiptIdField.getText();
        String customerId = customerIdField.getText();
        String productId = productIdField.getText();

        // Validate inputs
        if (receiptId.isEmpty() || customerId.isEmpty() || productId.isEmpty()) {
            messageLabel.setText("All fields must be filled!");
            return;
        }

        // Database connection details
        String url = "jdbc:mysql://localhost:3306/attempt";
        String user = "root"; // Update with your database user
        String password = "root"; // Update with your database password
        
        // SQL queries
        String selectReceiptQuery = "SELECT * FROM receipts WHERE receipt_id = ? AND customer_id = ? AND product_id = ?";
        String deleteReceiptQuery = "DELETE FROM receipts WHERE receipt_id = ?";
        String updateSupplierQuery = "UPDATE suppliers SET quantity = quantity + 1 WHERE supplier_id = (SELECT branch_id FROM receipts WHERE receipt_id = ?) AND product_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Check if the receipt exists and matches customer and product
            try (PreparedStatement ps = conn.prepareStatement(selectReceiptQuery)) {
                ps.setString(1, receiptId);
                ps.setString(2, customerId);
                ps.setString(3, productId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // If the receipt exists, proceed with the refund operation
                        // First, delete the receipt
                        try (PreparedStatement deletePs = conn.prepareStatement(deleteReceiptQuery)) {
                            deletePs.setString(1, receiptId);
                            deletePs.executeUpdate();
                        }

                        // Update the supplier's quantity for the product
                        try (PreparedStatement updatePs = conn.prepareStatement(updateSupplierQuery)) {
                            updatePs.setString(1, receiptId); // Pass the receipt ID for branch_id reference
                            updatePs.setString(2, productId);  // Pass the product ID to update the supplier's quantity
                            updatePs.executeUpdate();
                        }

                        messageLabel.setText("Refund successful!");
                        messageLabel.setForeground(Color.GREEN);
                    } else {
                        messageLabel.setText("No matching record found.");
                        messageLabel.setForeground(Color.RED);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            messageLabel.setText("Error connecting to database.");
            messageLabel.setForeground(Color.RED);
        }
    }
}
