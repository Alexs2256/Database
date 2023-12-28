import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Management_Reporting_Menu {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Management_And_Reporting managementAndReporting = new Management_And_Reporting();
    private static final ZooMenu menu = new ZooMenu();


    public static void main(String[] args) throws SQLException {
        int choice;

        do {
            displayMenu();
            choice = getUserChoice();

            switch (choice) {
                case 1:
                    revenueReportBySource();
                    break;
                case 2:
                    animallPopulationReport();
                    break;
                case 3:
                    top3Attractions();
                    break;
                case 4:
                    bestDaysInMonth();
                    break;
                case 5:
                    averageAttractionAttendConcec();
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
        System.out.println("========== Management and Reporting Menu ==========");
        System.out.println("1. Revenue Report by Source");
        System.out.println("2. Animal Population Report");
        System.out.println("3. Top 3 Attractions");
        System.out.println("4. Best Days in Month");
        System.out.println("5. Average Attraction Attendance and Concession");
        System.out.println("0. Exit");
        System.out.println("==================================================");
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

    private static void revenueReportBySource() {
        try {
            ResultSet result = managementAndReporting.revenueReportBySource();
            managementAndReporting.printRevReport(result);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void animallPopulationReport() {
        try {
            ResultSet result = managementAndReporting.animallPopulationReport();
            Management_And_Reporting.printAnimalPopulationReport(result);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void top3Attractions() {
        try {
            ResultSet result = managementAndReporting.top3Attractions();
            Management_And_Reporting.printTop3Attractions(result);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void bestDaysInMonth() {
        try {
            ResultSet result = managementAndReporting.bestDaysInMonth();
            Management_And_Reporting.printBestDaysInMonth(result);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void averageAttractionAttendConcec() {
        try {
            ResultSet result = managementAndReporting.averageAttractionAttendConcec();
            Management_And_Reporting.printAverageAttractionAttendConcec(result);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
