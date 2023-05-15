package BuisnessLayer.HR;

import java.util.ArrayList;
import java.util.Set;

// Define the Driver class
public class Driver extends Employee {

    // Define class attributes
    private double driver_max_weight_allowed; // The maximum weight allowed for the driver to carry
    private int driver_license; // The driver's license number

    // Define a constructor for the Driver class
    public Driver(double max_weight, int driver_license, String employeeFullName,
                  String employeeID, String bankAccountInformation, String startOfEmploymentDate, int salaryPerHour,
                  String employeePersonalDetails, ArrayList<JobType> employeesAuthorizedJobs, String termsOfEmployment,
                  String employeePassword, Set<Integer> branches) {
        super(employeeFullName, employeeID, bankAccountInformation, startOfEmploymentDate, salaryPerHour, employeePersonalDetails,
                employeesAuthorizedJobs, termsOfEmployment, employeePassword, branches);
        /*
         * Initialize a driver object with the given information.
         * Arguments:
         * driver_name (str): the name of the driver
         * driver_ID (str): the identification number of the driver
         * max_weight (int): the maximum weight allowed for the driver to carry
         * driver_license (int): the driver's license number
         */
        // Set the class attributes to the given arguments
        this.driver_max_weight_allowed = max_weight;
        this.driver_license = driver_license;
    }

    // Define a method to get a string representation of the Driver object
    @Override
    public String toString() {
        return "Driver's Name: '" + employeeFullName + '\'' +
                ", ID: = '" + employeeID + '\'' +
                ", Max weight allowed: = " + driver_max_weight_allowed +
                ", License: = " + driver_license;
    }

    // Define a method to get the driver's license number
    public int getDriver_license() {
        return driver_license;
    }


    // Define a method to get the maximum weight allowed for the driver to carry
    public double getDriver_max_weight_allowed() {
        return driver_max_weight_allowed;
    }

    // Define a method to set the maximum weight allowed for the driver to carry
    public void setDriver_max_weight_allowed(double driver_max_weight_allowed) {
        this.driver_max_weight_allowed = driver_max_weight_allowed;
    }

    // Define a method to set the driver's license number
    public void setDriver_license(int driver_license) {
        this.driver_license = driver_license;
    }


}
