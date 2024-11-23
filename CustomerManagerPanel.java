import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CustomerManagerPanel extends JPanel {
    private JButton createButton;
    private JButton viewButton;
    private JButton updateButton;
    private JButton deleteButton;

    public CustomerManagerPanel() {
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

    // CRUD Operations

    private void createRecord() {
        String customerId = JOptionPane.showInputDialog("Enter Customer ID:");
        String lastName = JOptionPane.showInputDialog("Enter Last Name:");
        String firstName = JOptionPane.showInputDialog("Enter First Name:");
        String age = JOptionPane.showInputDialog("Enter Age:");
        String address = JOptionPane.showInputDialog("Enter Address:");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database_name", "your_username", "your_password")) {
            String sql = "INSERT INTO customers (customer_id, last_name, first_name, age, address) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(customerId));
                stmt.setString(2, lastName);
                stmt.setString(3, firstName);
                stmt.setInt(4, Integer.parseInt(age));
                stmt.setString(5, address);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Record Created Successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating record: " + e.getMessage());
        }
    }

    private void viewRecord() {
        String customerId = JOptionPane.showInputDialog("Enter Customer ID to View:");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database_name", "your_username", "your_password")) {
            String sql = "SELECT * FROM customers WHERE customer_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(customerId));
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String customerData = "Customer ID: " + rs.getInt("customer_id") + "\n"
                            + "Last Name: " + rs.getString("last_name") + "\n"
                            + "First Name: " + rs.getString("first_name") + "\n"
                            + "Age: " + rs.getInt("age") + "\n"
                            + "Address: " + rs.getString("address");

                    JOptionPane.showMessageDialog(this, customerData);
                } else {
                    JOptionPane.showMessageDialog(this, "No record found with ID: " + customerId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving record: " + e.getMessage());
        }
    }

    private void updateRecord() {
        String customerId = JOptionPane.showInputDialog("Enter Customer ID to Update:");

        String lastName = JOptionPane.showInputDialog("Enter New Last Name:");
        String firstName = JOptionPane.showInputDialog("Enter New First Name:");
        String age = JOptionPane.showInputDialog("Enter New Age:");
        String address = JOptionPane.showInputDialog("Enter New Address:");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database_name", "your_username", "your_password")) {
            String sql = "UPDATE customers SET last_name = ?, first_name = ?, age = ?, address = ? WHERE customer_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, lastName);
                stmt.setString(2, firstName);
                stmt.setInt(3, Integer.parseInt(age));
                stmt.setString(4, address);
                stmt.setInt(5, Integer.parseInt(customerId));

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Record Updated Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "No record found with ID: " + customerId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating record: " + e.getMessage());
        }
    }

    private void deleteRecord() {
        String customerId = JOptionPane.showInputDialog("Enter Customer ID to Delete:");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database_name", "your_username", "your_password")) {
            String sql = "DELETE FROM customers WHERE customer_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(customerId));

                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Record Deleted Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "No record found with ID: " + customerId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage());
        }
    }
}
