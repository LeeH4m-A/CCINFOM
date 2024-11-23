import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class branchControl {

    private Connection connection;
    private JComboBox<Integer> branchComboBox;
    private JTextField branchNameField, locationField;
    private JPanel mainPanel, createDataPanel, readDataPanel, updateDataPanel, deleteDataPanel, containerPanel;
    private CardLayout cardLayout;

    public branchControl() {
        createConnection();
        initializePanels();
        showMainPanel();
    }

    // Establish database connection
    private void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the driver
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attempt", "root", "root");
            System.out.println("Database Connection Success");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found!");
        } catch (SQLException e) {
            System.err.println("Database Connection Failed!");
        }
    }

    // Initialize all panels with CardLayout
    private void initializePanels() {
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout); // This will act as the "giant card" container

        mainPanel = new JPanel(new FlowLayout());
        createDataPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        readDataPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        updateDataPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        deleteDataPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        containerPanel.add(mainPanel, "Main");
        containerPanel.add(createDataPanel, "CreateData");
        containerPanel.add(readDataPanel, "ReadData");
        containerPanel.add(updateDataPanel, "UpdateData");
        containerPanel.add(deleteDataPanel, "DeleteData");
    }

    // Show the main panel
    private void showMainPanel() {
        mainPanel.removeAll();

        JButton goToCreateDataButton = new JButton("Go to Create Data");
        goToCreateDataButton.addActionListener(e -> showCreateDataPanel());

        JButton goToReadDataButton = new JButton("Go to Read Data");
        goToReadDataButton.addActionListener(e -> showReadDataPanel());

        JButton goToUpdateDataButton = new JButton("Go to Update Data");
        goToUpdateDataButton.addActionListener(e -> showUpdateDataPanel());

        JButton goToDeleteDataButton = new JButton("Go to Delete Data");
        goToDeleteDataButton.addActionListener(e -> showDeleteDataPanel());

        mainPanel.add(goToCreateDataButton);
        mainPanel.add(goToReadDataButton);
        mainPanel.add(goToUpdateDataButton);
        mainPanel.add(goToDeleteDataButton);

        cardLayout.show(containerPanel, "Main");
    }

    // Show the Create Data panel
    private void showCreateDataPanel() {
        createDataPanel.removeAll();

        JLabel branchNameLabel = new JLabel("  Branch Name:");
        JTextField branchNameField = new JTextField();

        JLabel locationLabel = new JLabel("  Location:");
        JTextField locationField = new JTextField();

        JButton createButton = new JButton("Create");
        JButton backButton = new JButton("Back");

        createButton.addActionListener(e -> {
            String branchName = branchNameField.getText().trim();
            String location = locationField.getText().trim();

            if (branchName.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(containerPanel, "Branch Name and Location cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int newBranchId = generateNewBranchId();
            if (newBranchId > 0) {
                createBranchData(newBranchId, branchName, location);
            } else {
                JOptionPane.showMessageDialog(containerPanel, "Failed to generate a new Branch ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> showMainPanel());

        createDataPanel.add(branchNameLabel);
        createDataPanel.add(branchNameField);
        createDataPanel.add(locationLabel);
        createDataPanel.add(locationField);
        createDataPanel.add(createButton);
        createDataPanel.add(backButton);

        cardLayout.show(containerPanel, "CreateData");
    }

    // Implement showReadDataPanel(), showUpdateDataPanel(), showDeleteDataPanel() similarly

    private void showReadDataPanel() {
        readDataPanel.removeAll();

        JLabel branchIdLabel = new JLabel("  Branch ID:");
        branchComboBox = new JComboBox<>();
        branchComboBox.addItem(null);
        JLabel branchNameLabel = new JLabel("  Branch Name:");
        branchNameField = new JTextField();
        branchNameField.setEditable(false);
        JLabel locationLabel = new JLabel("  Location:");
        locationField = new JTextField();
        locationField.setEditable(false);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showMainPanel());

        readDataPanel.add(branchIdLabel);
        readDataPanel.add(branchComboBox);
        readDataPanel.add(branchNameLabel);
        readDataPanel.add(branchNameField);
        readDataPanel.add(locationLabel);
        readDataPanel.add(locationField);
        readDataPanel.add(new JLabel()); // Placeholder for alignment
        readDataPanel.add(backButton);

        populateBranchComboBox();

        branchComboBox.addActionListener(e -> {
            Integer selectedBranchId = (Integer) branchComboBox.getSelectedItem();
            if (selectedBranchId == null) {
                branchNameField.setText("");
                locationField.setText("");
            } else {
                readData(selectedBranchId);
            }
        });

        cardLayout.show(containerPanel, "ReadData");
    }

    // Show the Update Data panel
    private void showUpdateDataPanel() {
        updateDataPanel.removeAll();

        JLabel branchIdLabel = new JLabel("  Branch ID:");
        branchComboBox = new JComboBox<>();
        branchComboBox.addItem(null);
        JLabel branchNameLabel = new JLabel("  Branch Name:");
        branchNameField = new JTextField();
        branchNameField.setEditable(true);
        JLabel locationLabel = new JLabel("  Location:");
        locationField = new JTextField();
        locationField.setEditable(true);

        JButton updateButton = new JButton("Update");
        JButton backButton = new JButton("Back");

        updateButton.addActionListener(e -> {
            Integer selectedBranchId = (Integer) branchComboBox.getSelectedItem();
            if (selectedBranchId == null) {
                JOptionPane.showMessageDialog(containerPanel, "Please select a Branch ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String branchName = branchNameField.getText().trim();
            String location = locationField.getText().trim();

            if (branchName.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(containerPanel, "Branch Name and Location cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            updateBranchData(selectedBranchId, branchName, location);
        });

        backButton.addActionListener(e -> showMainPanel());

        updateDataPanel.add(branchIdLabel);
        updateDataPanel.add(branchComboBox);
        updateDataPanel.add(branchNameLabel);
        updateDataPanel.add(branchNameField);
        updateDataPanel.add(locationLabel);
        updateDataPanel.add(locationField);
        updateDataPanel.add(updateButton);
        updateDataPanel.add(backButton);

        populateBranchComboBox();

        branchComboBox.addActionListener(e -> {
            Integer selectedBranchId = (Integer) branchComboBox.getSelectedItem();
            if (selectedBranchId == null) {
                branchNameField.setText("");
                locationField.setText("");
            } else {
                readData(selectedBranchId);
            }
        });

        cardLayout.show(containerPanel, "UpdateData");
    }


    private void showDeleteDataPanel() {
        deleteDataPanel.removeAll();
    
        JLabel branchIdLabel = new JLabel("  Branch ID:");
        branchComboBox = new JComboBox<>();
        branchComboBox.addItem(null);
        JLabel branchNameLabel = new JLabel("  Branch Name:");
        branchNameField = new JTextField();
        branchNameField.setEditable(true);
        JLabel locationLabel = new JLabel("  Location:");
        locationField = new JTextField();
        locationField.setEditable(true);
    
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        branchComboBox.addActionListener(e -> {
            Integer selectedBranchId = (Integer) branchComboBox.getSelectedItem();
            if (selectedBranchId == null) {
                branchNameField.setText("");
                locationField.setText("");
            } else {
                readData(selectedBranchId);
            }
        });
    
        deleteButton.addActionListener(e -> {
            Integer selectedBranchId = (Integer) branchComboBox.getSelectedItem();
            if (selectedBranchId == null) {
                JOptionPane.showMessageDialog(containerPanel, "Please select a Branch ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(containerPanel, "Are you sure you want to delete this branch?", 
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteData(selectedBranchId);
                branchComboBox.setSelectedItem(null);
            }
        });
    
        backButton.addActionListener(e -> showMainPanel());
    
        deleteDataPanel.add(branchIdLabel);
        deleteDataPanel.add(branchComboBox);
        deleteDataPanel.add(branchNameLabel);
        deleteDataPanel.add(branchNameField);
        deleteDataPanel.add(locationLabel);
        deleteDataPanel.add(locationField);
        deleteDataPanel.add(deleteButton);
        deleteDataPanel.add(backButton);
    
        populateBranchComboBox();
    
        cardLayout.show(containerPanel, "DeleteData");
    }

    // Generate new Branch ID
    private int generateNewBranchId() {
        if (connection == null) {
            System.err.println("No connection available. Please establish a connection first.");
            return -1;
        }
    
        String query = "SELECT MAX(branch_id) AS max_id FROM branches";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            if (rs.next()) {
                return rs.getInt("max_id") + 1; // Increment the highest ID by 1
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching the maximum Branch ID!");
        }
    
        return -1; // Return -1 if an error occurs
    }

    // Populate branchComboBox with branch IDs
    private void createBranchData(int branchId, String branchName, String location) {
        if (connection == null) {
            System.err.println("No connection available. Please establish a connection first.");
            return;
        }

        String query = "INSERT INTO branches (branch_id, branch_name, location) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, branchId);
            pstmt.setString(2, branchName);
            pstmt.setString(3, location);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(containerPanel, "New branch created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(containerPanel, "Failed to create new branch.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.err.println("Error while creating new branch data!");
            JOptionPane.showMessageDialog(containerPanel, "An error occurred while creating new branch data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

        // Update branch data in the database
    private void updateBranchData(int branchId, String branchName, String location) {
        if (connection == null) {
            System.err.println("No connection available. Please establish a connection first.");
            return;
        }
    
        String query = "UPDATE branches SET branch_name = ?, location = ? WHERE branch_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, branchName);
            pstmt.setString(2, location);
            pstmt.setInt(3, branchId);
    
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(updateDataPanel, "Branch data updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(updateDataPanel, "Failed to update branch data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.err.println("Error while updating branch data!");
            JOptionPane.showMessageDialog(updateDataPanel, "An error occurred while updating branch data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Populate branch ID combo box
    private void populateBranchComboBox() {
        if (connection == null) {
            System.err.println("No connection available. Please establish a connection first.");
            return;
        }

        String query = "SELECT branch_id FROM branches";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            branchComboBox.removeAllItems(); // Clear previous items
            branchComboBox.addItem(null);    // Add null as the first option

            while (rs.next()) {
                branchComboBox.addItem(rs.getInt("branch_id"));
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching branch IDs!");
        }
    }


    // Fetch and display branch details
    private void readData(int branchId) {
        if (this.connection == null) {
            System.err.println("No connection available. Please establish a connection first.");
            return;
        }

        String query = "SELECT branch_name, location FROM branches WHERE branch_id = ?";
        try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
            pstmt.setInt(1, branchId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    branchNameField.setText(rs.getString("branch_name"));
                    locationField.setText(rs.getString("location"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching branch details!");
        }
    }

    private void deleteData(int branchId) {
        if (connection == null) {
            System.err.println("No connection available. Please establish a connection first.");
            return;
        }
    
        String query = "DELETE FROM branches WHERE branch_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, branchId);
    
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(deleteDataPanel, "Branch deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                populateBranchComboBox(); // Refresh the ComboBox after deletion
            } else {
                JOptionPane.showMessageDialog(deleteDataPanel, "Branch not found. Deletion failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.err.println("Error while deleting branch data!");
            JOptionPane.showMessageDialog(deleteDataPanel, "An error occurred while deleting branch data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Return the container panel
    public JPanel getPanel() {
        return containerPanel;
    }
}
