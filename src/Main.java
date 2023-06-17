import PresentationLayer.CLI.EmployeeUI;
import PresentationLayer.CLI.GeneralUI;
import PresentationLayer.CLI.HRUI;
import PresentationLayer.CLI.TransportMangerUI;
import PresentationLayer.GUI.HR.EmployeeFrame;
import PresentationLayer.GUI.HR.HrEmployeeMenu;
import PresentationLayer.GUI.HR.HrFrame;
import PresentationLayer.GUI.StoreManagerGui;
import PresentationLayer.GUI.TransportManager.TransportFrameMenu;
import javax.swing.*;

import java.sql.SQLException;

/**
 * A simple Java program that creates the entry of the system.
 * This program demonstrates object-oriented programming concepts and the use of the main method as an entry point.
 */
public class Main {

    /**
     * Entry point for the program - choose cli or gui.
     * @param args command-line arguments (not used in this program)
     */

    public static void main(String[] args) throws SQLException {

//        new EmployeeFrame();

        //CLI option
        if (args.length > 1 && args[0].equals("CLI")&& args[1].equals("HRManager")) {
            // Run CLI interface
            HRUI cli = new HRUI();
            cli.hrMenu();;
        }
        else if (args.length > 1 && args[0].equals("CLI") && args[1].equals("TransportManager")){
            TransportMangerUI cli = new TransportMangerUI();
            cli.Start_System();

        }
        else if (args.length > 1 && args[0].equals("CLI")&& args[1].equals("Employee")) {
            // Run GUI interface
            EmployeeUI cli = new EmployeeUI();
            cli.employeeMenu();
        }
        else if (args.length > 1 && args[0].equals("CLI")&& args[1].equals("StoreManager")) {
            // Run GUI interface
            GeneralUI cli = new GeneralUI();
            cli.StartSystem();
        }

        //GUI option
        else if (args.length > 1 && args[0].equals("GUI") && args[1].equals("TransportManager")){
            TransportFrameMenu transportFrameMenu = new TransportFrameMenu();
            transportFrameMenu.TransportFrameMenuOption();
        }
        else if (args.length > 1 && args[0].equals("GUI")&& args[1].equals("HRManager")) {
            // Run GUI interface
            HrFrame hr = new HrFrame();
            hr.hrOptions();
        }
        else if (args.length > 1 && args[0].equals("GUI")&& args[1].equals("Employee")) {
            // Run GUI interface
            new EmployeeFrame();
        }
        else if (args.length > 1 && args[0].equals("GUI")&& args[1].equals("StoreManager")) {
            StoreManagerGui StoreManagement = new StoreManagerGui();
            StoreManagement.StoreManagerGuiOptions();
        }
        else {
            System.out.println("You need to enter CLI or GUI and the job");
        }

    }
}





