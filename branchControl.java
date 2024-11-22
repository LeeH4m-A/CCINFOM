import java.sql.*;

public class branchControl {

    private Connection connection;

    // Method to create and return a database connection
    public void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the driver
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attempt3", "root", "root");
            System.out.println("Database Connection Success");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database Connection Failed!");
            e.printStackTrace();
        }
    }

    // Method to read data from the branch table
    public void readData() {
        if (connection == null) {
            System.err.println("No connection available. Please establish a connection first.");
            return;
        }

        String query = "SELECT * FROM branch";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("branch_id");
                String name = rs.getString("name");
                String location = rs.getString("location");

                // Print the data
                System.out.printf("Branch ID: %d, Name: %s, Location: %s%n", id, name, location);
            }
        } catch (SQLException e) {
            System.err.println("Error while reading data!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        branchControl branchControl = new branchControl();

        // Create the connection
        branchControl.createConnection();

        // Read the data
        branchControl.readData();
    }
}
