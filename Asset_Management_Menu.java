import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.SQLException;


public class Asset_Management_Menu {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ZooMenu menu = new ZooMenu();


    public static void main(String[] args) throws SQLException {
        Asset_Management obj = new Asset_Management();
        Print print = new Print();
        int choice;

        do {
            displayMenu();
            choice = getUserChoice();

            switch (choice) {
                case 1:
                    obj.insert_Animal();
                    break;
                case 2:
                    try {
                        ResultSet result = obj.getAnimalView();
                        print.printResultSet(result);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    obj.updateAnimal();
                    break;
                case 4:
                    obj.insert_Building();
                    break;
                case 5:
                    try {
                        ResultSet buildingResultSet = obj.getBuildingView();
                        print.printBuildingView(buildingResultSet);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    obj.updateBuilding();
                    break;
                case 7:
                    try {
                        obj.insert_AnimalShow();
                    } catch (IDAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    try {
                        ResultSet animalShowResultSet = obj.getAnimalShowView();
                        print.printAnimalShowView(animalShowResultSet);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    obj.updateAnimalShow();
                    break;
                case 10:
                    obj.insert_Employee();
                    break;
                case 11:
                    obj.updateEmlpoyee();
                    break;
                case 12:
                    print.printEmployeeViewEMP(obj.getEmployeeView());
                    break;
                case 0:
                    System.out.println("Exiting...");
                    menu.main(args);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 0);

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("========== Asset Management Menu ==========");
        System.out.println("1. Insert Animal");
        System.out.println("2. View Animals");
        System.out.println("3. Update Animal");
        System.out.println("4. Insert Building");
        System.out.println("5. View Buildings");
        System.out.println("6. Update Building");
        System.out.println("7. Insert Animal Show");
        System.out.println("8. View Animal Shows");
        System.out.println("9. Update Animal Show");
        System.out.println("10. Insert Employee");
        System.out.println("11. Update Employee");
        System.out.println("12. View Employee");
        System.out.println("0. Exit");
        System.out.println("===========================================");
    }

    private static int getUserChoice() {
        int choice = -1;
        try {
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
        return choice;
    }

}
