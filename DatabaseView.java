import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DatabaseView extends JFrame {
    private JPanel cardsPanel;
    private CardLayout cardLayout;

    // menu panels
    private JPanel mainMenuPanel;
    private JPanel recordsMenuPanel;
    private JPanel transactionsMenuPanel;
    private JPanel reportsMenuPanel;

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

        cardsPanel.add(mainMenuPanel, "MainMenu");
        cardsPanel.add(recordsMenuPanel, "RecordsMenu");
        cardsPanel.add(transactionsMenuPanel, "TransactionsMenu");
        cardsPanel.add(reportsMenuPanel, "ReportsMenu");

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
    

    private void initializeRecordsMenu() {
        recordsMenuPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Records Menu", SwingConstants.CENTER);
        JButton backButton = new JButton("Back to Main Menu");
        recordsMenuPanel.add(label, BorderLayout.CENTER);
        recordsMenuPanel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> showCard("MainMenu"));
    }

    private void initializeTransactionsMenu() {
        transactionsMenuPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Transactions Menu", SwingConstants.CENTER);
        JButton backButton = new JButton("Back to Main Menu");
        transactionsMenuPanel.add(label, BorderLayout.CENTER);
        transactionsMenuPanel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> showCard("MainMenu"));
    }

    private void initializeReportsMenu() {
        reportsMenuPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Reports Menu", SwingConstants.CENTER);
        JButton backButton = new JButton("Back to Main Menu");
        reportsMenuPanel.add(label, BorderLayout.CENTER);
        reportsMenuPanel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> showCard("MainMenu"));
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
}
