import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DatabaseView extends JFrame {
    private JPanel cardsPanel;
    private CardLayout cardLayout;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/attempt";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    // menu panels
    private JPanel mainMenuPanel;
    private JPanel recordsMenuPanel;
    private JPanel transactionsMenuPanel;
    private JPanel reportsMenuPanel;
    private JPanel customerRecordsMenuPanel;  // <-- Added this panel for customer records
    private JPanel refundGamePanel;  // <-- Added this panel for refund game
    private JPanel purchaseTransactionPanel;  // <-- Added this panel for purchase transaction
    private JPanel modifiedCustomerPanel;
    private JPanel branchReportPanel;

    // main menu buttons
    private JButton recordsButton;
    private JButton transactionsButton;
    private JButton reportsButton;
    private JButton exitButton;

    public DatabaseView() {
        setTitle("Database Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        initializeMainMenu();
        initializeRecordsMenu();
        initializeTransactionsMenu();
        initializeReportsMenu();
        initializeCustomerRecordsMenu();  // <-- Added this method to initialize the customer records menu
        initializeRefundGamePanel();  // <-- Added this method to initialize the refund game panel
        initializePurchaseTransactionPanel();  // <-- Initialize purchase transaction panel
        initializeModifiedCustomerManagementPanel();
		initializeSupplierManagerPanel();

        cardsPanel.add(mainMenuPanel, "MainMenu");
        cardsPanel.add(recordsMenuPanel, "RecordsMenu");
        cardsPanel.add(transactionsMenuPanel, "TransactionsMenu");
        cardsPanel.add(reportsMenuPanel, "ReportsMenu");
        cardsPanel.add(customerRecordsMenuPanel, "CustomerRecordsMenu");  // <-- Added customer records menu
        cardsPanel.add(refundGamePanel, "RefundGamePanel");  // <-- Added refund game panel
        cardsPanel.add(purchaseTransactionPanel, "PurchaseTransactionPanel");  // <-- Add purchase transaction panel to card layout
        cardsPanel.add(modifiedCustomerPanel, "CustomerTransaction");

        add(cardsPanel);

        cardLayout.show(cardsPanel, "MainMenu");
    }

    private void initializeMainMenu() {
        mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        recordsButton = new JButton("Manage Records");
        transactionsButton = new JButton("Manage Transactions");
        reportsButton = new JButton("Generate Reports");
        exitButton = new JButton("Exit");

        Dimension buttonSize = new Dimension(200, 50);
        recordsButton.setPreferredSize(buttonSize);
        transactionsButton.setPreferredSize(buttonSize);
        reportsButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        gbc.gridy = 0;
        mainMenuPanel.add(recordsButton, gbc);
        gbc.gridy = 1;
        mainMenuPanel.add(transactionsButton, gbc);
        gbc.gridy = 2;
        mainMenuPanel.add(reportsButton, gbc);
        gbc.gridy = 3;
        mainMenuPanel.add(exitButton, gbc);
    }

    private void initializePurchaseTransactionPanel() {
        purchaseTransactionPanel = new PurchaseTransactionPanel();  // <-- Create the panel

        // Add the back button to return to the Transactions menu
        JButton backButton = new JButton("Back to Transactions Menu");
        backButton.addActionListener(e -> showCard("TransactionsMenu"));
        purchaseTransactionPanel.add(backButton, BorderLayout.SOUTH);
    }
    
    // records menu
    private void initializeRecordsMenu() {
        recordsMenuPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Select a Record Type to Manage", SwingConstants.CENTER);
    
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> showCard("MainMenu"));
    
        recordsMenuPanel.add(label, BorderLayout.NORTH);
        recordsMenuPanel.add(backButton, BorderLayout.SOUTH);
    
        // Add buttons to select record types
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
    
        JButton customerButton = new JButton("Manage Customer Records");
        customerButton.addActionListener(e -> showCard("CustomerRecordsMenu"));  
        buttonPanel.add(customerButton);
    
        // Branch Control button
        branchControl branches = new branchControl();
        JPanel branchPanel = branches.getPanel();

        // Create a wrapper panel to hold both the existing branchControl content and the back button
        JPanel wrapperPanel = new JPanel(new BorderLayout());

        // Add the branchPanel content to the CENTER region of the wrapper
        wrapperPanel.add(branchPanel, BorderLayout.CENTER);

        // Add the back button in the SOUTH region
        JButton backButton2 = new JButton("Back to Records Menu");
        backButton2.addActionListener(e -> showCard("RecordsMenu"));
        wrapperPanel.add(backButton2, BorderLayout.SOUTH);

        // Add wrapperPanel as a card
        cardsPanel.add(wrapperPanel, "BranchControl");
    
        JButton branchControlButton = new JButton("Manage Branch Records");
        branchControlButton.addActionListener(e -> showCard("BranchControl"));
        buttonPanel.add(branchControlButton);
    
        recordsMenuPanel.add(buttonPanel, BorderLayout.CENTER);

		JButton supplierButton = new JButton("Manage Supplier Records");
		supplierButton.addActionListener(e -> showCard("SupplierManagerPanel"));
		buttonPanel.add(supplierButton);

		recordsMenuPanel.add(buttonPanel, BorderLayout.CENTER);
    }
    

    // customer records menu (this will show the CustomerManagerPanel)
    private void initializeCustomerRecordsMenu() {
        customerRecordsMenuPanel = new JPanel(new BorderLayout());

        CustomerManagerPanel customerManagerPanel = new CustomerManagerPanel();
        customerRecordsMenuPanel.add(customerManagerPanel, BorderLayout.CENTER);

        // back button to return to records menu
        JButton backButton = new JButton("Back to Records Menu");
        backButton.addActionListener(e -> showCard("RecordsMenu"));
        customerRecordsMenuPanel.add(backButton, BorderLayout.SOUTH);
    }

    // refund game panel
    private void initializeRefundGamePanel() {
        refundGamePanel = new RefundGamePanel();

        // back button to return to transactions menu
        JButton backButton = new JButton("Back to Transactions Menu");
        backButton.addActionListener(e -> showCard("TransactionsMenu"));
        refundGamePanel.add(backButton, BorderLayout.SOUTH);
    }

    private void initializeModifiedCustomerManagementPanel(){

        modifiedCustomerPanel = new customerInformation();
        JButton backButton = new JButton("Back to Transactions Menu");
        backButton.addActionListener(e -> showCard("TransactionsMenu"));

        modifiedCustomerPanel.add(backButton, BorderLayout.SOUTH);
    }

	private void initializeSupplierManagerPanel() {
		JPanel supplierManagerPanel = new JPanel(new BorderLayout());
	
		// Assuming SupplierManagerPanel is a similar panel like CustomerManagerPanel
		SupplierManagerPanel supplierPanel = new SupplierManagerPanel();
		supplierManagerPanel.add(supplierPanel, BorderLayout.CENTER);
	
		// Back button to return to the Records Menu
		JButton backButton = new JButton("Back to Records Menu");
		backButton.addActionListener(e -> showCard("RecordsMenu"));
		supplierManagerPanel.add(backButton, BorderLayout.SOUTH);
	
		cardsPanel.add(supplierManagerPanel, "SupplierManagerPanel");
	}

    private void initializeTransactionsMenu() {
        // Create the main panel with a BorderLayout
        transactionsMenuPanel = new JPanel(new BorderLayout());
    
        // Label at the center
        JLabel label = new JLabel("Transactions Menu", SwingConstants.CENTER);
        transactionsMenuPanel.add(label, BorderLayout.NORTH);
    
        // Create a panel for buttons with FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    
        JButton refundGameButton = new JButton("Video Game Refund");
        refundGameButton.addActionListener(e -> showCard("RefundGamePanel"));
        buttonPanel.add(refundGameButton);
    
        JButton modifiedCustomerButton = new JButton("Customer Transaction");
        modifiedCustomerButton.addActionListener(e -> showCard("CustomerTransaction"));
        buttonPanel.add(modifiedCustomerButton);
    
        JButton purchaseTransactionButton = new JButton("Purchase Transaction");
        purchaseTransactionButton.addActionListener(e -> showCard("PurchaseTransactionPanel"));
        buttonPanel.add(purchaseTransactionButton);

        // Create buttons and add action listeners
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> showCard("MainMenu"));
        buttonPanel.add(backButton);
    
        // Add button panel to the main panel at the center
        transactionsMenuPanel.add(buttonPanel, BorderLayout.CENTER);
    }
	
    private DefaultTableModel fetchBranchSales(int month, int year) {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Branch ID");
        tableModel.addColumn("Branch Name");
        tableModel.addColumn("Location");
        tableModel.addColumn("Sales");

        String query = """
            SELECT branches.branch_id, branches.branch_name, branches.location, 
                CASE
                    WHEN (ROUND(SUM(price), 2)) IS NULL THEN 0
                    ELSE (ROUND(SUM(price), 2))
                END AS branch_sales
            FROM branches
            LEFT JOIN receipts
                ON branches.branch_id = receipts.branch_id
            LEFT JOIN products
                ON receipts.product_id_purchased = products.product_id
            WHERE YEAR(receipts.date_of_purchase) = ?
                AND MONTH(receipts.date_of_purchase) = ?
            GROUP BY branches.branch_id
            ORDER BY branch_sales DESC;
        """;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Populate table model with results
            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getInt("branch_id"),
                        resultSet.getString("branch_name"),
                        resultSet.getString("location"),
                        resultSet.getDouble("branch_sales")
                };
                tableModel.addRow(row);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error retrieving data: " + e.getMessage());
    }

    return tableModel;
}

