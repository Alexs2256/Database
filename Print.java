import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Print {
    public static void main(String[] args) throws SQLException {

        Asset_Management obj = new Asset_Management();
























    }

    public static void printResultSet(ResultSet resultSet) throws SQLException {

        Asset_Management obj1 = new Asset_Management();

        // Print column headers
        System.out.printf("%-5s %-10s %-7s %-5s %-5s %-5s\n", "ID", "Status", "BirthYear", "SID", "BID", "EID");

        // Print each row
        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String status = resultSet.getString("Status");
            int birthYear = resultSet.getInt("BirthYear");
            int sid = resultSet.getInt("SID");
            int bid = resultSet.getInt("BID");
            int eid = resultSet.getInt("EID");

            System.out.printf("%-5d %-10s %-7d %-5d %-5d %-5d\n", id, status, birthYear, sid, bid, eid);
        }

    }

    public static void printBuildingView(ResultSet resultSet) {
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Name");
                String type = resultSet.getString("Type");

                System.out.println("Building ID: " + id);
                System.out.println("Building Name: " + name);
                System.out.println("Building Type: " + type);
                System.out.println("--------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the ResultSet, Statement, and Connection
            try {
                if (resultSet != null) resultSet.close();
                // Add similar close statements for other resources if needed
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void printAnimalShowView(ResultSet resultSet) {
        try {
            // Print column headers
            System.out.printf("%-8s %-8s %-15s %-15s %-15s\n", "ID", "PERDAY", "Senior_Price", "Adult_Price", "Child_Price");

            // Print each row
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                double perDay = resultSet.getDouble("PERDAY");
                double seniorPrice = resultSet.getDouble("Senior_Price");
                double adultPrice = resultSet.getDouble("Adult_Price");
                double childPrice = resultSet.getDouble("Child_Price");

                System.out.printf("%-8d %-8.2f %-15.2f %-15.2f %-15.2f\n", id, perDay, seniorPrice, adultPrice, childPrice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the ResultSet, Statement, and Connection
            try {
                if (resultSet != null) resultSet.close();
                // Add similar close statements for other resources if needed
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void printEmployeeViewEMP(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Print column headers
        System.out.printf("%-10s %-10s %-5s %-10s %-20s %-15s %-10s %-10s %-15s %-20s %-10s %-10s\n",
                "ID", "First", "Minit", "Last", "Street", "City", "State", "Zip", "Start_Date", "JobType", "HourID", "SuperID");

        // Print each row
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-10s", resultSet.getString(i));
            }
            System.out.println();
        }
    }
}