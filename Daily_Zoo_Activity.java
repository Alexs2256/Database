
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;


public class Daily_Zoo_Activity {

    public static int CID;
    public static int ZID;
    public static double revenue;
    public static void main(String[] args) throws SQLException {

        Daily_Zoo_Activity obj = new Daily_Zoo_Activity();

        obj.Attraction_Entry();


/*
        obj.insert_Attendee();
        ResultSet result = obj.getAttendanceView(ZID, revenue);
        obj.printAttendanceView(result);


 */

    }

    public void Attraction_Entry() {
        int ID = 0;
        Connection con = null;
        Statement st = null;
        BufferedReader br = null;
        String query = "";
        Asset_Management obj = new Asset_Management();

        while (true) {

            try {
                Class.forName("oracle.jdbc.OracleDriver");
                con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                        "abs83", "Brutekiller231!");

                st = con.createStatement();
                br = new BufferedReader(new InputStreamReader(System.in));

                    System.out.print("Enter ID: ");
                    ID = Integer.parseInt(br.readLine());

                    getTotalAnimalShowRev(ID);

                    if (!obj.exists(con, ID, "Revenue_Events")) {
                        System.out.println("Enter Revenue: ");
                        double revenue = Double.parseDouble(br.readLine());

                        System.out.print("Enter Number of Tickets Sold: ");
                        int number = Integer.parseInt(br.readLine());

                        System.out.print("Enter date (yyyy-MM-dd): ");
                        String dateString = br.readLine();

                        query = "INSERT INTO Revenue_Events(RTID, DATETIME, REVENUE, TICKETS_SOLD) VALUES (" + ID + ", TO_DATE('" + dateString + "', 'yyyy-MM-dd'), " + revenue + ", " + number + ")";
                        st.executeUpdate(query);
                    } else {
                        System.out.println("Do you want to continue?: Yes/No");
                        String answer = br.readLine();

                        if (answer.equalsIgnoreCase("No")) {
                            System.out.println("Insert Successful");
                            con.close();
                            break;
                        } else
                            continue;
                    }

                    System.out.println("Do you want to continue?: Yes/No");
                    String answer = br.readLine();

                    if (answer.equalsIgnoreCase("No")) {
                        System.out.println("Insert Successful");
                        con.close();
                        br.close();
                        return;
                    } else
                        continue;

                } catch (IDAlreadyExistsException e) {
                    System.out.println(ID + " already exists within Concessions table");
                    continue;
                } catch (SQLIntegrityConstraintViolationException e1) {
                    System.out.println("Error, constraint violated");
                    continue;
                } catch (NumberFormatException e2) {
                    System.out.println("Wrong Input");
                    continue;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

        }
    }


    public ResultSet getTotalAnimalShowRev(int ID) throws SQLException {

        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");

        String query = "SELECT ID, " +
                "SUM(CASE WHEN SENIOR_Price > 0 THEN PERDAY ELSE 0 END + " +
                "CASE WHEN ADULT_Price > 0 THEN PERDAY ELSE 0 END + " +
                "CASE WHEN CHILD_Price > 0 THEN PERDAY ELSE 0 END) AS NumberOfTicketsSold, " +
                "SUM(CASE WHEN PERDAY * (SENIOR_PRICE + ADULT_PRICE + CHILD_PRICE) > 0 THEN PERDAY * (SENIOR_PRICE + ADULT_PRICE + CHILD_PRICE) ELSE 0 END) AS TOTAL_REV " +
                "FROM RevenueTypes NATURAL JOIN AnimalShow " +
                "WHERE ID = ? " +
                "GROUP BY ID";

        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setInt(1, ID);  // Set the parameter value
        ResultSet resultSet = preparedStatement.executeQuery();

        // Printing values
        while (resultSet.next()) {
            int animalShowID = resultSet.getInt("ID");
            double numberOfTicketsSold = resultSet.getDouble("NumberOfTicketsSold");
            double totalRevenue = resultSet.getDouble("TOTAL_REV");

            System.out.println("Animal Show ID: " + animalShowID);
            System.out.println("Number of Tickets Sold: " + numberOfTicketsSold);
            System.out.println("Total Revenue: " + totalRevenue);
            System.out.println("--------------------------");
        }

        return resultSet;
    }


    // ATTRACTION ENDS--------------------

