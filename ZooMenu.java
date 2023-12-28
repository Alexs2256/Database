import java.sql.SQLException;
import java.util.Scanner;


public class ZooMenu {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        int choice;

        do {
            displayMainMenu();
            choice = getUserChoice();

            switch (choice) {
                case 1:
                    Asset_Management_Menu.main(args);
                    break;
                case 2:
                    Daily_Zoo_Activity_Menu.main(args);
                    break;
                case 3:
                    Management_Reporting_Menu.main(args);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 0);

        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("========== Main Zoo Menu ==========");
        System.out.println("1. Asset Management Menu");
        System.out.println("2. Daily Zoo Activity Menu");
        System.out.println("3. Management and Reporting Menu");
        System.out.println("0. Exit");
        System.out.println("====================================");
    }

    private static int getUserChoice() {
        int choice = -1;
        boolean validInput = false;


            try {
                System.out.print("Enter your choice: ");
                if (scanner.hasNextLine()) {
                    choice = Integer.parseInt(scanner.nextLine());
                    validInput = true;
                } else {
                    System.out.println("No input available. Please try again.");
                    scanner.nextLine(); // Consume the invalid input
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }

        return choice;
    }

}
