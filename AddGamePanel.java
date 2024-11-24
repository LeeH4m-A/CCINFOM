import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddGamePanel extends JPanel {
    private JTextField gameIdField, gameTitleField, branchIdField;
    private JButton addGameButton;
    private JLabel messageLabel;

    public AddGamePanel() {
        setLayout(new GridLayout(5, 2, 10, 10));

        // Initialize fields and labels
        JLabel gameIdLabel = new JLabel("Game ID:");
        gameIdField = new JTextField();

        JLabel gameTitleLabel = new JLabel("Game Title:");
        gameTitleField = new JTextField();

        JLabel branchIdLabel = new JLabel("Branch ID:");
        branchIdField = new JTextField();

        addGameButton = new JButton("Add Game to Supply");
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);

        // Add components to the panel
        add(gameIdLabel);
        add(gameIdField);
        add(gameTitleLabel);
        add(gameTitleField);
        add(branchIdLabel);
        add(branchIdField);
        add(addGameButton);
        add(messageLabel);

        // Set button action
        addGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddGame();
            }
        });
    }

    private void handleAddGame() {
        String gameId = gameIdField.getText();
        String gameTitle = gameTitleField.getText();
        String branchId = branchIdField.getText();

        // Validate inputs
        if (gameId.isEmpty() || gameTitle.isEmpty() || branchId.isEmpty()) {
            messageLabel.setText("All fields must be filled!");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/your_database";
        String user = "root";
        String password = "password";

        String selectGameQuery = "SELECT * FROM supply WHERE product_id = ? AND branch_id = ?";
        String updateGameQuery = "UPDATE supply SET quantity = quantity + 1 WHERE product_id = ? AND branch_id = ?";
        String insertGameQuery = "INSERT INTO supply (product_id, branch_id, product_name, quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
        
            try (PreparedStatement ps = conn.prepareStatement(selectGameQuery)) {
                ps.setString(1, gameId);
                ps.setString(2, branchId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        
                        try (PreparedStatement updatePs = conn.prepareStatement(updateGameQuery)) {
                            updatePs.setString(1, gameId);
                            updatePs.setString(2, branchId);
                            updatePs.executeUpdate();
                            messageLabel.setText("Game quantity updated successfully!");
                            messageLabel.setForeground(Color.GREEN);
                        }
                    } else {
                        
                        try (PreparedStatement insertPs = conn.prepareStatement(insertGameQuery)) {
                            insertPs.setString(1, gameId);
                            insertPs.setString(2, branchId);
                            insertPs.setString(3, gameTitle);
                            insertPs.setInt(4, 1);
                            insertPs.executeUpdate();
                            messageLabel.setText("Game added successfully!");
                            messageLabel.setForeground(Color.GREEN);
                        }
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

