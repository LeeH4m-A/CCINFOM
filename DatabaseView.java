import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class DatabaseView extends JFrame {
    private JPanel cardsPanel;
    private CardLayout cardLayout;

    // menu panels
    private JPanel mainMenuPanel;
    private JPanel recordsMenuPanel;
    private JPanel transactionsMenuPanel;
    private JPanel reportsMenuPanel;
    private JPanel customerRecordsMenuPanel;  // <-- Added this panel for customer records
    private JPanel refundGamePanel;  // <-- Added this panel for refund game

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


        cardsPanel.add(mainMenuPanel, "MainMenu");
        cardsPanel.add(recordsMenuPanel, "RecordsMenu");
        cardsPanel.add(transactionsMenuPanel, "TransactionsMenu");
        cardsPanel.add(reportsMenuPanel, "ReportsMenu");
        cardsPanel.add(customerRecordsMenuPanel, "CustomerRecordsMenu");  // <-- Added customer records menu
        cardsPanel.add(refundGamePanel, "RefundGamePanel");  // <-- Added refund game panel

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
    
    private void initializeTransactionsMenu() {
        transactionsMenuPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Transactions Menu", SwingConstants.CENTER);
        JButton backButton = new JButton("Back to Main Menu");
        transactionsMenuPanel.add(label, BorderLayout.CENTER);
        transactionsMenuPanel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> showCard("MainMenu"));

        JButton refundGameButton = new JButton("Video Game Refund");
        refundGameButton.addActionListener(e -> showCard("RefundGamePanel"));
        transactionsMenuPanel.add(refundGameButton, BorderLayout.NORTH);
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
        gbc.gridy = 4;
        centerPanel.add(branchPerformanceButton, gbc);

        JButton consumerEngagementButton = new JButton("Generate Consumer Engagement Report");
        gbc.gridy = 5;
        centerPanel.add(consumerEngagementButton, gbc);

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
