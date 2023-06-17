package BuisnessLayer.HR;

import BuisnessLayer.TransportManager.*;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.*;

public class CompanyController {
    public ArrayList<Branch> branchesList;
    public EmployeeController employeeController;
    public DriverController driverController;

    public CompanyController() {
        employeeController = new EmployeeController();
        driverController = new DriverController();
        branchesList = new ArrayList<>(10);
        branchesList.add(new Branch(1));
        branchesList.add(new Branch(2));
        branchesList.add(new Branch(3));
        branchesList.add(new Branch(4));
        branchesList.add(new Branch(5));
        branchesList.add(new Branch(6));
        branchesList.add(new Branch(7));
        branchesList.add(new Branch(8));
        branchesList.add(new Branch(9));
        branchesList.add(new Branch(10));
    }

    /* register employee add to wanted branches :
        - when creating the employee, always put in the jobType general worker
        - jobTypes - allow only from the enum  */
    public boolean registerNewEmployee(String employeeName, String employeeID, String bankInformation,
                                       String startOfEmploymentDate, int salaryPerHour, String employeePersonalDetails, ArrayList<JobType> employeesAuthorizedJobs,
                                       String termsOfEmployment, String employeePassword, Set<Integer> branch) {
        // create this new employee
        Employee registerNewEmployee = new Employee(employeeName, employeeID, bankInformation, startOfEmploymentDate,
                salaryPerHour, employeePersonalDetails, employeesAuthorizedJobs,
                termsOfEmployment, employeePassword, branch);
        try {
            this.employeeController.employeeDAO.insert(registerNewEmployee);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return true;
    }

    /* function that add employee to branch*/
    public void addEmployeeToBranch(String employeeID, int branch) {
        Employee employee = null;
        try {
            employee = this.employeeController.employeeDAO.getEmployeeByID(employeeID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        if(employee != null) {
            employee.branches.add(branch);
        }
    }

    /* register driver add to the company:
    - when creating the employee, always put in the jobType general worker
    - jobTypes - allow only from the enum  */
    public boolean registerNewDriver(double max_weight_allowed_insert_for_new_driver, int driver_license_insert_for_new_driver,
                                     String employeeName, String employeeID, String bankInformation, String startOfEmploymentDate,
                                     int salaryPerHour, String employeePersonalDetails, String termsOfEmployment, String employeePassword) {
        ArrayList<JobType> driverJobs = new ArrayList<>();
        Set<Integer> branches = new HashSet<>();
        Employee newDriver = new Driver(max_weight_allowed_insert_for_new_driver, driver_license_insert_for_new_driver, employeeName,
                employeeID, bankInformation, startOfEmploymentDate, salaryPerHour,
                employeePersonalDetails, driverJobs, termsOfEmployment, employeePassword, branches);
        driverController.addRegisteredDriver(newDriver);
        return true;
    }

    /* Login function-check password for employee */
    public boolean CheckEmployeePassword(String employeeID, String employeePassword) {
        Employee employee = null;
        Driver driver = null;
        try {
            driver = this.driverController.driverDAO.getDriverByID(employeeID);
            employee = this.employeeController.employeeDAO.getEmployeeByID(employeeID);
            if(employee!=null) {
                return employee.getEmployeePassword().equals(employeePassword);
            }
            if(driver!=null){
                return driver.getEmployeePassword().equals(employeePassword);
            }
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return false;
    }

    /* function for employees to submit a shift they can work at and add the shift to the employee's shiftSubmission
        the function checks that it is before thursday 12:00 when the shift submission for next week is closing
        if the shift doesn't exist, the employee can't submit */
    public void submitAShiftForEmployee(String employeeId, String date, String shiftType) {
        ShiftType shiftTypeToSubmit;
        if (shiftType.equals("m")) {
            shiftTypeToSubmit = ShiftType.MORNING;
        }
        else {
            shiftTypeToSubmit = ShiftType.EVENING;
        }
        LocalDate shiftDate = LocalDate.parse(date);
        //check if the date of the shift is only in the next week sunday - saturday
        LocalDate nowDate = LocalDate.now();
        LocalDate startOfWeek = nowDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        //if not a valid date
        if (!(!shiftDate.isAfter(endOfWeek) && !shiftDate.isBefore(startOfWeek))) {
            throw new RuntimeException("You can only " + "submit shifts for the next week");
        }
        // add the shift to the submitting shifts
        // check for driver of employee
        if (driverController.getDriverInCompany(employeeId) != null) {
            Driver wantedDriver = driverController.getDriverInCompany(employeeId);
            wantedDriver.addShiftToShiftTypeMap(shiftDate, shiftTypeToSubmit);
            try {
                this.driverController.driverDAO.update(wantedDriver);
            }
            catch (SQLException e){
                System.out.println("SQL exception");
            }
        } else {
            Employee wantedEmployee = employeeController.getEmployeeById(employeeId);
            wantedEmployee.addShiftToShiftTypeMap(shiftDate, shiftTypeToSubmit);
            try {
                this.employeeController.employeeDAO.update(wantedEmployee);
            }
            catch (SQLException e){
                System.out.println("SQL exception");
            }
        }
    }

    /*function that check for specific branch if there is storekeeper assign for transport*/
    public boolean checkIfStoreKeeperAssigned(int site_id, LocalDate date, LocalTime time) {
        Branch branch = branchesList.get(site_id-1);
        //check if the hours of the branch suit to the transport
        Shift shift = branch.getShiftByDateAndTime(date, time);
        if(shift!=null) {
            return branch.shiftManagement.checkIfStoreKeeperAssignedInBranch(date, time);
        }
        return false;
    }

    public String displayAvailableDrivers(LocalDate date, LocalTime time, double weight, int license){
        return driverController.displayAllAvailableDrivers(date, time, weight, license);
    }

    public ArrayList<Driver> getAvailableDrivers(LocalDate date, LocalTime time, double weight, int license){
        return driverController.getAllAvailableDrivers(date, time, weight, license);
    }

    public boolean addDriverToShift(String driverId, LocalDate date, LocalTime time){
        return driverController.assignDriverToShift(driverId, date, time);
    }

    // check if this employee is already in the company - return true if there is employee
    public void checkIfEmployeesInCompany(String employeeID){
        Employee employee = null;
        Driver driver = null;
        try {
            employee = this.employeeController.employeeDAO.getEmployeeByID(employeeID);
            driver = this.driverController.driverDAO.getDriverByID(employeeID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        if(employee != null || driver != null){
            throw new RuntimeException("This employee is already registered");
        }
    }

    // check if this employee is already in the company - return true if there is employee
    public void checkIfEmployeesNotInCompany(String employeeID){
        Employee employee = null;
        Driver driver = null;
        try {
            driver = this.driverController.driverDAO.getDriverByID(employeeID);

            employee = this.employeeController.employeeDAO.getEmployeeByID(employeeID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        if(employee == null && driver == null){
            throw new RuntimeException("This is not a registered employee in the company");
        }
    }

    public void changeJobTypeCount(String date, String shiftType, String jobTypeNumberString, int branch, int jobCountInt) {
        branchesList.get(branch).shiftManagement.changeJobTypeCount(date, shiftType, jobTypeNumberString, jobCountInt);
    }

    public void changeShiftTime(String date, String shiftType, String[] hours, int branch) {
        branchesList.get(branch).shiftManagement.changeShiftTime(date, shiftType, hours);
    }

    public String displayShift(String date, String shiftType, int branch) {
        Map<String, Employee> registeredEmployeesInBranch = new HashMap<>();
        try {
            registeredEmployeesInBranch = this.employeeController.employeeDAO.getAllEmployeesInBranch(branch);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return branchesList.get(branch).shiftManagement.displayShift(date, shiftType, registeredEmployeesInBranch);
    }

    public List<String> getEmployeesInShift(String date, String shiftType, int branch) {
        Map<String, Employee> registeredEmployeesInBranch = new HashMap<>();
        try {
            registeredEmployeesInBranch = this.employeeController.employeeDAO.getAllEmployeesInBranch(branch);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return branchesList.get(branch).shiftManagement.displayEmployeesInShift(date, shiftType, registeredEmployeesInBranch);
    }



    public Pair<Shift, JobType> checkJobToAdd(String date, String shiftType, String jobTypeNumberString, int branch) {
        Map<String, Employee> registeredEmployeesInBranch = new HashMap<>();
        try {
            registeredEmployeesInBranch = this.employeeController.employeeDAO.getAllEmployeesInBranch(branch);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return branchesList.get(branch).shiftManagement.checkJobToAdd(date, shiftType,
                registeredEmployeesInBranch, jobTypeNumberString);
    }

    public void addEmployeeToShift(String employeesIDString, JobType wantedJob, Shift shiftToAddEmployee, int branch){
        Employee employee = null;
        try {
            employee = this.employeeController.employeeDAO.getEmployeeByID(employeesIDString);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        branchesList.get(branch).shiftManagement.addEmployeeToShift(employee, wantedJob, shiftToAddEmployee);
        try {
            if(employee!=null) {
                this.employeeController.employeeDAO.update(employee);
            }
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
    }

    public void removeEmployeeFromShift(String date, String shiftType, String employeesIDString, int branch) {
        Employee employee = null;
        try {
            employee = this.employeeController.employeeDAO.getEmployeeByID(employeesIDString);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        branchesList.get(branch).shiftManagement.removeEmployeeFromShift(date, shiftType , employee);
        try {
            if(employee!=null) {
                this.employeeController.employeeDAO.update(employee);
            }
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
    }

    public void isShiftValid(String date, String shiftType,int branch) {
        branchesList.get(branch).shiftManagement.isShiftValid(date, shiftType);
    }

//    public void setSalaryPerHourForEmployee(String employeeID, int newSalary){
//        employeeController.setSalaryPerHourForEmployee(employeeID, newSalarygetShiftByBranchDateAndType);
//    }

//    public void setPersonalDetailsForEmployee(String employeeID, String details){
//        employeeController.setPersonalDetailsForEmployee(employeeID, details);
//    }

//    public void setBankAccountForEmployee(String employeeID, String newBankInfo){
//        employeeController.setBankAccountForEmployee(employeeID, newBankInfo);
//    }

    public void checkIfEmployeeExist(String employeeID){
        checkIfEmployeesNotInCompany(employeeID);
    }

    public void displayEmployeesJobs(String employeeID){
        employeeController.displayEmployeesJobs(employeeID);
    }

    public List<String> getEmployeesJobs(String employeeID){
        return employeeController.getEmployeesJobs(employeeID);
    }

    public void AddJobTypeToEmployee(String employeeID, String jobType){
        employeeController.AddJobTypeToEmployee(employeeID, jobType);
    }

    public void removeJobTypeFromEmployee(String employeeID, String jobType){
        employeeController.removeJobTypeFromEmployee(employeeID, jobType);
    }

    public String displayCashierCancellation(String date, String shiftType, int branch){
        return branchesList.get(branch).shiftManagement.displayCashierCancellation(date, shiftType);
    }

    public void addOrDeleteCashierCancellation(String shiftDate,  String shiftType,  String employeeID,  String barcode, int branch, String addOrDelete){
        Map<String, Employee> registeredEmployeesInBranch = new HashMap<>();
        try {
            registeredEmployeesInBranch = this.employeeController.employeeDAO.getAllEmployeesInBranch(branch);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        branchesList.get(branch).shiftManagement.addOrDeleteCashierCancellation(shiftDate, shiftType, employeeID, barcode, addOrDelete, registeredEmployeesInBranch);
    }

    public String displayAllShiftsDetails(int branch){
        Map<String, Employee> registeredEmployeesInBranch = new HashMap<>();
        try {
            registeredEmployeesInBranch = this.employeeController.employeeDAO.getAllEmployeesInBranch(branch );
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return branchesList.get(branch).shiftManagement.displayAllShiftsDetails(registeredEmployeesInBranch);
    }

//    public List<String> getAllShiftsDetails(int branch) {
//        Map<String, Employee> registeredEmployeesInBranch = new HashMap<>();
//        Set<Shift> allShifts = new HashSet<>();
//        try {
//            registeredEmployeesInBranch = this.employeeController.employeeDAO.getAllEmployeesInBranch(branch);
//        } catch (SQLException e) {
//            System.out.println("SQL exception");
//        }
//
//
//        return branchesList.get(branch).shiftManagement.getAllShiftsDetails(registeredEmployeesInBranch);
//    }

    public Object[][]  getAllShiftsDetails(int branch) {
        Map<String, Employee> registeredEmployeesInBranch = new HashMap<>();
        Set<Shift> allShifts = new HashSet<>();
        try {
            registeredEmployeesInBranch = this.employeeController.employeeDAO.getAllEmployeesInBranch(branch);
        } catch (SQLException e) {
            System.out.println("SQL exception");
        }
        return branchesList.get(branch).shiftManagement.getAllShiftsDetails(registeredEmployeesInBranch);
    }

    public String displayAllRegisterEmployee(int branch){
        Map<String, Employee> registeredEmployeesInBranch = new HashMap<>();
        try {
            registeredEmployeesInBranch = this.employeeController.employeeDAO.getAllEmployeesInBranch(branch);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return branchesList.get(branch).displayAllRegisterEmployee(registeredEmployeesInBranch);
    }

    public String displayAllRegisterEmployee2(int branch){
        Map<String, Employee> registeredEmployeesInBranch = new HashMap<>();
        try {
            registeredEmployeesInBranch = this.employeeController.employeeDAO.getAllEmployeesInBranch(branch);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return branchesList.get(branch).displayAllRegisterEmployee2(registeredEmployeesInBranch);
    }


    public List<String> getAvailableEmployeesForJobType(String date, String shiftType, String jobTypeNumberString, int branch) {
        Map<String, Employee> registeredEmployeesInBranch = new HashMap<>();
        try {
            registeredEmployeesInBranch = this.employeeController.employeeDAO.getAllEmployeesInBranch(branch);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return branchesList.get(branch).shiftManagement.getAvailableEmployeesForJobType(date, shiftType, jobTypeNumberString, registeredEmployeesInBranch);
    }

    public String displayAvailableEmployeesForJobType(String date, String shiftType, String jobTypeNumberString, int branch) {
        Map<String, Employee> registeredEmployeesInBranch = new HashMap<>();
        try {
            registeredEmployeesInBranch = this.employeeController.employeeDAO.getAllEmployeesInBranch(branch);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return branchesList.get(branch).shiftManagement.displayAvailableEmployeesForJobType(date, shiftType, jobTypeNumberString,
                registeredEmployeesInBranch);
    }

    public LocalDate[] currDateAndEndOfWeek(int branch){
        return branchesList.get(branch).shiftManagement.currDateAndEndOfWeek();
    }

    public String SalaryReportForAllEmployee(int branch){
        Map<String, Employee> registeredEmployeesInBranch = new HashMap<>();
        try {
            registeredEmployeesInBranch = this.employeeController.employeeDAO.getAllEmployeesInBranch(branch);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return branchesList.get(branch).SalaryReportForAllEmployee(registeredEmployeesInBranch);
    }



    public void checkIfShiftsAlreadyMade(int branch){
        branchesList.get(branch).shiftManagement.checkIfShiftsAlreadyMade();
    }

    public void createNewShiftsForNextWeek(String shiftType, LocalDate shiftDate, String startTimeOfShiftMorning, String endTimeOfShiftMorning, String startTimeOfShiftEvening, String endTimeOfShiftEvening,
                                           int cashierCount, int storeKeeperCount, int generalEmployeeCount, int securityCount, int usherCount, int cleanerCount, int shiftManagerCount, int branch){
        branchesList.get(branch).shiftManagement.createNewShiftsForNextWeek(shiftType, shiftDate, startTimeOfShiftMorning, endTimeOfShiftMorning, startTimeOfShiftEvening, endTimeOfShiftEvening,
        cashierCount, storeKeeperCount, generalEmployeeCount, securityCount, usherCount, cleanerCount, shiftManagerCount);
    }

    public  void addBonusToEmployee(String employeeId, int bonus){
        Employee employee = null;
        Driver driver = null;
        try {
            driver = this.driverController.driverDAO.getDriverByID(employeeId);
            employee = this.employeeController.employeeDAO.getEmployeeByID(employeeId);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        if(employee == null && driver == null)
            throw new RuntimeException("This employee not in the company");
        else {
            if(employee!=null)
                employee.setBonus(bonus);
            if(driver!=null)
                driver.setBonus(bonus);
        }
        try {
            if(employee!=null)
                this.employeeController.employeeDAO.update(employee);
            if(driver!=null)
                this.driverController.driverDAO.update(driver);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
    }

    /* function to change bank account to a specific employee*/
    public void setBankAccountForEmployee(String employeeID, String newBankInfo) {
        Employee employee = null;
        Driver driver = null;
        try {
            driver = this.driverController.driverDAO.getDriverByID(employeeID);
            employee = this.employeeController.employeeDAO.getEmployeeByID(employeeID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        if(employee == null && driver == null)
            throw new RuntimeException("This employee not in the company");
        //change the BankInformation
        else {
            if(employee!=null)
                employee.setBankAccountInformation(newBankInfo);
            if(driver!=null)
                driver.setBankAccountInformation(newBankInfo);
        }
        try {
            if(employee!=null)
                this.employeeController.employeeDAO.update(employee);
            if(driver!=null)
                this.driverController.driverDAO.update(driver);
        }
        catch (SQLException e){
            System.out.println("SQL exception");;
        }
    }

    /* function that changes the salary Per hour for a specific employee */
    public void setSalaryPerHourForEmployee(String employeeID, int newSalary) {
        Employee employee = null;
        Driver driver = null;
        try {
            driver = this.driverController.driverDAO.getDriverByID(employeeID);
            employee = this.employeeController.employeeDAO.getEmployeeByID(employeeID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        if(employee == null && driver == null)
            throw new RuntimeException("This employee not in the company");
            //change the BankInformation
        else {
            if(employee!=null)
                employee.setSalaryPerHour(newSalary);
            if(driver!=null)
                driver.setSalaryPerHour(newSalary);
        }
        try {
            if(employee!=null)
                this.employeeController.employeeDAO.update(employee);
            if(driver!=null)
                this.driverController.driverDAO.update(driver);
        }
        catch (SQLException e){
            System.out.println("SQL exception");;
        }
    }

    /* function to add personal details to a specific employee*/
    public void setPersonalDetailsForEmployee(String employeeID, String details) {
        Employee employee = null;
        Driver driver = null;
        try {
            driver = this.driverController.driverDAO.getDriverByID(employeeID);
            employee = this.employeeController.employeeDAO.getEmployeeByID(employeeID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        if(employee == null && driver == null)
            throw new RuntimeException("This employee not in the company");
            //change the BankInformation
        else {
            if(employee!=null)
                employee.addEmployeePersonalDetails(details);
            if(driver!=null)
                driver.addEmployeePersonalDetails(details);
        }
        try {
            if(employee!=null)
                this.employeeController.employeeDAO.update(employee);
            if(driver!=null)
                this.driverController.driverDAO.update(driver);
        }
        catch (SQLException e){
            System.out.println("SQL exception");;
        }

    }

    public void updateDriverMaxWeightAllowed(String employeeID, double max_weight_allowed_insert_for_new_driver){
        driverController.updateDriverMaxWeightAllowed(employeeID, max_weight_allowed_insert_for_new_driver);
    }
    public void updateDriverLicense(String employeeID, int driver_license_insert_for_new_driver){
        driverController.updateDriverLicense(employeeID, driver_license_insert_for_new_driver);
    }

    public String driversSalaryReport(){
        return driverController.driversSalaryReport();
    }

    public String  getTransportsForShift(String date, String shiftType, int branch, String employeeID){
        Map<String, Employee> registeredEmployeesInBranch = new HashMap<>();
        try {
            registeredEmployeesInBranch = this.employeeController.employeeDAO.getAllEmployeesInBranch(branch - 1);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return branchesList.get(branch - 1).shiftManagement.getTransportsForShift(date, shiftType, employeeID, branch, registeredEmployeesInBranch);
    }

    public void resetDataBaseAssignment1(){
        try {
            driverController.driverDAO.delete();
            driverController.shiftDriverDAO.delete();
            employeeController.employeeDAO.delete();
            branchesList.get(0).shiftManagement.shiftDAO.delete();
        }
        catch (SQLException e){
            System.out.println("Failed while building the system: " + e.getMessage());
        }

        Set<Integer> branchSet1 = new HashSet<>();
        branchSet1.add(1);
        branchSet1.add(2);
        Set<Integer> branchSet2 = new HashSet<>();
        branchSet2.add(1);
        branchSet2.add(2);
        branchSet2.add(3);

        ArrayList<JobType> employeeJobs1 = new ArrayList<>(Arrays.asList(JobType.CASHIER, JobType.CLEANER));
        Employee employee1 = new Employee("Linoy Bitan", "1", "Bank Of Life",
                "2023-05-01", 100, "Student", employeeJobs1, "less is more", "1", branchSet1);
        ArrayList<JobType> employeeJobs2 = new ArrayList<>(Arrays.asList(JobType.SECURITY, JobType.CLEANER));
        Employee employee2 = new Employee("Netta Meiri", "2", "Bank Of Life",
                "2023-05-01", 50, "Student", employeeJobs2, "less is more", "2", branchSet1);
        ArrayList<JobType> employeeJobs3 = new ArrayList<>(Arrays.asList(JobType.SHIFT_MANAGER, JobType.STOREKEEPER));
        Employee employee3 = new Employee("Sarah Levy", "3", "Bank Of Life",
                "2023-05-01", 200, "Parent", employeeJobs3, "less is more", "3", branchSet1);
        ArrayList<JobType> employeeJobs4 = new ArrayList<>(Arrays.asList(JobType.USHER, JobType.SHIFT_MANAGER));
        Employee employee4 = new Employee("Itay Levy", "4", "Bank Of Life",
                "2023-05-01", 200, "Parent", employeeJobs4, "less is more", "4", branchSet1);
        ArrayList<JobType> employeeJobs5 = new ArrayList<>(Collections.singletonList(JobType.USHER));
        Employee employee5 = new Employee("Moshe Cohen", "5", "Bank Of Life",
                "2023-05-01", 90, "Student", employeeJobs5, "less is more", "5", branchSet1);

        try {
            employeeController.employeeDAO.insert(employee1);
            employeeController.employeeDAO.insert(employee2);
            employeeController.employeeDAO.insert(employee3);
            employeeController.employeeDAO.insert(employee4);
            employeeController.employeeDAO.insert(employee5);
        }
        catch (Exception e) {
            System.out.println("Failed to insert employees to system: " + e.getMessage());
        }

        // search for the date of sunday of that week
        LocalDate currentDate = LocalDate.now();
        LocalDate startOfWeek = currentDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        employee1.employeeSubmittingShifts.put(startOfWeek,  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
        employee2.employeeSubmittingShifts.put(startOfWeek,  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
        employee3.employeeSubmittingShifts.put(startOfWeek,  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
        employee4.employeeSubmittingShifts.put(startOfWeek,  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));

        employee1.employeeSubmittingShifts.put(startOfWeek.plusDays(1),  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
        employee2.employeeSubmittingShifts.put(startOfWeek.plusDays(1),  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
        employee3.employeeSubmittingShifts.put(startOfWeek.plusDays(1),  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
        employee4.employeeSubmittingShifts.put(startOfWeek.plusDays(1),  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
        employee5.employeeSubmittingShifts.put(startOfWeek.plusDays(1),  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));

        try {
            employeeController.employeeDAO.update(employee1);
            employeeController.employeeDAO.update(employee2);
            employeeController.employeeDAO.update(employee3);
            employeeController.employeeDAO.update(employee4);
            employeeController.employeeDAO.update(employee5);
        }
        catch (Exception e) {
            System.out.println("Failed to update employees submitting shifts to system: " + e.getMessage());
        }

        LocalTime startTimeofMorningShift = LocalTime.of(8,0);
        LocalTime endTimeofMorningShift = LocalTime.of(14,0);
        LocalTime startTimeofEveningShift = LocalTime.of(14,0);
        LocalTime endTimeofEveningShift = LocalTime.of(20,0);
        Shift shiftSundayMorning = new Shift(startOfWeek, ShiftType.MORNING, startTimeofMorningShift, endTimeofMorningShift,
                1, 0,0,0,0,1, 1);
        Shift shiftSundayEvening = new Shift(startOfWeek, ShiftType.EVENING, startTimeofEveningShift, endTimeofEveningShift,
                0, 0,0,0,0,0, 1);
        Shift shiftMondayMorning = new Shift(startOfWeek.plusDays(1), ShiftType.MORNING, startTimeofMorningShift, endTimeofMorningShift,
                0, 1,0,0,1,2, 1);

        try {
            branchesList.get(0).shiftManagement.shiftDAO.insert(1, shiftSundayMorning);
            branchesList.get(0).shiftManagement.shiftDAO.insert(1, shiftSundayEvening);
            branchesList.get(0).shiftManagement.shiftDAO.insert(1, shiftMondayMorning);

        }
        catch (Exception e) {
            System.out.println("Failed to insert shifts to system: " + e.getMessage());
        }
    }

    public void resetDataBaseAssignment2(){
        try {
            driverController.driverDAO.delete();
            driverController.shiftDriverDAO.delete();
            employeeController.employeeDAO.delete();
            branchesList.get(0).shiftManagement.shiftDAO.delete();
        }
        catch (SQLException e){
            System.out.println("Failed while building the system: " + e.getMessage());
        }

        Set<Integer> branchSet1 = new HashSet<>();
        branchSet1.add(1);
        branchSet1.add(2);
        Set<Integer> branchSet2 = new HashSet<>();
        branchSet2.add(1);
        branchSet2.add(2);
        branchSet2.add(3);

        ArrayList<JobType> employeeJobs1 = new ArrayList<>(Arrays.asList(JobType.CASHIER, JobType.CLEANER, JobType.SHIFT_MANAGER, JobType.STOREKEEPER, JobType.SECURITY));
        Employee employee1 = new Employee("Linoy Bitan", "1", "Bank Of Life",
                "2023-05-01", 100, "Student", employeeJobs1, "less is more", "1", branchSet1);
        ArrayList<JobType> employeeJobs2 = new ArrayList<>(Arrays.asList(JobType.SECURITY, JobType.CLEANER, JobType.STOREKEEPER));
        Employee employee2 = new Employee("Netta Meiri", "2", "Bank Of Life",
                "2023-05-01", 50, "Student", employeeJobs2, "less is more", "2", branchSet2);
        ArrayList<JobType> employeeJobs3 = new ArrayList<>(Arrays.asList(JobType.SHIFT_MANAGER, JobType.STOREKEEPER));
        Employee employee3 = new Employee("Sarah Levy", "3", "Bank Of Life",
                "2023-05-01", 200, "Parent", employeeJobs3, "less is more", "3", branchSet1);
        ArrayList<JobType> employeeJobs4 = new ArrayList<>(Arrays.asList(JobType.USHER, JobType.SHIFT_MANAGER));
        Employee employee4 = new Employee("Itay Levy", "4", "Bank Of Life",
                "2023-05-01", 200, "Parent", employeeJobs4, "less is more", "4", branchSet2);
        ArrayList<JobType> employeeJobs5 = new ArrayList<>(Arrays.asList(JobType.USHER, JobType.STOREKEEPER, JobType.CASHIER));
        Employee employee5 = new Employee("Moshe Cohen", "5", "Bank Of Life",
                "2023-05-01", 90, "Student", employeeJobs5, "less is more", "5", branchSet1);

        try {
            employeeController.employeeDAO.insert(employee1);
            employeeController.employeeDAO.insert(employee2);
            employeeController.employeeDAO.insert(employee3);
            employeeController.employeeDAO.insert(employee4);
            employeeController.employeeDAO.insert(employee5);
        }
        catch (Exception e) {
            System.out.println("Failed to insert employees to system: " + e.getMessage());
        }

        ArrayList<JobType> employeeJobs = new ArrayList<>();
        Set<Integer> branchSet3 = new HashSet<>();
        Driver driver1 = new Driver(3000, 2, "Harry Potter", "6", "Gringots", "2023-05-01",
                50, "wizard", employeeJobs, "only with magic", "111", branchSet3);
        Driver driver2 = new Driver(2250, 1, "Barak Obama", "7", "Hapoalim", "2023-05-01",
                50, "student", employeeJobs, "only with a smile", "222", branchSet3);
        Driver driver3 = new Driver(3000, 2, "Noa Kirel", "8", "Poalim", "2023-05-01",
                50, "Diva", employeeJobs, "only with a smile", "333", branchSet3);
        Driver driver4 = new Driver(3000, 2, "Garfield", "9", "Leumi", "2023-05-01",
                50, "Cat", employeeJobs, "only short drives", "444", branchSet3);
        Driver driver5 = new Driver(3000, 2, "Roni Duani", "10", "Bank for rich", "2023-05-01",
                50, "Diva", employeeJobs, "only good vibe", "555", branchSet3);
        try {
            driverController.driverDAO.insert(driver1);
            driverController.driverDAO.insert(driver2);
            driverController.driverDAO.insert(driver3);
            driverController.driverDAO.insert(driver4);
            driverController.driverDAO.insert(driver5);
        }
        catch (Exception e) {
            System.out.println("Failed to insert employees to system: " + e.getMessage());
        }
    }

    public boolean isEmployee(String employeeID){
        Employee employee = null;
        try {
            employee = this.employeeController.employeeDAO.getEmployeeByID(employeeID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        if(employee != null){
            return true;
        }
        return false;
    }

    public boolean isDriver(String employeeID){
        Driver driver = null;
        try {
            driver = this.driverController.driverDAO.getDriverByID(employeeID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        if(driver != null){
            return true;
        }
        return false;
    }
}

