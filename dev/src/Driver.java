// Define the Driver class
public class Driver {

    // Define class attributes
    private String driver_name; // The name of the driver
    private String driver_ID; // The identification number of the driver
    private DriversStatus driver_status; // The status of the driver, whether he is available or busy
    private int driver_max_weight_allowed; // The maximum weight allowed for the driver to carry
    private int driver_license; // The driver's license number

    // Define a constructor for the Driver class
    public Driver(String driver_name, String driver_ID, int max_weight, int driver_license) {
        /*
         * Initialize a driver object with the given information.
         * Arguments:
         * driver_name (str): the name of the driver
         * driver_ID (str): the identification number of the driver
         * max_weight (int): the maximum weight allowed for the driver to carry
         * driver_license (int): the driver's license number
         */
        // Set the class attributes to the given arguments
        this.driver_name = driver_name;
        this.driver_ID = driver_ID;
        this.driver_status = DriversStatus.Available;
        this.driver_max_weight_allowed = max_weight;
        this.driver_license = driver_license;
    }

    // Define a method to get the maximum weight allowed for the driver to carry
    public int getDriver_max_weight_allowed() {
        return driver_max_weight_allowed;
    }

    // Define a method to get the name of the driver
    public String getDriver_name() {
        return driver_name;
    }

    // Define a method to set the name of the driver
    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    // Define a method to get a string representation of the Driver object
    @Override
    public String toString() {
        return "Driver {" +
                "Driver's Name: '" + driver_name + '\'' +
                ", ID: = '" + driver_ID + '\'' +
                ", Max weight allowed: = " + driver_max_weight_allowed +
                ", License: = " + driver_license +
                ", Status: = " + driver_status +
                '}';
    }

    // Define a method to get the identification number of the driver
    public String getDriver_ID() {
        return driver_ID;
    }

    // Define a method to set the identification number of the driver
    public void setDriver_ID(String driver_ID) {
        this.driver_ID = driver_ID;
    }

    // Define a method to get the driver's license number
    public int getDriver_license() {
        return driver_license;
    }

    // Define a method to set the driver's license number
    public void setDriver_level_skill(int driver_license) {
        this.driver_license = driver_license;
    }

    // Define a method to set the maximum weight allowed for the driver to carry
    public void setDriver_max_weight_allowed(int driver_max_weight_allowed) {
        this.driver_max_weight_allowed = driver_max_weight_allowed;
    }

    // Define a method to set the driver's status to the given status
    public void setDriver_status(DriversStatus driver_status) {
        /*
         * Set the driver's status to the given status.
         * Arguments:
         * driver_status (DriversStatus): the new status of the driver
         */
        this.driver_status = driver_status;
    }

    // Define a method to get the driver's status
    public DriversStatus getDriver_status() {
        return driver_status;
    }

    // Define a method to set the driver's license number
    public void setDriver_license(int driver_license) {
        this.driver_license = driver_license;
    }
}
