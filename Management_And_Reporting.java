import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.SimpleDateFormat;




public class Management_And_Reporting  {

    public static void main(String[] args) throws IOException, SQLException {

      Management_And_Reporting obj = new Management_And_Reporting();


/*
     ResultSet result = obj.averageAttractionAttendConcec();
      obj.printResultSetAverage(result);
      /
 */
/*
        ResultSet result = obj.animallPopulationReport();
        printAnimalPopulationReport(result);

 */
        /*
        ResultSet result = obj.top3Attractions();
        printTop3Attractions(result);

         /*

         /*
        ResultSet result = obj.bestDaysInMonth();
        printBestDaysInMonth(result);

         */

        ResultSet result = obj.averageAttractionAttendConcec();
        printAverageAttractionAttendConcec(result);

    }

    public ResultSet revenueReportBySource() throws IOException, SQLException {

        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");


            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter date (yyyy-MM-dd): ");
            String dateString = br.readLine();

            String query = "SELECT NAME, TYPE, REVENUE, DATETIME " +
                    "FROM REVENUE_EVENTS RE, REVENUETYPES RT " +
                    "WHERE RE.RTID = RT.ID AND RE.DATETIME = TO_DATE('" + dateString + "', 'yyyy-MM-dd') " +
                    "ORDER BY TO_CHAR(RE.DATETIME, 'yyyy-MM-dd')";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            printRevReport(resultSet);

        return resultSet;
    }

    public static void printRevReport(ResultSet resultSet) throws SQLException {

        System.out.printf("%-20s %-20s %-20s %-20s%n", "NAME", "TYPE", "REVENUE", "DATETIME");

        // Print each row
        while (resultSet.next()) {
            String name = resultSet.getString("NAME");
            String type = resultSet.getString("TYPE");
            double revenue = resultSet.getDouble("REVENUE");
            Date datetime = resultSet.getDate("DATETIME");

            System.out.printf("%-20s %-20s %-20s %-20s%n", name, type, revenue, datetime);
        }
    }

    public ResultSet animallPopulationReport() throws IOException, SQLException {

        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


        System.out.print("Enter Species ID: ");
        int speciesId = Integer.parseInt(br.readLine());

        String query = "SELECT S.ID, S.NAME, A.Status " +
                "    SUM(S.FOODCOST) AS TotalFoodCost, " +
                "    SUM(CASE WHEN E.JOBTYPE = 'VET' THEN E.HourID * 40 ELSE 0 END) AS VetCost, " +
                "    SUM(CASE WHEN E.JOBTYPE = 'AnimalCare Spec' THEN E.HourID * 40 ELSE 0 END) AS AnimalCareCost " +
                "FROM ANIMAL A " +
                "JOIN SPECIES S ON A.SID = S.ID " +
                "JOIN CARES_FOR C ON S.ID = C.SID " +
                "JOIN EMPLOYEE E ON E.ID = C.ID " +
                "WHERE S.ID = ? " +
                "GROUP BY S.ID, S.NAME, S.Status; ";

        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setInt(1, speciesId); // Bind the parameter
        ResultSet resultSet = preparedStatement.executeQuery();



        return resultSet;
    }
    public ResultSet top3Attractions () throws IOException, SQLException {

        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String query = "SELECT R.ID, R.NAME, R.TYPE, SUM(RT.REVENUE) AS TOTAL_REVENUE, SUM(RT.Tickets_Sold) AS TOTAL_TICKETS_SOLD " +
                "FROM REVENUE_EVENTS RT " +
                "JOIN REVENUETYPES R ON RT.RTID = R.ID " +
                "WHERE RT.DATETIME BETWEEN TO_DATE('2000-12-03', 'yyyy-mm-dd') AND TO_DATE('2002-12-31', 'yyyy-mm-dd') AND R.Type = 'Attraction' " +
                "GROUP BY R.ID, R.NAME, R.TYPE " +
                "ORDER BY TOTAL_REVENUE DESC " +
                "FETCH FIRST 3 ROWS ONLY";

        PreparedStatement preparedStatement = con.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet;
    }

