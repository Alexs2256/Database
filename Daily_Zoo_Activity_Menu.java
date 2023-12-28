import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Daily_Zoo_Activity_Menu {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Daily_Zoo_Activity dailyZooActivity = new Daily_Zoo_Activity();
    private static final ZooMenu menu = new ZooMenu();


    public static void main(String[] args) throws SQLException {
        int choice;

        do {
            displayMenu();
            choice = getUserChoice();

            switch (choice) {
                case 1:
                    Attraction_Entry();
                    break;
                case 2:
                    Insert_Concession();
                    break;
                case 3:
                    Insert_Attendee();
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
        System.out.println("========== Daily Zoo Activity Menu ==========");
        System.out.println("1. Attraction Entry");
        System.out.println("2. Insert Concession");
        System.out.println("3. Insert Attendee");
        System.out.println("0. Exit");
        System.out.println("============================================");
    }

    private static int getUserChoice() {
        int choice = -1;
        try {
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());
        }  catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
        return choice;
    }

    private static void Attraction_Entry() {
        dailyZooActivity.Attraction_Entry();
    }

    private static void Insert_Concession() {

        dailyZooActivity.insert_Concession();
        try {
            ResultSet concessionView = dailyZooActivity.getConcessionView(Daily_Zoo_Activity.CID);
            dailyZooActivity.printConcessionView(concessionView);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void Insert_Attendee() {
        dailyZooActivity.insert_Attendee();
    }
}
