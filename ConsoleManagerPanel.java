import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ConsoleManagerPanel extends JPanel {
    private JButton createButton, viewButton, updateButton, deleteButton;

    public ConsoleManagerPanel() {
        setLayout(new GridLayout(4, 1, 10, 10));

        createButton = new JButton("Create Console");
        viewButton = new JButton("View Console");
        updateButton = new JButton("Update Console");
        deleteButton = new JButton("Delete Console");

        createButton.addActionListener(e -> createConsole());
        viewButton.addActionListener(e -> viewConsole());
        updateButton.addActionListener(e -> updateConsole());
        deleteButton.addActionListener(e -> deleteConsole());

        add(createButton);
        add(viewButton);
        add(updateButton);
        add(deleteButton);
    }

    private static final String URL = "jdbc:mysql://localhost:3306/attempt";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private void createConsole() {
        String name = JOptionPane.showInputDialog("Enter Console Name:");
        String price = JOptionPane.showInputDialog("Enter Price:");
        String year = JOptionPane.showInputDialog("Enter Release Year:");
        String developer = JOptionPane.showInputDialog("Enter Developer:");
        String generation = JOptionPane.showInputDialog("Enter Generation:");

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO consoles (console_name, price, release_year, developer, generation) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setDouble(2, Double.parseDouble(price));
                stmt.setInt(3, Integer.parseInt(year));
                stmt.setString(4, developer);
                stmt.setString(5, generation);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Console Created Successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating console: " + e.getMessage());
        }
    }

    private void viewConsole() {
        String name = JOptionPane.showInputDialog("Enter Console Name to View:");

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM consoles WHERE console_name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, name);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String data = "Console Name: " + rs.getString("console_name") + "\n" +
                                  "Price: " + rs.getDouble("price") + "\n" +
                                  "Release Year: " + rs.getInt("release_year") + "\n" +
                                  "Developer: " + rs.getString("developer") + "\n" +
                                  "Generation: " + rs.getString("generation");
                    JOptionPane.showMessageDialog(this, data);
                } else {
                    JOptionPane.showMessageDialog(this, "No Console found with Name: " + name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving console: " + e.getMessage());
        }
    }

    private void updateConsole() {
        String name = JOptionPane.showInputDialog("Enter Console Name to Update:");
        String price = JOptionPane.showInputDialog("Enter New Price:");
        String year = JOptionPane.showInputDialog("Enter New Release Year:");
        String developer = JOptionPane.showInputDialog("Enter New Developer:");
        String generation = JOptionPane.showInputDialog("Enter New Generation:");

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE consoles SET price = ?, release_year = ?, developer = ?, generation = ? WHERE console_name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setDouble(1, Double.parseDouble(price));
                stmt.setInt(2, Integer.parseInt(year));
                stmt.setString(3, developer);
                stmt.setString(4, generation);
                stmt.setString(5, name);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Console Updated Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "No Console found with Name: " + name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating console: " + e.getMessage());
        }
    }

    private void deleteConsole() {
        String name = JOptionPane.showInputDialog("Enter Console Name to Delete:");

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM consoles WHERE console_name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, name);

                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Console Deleted Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "No Console found with Name: " + name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting console: " + e.getMessage());
        }
    }
}
