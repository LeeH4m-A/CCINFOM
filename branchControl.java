import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class branchControl {

    private Connection connection;
    private JComboBox<Integer> branchComboBox;
    private JTextField branchNameField, locationField;
    private JFrame mainFrame;
    private JPanel mainPanel, readDataPanel, updateDataPanel, deleteDataPanel;
    private CardLayout cardLayout;

    public branchControl() {
        createConnection();
        initializeMainFrame();
        showMainPanel();
    }

    // Establish database connection
    public void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the driver
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attempt", "root", "root");
            System.out.println("Database Connection Success");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database Connection Failed!");
            e.printStackTrace();
        }
    }

    // Initialize the main frame with CardLayout
    private void initializeMainFrame() {
        mainFrame = new JFrame("Branch Control");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        mainFrame.setLayout(cardLayout);

        // Panels for each screen
        mainPanel = new JPanel(new FlowLayout());
        readDataPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        updateDataPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        deleteDataPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        mainFrame.add(mainPanel, "Main");
        mainFrame.add(readDataPanel, "ReadData");
        mainFrame.add(updateDataPanel, "UpdateData");

        mainFrame.setSize(400, 300);
        mainFrame.setVisible(true);
    }

    // Show the main panel
    private void showMainPanel() {
        mainPanel.removeAll();

        JButton goToReadDataButton = new JButton("Go to Read Data");
        goToReadDataButton.addActionListener(e -> showReadDataPanel());

        JButton goToUpdateDataButton = new JButton("Go to Update Data");
        goToUpdateDataButton.addActionListener(e -> showUpdateDataPanel());

        mainPanel.add(goToReadDataButton);
        mainPanel.add(goToUpdateDataButton);

        cardLayout.show(mainFrame.getContentPane(), "Main");
    }

    // Show the Read Data panel
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

        cardLayout.show(mainFrame.getContentPane(), "ReadData");
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
                JOptionPane.showMessageDialog(mainFrame, "Please select a Branch ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String branchName = branchNameField.getText().trim();
            String location = locationField.getText().trim();

            if (branchName.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Branch Name and Location cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
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

        cardLayout.show(mainFrame.getContentPane(), "UpdateData");
    }


    private void showDeleteDataPanel(){

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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(branchControl::new);
    }
}
