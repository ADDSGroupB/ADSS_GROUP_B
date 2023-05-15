package PresentationLayer;

import java.sql.SQLException;
import java.util.Scanner;

public class GeneralUI {

    public void StartSystem() throws SQLException {

        while(true) {
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

            switch (choice_main_menu){

                case 1:;
                    /** Create an instance of the UI of HR  */
                    HRUI ui = new HRUI();
                    /** Call the Start_System method of the UI of HR to start the system */
                    ui.menu();
                    break;

                case 2:;
                    /** Create an instance of the UI of TransportManager */
                    TransportMangerUI userInterface = new TransportMangerUI();
                    /** Call the Start_System method of the UI of TransportManager to start the system */
                    userInterface.Start_System();
                    break;

                case 3:;
                    System.out.println("Exiting ...  -___-");
                    return;

                default:
                    System.out.println("Your choice is wrong, please try again ");
            }
        }
    }
}
