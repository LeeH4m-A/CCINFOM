import javax.swing.*;
import java.sql.*;
import java.util.Vector;

public class ConsumerEngagementReport {

    private static final String URL = "jdbc:mysql://localhost:3306/attempt"; // Update with your database URL
    private static final String USER = "root"; // Update with your database user
    private static final String PASSWORD = "root"; // Update with your database password

    public static void generate(JPanel parentPanel, int year, int month) {
        // SQL Query
        String sql = """
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
            WHERE YEAR(receipts.date_of_purchase) = ? AND MONTH(receipts.date_of_purchase) = ?
            GROUP BY customers.customer_id, customers.last_name, customers.first_name, branches.branch_id
            ORDER BY customers.customer_id ASC;
        """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, year);
            stmt.setInt(2, month);

            try (ResultSet rs = stmt.executeQuery()) {
                // Convert ResultSet to JTable
                JTable reportTable = buildTableFromResultSet(rs);

                // Display the JTable in a dialog
                JScrollPane scrollPane = new JScrollPane(reportTable);
                JDialog dialog = new JDialog((Frame) null, "Consumer Engagement Report", true);
                dialog.add(scrollPane);
                dialog.setSize(800, 600);
                dialog.setLocationRelativeTo(parentPanel);
                dialog.setVisible(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentPanel, "Error generating report: " + e.getMessage());
        }
    }

    private static JTable buildTableFromResultSet(ResultSet rs) throws SQLException {
        // Extract column names
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }

        // Extract rows
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }

        // Return JTable
        return new JTable(data, columnNames);
    }
}
