package BuisnessLayer.HR;

import DataAccessLayer.HR.DriverDAO;
import DataAccessLayer.HR.ShiftDriverDAO;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DriverController {

    public DriverDAO driverDAO;
    public ShiftDriverDAO shiftDriverDAO;

    public DriverController() {
        this.driverDAO = DriverDAO.getInstance();
        this.shiftDriverDAO = ShiftDriverDAO.getInstance();
    }


    public void addRegisteredDriver(Employee driver) {
        try {
            this.driverDAO.insert((Driver) driver);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
    }

    /*function that presents for the manager the drivers that are available to work in shift*/
    public String displayAllAvailableDrivers(LocalDate date, LocalTime time, double weight, int license) {
        String output = "";
        // find this specific shift
        ShiftDriver shiftToUpdate = findShiftDriver(date, time);
        if(shiftToUpdate == null){
            LocalTime startMorning = LocalTime.of(8, 0, 0);
            LocalTime finishMorning = LocalTime.of(15, 0, 0);
            LocalTime endEvening = LocalTime.of(23, 0, 0);
            if((time.equals(startMorning) || time.isAfter(startMorning)) && (time.equals(finishMorning) || time.isBefore(finishMorning))){
                shiftToUpdate = new ShiftDriver(date, ShiftType.MORNING, startMorning, finishMorning);
            }
            if(((time.equals(finishMorning) || time.isAfter(finishMorning)) && (time.equals(endEvening) || time.isBefore(endEvening)))){
                shiftToUpdate = new ShiftDriver(date, ShiftType.EVENING, finishMorning, endEvening);
            }
            try {
                shiftDriverDAO.insert(shiftToUpdate);
            }
            catch (SQLException e){
                System.out.println("SQL exception in DriverController");
                System.out.println(e.getMessage());
            }
        }
        HashMap<String, Driver> allDrivers = new HashMap<>();
        int flag = 0;
        try {
             allDrivers = this.driverDAO.getAllDriver();
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }

        // iterate all the drivers and find available drivers to this shift
        for (String driverId : allDrivers.keySet()) {
            Driver find_driver = allDrivers.get(driverId);
            if (checkIfDriverValidToWork(find_driver, shiftToUpdate, weight, license)) {
                flag = 1;
                output += find_driver.toString();
                output += "\n---------------------------------------------------------------------";
                output+= "\n";
            }
        }
        if (flag == 0) {
            output += "There are no available employees for this shift.";
        }
        return output;
    }

    /*function that presents for the manager the drivers that are available to work in shift*/
    public ArrayList<Driver> getAllAvailableDrivers(LocalDate date, LocalTime time, double weight, int license) {
        ArrayList<Driver >drivers = new ArrayList<>();
        // find this specific shift
        ShiftDriver shiftToUpdate = findShiftDriver(date, time);
        if(shiftToUpdate == null){
            LocalTime startMorning = LocalTime.of(8, 0, 0);
            LocalTime finishMorning = LocalTime.of(15, 0, 0);
            LocalTime endEvening = LocalTime.of(23, 0, 0);
            if((time.equals(startMorning) || time.isAfter(startMorning)) && (time.equals(finishMorning) || time.isBefore(finishMorning))){
                shiftToUpdate = new ShiftDriver(date, ShiftType.MORNING, startMorning, finishMorning);
            }
            if(((time.equals(finishMorning) || time.isAfter(finishMorning)) && (time.equals(endEvening) || time.isBefore(endEvening)))){
                shiftToUpdate = new ShiftDriver(date, ShiftType.EVENING, finishMorning, endEvening);
            }
            try {
                shiftDriverDAO.insert(shiftToUpdate);
            }
            catch (SQLException e){
                System.out.println("SQL exception in DriverController");
                System.out.println(e.getMessage());
            }
        }
        HashMap<String, Driver> allDrivers = new HashMap<>();
        int flag = 0;
        try {
            allDrivers = this.driverDAO.getAllDriver();
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }

        // iterate all the drivers and find available drivers to this shift
        for (String driverId : allDrivers.keySet()) {
            Driver find_driver = allDrivers.get(driverId);
            if (checkIfDriverValidToWork(find_driver, shiftToUpdate, weight, license)) {
                drivers.add(find_driver);
            }
        }
        return drivers;
    }

    /*
        function that checks if the given driver is valid to work in the given shift according to 3 constraints:
        - check if the given shift is in the given employees submitting shifts
        - check if employee is already works in other shift that day
        - check if employee is working six days
        - check if the license of the driver is bigger or equal
        - check if the weight of the driver is bigger or equal
    */
    public boolean checkIfDriverValidToWork(Driver driver, ShiftDriver shift, double weight, int license) {
        // if the driver submitted this shift
        if (driver.employeeSubmittingShifts.containsKey(shift.shiftDate) &&
                driver.employeeSubmittingShifts.get(shift.shiftDate).contains(shift.shiftType)) {

            //check if driver is not already working in another shift that day
            if (!driver.employeesAssignmentForShifts.containsKey(shift.shiftDate)) {
                //checks if driver is working less than six days
                if (driver.shiftCountForWeek < 6) {
                    //checking the license
                    return driver.getDriver_license() >= license && driver.getDriver_max_weight_allowed() >= weight;
                }
            }
        }
        return false;
    }

    /* function that assign driver to shift by id*/
    public boolean assignDriverToShift(String driverId, LocalDate date, LocalTime time) {
        Driver driver = getDriverInCompany(driverId);
        if (driver != null) {
            ShiftDriver shiftToUpdate = findShiftDriver(date, time);
            // add this driver to this shift
            shiftToUpdate.addDriverToShift(driverId, driver);
            // add the shift to the employee's assigned shifts
            driver.addShiftToShiftsMap(date, shiftToUpdate.shiftType);
            // add 1 to shiftCountForWeek
            driver.shiftCountForWeek++;
            try {
                this.driverDAO.update(driver);
                this.shiftDriverDAO.update(shiftToUpdate);
            }
            catch (SQLException e){
                System.out.println("SQL exception");;
            }
            return true;
        }
        return false;
    }

    /*function that delete driver from shift by id*/
    public boolean removeDriverFromShift(String driverId, LocalDate date, LocalTime time) {
        ShiftDriver shiftToUpdate = findShiftDriver(date, time);
        // delete this driver from this shift
        shiftToUpdate.removeDriverFromShift(driverId);
        Driver driver = getDriverInCompany(driverId);
        // remove the shift from the employee's assigned shifts
        driver.removeShiftFromShiftsMap(date, shiftToUpdate);
        // sub 1 to shiftCountForWeek
        driver.shiftCountForWeek--;
        try {
            this.driverDAO.update(driver);
            this.shiftDriverDAO.update(shiftToUpdate);
        }
        catch (SQLException e){
            System.out.println("SQL exception");;
        }
        return true;
    }

    /*function that check if employee is a register driver in the company, if there isn't return null*/
    public Driver getDriverInCompany(String driverId) {
        try {
            return this.driverDAO.getDriverByID(driverId);
        }
        catch (SQLException e){
            System.out.println("SQL exception");;
        }
        return null;
    }

    /*function that find ShiftDriver by date and time  */

    public ShiftDriver findShiftDriver(LocalDate date, LocalTime time) {
        Set<ShiftDriver> shitsForDay = new HashSet<>();
        try {
            shitsForDay = this.shiftDriverDAO.getDriverShiftsByDate(date.toString());
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }

        ShiftDriver shiftToFind = null;
        if(shitsForDay != null) {
            for (ShiftDriver shift : shitsForDay) {
                // check if there is morning or evening shift
                if (shift.shiftDate.equals(date) && (time.isBefore(shift.endTimeOfShift) || time.equals(shift.endTimeOfShift))
                        && (time.isAfter(shift.startTimeOfShift) || time.equals(shift.startTimeOfShift))) {
                    shiftToFind = shift;
                    break;
                }
            }
        }
        return shiftToFind;
    }

    /* function that present all driver shifts for the next week - drivers - name and id */
    public void displayAllShiftsDetails() {
        HashMap<LocalDate, Set<ShiftDriver>> allDriverShifts = new HashMap<>();
        try {
            allDriverShifts = shiftDriverDAO.getAllDriverShifts();
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        for (LocalDate shiftDate : allDriverShifts.keySet()) {
            // display hours of this day
            System.out.printf("Shift date : %s\n", shiftDate);
            Set<ShiftDriver> shitsForDay = allDriverShifts.get(shiftDate);
            for (ShiftDriver shift : shitsForDay) {
                //print this specific shift
                Map<String, Driver> registeredDrivers = new HashMap<>();
                try {
                    registeredDrivers = driverDAO.getAllDriver();
                }
                catch (SQLException e){
                    System.out.println("SQL exception");
                }
                shift.displayShiftDriver(registeredDrivers);
            }
            System.out.println("-----------------------------------------------------------");
        }
    }

    /* functions to update driver details*/
    public void updateDriverMaxWeightAllowed(String employeeID, double max_weight_allowed_insert_for_new_driver) {
        Driver driver = null;
        try {
            driver = this.driverDAO.getDriverByID(employeeID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        if (driver == null)
            throw new RuntimeException("This employee not in the company / not a driver");
        else {
            driver.setDriver_max_weight_allowed(max_weight_allowed_insert_for_new_driver);
            try {
                this.driverDAO.update(driver);            }
            catch (SQLException e){
                System.out.println("SQL exception");
            }
        }
    }

    public void updateDriverLicense(String employeeID, int driver_license_insert_for_new_driver) {
        Driver driver= null;
        try {
            driver = this.driverDAO.getDriverByID(employeeID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        if (driver == null)
            throw new RuntimeException("This employee not in the company / not a driver");
        else {
            driver.setDriver_license(driver_license_insert_for_new_driver);
            try {
                this.driverDAO.update(driver);            }
            catch (SQLException e){
                System.out.println("SQL exception");
            }
        }
    }

    /* function to calculate and print the salary report for all drivers (check for each driver how many shifts and their hours and multiply the salary)*/
    public String driversSalaryReport() {
        HashMap<String, Driver>  allDrivers = new HashMap<>();
        try {
            allDrivers = driverDAO.getAllDriver();
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        String salaryReport = "";
        for (String employeeID : allDrivers.keySet()) {
            // find this employee
            Employee findEmployee = allDrivers.get(employeeID);
            // calculate salary, with bonus and return the salary report for each employee
            int salary = weekWorkHoursEmployee(findEmployee) * (findEmployee.salaryPerHour + findEmployee.getBonus());
            salaryReport += "Employee: " + findEmployee.employeeFullName + " Id: " + findEmployee.employeeID + " Salary: " + salary +"\n";
        }
        return salaryReport;
    }

    /* function that calculate total hours that driver work this week*/
    public int weekWorkHoursEmployee(Employee employee) {
        Set<ShiftDriver> shifts = null;
        int hourCounter = 0;
        for (LocalDate shiftDate : ((Driver) employee).employeesAssignmentForShifts.keySet()) {
            try {
                Set<ShiftType> shiftTypeSet = employee.employeesAssignmentForShifts.get(shiftDate);
                shifts = this.shiftDriverDAO.getDriverShiftsByDate(shiftDate.toString());
                for(ShiftDriver shift : shifts) {
                    if (shift.shiftType.equals(ShiftType.MORNING) && shiftTypeSet.contains(ShiftType.MORNING)) {
                        hourCounter += Duration.between(shift.startTimeOfShift, shift.endTimeOfShift).toHours();
                        break;
                    }
                    if(shift.shiftType.equals(ShiftType.EVENING) && shiftTypeSet.contains(ShiftType.EVENING)) {
                        hourCounter += Duration.between(shift.startTimeOfShift, shift.endTimeOfShift).toHours();
                        break;
                    }
                }
            }
            catch (SQLException e){
                System.out.println("SQL exception");
                System.out.println(e.getMessage());
            }
        }
        return hourCounter;
    }

    /*function that return set of shift by date*/
    public Set<ShiftDriver> getShiftByDate(LocalDate date) {
        try {
            return this.shiftDriverDAO.getDriverShiftsByDate(date.toString());
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return null;
    }
}
