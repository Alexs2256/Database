import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.InputMismatchException;


public class Asset_Management {
    public int dummyID;
    public int ID;

    public void insert_Animal() throws SQLException {

        int ID = 0, species = 0, building = 0, enclosure = 0;

        Connection con = null;
        Statement st = null;
        BufferedReader br = null;
        String query2 = "";


        try {

            while (true) {

                Class.forName("oracle.jdbc.OracleDriver");

                con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                        "abs83", "Brutekiller231!");

                st = con.createStatement();
                br = new BufferedReader(new InputStreamReader(System.in));

                try {

                    System.out.print("Enter Animal ID: ");
                    ID = Integer.parseInt(br.readLine());

                    if (exists(con, ID, "Animal"))
                        throw new IDAlreadyExistsException("This ID already exists");

                    System.out.print("Enter Animal SID: ");
                    species = Integer.parseInt(br.readLine());

                    System.out.print("Enter Animal BID: ");
                    building = Integer.parseInt(br.readLine());

                    System.out.print("Enter Animal EID: ");
                    enclosure = Integer.parseInt(br.readLine());

                } catch (IDAlreadyExistsException e3) {
                    System.out.print(ID + " already exists within Animals table");
                } catch (SQLIntegrityConstraintViolationException e1) {
                    System.out.println("Must be a valid building ID");
                     continue;
                } catch (NumberFormatException e2) {
                    System.out.println("Input must be numerical");
                    continue;
                }
                String query = "INSERT INTO animal(ID, SID, BID, EID) VALUES (" + ID + ", " + species + ", " + building + ", " + enclosure + ")";
                System.out.print("Enter the name of the species: ");
                String sName = br.readLine();
                System.out.print("Enter the food cost of the species: ");
                int foodCost = Integer.parseInt(br.readLine());
                if (!exists(con,species, "Species")) {
                    query2 = "INSERT INTO Species(ID, Name, FoodCost) VALUES (" + species + ", '" + sName + "', '" + foodCost + "')";
                    st.executeUpdate(query2);
                } if (!exists(con, building, "Building")) {
                    System.out.print("Enter the building name where this animal resides: ");
                    String bName = br.readLine();
                    String query3 = "INSERT INTO Building(ID, Name) VALUES (" + building + ", '" + bName + "')";
                    st.executeUpdate(query3);
                }
                String query4 = "INSERT INTO Enclosure(ID, BID) VALUES (" + enclosure + ", " + building + ")";

                st.executeUpdate(query4);
                st.executeUpdate(query);

                System.out.print("Do you want to add more to Animals?: Yes/No ");
                String answer = br.readLine();

                if (answer.equalsIgnoreCase("No")) {
                    con.close();
                    break;
                }  else
                    continue;
            }
        } catch (SQLIntegrityConstraintViolationException e) {

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

            System.out.print("Insert Successful");
    }

    public ResultSet getAnimalView() throws SQLException {

        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");

        String query = "SELECT * FROM ANIMALS";

        PreparedStatement preparedStatement = con.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet;

    }