    public void insert_Concession() {

        int ID = 0;

        Connection con = null;
        Statement st = null;
        BufferedReader br = null;

        Asset_Management obj = new Asset_Management();

        while (true) {

            try {

                Class.forName("oracle.jdbc.OracleDriver");
                con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                        "abs83", "Brutekiller231!");

                st = con.createStatement();
                br = new BufferedReader(new InputStreamReader(System.in));

                System.out.print("Enter ID: ");
                ID = Integer.parseInt(br.readLine());

                if (obj.exists(con, ID, "Concession"))
                    throw new IDAlreadyExistsException("This ID already exists");

                CID = ID;

                System.out.print("Enter Product: ");
                String product = br.readLine();

                String query = "INSERT INTO Concession(ID, Product) VALUES  (" + ID + ", '" + product + "')";

                System.out.print("Which building was this sold in? Enter a valid BID: ");
                int bid = Integer.parseInt(br.readLine());

                String query2 = "INSERT INTO RevenueTypes(ID, Name, Type, BID) VALUES (" + ID + ", '" + product + "', 'Concession', '" + bid + "')";

                st.executeUpdate(query2);
                st.executeUpdate(query);

                System.out.println("Do you want to add more to Concession?: Yes/No");
                String answer = br.readLine();

                if (answer.equalsIgnoreCase("No")) {
                    System.out.println("Insert Successful");
                    con.close();
                    break;
                } else
                    continue;

            } catch (IDAlreadyExistsException e) {
                System.out.println(ID + " already exists within Concessions table");
                continue;
            } catch (SQLIntegrityConstraintViolationException e1) {
                System.out.println("Error, constraint violated");
                continue;
            } catch (NumberFormatException e2) {
                System.out.println("Wrong Input");
                continue;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public ResultSet getConcessionView(int ID) throws SQLException {

        String selectQuery = "";
        Asset_Management obj = new Asset_Management();

        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Statement st = con.createStatement();
        PreparedStatement preparedStatement = null;


            try {

                if (!obj.exists(con, ID, "Revenue_Events")) {
                    System.out.print("Enter date (yyyy-MM-dd): ");
                    String dateString = br.readLine();

                    System.out.print("Enter Revenue Earned: ");
                    double revenue = Double.parseDouble(br.readLine());

                    String query = "INSERT INTO Revenue_Events(RTID,DATETIME,REVENUE)  VALUES(" + ID + ", TO_DATE('" + dateString + "', 'yyyy-MM-dd'), " + revenue + ")";
                    st.executeUpdate(query);
                }

                String query2 = "SELECT RTID, NAME, REVENUE FROM REVENUE_EVENTS NATURAL JOIN REVENUETYPES";
                st.executeUpdate(query2);

                selectQuery = "SELECT RTID, NAME, DATETIME, REVENUE " +
                        "FROM REVENUE_EVENTS RE, REVENUETYPES RT " +
                        "WHERE RE.RTID = RT.ID AND RT.Type = 'Concession'";


            } catch (SQLIntegrityConstraintViolationException e1) {
                System.out.println("Error, constraint violated");
                e1.printStackTrace();
            } catch (NumberFormatException e2) {
                System.out.println("Wrong Input");
                e2.printStackTrace();
            } catch (SQLException e3) {
                e3.printStackTrace();
            }catch(Exception ex){
                ex.printStackTrace();
            }

            preparedStatement = con.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();


        return resultSet;

    }
    public void printConcessionView(ResultSet resultSet) throws SQLException {
        resultSet = getConcessionView(CID);

        try {
            while (resultSet.next()) {
                int rtID = resultSet.getInt("RTID");
                String name = resultSet.getString("NAME");
                Date dateTime = resultSet.getDate("DATETIME");
                double revenue = resultSet.getDouble("REVENUE");

                System.out.println("Concession ID: " + rtID);
                System.out.println("Name: " + name);
                System.out.println("Date: " + dateTime);
                System.out.println("Revenue: " + revenue);
                System.out.println("--------------------------");
            }
        } finally {
            // Close the ResultSet, Statement, and Connection
            if (resultSet != null) resultSet.close();
            // Add similar close statements for other resources if needed
        }
    }

    public void insert_Attendee() {

        double seniorPrice = 20.00, adultPrice = 30.00, childPrice = 10.00;
        String query = "";

        Asset_Management obj = new Asset_Management();

        int ID = 0;

        Connection con = null;
        Statement st = null;
        BufferedReader br = null;

            while (true) {

                try {

                Class.forName("oracle.jdbc.OracleDriver");

                con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                        "abs83", "Brutekiller231!");

                st = con.createStatement();
                br = new BufferedReader(new InputStreamReader(System.in));
                String type = "";

                try {

                    System.out.print("Enter Attendee ID: ");
                    ID = Integer.parseInt(br.readLine());

                    if (obj.exists(con, ID, "ZooAdmissions"))
                        throw new IDAlreadyExistsException("This ID already exists");

                    ZID = ID;

                    System.out.print("Enter Attendee type [Senior/Adult/Child]:  ");
                    type = br.readLine();

                    if (type.equalsIgnoreCase("Senior")) {
                        query = "INSERT INTO ZooAdmissions(ID, Senior_Price) VALUES (" + ID + ", " + seniorPrice + ")";
                        revenue = seniorPrice;
                    }  else if (type.equalsIgnoreCase("Adult")) {
                        query = "INSERT INTO ZooAdmissions(ID, Adult_Price) VALUES (" + ID + ", " + adultPrice + ")";
                        revenue = adultPrice;
                    } else {
                        query = "INSERT INTO ZooAdmissions(ID, child_Price) VALUES (" + ID + ", " + childPrice + ")";
                        revenue = childPrice;
                    }

                } catch (IDAlreadyExistsException e3) {
                    System.out.print(ID + " already exists within Animals table");
                } catch (SQLIntegrityConstraintViolationException e1) {
                    System.out.println("Must be a valid building ID");
                    continue;
                } catch (NumberFormatException e2) {
                    System.out.println("Input must be numerical");
                    continue;
                }

                System.out.print("Which building was ticket sold in? Enter a valid BID: ");
                int bid = Integer.parseInt(br.readLine());

                System.out.print("Name of Attendee:  ");
                String name = br.readLine();

                String query2 = "INSERT INTO RevenueTypes(ID, Name, Type, BID) VALUES (" + ID + ", '" + name + "', '"+type+"', '" + bid + "')";

                st.executeUpdate(query2);
                st.executeUpdate(query);
                getAttendanceView(ID, revenue);

                System.out.print("Do you want to add more to ZooAdmissions?: Yes/No ");
                String answer = br.readLine();

                if (answer.equalsIgnoreCase("No")) {
                    System.out.print("Insert Successful");
                    con.close();
                    break;
                } else
                continue;
            } catch(IDAlreadyExistsException e){
                System.out.println(ID + " already exists within Concessions table");
                continue;
            } catch(SQLIntegrityConstraintViolationException e1){
                System.out.println("Error, constraint violated");
                e1.printStackTrace();
                continue;
            } catch(NumberFormatException e2){
                System.out.println("Wrong Input");
                continue;
            } catch(Exception ex){
                ex.printStackTrace();
            }

        }

        }

        public ResultSet getAttendanceView(int ID, double revenue) throws SQLException {

            String selectQuery = "";
            Asset_Management obj = new Asset_Management();

            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                    "abs83", "Brutekiller231!");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            Statement st = con.createStatement();
            PreparedStatement preparedStatement = null;

            try {

                if (!obj.exists(con, ID, "Revenue_Events")) {
                    System.out.print("Enter date (yyyy-MM-dd): ");
                    String dateString = br.readLine();

                    String query = "INSERT INTO Revenue_Events(RTID,DATETIME,REVENUE)  VALUES(" + ID + ", " +
                            "TO_DATE('" + dateString + "', 'yyyy-MM-dd'), " + revenue + ")";
                    st.executeUpdate(query);
                }

                 selectQuery = "SELECT RT.NAME, RT.TYPE, RE.DATETIME, SUM(RE.REVENUE) AS TOTAL_REV " +
                         "FROM REVENUE_EVENTS RE " +
                         "JOIN REVENUETYPES RT ON RE.RTID = RT.ID " +
                         "WHERE RT.TYPE IN ('Senior', 'Adult', 'Child') " +
                         "GROUP BY RT.NAME, RT.TYPE, RE.DATETIME";



            } catch (SQLIntegrityConstraintViolationException e1) {
                System.out.println("Error, constraint violated");
                e1.printStackTrace();
            } catch (NumberFormatException e2) {
                System.out.println("Wrong Input");
                e2.printStackTrace();
            } catch (SQLException e3) {
                e3.printStackTrace();
            }catch(Exception ex){
                ex.printStackTrace();
            }

            preparedStatement = con.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();


            return resultSet;

        }

    public void printAttendanceView(ResultSet resultSet) {
        try {
             resultSet = getAttendanceView(ZID, revenue);

            while (resultSet.next()) {
                String name = resultSet.getString("NAME");
                String type = resultSet.getString("Type");
                Date datetime = resultSet.getDate("DATETIME");
                double rev = resultSet.getDouble("TOTAL_REV");

                System.out.println("Name: " + name);
                System.out.println("Type: " + type);
                System.out.println("DateTime: " + datetime);
                System.out.println("Revenue: " + rev);
                System.out.println("--------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}