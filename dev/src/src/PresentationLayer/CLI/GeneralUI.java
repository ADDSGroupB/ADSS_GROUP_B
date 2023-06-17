package PresentationLayer.CLI;

import ServiceLayer.HR.CompanyService;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GeneralUI {
    CompanyService service = new CompanyService();
    public void StartSystem() throws SQLException {

        while (true) {
            System.out.println("Which system do you want to enter to ? ");
            System.out.println("1. HR ");
            System.out.println("2. TransportManager ");
            System.out.println("3. Get out of the system ");

            int choice_main_menu;
            Scanner console1 = new Scanner(System.in);
            try {
                choice_main_menu = console1.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter an integer");
                continue;
            }

            switch (choice_main_menu) {

                case 1:
                    ;
                    Scanner scanner = new Scanner(System.in);
                    int choice;
                    while (true) {
                        System.out.println("Enter your choice: ");
                        System.out.println("1. Login as HR manager");
                        System.out.println("2. Login as employee");
                        System.out.println("3. Reset system assignment 1");
                        System.out.println("4. Reset system assignment 2");
                        System.out.println("5. Exit");
                        try {
                            choice = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid choice, please enter 1-5.");
                            scanner.next();
                            continue;
                        }
                        boolean checkLogIn;
                        switch (choice) {
                            case 1:
                                /** Create an instance of the UI of HR  */
                                HRUI ui = new HRUI();
                                /** Call the Start_System method of the UI of HR to start the system */
                                ui.hrMenu();
                                break;
                            case 2:

                                /** Create an instance of the UI of HR  */
                                EmployeeUI ui1 = new EmployeeUI();
                                /** Call the Start_System method of the UI of HR to start the system */
                                ui1.employeeMenu();
                                break;
                            case 3:
                                service.resetDataBaseAssignment1();
                                break;
                            case 4:
                                service.resetDataBaseAssignment2();
                                break;

                            case 5:
                                ;
                                System.out.println("Exiting ...  -___-");
                                return;

                            default:
                                System.out.println("Your choice is wrong, please try again ");
                        }
                    }
                case 2:;
                    TransportMangerUI ui2 = new TransportMangerUI();
                    ui2.Start_System();
            }
        }
    }
}