    public void updateAnimal() {

        int id = 0;

        Connection con = null;
        Statement statement = null;
        BufferedReader rd = null;

        while (true) {

            try {

                Class.forName("oracle.jdbc.OracleDriver");
                con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                        "abs83", "Brutekiller231!");

                statement = con.createStatement();
                rd = new BufferedReader(new InputStreamReader(System.in));

                try {
                System.out.print("Please enter your animal ID: ");
                 id = Integer.parseInt(rd.readLine());

                if (!exists(con, id, "Animal"))
                    throw new IDDoesntExistException("");
                } catch (IDDoesntExistException ex) {
                    System.out.println(id + " doesn't exist in Animals table");
                    continue;
                }

                System.out.println("Enter the Column name of the table you want to update");
                System.out.print("Column name: ");
                String columnName = rd.readLine();
                updateAnimalField(con, "Animal", columnName, id);
                System.out.print("Continue updating Animals? Yes/No: ");
                String answer = rd.readLine();
                if (answer.equalsIgnoreCase("No")) {
                    System.out.print("Update Successful");
                    break;
                } else
                    continue;
            } catch (NumberFormatException e) {
                System.out.println("Wrong value type entered");
                continue;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean exists(Connection con, int ID, String table) throws SQLException {

        String query = "";
        int RTID = 0;
        PreparedStatement prepStatement = null;

        con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");
        if (table.equalsIgnoreCase("Revenue_Events")) {
            RTID=ID;
            query = "SELECT * FROM " + table + " WHERE RTID = ?";
            prepStatement = con.prepareStatement(query);
            prepStatement.setInt(1, RTID);
        }
        else {
            query = "SELECT * FROM " + table + " WHERE ID = ?";
            prepStatement = con.prepareStatement(query);
            prepStatement.setInt(1, ID);
        }

        return prepStatement.executeQuery().next();

    }

    public void updateAnimalField(Connection con, String tableName, String columnName, int id) throws SQLException {

        Asset_Management obj = new Asset_Management();

        Statement statement = null;
        BufferedReader rd = null;
        int val = 0;

        con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");
        statement = con.createStatement();
        rd = new BufferedReader(new InputStreamReader(System.in));

        String query1 = "UPDATE " + tableName + " SET " + columnName + "=? " + "WHERE ID =?";
        PreparedStatement prepStatement = con.prepareStatement(query1);

            try {
                if (columnName.equalsIgnoreCase("Status") || columnName.equalsIgnoreCase("BirthYear")) {

                    System.out.print("Enter new Value: ");
                    String value = rd.readLine();
                    prepStatement.setString(1, value);

                } else if (columnName.equals("SID") || columnName.equals("BID") || columnName.equals("EID")) {

                    System.out.println("Note: Updating a foreign key may result in a new value in its parent table.");
                    System.out.print("Do you wish to proceed? [Yes/No]:  ");
                    String answer = rd.readLine();

                    if (answer.equalsIgnoreCase("No")) {
                        System.out.println("Update canceled.");
                        return;
                    }

                    String parent = "";

                    if (columnName.equals("SID")) {
                        parent = "Species";
                        int sid = obj.insertSpecies_Enclosure(con, parent);
                        prepStatement.setInt(1, sid);
                    } else if (columnName.equals("BID")) {
                        prepStatement.setInt(1, obj.insert_Building());
                    } else {
                        parent = "Enclosure";
                        prepStatement.setInt(1, obj.insertSpecies_Enclosure(con, parent));
                    }

                } else {
                    System.out.print("Enter new Value: ");
                    int value = Integer.parseInt(rd.readLine());
                    if (exists(con, value, "Animal") && columnName.equals("ID"))
                        throw new SQLIntegrityConstraintViolationException(value + " already Exists");
                       prepStatement.setInt(1, value);
                }

            } catch (SQLIntegrityConstraintViolationException ex) {
                System.out.println("ID already exists in Animals");
                ex.printStackTrace();
                return;
            } catch (NumberFormatException e) {
                System.out.println("Wrong value type entered");
            } catch (Exception ex) {

            }

        prepStatement.setInt(2, id);
        prepStatement.executeUpdate(); // fix this

    }
    public int insertSpecies_Enclosure(Connection con, String table) {

        int ID = 0, BID = 0, eid = 0;
        Statement st = null;
        BufferedReader br = null;

        while (true) {

        try {

            String query1 = "", query2 = "";

            Class.forName("oracle.jdbc.OracleDriver");

            con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                    "abs83", "Brutekiller231!");

            st = con.createStatement();
            br = new BufferedReader(new InputStreamReader(System.in));

                try {
                    System.out.print("Enter " + table + " ID: ");
                    ID = Integer.parseInt(br.readLine());

                    if (exists(con, ID, table))
                        throw new IDAlreadyExistsException("This ID already exists");
                } catch (IDAlreadyExistsException e) {
                    System.out.println(ID + " already exists within " + table + " table");
                    continue;
                }

                if (table.equalsIgnoreCase("Species")) {

                    System.out.print("Enter Species Name: ");
                    String species = br.readLine();

                    System.out.print("Enter FoodCost: ");
                    double foodCost = Integer.parseInt(br.readLine());

                    query1 = "INSERT INTO Species(ID, Name, FoodCost) VALUES (" + ID + ", '" + species + "', '" + foodCost + "')";
                    st.executeUpdate(query1);

                    try {
                        System.out.print("Enter ID of the Employer who cares for this species: ");
                        eid = Integer.parseInt(br.readLine());
                        if (!exists(con, eid, "Employee"))
                            throw new SQLIntegrityConstraintViolationException("This employee doesn't exist");
                        System.out.print("Enter Job Type [Veterinarian/AnimalCare Specialist]");
                        String jobType = br.readLine();
                        String updateQuery = "UPDATE Employee SET JobType = '" + jobType + "' WHERE ID = " + eid;
                        st.executeUpdate(updateQuery);
                    } catch (SQLIntegrityConstraintViolationException e) {
                        System.out.print("No existing employee matches this ID, insert new employee: ");
                        insert_Employee();
                    }

                    String query = "INSERT INTO CARES_FOR(SID, ID) VALUES (" + ID + ",'" + eid + "')";

                    PreparedStatement st2 = con.prepareStatement(query);
                    st2.executeUpdate(query);

                } else {
                    try {
                        System.out.print("Enter BID for Enclosure: ");
                        BID = Integer.parseInt(br.readLine());

                        if (!exists(con, BID, "Building"))
                            throw new SQLIntegrityConstraintViolationException("");
                    } catch (SQLIntegrityConstraintViolationException i) {
                        System.out.print("Error, the building must exist withing buildings table");
                    }

                    System.out.print("Enter SQFT for Enclosure: ");
                    int sqft = Integer.parseInt(br.readLine());

                    query2 = "INSERT INTO Enclosure(ID, BID, SQFT) VALUES (" + ID + ", '" + BID + "', " + sqft + ")";
                    st.executeUpdate(query2);
                }

            } catch(Exception e){
            e.printStackTrace();
            }

            break;
        }

        return ID;
    }

    // ANIMAL ENDS ----------------------------------------------

    public int insert_Building() {

        int ID = 0;
        Connection con = null;
        Statement st = null;
        BufferedReader br = null;

        try {

            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                    "abs83", "Brutekiller231!");

            st = con.createStatement();
            br = new BufferedReader(new InputStreamReader(System.in));

            while (true) {

                try {
                    System.out.print("Enter Building ID: ");
                    ID = Integer.parseInt(br.readLine());
                    System.out.print("Enter Building Name: ");
                    String name = br.readLine();
                    System.out.print("Enter Building Type: ");
                    String type = br.readLine();


                String query = "INSERT INTO Building(ID, Name, Type) VALUES (" + ID + ", '" + name + "', '" + type + "')";
                st.executeUpdate(query);

                } catch (NumberFormatException e1) {
                    System.out.println("Wrong input type");
                    continue;
                }

                System.out.println("Do you want to add more to Building?: Yes/No");
                String answer = br.readLine();

                if (answer.equalsIgnoreCase("No")) {
                    con.close();
                    break;
                } else
                    continue;
            }

        }  catch (SQLIntegrityConstraintViolationException e) {
            System.out.print(ID + " already exists within Buildings table");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ID;
    }

    public ResultSet getBuildingView() throws SQLException {

        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");

        String query = "SELECT * FROM BUILDING";

        PreparedStatement preparedStatement = con.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet;

    }

    public void updateBuilding() {

        Connection con = null;
        Statement statement = null;
        BufferedReader rd = null;

        while (true) {

            try {

                Class.forName("oracle.jdbc.OracleDriver");

                con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                        "abs83", "Brutekiller231!");

                statement = con.createStatement();

                rd = new BufferedReader(new InputStreamReader(System.in));

                System.out.print("Please enter your Building ID: ");
                int id = Integer.parseInt(rd.readLine());

                if (!exists(con, id, "Building")) {
                    throw new IDDoesntExistException("This Building doesnt exist");
                }

                System.out.println("Enter the Column name you want to update");
                System.out.print("Column name: ");
                String columnName = rd.readLine();

                updateBuildingField(con, "Building", columnName, id);

                System.out.println("Continue updating [Yes/No] ?");
                String answer = rd.readLine();

                if (answer.equalsIgnoreCase("No"))
                    break;
                else
                    continue;
            } catch (SQLIntegrityConstraintViolationException e2) {
                e2.printStackTrace();
                continue;
            } catch (IDDoesntExistException e1) {
                e1.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void updateBuildingField(Connection con, String tableName, String columnName, int id) throws SQLException, IOException {

        Statement statement = null;
        BufferedReader rd = null;
        int val = 0;
        boolean skip = false;

        con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");
        statement = con.createStatement();
        rd = new BufferedReader(new InputStreamReader(System.in));

        String query1 = "UPDATE " + tableName + " SET " + columnName + "=? " + "WHERE ID =?";
        PreparedStatement prepStatement = con.prepareStatement(query1);

        if (columnName.equals("ID")) {
            System.out.print("Enter new Value: ");
            int value = Integer.parseInt(rd.readLine());
            prepStatement.setInt(1, value);

        } else {
            System.out.print("Enter new Value: ");
            String value = rd.readLine();
            prepStatement.setString(1, value);
        }

        prepStatement.setInt(2,id);
        prepStatement.executeUpdate();
}

    // Building End ------------------------------------------

    public void insert_AnimalShow() throws IDAlreadyExistsException {

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

                System.out.print("Enter Show ID: ");
                ID = Integer.parseInt(br.readLine());

                if (exists(con, ID, "AnimalShow"))
                    throw new IDAlreadyExistsException("This ID already exists");

                System.out.print("Enter how many times showed per day: ");
                int perDay = Integer.parseInt(br.readLine());

                System.out.println("If a senior,child, or adult did not attend, enter 0");
                System.out.print("Enter Senior Ticket Price ");
                double sPrice = Double.parseDouble(br.readLine());

                System.out.print("Enter Adult Price: ");
                double aPrice = Double.parseDouble(br.readLine());

                System.out.print("Enter Child Price: ");
                double cPrice = Double.parseDouble(br.readLine());

                String query = "INSERT INTO AnimalShow(ID, PERDAY, Senior_Price, Adult_Price, Child_Price) VALUES (" + ID + ", "
                        + perDay + ", " + sPrice + ",  " + aPrice + ",  " + cPrice + ")";
                System.out.print("Enter the shows name: ");
                String sName = br.readLine();
                System.out.print("Enter the building ID in which the show is held: ");
                int BID = Integer.parseInt(br.readLine());
                String query2 = "INSERT INTO REVENUETYPES(ID, Name, Type, BID) VALUES (" + ID + ", '" + sName + "', 'Attraction', " + BID + ")";

                if (!exists(con, BID, "Building")) {
                    System.out.print("This building doesn't exists, please enter its name: ");
                    String buildingName = br.readLine();

                    try {
                        String bQuery = "INSERT INTO Building(ID, Name) VALUES (" + BID + ", '" + buildingName + "')";
                        Statement statement2 = con.createStatement();
                        statement2.execute(bQuery);
                        statement2.executeUpdate(bQuery);
                    } catch (SQLIntegrityConstraintViolationException ex) {
                        System.out.println("Enter different building name: ");
                        continue;
                    }
                }
                st.executeUpdate(query2);
                st.executeUpdate(query);

                System.out.print("Do you want to add more to Animal Show?: Yes/No");
                String answer = br.readLine();

                if (answer.equalsIgnoreCase("No")) {
                    con.close();
                    break;
                }  else
                    continue;

            } catch (IDAlreadyExistsException e) {
                System.out.print("This ID already exists, try again");
                continue;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            br.close();
            st.close();
            con.close();
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }


    }

    public ResultSet getAnimalShowView() throws SQLException {

        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");

        String query = "SELECT * FROM ANIMALSHOW";

        PreparedStatement preparedStatement = con.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet;

    }

    public void updateAnimalShow() {

        Connection con = null;
        Statement statement = null;
        BufferedReader rd = null;

        while (true) {

            try {

                Class.forName("oracle.jdbc.OracleDriver");

                con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                        "abs83", "Brutekiller231!");

                statement = con.createStatement();

                rd = new BufferedReader(new InputStreamReader(System.in));

                System.out.print("Please enter your Animal Show ID: ");
                int id = Integer.parseInt(rd.readLine());

                if (!exists(con, id, "AnimalShow"))
                    throw new IDDoesntExistException("The ID " + id + " doesn't exist in this table");

                System.out.println("Enter the Column name you want to update");
                System.out.print("Column name: ");
                String columnName = rd.readLine();

                updateAttractionField(con, "AnimalShow", columnName, id);

                System.out.print("Do you want to add more to Animal Show?: Yes/No");
                String answer = rd.readLine();

                if (answer.equalsIgnoreCase("No")) {
                    System.out.print("Update Successful");
                    con.close();
                    break;
                } else
                    continue;

            } catch (IDDoesntExistException e) {
                e.printStackTrace();
                continue;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void updateAttractionField(Connection con, String tableName, String columnName, int id) throws SQLException, IOException {

        Statement statement = null;
        BufferedReader rd = null;
        int val = 0;

        con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");
        statement = con.createStatement();
        rd = new BufferedReader(new InputStreamReader(System.in));

        String query1 = "UPDATE " + tableName + " SET " + columnName + "=? " + "WHERE ID =?";
        PreparedStatement prepStatement = con.prepareStatement(query1);

        if (columnName.equalsIgnoreCase("ID")) {

            System.out.println("This is a foreign key, you must either change the value to a already existing value or create a new one: ");
            System.out.print("[Create new/Use existing value] Please type either new or existing: ");
            String answer = rd.readLine();

            if (answer.equalsIgnoreCase("new")) {
                insertIntoRevTypes(con);
                prepStatement.setInt(1, dummyID);
            }

        } else if (columnName.equalsIgnoreCase("PerDay")) {
            System.out.print("Enter new Value: ");
            String value = rd.readLine();
            prepStatement.setString(1, value);
        } else {
            System.out.print("Enter new Value: ");
            double value = Double.parseDouble(rd.readLine());
            prepStatement.setDouble(1, value);
        }


        prepStatement.setDouble(2, id);
        prepStatement.executeUpdate();
    }

    public void insertIntoRevTypes(Connection con) {

        Statement statement1 = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            statement1 = con.createStatement();
            System.out.print("Enter the shows ID: ");
            dummyID = Integer.parseInt(br.readLine());
            System.out.print("Enter the shows name: ");
            String sName = br.readLine();
            System.out.print("Enter the building ID in which the show is held: ");
            int BID = Integer.parseInt(br.readLine());
            String query2 = "INSERT INTO REVENUETYPES(ID, Name, Type, BID) VALUES (" + dummyID + ", '" + sName + "', 'Attraction', '" + BID + "')";

            if (!exists(con, BID, "Building")) {
                System.out.print("This building doesn't exists, please enter its name: ");
                String buildingName = br.readLine();

                try {
                    String bQuery = "INSERT INTO Building(ID, Name) VALUES (" + BID + ", '" + buildingName + "')";
                    Statement statement2 = con.createStatement();
                    statement2.execute(bQuery);
                    statement2.executeUpdate(bQuery);
                } catch (SQLIntegrityConstraintViolationException ex) {
                }

            }

            statement1.execute(query2);

        } catch (NumberFormatException e1) {
            System.out.print("Wrong input type");
            e1.printStackTrace();
        }  catch (IDAlreadyExistsException e) {
            System.out.print("This ID already exists, try again");
            return;
        } catch (SQLIntegrityConstraintViolationException e2) {
            System.out.print("Constraint violated");
            e2.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

}

// ANIMALSHOW ENDS -------------------------------------------------------------------------------------------

    public void insert_Employee() {


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

                    System.out.print("Enter Employee ID: ");
                    ID = Integer.parseInt(br.readLine());


                if (exists(con, ID, "EMPLOYEE"))
                    throw new IDAlreadyExistsException("This ID already exists");

                System.out.print("Enter Employee's First Name: ");
                String fName = br.readLine();

                System.out.print("Enter Employee's Last Name: ");
                String lName = br.readLine();

                System.out.print("Enter Employee's Hourly Rate ID: ");
                int hID = Integer.parseInt(br.readLine());

                String query = "INSERT INTO EMPLOYEE(ID, FIRST, LAST, HOURID) VALUES (" + ID + ", '" + fName + "', '" + lName + "', " + hID + ")";

                String query2 = "INSERT INTO HOURLYRATE(ID) VALUES (" + hID + ")";

                if (exists(con, hID, "HourlyRate"))
                    throw new IDAlreadyExistsException("This ID already exists ");

                st.executeUpdate(query2);
                st.executeUpdate(query);

                System.out.print("Do you want to add more to Employees?: Yes/No ");
                String answer = br.readLine();

                if (answer.equalsIgnoreCase("No")) {
                    System.out.print("Insert Successful");
                    con.close();
                    break;
                }  else
                    continue;

            } catch (NumberFormatException e1) {
              System.out.print("Invalid data type");
              continue;
            } catch (IDAlreadyExistsException e3) {
               e3.printStackTrace();
               continue;
           } catch (SQLIntegrityConstraintViolationException e2) {
              System.out.print("Constraint Violated");
              e2.printStackTrace();
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

      public void updateEmlpoyee() {

          Connection con = null;
          Statement statement = null;
          BufferedReader rd = null;

          while (true) {
              try {
                  Class.forName("oracle.jdbc.OracleDriver");

                  con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                          "abs83", "Brutekiller231!");

                  statement = con.createStatement();

                  rd = new BufferedReader(new InputStreamReader(System.in));

                  System.out.print("Please enter your Employee ID: ");
                  int id = Integer.parseInt(rd.readLine());

                  if (!exists(con, id, "EMPLOYEE"))
                      throw new IDDoesntExistException("Sorry no Employee with ID: " + id + " exists");

                  System.out.println("Enter the Column name you want to update");
                  System.out.print("Column name: ");
                  String columnName = rd.readLine();

                  updateEmployeeField(con, "Employee", columnName, id);

                  System.out.print("Do you to continue updating Employees?: Yes/No ");
                  String answer = rd.readLine();

                  if (answer.equalsIgnoreCase("No")) {
                      con.close();
                      break;
                  }  else
                      continue;

              } catch (SQLIntegrityConstraintViolationException e2) {
                      System.out.print("Constraint Violated");
                      e2.printStackTrace();
              } catch (NumberFormatException e2) {
                  System.out.print("Invalid Entry Type");
                  continue;
              } catch (IDDoesntExistException e1) {
                  e1.printStackTrace();
                  continue;
              } catch (Exception ex) {
                  ex.printStackTrace();
              }

          }

    }

    public void updateEmployeeField(Connection con, String tableName, String columnName, int id) throws SQLException, IOException {

        Statement statement = null;
        BufferedReader rd = null;
        int val = 0;

        con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");
        statement = con.createStatement();
        rd = new BufferedReader(new InputStreamReader(System.in));

        String query1 = "UPDATE " + tableName + " SET " + columnName + "=? " + "WHERE ID =?";
        PreparedStatement prepStatement = con.prepareStatement(query1);

        if (columnName.equalsIgnoreCase("HOURID") || columnName.equalsIgnoreCase("SUPERID")||
                columnName.equalsIgnoreCase("CID") || columnName.equalsIgnoreCase("ZID")) {

            System.out.println("This is a foreign key, you must either change the value to a already existing value or create a new one: ");
            System.out.print("[Create new/Use existing value] Please type either new or existing: ");
            String answer = rd.readLine();

            if (answer.equalsIgnoreCase("new")) {
               int newVal = insertEmpForeignK(con, columnName, id);
               prepStatement.setInt(1,   newVal);
            }
        } else if (columnName.equalsIgnoreCase("ID") || columnName.equalsIgnoreCase("ZIP") ) {
            System.out.print("Enter new Value: ");
            int value = Integer.parseInt(rd.readLine());
            prepStatement.setInt(1, value);
        } else if (columnName.equalsIgnoreCase("JobType")) {
            System.out.print("Enter new Value: ");
            String value = rd.readLine();
            prepStatement.setString(1, value);
        } else {
            System.out.print("Enter new Value: ");
            String value = rd.readLine();
            prepStatement.setString(1, value);
        }

        prepStatement.setInt(2, id);
        prepStatement.executeUpdate();
    }

    public int insertEmpForeignK(Connection con, String column, int id) {
        Statement statement = null;
        BufferedReader rd = null;
        int val = 0;
        String query2 = "";

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                    "abs83", "Brutekiller231!");
            statement = con.createStatement();
            rd = new BufferedReader(new InputStreamReader(System.in));

            if (column.equalsIgnoreCase("HourID")) {
                System.out.print("Enter new value for HID: ");
                val = Integer.parseInt(rd.readLine());
                query2 = "INSERT INTO HOURLYRATE(ID) VALUES (?)";
            } else if (column.equalsIgnoreCase("SuperID")) {
                System.out.print("Enter new value for SuperID: ");
                val = Integer.parseInt(rd.readLine());
                try {
                    if (!exists(con, val, "Employee"))
                        throw new SQLIntegrityConstraintViolationException("This employee doesn't exist");
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("This Employee doesn't exist, you must add a new employee with " +
                            "this superID");
                    ID = val;
                    insert_Employee();
                    return val;
                }
                query2 = "UPDATE Employee SET SuperID = ? WHERE ID = ?";
            } else if (column.equalsIgnoreCase("CID")) {
                System.out.print("Enter new value for CID: ");
                val = Integer.parseInt(rd.readLine());
                query2 = "INSERT INTO CONCESSION(ID) VALUES (?)";
            } else {
                System.out.print("Enter new value for ZID: ");
                val = Integer.parseInt(rd.readLine());
                query2 = "INSERT INTO ZOOADMISSIONS(ID) VALUES (?)";
            }

            // Using PreparedStatement to avoid SQL injection
            try (PreparedStatement preparedStatement = con.prepareStatement(query2)) {
                preparedStatement.setInt(1, val);
                if (column.equalsIgnoreCase("SuperID"))
                    preparedStatement.setInt(2, id);

                preparedStatement.executeUpdate();
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return val;
    }


    public ResultSet getEmployeeView() throws SQLException {

        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@prophet.njit.edu:1521:course",
                "abs83", "Brutekiller231!");

        String query = "SELECT * FROM EMPLOYEE";

        PreparedStatement preparedStatement = con.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet;

    }
}