private DefaultTableModel fetchCustomerEngagement(int month, int year) {
    DefaultTableModel tableModel = new DefaultTableModel();
    tableModel.addColumn("Customer ID");
    tableModel.addColumn("Last Name");
    tableModel.addColumn("First Name");
    tableModel.addColumn("Branch ID");
    tableModel.addColumn("Total Purchases");
    tableModel.addColumn("Number of Visits");

    String query = """
        SELECT DISTINCT customers.customer_id, customers.last_name, customers.first_name, branches.branch_id,
            (SELECT ROUND(SUM(products.price), 2)
             FROM receipts AS r
             JOIN products AS p
             ON r.product_id_purchased = p.product_id
             WHERE r.branch_id = receipts.branch_id
             GROUP BY branch_id
            ) AS total_purchases,
            COUNT(receipts.branch_id) AS num_visits
        FROM customers
        JOIN receipts 
        ON customers.customer_id = receipts.customer_id
        JOIN branches
        ON receipts.branch_id = branches.branch_id
        JOIN products
        ON receipts.product_id_purchased = products.product_id
        WHERE YEAR(receipts.date_of_purchase) = ?
            AND MONTH(receipts.date_of_purchase) = ?
        GROUP BY customers.customer_id, customers.last_name, customers.first_name, branches.branch_id
        ORDER BY customers.customer_id ASC;
    """;

    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        // Set parameters
        preparedStatement.setInt(1, year);
        preparedStatement.setInt(2, month);

        ResultSet resultSet = preparedStatement.executeQuery();

        // Populate table model with results
        while (resultSet.next()) {
            Object[] row = {
                resultSet.getInt("customer_id"),
                resultSet.getString("last_name"),
                resultSet.getString("first_name"),
                resultSet.getInt("branch_id"),
                resultSet.getDouble("total_purchases"),
                resultSet.getInt("num_visits")
            };
            tableModel.addRow(row);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error retrieving data: " + e.getMessage());
    }

    return tableModel;
}

