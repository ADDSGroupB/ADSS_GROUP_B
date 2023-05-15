import PresentationLayer.GeneralUI;
import PresentationLayer.HRUI;
import PresentationLayer.TransportMangerUI;

import java.sql.SQLException;

/**
 * A simple Java program that creates the entry of the system.
 * This program demonstrates object-oriented programming concepts and the use of the main method as an entry point.
 */
public class Main {

    /**
     * Entry point for the program.
     * @param args command-line arguments (not used in this program)
     */

    public static void main(String[] args) throws SQLException {

        /** Enter to the systems */
        GeneralUI generalUI = new GeneralUI();
        generalUI.StartSystem();

    }
}