    public ResultSet bestDaysInMonth() throws IOException, SQLException {

        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter start date (yyyy-MM-dd): ");
        String startDate = br.readLine();
        System.out.print("Enter end date (yyyy-MM-dd): ");
        String endDate = br.readLine();

        String query = "SELECT TRUNC(DATETIME) AS DAY, SUM(REVENUE) AS TOTAL_REVENUE " +
                "FROM REVENUE_EVENTS " +
                "WHERE TRUNC(DATETIME) BETWEEN TO_DATE('"+startDate+"', 'yyyy-mm-dd') AND TO_DATE('"+endDate+"', 'yyyy-mm-dd') " +
                "GROUP BY TRUNC(DATETIME) " +
                "ORDER BY TOTAL_REVENUE DESC " +
                "FETCH FIRST 5 ROWS ONLY";

        PreparedStatement preparedStatement = con.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet;
    }

    public ResultSet averageAttractionAttendConcec() throws IOException, SQLException {

        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter start date (yyyy-MM-dd): ");
        String startDate = br.readLine();
        System.out.print("Enter end date (yyyy-MM-dd): ");
        String endDate = br.readLine();

        String query = "SELECT RT.TYPE, COUNT(TYPE) AS COUNT, ROUND(AVG(RE.REVENUE), 2) AS \"AVERAGE_REVENUE\" " +
                "FROM REVENUE_EVENTS RE " +
                "JOIN REVENUETYPES RT ON RE.RTID = RT.ID " +
                "WHERE RE.DATETIME BETWEEN TO_DATE('"+startDate+"', 'yyyy-mm-dd') AND TO_DATE('"+endDate+"', 'yyyy-mm-dd') " +
                "AND RT.TYPE IS NOT NULL " +
                "GROUP BY RT.TYPE";


        PreparedStatement preparedStatement = con.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet;
    }

    public void printResultSetAverage(ResultSet resultSet) throws SQLException {
        try {
            while (resultSet.next()) {
                // Assuming you have columns named TYPE, COUNT, and AVERAGE_REVENUE in your result set
                String type = resultSet.getString("TYPE");
                int count = resultSet.getInt("COUNT");
                double averageRevenue = resultSet.getDouble("AVERAGE_REVENUE");

                System.out.println("Type: " + type);
                System.out.println("Count: " + count);
                System.out.println("Average Revenue: " + averageRevenue);
                System.out.println("--------------------------");
            }
        } finally {
            if (resultSet != null) resultSet.close();
        }
    }

    public static void printAnimalPopulationReport(ResultSet resultSet) throws SQLException {
        System.out.printf("%-5s %-20s %-10s %-15s\n", "ID", "Name", "Status", "TotalFoodCost");

        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");
            String status = resultSet.getString("Status");
            double totalFoodCost = resultSet.getDouble("TotalFoodCost");

            System.out.printf("%-5d %-20s %-10s %-15.2f\n", id, name, status, totalFoodCost);
        }
    }

    public static void printTop3Attractions(ResultSet resultSet) throws SQLException {
        System.out.println("Top Three Attractions In the Month of December");
        System.out.printf("%-5s %-20s %-15s %-15s %-20s\n", "ID", "Name", "Type", "TotalRevenue", "TotalTicketsSold");

        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");
            String type = resultSet.getString("TYPE");
            double totalRevenue = resultSet.getDouble("TOTAL_REVENUE");
            int totalTicketsSold = resultSet.getInt("TOTAL_TICKETS_SOLD");

            System.out.printf("%-5d %-20s %-15s %-15.2f %-20d\n", id, name, type, totalRevenue, totalTicketsSold);
        }
    }


    public static void printBestDaysInMonth(ResultSet resultSet) throws SQLException {
        System.out.printf("%-15s %-15s\n", "Day", "TotalRevenue");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        while (resultSet.next()) {
            java.sql.Date sqlDate = resultSet.getDate("DAY");
            String day = dateFormat.format(sqlDate);
            double totalRevenue = resultSet.getDouble("TOTAL_REVENUE");

            System.out.printf("%-15s %-15.2f\n", day, totalRevenue);
        }
    }

    public static void printAverageAttractionAttendConcec(ResultSet resultSet) throws SQLException {
        System.out.printf("%-15s %-10s %-15s\n", "Type", "Count", "AverageRevenue");

        while (resultSet.next()) {
            String type = resultSet.getString("TYPE");
            int count = resultSet.getInt("COUNT");
            double averageRevenue = resultSet.getDouble("AVERAGE_REVENUE");

            System.out.printf("%-15s %-10d %-15.2f\n", type, count, averageRevenue);
        }
    }










}