private DefaultTableModel fetchGamePerformance(int month, int year) {
    DefaultTableModel tableModel = new DefaultTableModel();
    tableModel.addColumn("Game Title");
    tableModel.addColumn("Console");
    tableModel.addColumn("Total Copies Sold");
    tableModel.addColumn("Total Revenue");

    String query = """
        SELECT p.product_name AS game_title, 
               p.console AS console,
               SUM(r.quantity) AS total_copies_sold,
               SUM(p.price * r.quantity) AS total_revenue
        FROM receipts r
        JOIN products p ON r.product_id_purchased = p.product_id
        WHERE YEAR(r.date_of_purchase) = ? AND MONTH(r.date_of_purchase) = ?
        GROUP BY p.product_name, p.console
        ORDER BY total_revenue DESC;
    """;

    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        // Set parameters for month and year
        preparedStatement.setInt(1, year);
        preparedStatement.setInt(2, month);

        ResultSet resultSet = preparedStatement.executeQuery();
        // Populate table model with results
        while (resultSet.next()) {
            Object[] row = {
                resultSet.getString("game_title"),
                resultSet.getString("console"),
                resultSet.getInt("total_copies_sold"),
                resultSet.getDouble("total_revenue")
            };
            tableModel.addRow(row);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error retrieving data: " + e.getMessage());
    }
    return tableModel;
}


    private void initializeReportsMenu() {
        reportsMenuPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Generate Reports", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        reportsMenuPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel monthLabel = new JLabel("Month:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(monthLabel, gbc);

        JComboBox<String> monthComboBox = new JComboBox<>(new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        centerPanel.add(monthComboBox, gbc);

        JLabel yearLabel = new JLabel("Year:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(yearLabel, gbc);

        JTextField yearField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        centerPanel.add(yearField, gbc);

        JButton gamePerformanceButton = new JButton("Generate Game Performance Report");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        centerPanel.add(gamePerformanceButton, gbc);

        JButton consolePerformanceButton = new JButton("Generate Console Performance Report");
        gbc.gridy = 3;
        centerPanel.add(consolePerformanceButton, gbc);

        JButton branchPerformanceButton = new JButton("Generate Branch Performance Report");
        branchPerformanceButton.addActionListener(e -> {
            try {
                // Get selected month and year when button is clicked
                int selectedMonth = monthComboBox.getSelectedIndex() + 1; // Convert to 1-based month
                int selectedYear = Integer.parseInt(yearField.getText().trim()); // Parse year and trim input
        
                // Create a new branchReport panel and pass the selectedMonth and selectedYear
                branchReportPanel = new JPanel(new BorderLayout());
        
                // Table to display results
                JTable resultsTable = new JTable();
                JScrollPane scrollPane = new JScrollPane(resultsTable);
        
                // Add components to the panel
                branchReportPanel.add(new JLabel("Branch Performance Report", SwingConstants.CENTER), BorderLayout.NORTH);
                branchReportPanel.add(scrollPane, BorderLayout.CENTER);
        
                // Fetch and display data
                DefaultTableModel model = fetchBranchSales(selectedMonth, selectedYear);
                resultsTable.setModel(model);
        
                // Back button to return to the Reports menu
                JButton backButtonTransact = new JButton("Back to Transactions Menu");
                backButtonTransact.addActionListener(ev -> showCard("ReportsMenu"));
                branchReportPanel.add(backButtonTransact, BorderLayout.SOUTH);
        
                // Show the report in the "BranchReport" card
                cardsPanel.add(branchReportPanel, "BranchReport");
                showCard("BranchReport");
        
            } catch (NumberFormatException ex) {
                // Handle invalid year input gracefully
                JOptionPane.showMessageDialog(reportsMenuPanel, "Please enter a valid year.");
            }
        });
        gbc.gridy = 4;
        centerPanel.add(branchPerformanceButton, gbc);

        JButton consumerEngagementButton = new JButton("Generate Consumer Engagement Report");
        gbc.gridy = 5;
        centerPanel.add(consumerEngagementButton, gbc);

	JButton consumerEngagementButton = new JButton("Generate Consumer Engagement Report");
	gbc.gridy = 5;
	centerPanel.add(consumerEngagementButton, gbc);

	// Add functionality to the consumerEngagementButton
	consumerEngagementButton.addActionListener(e -> {
  	  try {
	        // Get selected month and year
	        int selectedMonth = monthComboBox.getSelectedIndex() + 1; // Convert to 1-based month
	        int selectedYear = Integer.parseInt(yearField.getText().trim()); // Parse year and trim input
	
	        // Create a new customerEngagementReport panel
	        JPanel customerEngagementReportPanel = new JPanel(new BorderLayout());
	
	        // Table to display results
	        JTable resultsTable = new JTable();
	        JScrollPane scrollPane = new JScrollPane(resultsTable);
	
	        // Add components to the panel
	        customerEngagementReportPanel.add(new JLabel("Customer Engagement Report", SwingConstants.CENTER), BorderLayout.NORTH);
	        customerEngagementReportPanel.add(scrollPane, BorderLayout.CENTER);
	
	        // Fetch and display data
	        DefaultTableModel model = fetchCustomerEngagement(selectedMonth, selectedYear);
	        resultsTable.setModel(model);
	
	        // Back button to return to the Reports menu
	        JButton backButton = new JButton("Back to Reports Menu");
	        backButton.addActionListener(ev -> showCard("ReportsMenu"));
	        customerEngagementReportPanel.add(backButton, BorderLayout.SOUTH);
	
	        // Show the report in the "CustomerEngagementReport" card
	        cardsPanel.add(customerEngagementReportPanel, "CustomerEngagementReport");
	        showCard("CustomerEngagementReport");
	
	    } catch (NumberFormatException ex) {
	        // Handle invalid year input gracefully
	        JOptionPane.showMessageDialog(reportsMenuPanel, "Please enter a valid year.");
	    }
	});

		gamePerformanceButton.addActionListener(e -> {
		try {
			// Get selected month and year from the input fields
			int selectedMonth = monthComboBox.getSelectedIndex() + 1; // Convert to 1-based month
			int selectedYear = Integer.parseInt(yearField.getText().trim()); // Parse year and trim input
	
			// Create a new panel to display the Game Performance Report
			JPanel gamePerformanceReportPanel = new JPanel(new BorderLayout());
	
			// Table to display results
			JTable resultsTable = new JTable();
			JScrollPane scrollPane = new JScrollPane(resultsTable);
	
			// Add components to the panel
			gamePerformanceReportPanel.add(new JLabel("Game Performance Report", SwingConstants.CENTER), BorderLayout.NORTH);
			gamePerformanceReportPanel.add(scrollPane, BorderLayout.CENTER);
	
			// Fetch and display the data in the table
			DefaultTableModel model = fetchGamePerformance(selectedMonth, selectedYear);
			resultsTable.setModel(model);
	
			// Back button to return to the Reports menu
			JButton backButton = new JButton("Back to Reports Menu");
			backButton.addActionListener(ev -> showCard("ReportsMenu")); // Navigate back to the reports menu
			gamePerformanceReportPanel.add(backButton, BorderLayout.SOUTH);
	
			// Show the report in the "GamePerformanceReport" card
			cardsPanel.add(gamePerformanceReportPanel, "GamePerformanceReport");
			showCard("GamePerformanceReport");  // Navigate to the report view
	
		} catch (NumberFormatException ex) {
			// Handle invalid year input gracefully
			JOptionPane.showMessageDialog(reportsMenuPanel, "Please enter a valid year.");
		}
		});
	    

        reportsMenuPanel.add(centerPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.setPreferredSize(new Dimension(50, 30));
        backButton.addActionListener(e -> showCard("MainMenu"));
        reportsMenuPanel.add(backButton, BorderLayout.SOUTH);
    }

    // menu listeners
    public void addMainMenuListeners(ActionListener recordsListener, ActionListener transactionsListener,
                                     ActionListener reportsListener, ActionListener exitListener) {
        recordsButton.addActionListener(recordsListener);
        transactionsButton.addActionListener(transactionsListener);
        reportsButton.addActionListener(reportsListener);
        exitButton.addActionListener(exitListener);
    }

    public void showCard(String cardName) {
        cardLayout.show(cardsPanel, cardName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseView databaseView = new DatabaseView();
            databaseView.setVisible(true);

            // Add listeners for the main menu buttons
            databaseView.addMainMenuListeners(
                e -> databaseView.showCard("RecordsMenu"),
                e -> databaseView.showCard("TransactionsMenu"),
                e -> databaseView.showCard("ReportsMenu"),
                e -> System.exit(0) // Exit application
            );
        });
    }
}
