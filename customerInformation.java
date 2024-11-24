import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class customerInformation extends JPanel {
    private static Connection connection;

    public customerInformation() {
        createConnection();
        showSearchPanel();
    }

    private void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attempt", "root", "root");
            System.out.println("Database Connection Success");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found!");
        } catch (SQLException e) {
            System.err.println("Database Connection Failed!");
        }
    }

    private void showSearchPanel() {
        JPanel searchPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();

        searchPanel.add(new JLabel("First Name:"));
        searchPanel.add(firstNameField);
        searchPanel.add(new JLabel("Last Name:"));
        searchPanel.add(lastNameField);

        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");

        searchButton.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both first name and last name.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                searchPerson(firstName, lastName);
            }
        });

        backButton.addActionListener(e -> removeAllAndReinitialize());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(searchButton);

        removeAll();
        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void removeAllAndReinitialize() {
        removeAll();
        showSearchPanel();
        revalidate();
        repaint();
    }

    private void searchPerson(String firstName, String lastName) {
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Database connection not available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "SELECT * FROM customers WHERE LOWER(first_name) = LOWER(?) AND LOWER(last_name) = LOWER(?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String foundFirstName = rs.getString("first_name");
                String foundLastName = rs.getString("last_name");
                String foundAge = rs.getString("age");
                String foundAddress = rs.getString("address");

                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Customer Found:\nName: " + foundFirstName + " " + foundLastName +
                    "\nAge: " + foundAge + "\nAddress: " + foundAddress +
                    "\n\nWould you like to modify the details?",
                    "Customer Found",
                    JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {
                    updatePerson(firstName, lastName);
                }
            } else {
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Customer not found. Would you like to create a new customer?",
                    "Customer Not Found",
                    JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {
                    createPerson(firstName, lastName);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePerson(String firstName, String lastName) {
        String newAge = JOptionPane.showInputDialog(this, "Enter new Age for " + firstName + " " + lastName);
        String newAddress = JOptionPane.showInputDialog(this, "Enter new address for " + firstName + " " + lastName);

        String updateQuery = "UPDATE customers SET age = ?, address = ? WHERE LOWER(first_name) = LOWER(?) AND LOWER(last_name) = LOWER(?)";

        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setString(1, newAge);
            stmt.setString(2, newAddress);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Customer details updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No changes made.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createPerson(String firstName, String lastName) {
        String age = JOptionPane.showInputDialog(this, "Enter Age:");
        String address = JOptionPane.showInputDialog(this, "Enter Address:");

        String insertQuery = "INSERT INTO customers (customer_id, first_name, last_name, age, address) VALUES (?, ?, ?, ?, ?)";
        int customerId = generateNewCustomerId();

        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setInt(1, customerId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, age);
            stmt.setString(5, address);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Customer created successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static int generateNewCustomerId() {
        if (connection == null) {
            return -1;
        }

        String query = "SELECT MAX(customer_id) AS max_id FROM customers";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            System.err.println("Error generating new customer ID: " + e.getMessage());
        }

        return -1;
    }
}
