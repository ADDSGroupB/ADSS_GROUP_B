package PresentationLayer.CLI;

import BuisnessLayer.HR.JobType;
import BuisnessLayer.HR.Pair;
import BuisnessLayer.HR.Shift;
import ServiceLayer.HR.CompanyService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class HRUI {

    CompanyService service;
    //DataBase dataBase;

    public HRUI() {
        service = new CompanyService();
    }

    // main menu
    /* menu for user that allowing to register as employee or HR or exit
     * this menu calling to HR or employee  menu accordingly */
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello! Welcome to supermarket Lee!");
        int choice;
        while (true) {
            System.out.println("Enter your choice: ");
//            System.out.println("1. Login as HR manager");
//            System.out.println("2. Login as employee");
            System.out.println("1. Reset system assignment 1");
            System.out.println("2. Reset system assignment 2");
            System.out.println("3. Exit");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, please enter 1-5.");
                scanner.next();
                continue;
            }
            boolean checkLogIn;
            switch (choice) {
//                case 1:
//                    checkLogIn = logInHR();
//                    if (checkLogIn) {
//                        hrMenu();
//                    }
//                    break;
//                case 2:
//                    Pair<Boolean, String> returnValues = logInEmployee();
//                    if (returnValues == null)
//                        break;
//                    checkLogIn = returnValues.getKey();
//                    String employeeID = returnValues.getValue();
//                    if (checkLogIn) {
//                        employeeMenu(employeeID);
//                    }
//                    break;
                case 1:
                    service.resetDataBaseAssignment1();
                    break;
                case 2:
                    service.resetDataBaseAssignment2();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, please enter 1-3.");
                    break;
            }
        }
    }



    public void hrMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        while (true) {
            int choice3 = 0;
            int choice2 = 0;
            System.out.println("Enter your choice: ");
            System.out.println("1. Register new employee");
            System.out.println("2. Register new driver");
            System.out.println("3. Edit employee details");
            System.out.println("4. Drivers salary report");
            System.out.println("5. Choose a branch");
            System.out.println("6. Exit");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, please enter 1-6.");
                scanner.next();
                continue;
            }
            switch (choice) {
                case 1:
                    askForEmployeeDetailsToAdd("E");
                    break;
                case 2:
                    askForEmployeeDetailsToAdd("D");
                    break;
                case 3:
                    // add and remove job type to employee, set salary, add personal details, change bank account
                    while (choice3 != 8) {
                        System.out.println("Enter your choice: ");
                        System.out.println("1. Add job authorization");
                        System.out.println("2. Remove job authorization");
                        System.out.println("3. Change salary per hour");
                        System.out.println("4. Change back account details");
                        System.out.println("5. Add personal details");
                        System.out.println("6. Add bonus to salary");
                        System.out.println("7. Edit driver's details");
                        System.out.println("8. Exit");
                        try {
                            choice3 = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid choice, please enter 1 - 8");
                            scanner.next();
                            continue;
                        }
                        switch (choice3) {
                            case 1:
                                AddJobTypeToEmployee();
                                break;
                            case 2:
                                removeJobTypeFromEmployee();
                                break;
                            case 3:
                                setSalaryPerHourForEmployee();
                                break;
                            case 4:
                                setBankAccountForEmployee();
                                break;
                            case 5:
                                setPersonalDetailsForEmployee();
                                break;
                            case 6:
                                addBonusToEmployee();
                                break;
                            case 7:
                                while (choice2 != 3) {
                                    System.out.println("Enter your choice: ");
                                    System.out.println("1. Change driver's license");
                                    System.out.println("2. Change driver's max weight allowed");
                                    System.out.println("3. Exit");
                                    try {
                                        choice2 = scanner.nextInt();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid choice, please enter 1 - 8");
                                        scanner.next();
                                        continue;
                                    }
                                    switch (choice2) {
                                        case 1:
                                            updateDriverLicense();
                                            break;
                                        case 2:
                                            updateDriverMaxWeightAllowed();
                                            break;
                                        case 3:
                                            break;
                                        default:
                                            System.out.println("Invalid choice, please enter 1-3.");
                                            break;
                                    }
                                }
                                break;
                            case 8:
                                break;
                            default:
                                System.out.println("Invalid choice, please enter 1-8.");
                                break;
                        }
                    }
                    break;
                case 4:
                    driversSalaryReport();
                    break;
                case 5:
                    String branchToAdd = "0";
                    while (!(branchToAdd.matches("^[1-9]$") || branchToAdd.matches("10"))) {
                        System.out.print("On what branch of the supermarket do you want to work? (1-10) : ");
                        branchToAdd = scanner.next();
                    }
                    int branchInt = Integer.parseInt(branchToAdd) - 1;
                    hrBranchMenu(branchInt);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice, please enter 1-6.");
                    break;
            }
        }
    }

    public void hrBranchMenu(int branch) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        while (true) {
            int choice1 = 0;
            int choice2 = 0;
            System.out.println("Enter your choice: ");
            System.out.println("1. Create shifts for next week");
            System.out.println("2. Assign/remove employee from shift");
            System.out.println("3. Edit shift details");
            System.out.println("4. Display next week's shifts details");
            System.out.println("5. Cancelled product");
            System.out.println("6. Employees salary report");
            System.out.println("7. Display all employees");
            System.out.println("8. Check if a shift is valid");
            System.out.println("9. Exit");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, please enter 1 - 9");
                scanner.next();
                continue;
            }
            switch (choice) {
                case 1:
                    createNewShiftsForNextWeek(branch);
                    break;
                case 2:
                    editAShift(branch);
                    break;
                case 3:
                    // edit shift hour, edit shift job count
                    while (choice1 != 3) {
                        System.out.println("Enter your choice: ");
                        System.out.println("1. Change a shift's hours");
                        System.out.println("2. Change a shift's jobs count");
                        System.out.println("3. Exit");
                        try {
                            choice1 = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid choice, please enter 1 - 3");
                            scanner.next();
                            continue;
                        }
                        switch (choice1) {
                            case 1:
                                changeShiftTime(branch);
                                break;
                            case 2:
                                changeJobTypeCount(branch);
                            case 3:
                                break;
                            default:
                                System.out.println("Invalid choice, please enter 1, 2 or 3.");
                                break;
                        }
                    }
                    break;
                case 4:
                    displayAllShiftsDetails(branch);
                    break;
                case 5:
                    // add, remove, display all cancelled products
                    while (choice2 != 4){
                        System.out.println("Enter your choice: ");
                        System.out.println("1. Display cancellation products");
                        System.out.println("2. Add cancelled product");
                        System.out.println("3. Remove cancelled product");
                        System.out.println("4. Exit");
                        try {
                            choice2 = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid choice, please enter 1 - 4");
                            scanner.next();
                            continue;
                        }
                        switch (choice2) {
                            case 1:
                                displayCashierCancellation(branch);
                                break;
                            case 2:
                                addOrDeleteCashierCancellation(branch, "A");
                                break;
                            case 3:
                                addOrDeleteCashierCancellation(branch, "D");
                                break;
                            case 4:
                                break;
                            default:
                                System.out.println("Invalid choice, please enter 1-4.");
                                break;
                        }
                    }
                    break;
                case 6:
                    SalaryReportForAllEmployee(branch);
                    break;
                case 7:
                    displayAllRegisterEmployee(branch);
                    break;
                case 8:
                    isShiftValid(branch);
                    break;
                case 9:
                    return;
                default:
                    System.out.println("Invalid choice, please enter 1-9.");
                    break;
            }

        }
    }

    //-----------------------------------------function for the UI layer---------------------------------------------//


    //---------------------------------------------Register functions------------------------------------------------//


    /* function that ask for employee details */
    public void askForEmployeeDetailsToAdd(String DorE){
        //collecting details about the new employee and check the validation od the details
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter employee's full name: ");
        String employeeName = scanner.nextLine();
        String employeeID = getEmployeeId();
        // check if this employee is already in the company
        try{
            service.checkIfEmployeesInCompany(employeeID);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.print("Enter the employee's bank information: ");
        String bankInformation = scanner.nextLine();
        while (bankInformation.isEmpty()) {
            System.out.print("Bank account details cannot be empty, please enter again: ");
            bankInformation = scanner.nextLine();
        }
        System.out.print("Enter the employee's start of employment date: ");
        String startOfEmploymentDate = scanner.nextLine();
        System.out.print("Enter the employee's salary per hour: ");
        String salaryPerHourString = scanner.nextLine();
        int salaryPerHour = getPositiveInt(salaryPerHourString);
        System.out.print("Enter the employee's terms of employment: ");
        String termsOfEmployment = scanner.nextLine();
        System.out.print("Enter the employee's personal details: ");
        String employeePersonalDetails = scanner.nextLine();
        System.out.print("Enter the employee's password for the system: ");
        String employeePassword = scanner.nextLine();

        // get more details and register new employee
        if(DorE.equals("E")) {
            getRegisterEmployeeDetails(employeeName, employeeID, bankInformation, startOfEmploymentDate, salaryPerHour,
                    employeePersonalDetails, termsOfEmployment, employeePassword);
        }
        else{
            getRegisterDriverDetails(employeeName, employeeID, bankInformation, startOfEmploymentDate, salaryPerHour,
                    employeePersonalDetails, termsOfEmployment, employeePassword);
        }
        System.out.println("The employee has been registered");
    }

    /* function that ask for driver details */
    public void getRegisterDriverDetails(String employeeName, String employeeID, String bankInformation,
                                         String startOfEmploymentDate, int salaryPerHour, String employeePersonalDetails,
                                         String termsOfEmployment, String employeePassword){
        double max_weight_allowed_insert_for_new_driver = getDriversMaxWeightAllowed();
        int driver_license_insert_for_new_driver = getDriversLicense();
        service.registerNewDriver(max_weight_allowed_insert_for_new_driver, driver_license_insert_for_new_driver, employeeName, employeeID,
                bankInformation, startOfEmploymentDate, salaryPerHour,
                employeePersonalDetails, termsOfEmployment, employeePassword);
    }

    public double getDriversMaxWeightAllowed(){
        double max_weight_allowed_insert_for_new_driver;
        while (true) {
            System.out.println("Please enter driver max weight allowed: [1500,2250,3000] ");
            Scanner console27 = new Scanner(System.in);
            try {
                max_weight_allowed_insert_for_new_driver = console27.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter a valid number");
                continue;
            }

            if (max_weight_allowed_insert_for_new_driver != 1500 && max_weight_allowed_insert_for_new_driver != 2250 && max_weight_allowed_insert_for_new_driver != 3000) {
                System.out.println("Driver's max weight allowed can only be 1500 / 2250 / 3000");

            } else break;
        }
        return max_weight_allowed_insert_for_new_driver;
    }

    public int getDriversLicense(){
        int driver_license_insert_for_new_driver;
        /* asking for all the details for the driver */
        while (true) {
            System.out.println("Please enter driver license [1/2/3]: ");
            Scanner console28 = new Scanner(System.in);

            try {
                driver_license_insert_for_new_driver = console28.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter a valid integer");
                continue;
            }

            if (driver_license_insert_for_new_driver != 1 && driver_license_insert_for_new_driver != 2 && driver_license_insert_for_new_driver != 3) {
                System.out.println("Driver license can only be 1 / 2 / 3.");
            } else break;
        }
        return driver_license_insert_for_new_driver;
    }

    /* register employee add to wanted branches :
        - when creating the employee, always put in the jobType general worker
        - jobTypes - allow only from the enum  */
    public void getRegisterEmployeeDetails(String employeeName, String employeeID, String bankInformation,
                                           String startOfEmploymentDate, int salaryPerHour, String employeePersonalDetails,
                                           String termsOfEmployment, String employeePassword) {
        Scanner scanner = new Scanner(System.in);
        // asking for the authorized jobs of the employee, add general employee to everyone as default
        ArrayList<JobType> employeesAuthorizedJobs = new ArrayList<>();
        for (JobType jobType : JobType.values()) {
            if (jobType.equals(JobType.GENERAL_EMPLOYEE)) {
                employeesAuthorizedJobs.add(jobType);
            } else {
                System.out.printf("Is the employee authorized to be a %s ? - (y/n) : ", jobType);
                String employeeJobType = scanner.nextLine();
                while (!(employeeJobType.equals("y") || employeeJobType.equals("n"))) {
                    System.out.println("You have entered an invalid answer, please enter (y/n) : ");
                    employeeJobType = scanner.nextLine();
                }
                if (employeeJobType.equals("y")) {
                    employeesAuthorizedJobs.add(jobType);
                }
            }
        }
        // add employee to branches
        Set<Integer> branchList = getBranchesOfEmployee(employeeID);
        // register new employee to company
        service.registerNewEmployee(employeeName, employeeID, bankInformation, startOfEmploymentDate, salaryPerHour,
                employeePersonalDetails, employeesAuthorizedJobs, termsOfEmployment, employeePassword, branchList);

    }

    /* function that get branches number and add the employee to those branches*/
    public Set<Integer> getBranchesOfEmployee(String employeeID){
        Set<Integer> branchList = new HashSet<>();
        Scanner scanner = new Scanner(System.in);
        // add this employee to all branches he belongs
        String branchToAdd = "0";
        String flag = "y";
        while(flag.equals("y")){
            while(!(branchToAdd.matches("^[1-9]$") || branchToAdd.matches("10"))){
                System.out.print("To what branches of the supermarket do you want to add this employee? (1-10) : ");
                branchToAdd = scanner.nextLine();
                if(!(branchToAdd.matches("^[1-9]$") || branchToAdd.matches("10")))
                    System.out.println("You have entered an invalid answer, please enter number in 1-10 : ");
            }
            int branchInt = Integer.parseInt(branchToAdd) - 1;
            service.addEmployeeToBranch(employeeID, branchInt);
            branchList.add(branchInt + 1);
            branchToAdd = "0";
            System.out.print("Do you want to add to another branch? (y/n) : ");
            flag = scanner.nextLine();
            while (!(flag.equals("y") || flag.equals("n"))) {
                System.out.println("You have entered an invalid answer, please enter (y/n) : ");
                flag = scanner.nextLine();
            }
        }
        return branchList;
    }


    // -------------------------------------------Driver functions--------------------------------------------------//

    public void updateDriverMaxWeightAllowed(){
        String driverID = getEmployeeId();
        double max_weight_allowed_insert_for_new_driver = getDriversMaxWeightAllowed();
        try {
            service.updateDriverMaxWeightAllowed(driverID, max_weight_allowed_insert_for_new_driver);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    public void updateDriverLicense(){
        String driverID = getEmployeeId();
        int driver_license_insert_for_new_driver = getDriversLicense();
        try {
            service.updateDriverLicense(driverID, driver_license_insert_for_new_driver);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }


    // ----------------------------------------------Employee functions--------------------------------------------------//


    public void setSalaryPerHourForEmployee(){
        String employeeID = getEmployeeId();
        //ask for the salary to change
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new employee's salary per hour: ");
        String salaryPerHourString = scanner.nextLine();
        int newSalaryPerHour = getPositiveInt(salaryPerHourString);
        service.setSalaryPerHourForEmployee(employeeID, newSalaryPerHour);
    }

    public void setPersonalDetailsForEmployee(){
        String employeeID = getEmployeeId();
        //ask for the personal details to add
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the employee's personal details to add : ");
        String newEmployeePersonalDetails = scanner.nextLine();
        service.setPersonalDetailsForEmployee(employeeID, newEmployeePersonalDetails);
    }

    public void setBankAccountForEmployee(){
        String employeeID = getEmployeeId();
        //ask for the personal details to add
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new employee's bank information: ");
        String newBankInformation = scanner.nextLine();
        while (newBankInformation.isEmpty()) {
            System.out.print("Bank account details cannot be empty, please enter again: ");
            newBankInformation = scanner.nextLine();
        }
        service.setBankAccountForEmployee(employeeID, newBankInformation);
    }

    /* function to add a job type to a specific employee */
    public void AddJobTypeToEmployee() {
        // get employee
        String employeeID = getEmployeeId();
        try{
            service.checkIfEmployeeExist(employeeID);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return;
        }
        // display jobs
        service.displayEmployeesJobs(employeeID);
        // get job
        String jobTypeString = getJobType();
        // add job and display jobs
        service.AddJobTypeToEmployee(employeeID,jobTypeString);
        // display jobs
        service.displayEmployeesJobs(employeeID);
    }

    /* function to remove a job type to a specific employee */
    public void removeJobTypeFromEmployee() {
        // get employee
        String employeeID = getEmployeeId();
        try{
            service.checkIfEmployeeExist(employeeID);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return;
        }

        // display jobs
        try {
            service.displayEmployeesJobs(employeeID);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return;
        }
        String jobTypeToRemove = getJobType();
        try{
            service.removeJobTypeFromEmployee(employeeID, jobTypeToRemove);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return;
        }
        // display jobs
        try {
            service.displayEmployeesJobs(employeeID);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return;
        }
    }

    // ----------------------------------------------Shift functions--------------------------------------------------//

    public void createNewShiftsForNextWeek(int branch){
        try{
            service.checkIfShiftsAlreadyMade(branch);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return;
        }
        LocalDate[] datesOfWeek = service.currDateAndEndOfWeek(branch);
        Scanner scanner = new Scanner(System.in);
        String shiftType;
        LocalDate shiftDate = datesOfWeek[0];
        // ask for the time of the morning and evening shifts for the week
        String startTimeOfShiftMorning = null;
        String endTimeOfShiftMorning = null;
        String startTimeOfShiftEvening = null;
        String endTimeOfShiftEvening = null;
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                shiftType = "morning";
            } else {
                shiftType = "evening";
            }
            System.out.printf("Please enter the following details for this week's %s shifts : \n", shiftType);
            String[] result = getHoursForShift();
            if (i == 0) {
                startTimeOfShiftMorning = result[0];
                endTimeOfShiftMorning = result[1];
            } else {
                startTimeOfShiftEvening = result[0];
                endTimeOfShiftEvening = result[1];
            }
        }
        // while not all shifts of the week were creating:
        // loop all the days in this week and create shifts
        while (shiftDate.isBefore(datesOfWeek[1])) {
            // collect details from HR manager about the shifts for this day
            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    shiftType = "morning";
                } else {
                    shiftType = "evening";
                }
                System.out.printf("Do you want to cancel %s %s shift ? - (y/n) : ", shiftDate, shiftType);
                String shiftCancelledYrN = scanner.nextLine();
                while (!(shiftCancelledYrN.equals("y") || shiftCancelledYrN.equals("n"))) {
                    System.out.println("You have entered an invalid answer, please enter (y/n) : ");
                    shiftCancelledYrN = scanner.nextLine();
                }
                // if the shift is close so don't create this shift
                if (shiftCancelledYrN.equals("y")) {
                    continue;
                }
                // asking for the count for each jobs of employee
                String temp;
                Integer cashierCount = 0, storeKeeperCount = 0, generalEmployeeCount = 0, securityCount = 0, usherCount = 0, cleanerCount = 0, shiftManagerCount = 0;
                System.out.print("Enter the number of cashier workers you want in this shift : ");
                temp = scanner.nextLine();
                cashierCount = getPositiveInt(temp);
                System.out.print("Enter the number of storeKeeper workers you want in this shift : ");
                temp = scanner.nextLine();
                storeKeeperCount = getPositiveInt(temp);
                System.out.print("Enter the number of generalEmployee workers you want in this shift : ");
                temp = scanner.nextLine();
                generalEmployeeCount = getPositiveInt(temp);
                System.out.print("Enter the number of security workers you want in this shift : ");
                temp = scanner.nextLine();
                securityCount = getPositiveInt(temp);
                System.out.print("Enter the number of usher workers you want in this shift : ");
                temp = scanner.nextLine();
                usherCount = getPositiveInt(temp);
                System.out.print("Enter the number of cleaner workers you want in this shift : ");
                temp = scanner.nextLine();
                cleanerCount = getPositiveInt(temp);
                System.out.print("Enter the number of shift manager workers you want in this shift : ");
                temp = scanner.nextLine();
                shiftManagerCount = getPositiveInt(temp);
                while(shiftManagerCount < 1){
                    System.out.println("There has to be at least 1 shift manager, please enter again : ");
                    temp = scanner.nextLine();
                    shiftManagerCount = getPositiveInt(temp);
                }
                service.createNewShiftsForNextWeek(shiftType, shiftDate, startTimeOfShiftMorning, endTimeOfShiftMorning, startTimeOfShiftEvening, endTimeOfShiftEvening,
                        cashierCount, storeKeeperCount, generalEmployeeCount, securityCount, usherCount, cleanerCount, shiftManagerCount, branch);

            }
            shiftDate = shiftDate.plusDays(1);
        }
    }

    /* function to change the number of employees of each job type */
    public void changeJobTypeCount(int branch) {
        Scanner scanner = new Scanner(System.in);
        Pair<String, String> returnValues = null;
        returnValues =  getShiftDetails();
        String jobTypeNumberString = getJobType();
        System.out.printf("How many do you want in this shift? ");
        String jobCount = scanner.nextLine();
        int jobCountInt = getPositiveInt(jobCount);
        try{
            service.changeJobTypeCount(returnValues.getKey(), returnValues.getValue(), jobTypeNumberString, branch, jobCountInt);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    /* The function asks the Hr manager for new hours for a shift and change them */
    public void changeShiftTime(int branch) {
        // ask for shift details
        Pair<String, String> returnValues = null;
        returnValues =  getShiftDetails();
        // ask for hours
        String[] result = getHoursForShift();
        try{
            service.changeShiftTime(returnValues.getKey(), returnValues.getValue(),result, branch);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    /*  HR manager function to edit a shift:
        1. present this current shift (employees (name and  id) and jobs)
        2. ask if he wants to add or remove employee and call to the function*/
    public boolean editAShift(int branch) {
        // ask for shift details
        Pair<String, String> returnValues = null;
        returnValues =  getShiftDetails();
        // present this current shift
        try {
            System.out.println(service.displayShift(returnValues.getKey(), returnValues.getValue(), branch));
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return false;
        }
        // ask if he wants to add or remove employee and call to the function
        System.out.print("Do you want to add or remove employee from shift ? -(a/r). a-> add, r-> remove : ");
        Scanner scanner = new Scanner(System.in);
        String removeOrAdd = scanner.nextLine();
        while (!(removeOrAdd.equals("r") || removeOrAdd.equals("a"))) {
            System.out.println("You have entered an invalid answer, please enter (a/r) : ");
            removeOrAdd = scanner.nextLine();
        }
        // check the input if the HR want to add or remove employee and call to the relevant function
        if (removeOrAdd.equals("r")) {
            scanner = new Scanner(System.in);
            // if this shift is defined by HR manager
            System.out.print("Enter the employee's ID: ");
            String employeesIDString = scanner.nextLine();
            removeEmployeeFromShift(returnValues.getKey(), returnValues.getValue(), employeesIDString, branch);
        } else {
            addEmployeeToShift(returnValues.getKey(), returnValues.getValue(), branch);
        }
        return true;
    }


    /* function for adding employee to shift:
            1. ask for the job that he wants to add
            2. present all the employees with that job that can work
            3. choose employee
            4. add employee to the shift - get the employee object with the id to send to the adding function
            5. add 1 to the employee's field of shiftCountForWeek
            6. present the shift*/
    public void addEmployeeToShift(String date, String shiftType, int branch) {
        //ask for job type num
        String jobTypeNumberString = getJobType();
        // ask for shift details
        Pair<Shift, JobType> returnValues = null;
        try{
            returnValues =  service.checkJobToAdd(date, shiftType, jobTypeNumberString, branch);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return;
        }
        //display available employees
        displayAvailableEmployeesForJobType(date, shiftType, jobTypeNumberString, branch);
        // choose employee
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter employee's ID you want to assign: ");
        String employeesIDString = scanner.nextLine();
        try{
            service.addEmployeeToShift(employeesIDString, returnValues.getValue(), returnValues.getKey(), branch);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return;
        }
        try {
            // present the shift
            System.out.println(service.displayShift(date, shiftType, branch));
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    /*  function for deleting employee from shift:
        ask for the employee id to delete, delete employee and present the shift */
    public boolean removeEmployeeFromShift(String date, String shiftType, String employeesIDString, int branch) {
        try{
            service.removeEmployeeFromShift(date, shiftType, employeesIDString, branch);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return false;
        }
        // present the shift
        try {
            System.out.println(service.displayShift(date, shiftType, branch));
        }
        catch(RuntimeException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    // ask for shift to check if is valid - has the wanted count of employees assignment
    public void isShiftValid(int branch){
        // ask for shift details
        Pair<String, String> returnValues = null;
        returnValues =  getShiftDetails();
        try {
            service.isShiftValid(returnValues.getKey(), returnValues.getValue(), branch);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("The shift is valid.");
    }

    // ---------------------------------------Cancellation data functions---------------------------------------------//

    /* display all cancellation in cashier in specific shift:employees and products they cancelled*/
    public void displayCashierCancellation(int branch){
        // ask for shift details
        Pair<String, String> shiftDetails = null;
        shiftDetails =  getShiftDetails();
        try {
            System.out.println(service.displayCashierCancellation(shiftDetails.getKey(), shiftDetails.getValue(), branch));
        }
        catch(RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    /* function for add or deleting cashier cancellation - by the shift manager*/
    public void addOrDeleteCashierCancellation(int branch, String addOrDelete) {
        Pair<String, String> shiftDetails = getShiftDetails();
        Pair<String, String> cashierCancellationData = getCashierCancellationData();
        try {
            service.addOrDeleteCashierCancellation(shiftDetails.getKey(), shiftDetails.getValue(),
                    cashierCancellationData.getValue(), cashierCancellationData.getKey(), branch, addOrDelete);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addBonusToEmployee(){
        String employeeID = getEmployeeId();
        Scanner scanner = new Scanner(System.in);
        int bonusToAdd = 0;
        // add the bonus to employee
        System.out.print("How much bonus do you want to add ? ");
        String bonus = scanner.nextLine();
        bonusToAdd = getPositiveInt(bonus);
        try{
            service.addBonusToEmployee(employeeID, bonusToAdd);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    //----------------------------------------------Login function--------------------------------------------------//

//    /* Login function for employee */
//    public Pair<Boolean, String> logInEmployee(){
//        Scanner scanner = new Scanner(System.in);
//        String employeeID = getEmployeeId();
//        // check if this employee is not registered in the company
//        try{
//            service.checkIfEmployeesNotInCompany(employeeID);
//        }
//        catch (RuntimeException e){
//            System.out.println(e.getMessage());
//            return null;
//        }
//        System.out.print("Enter your password: ");
//        String employeePassword = scanner.nextLine();
//        // Check if this is the correct password of the employee
//        boolean loginCheck = service.CheckEmployeePassword(employeeID, employeePassword);
//        if(!loginCheck) {
//            System.out.println("You entered a wrong password");
//            return null;
//        }
//        return new Pair<>(loginCheck,employeeID);
//    }
//
//    /* Login function for HR */
//    public boolean logInHR(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter your password: ");
//        String employeePassword = scanner.nextLine();
//        if(employeePassword.equals("LN")){
//            return true;
//        }
//        System.out.println("You entered a wrong password.");
//        return false;
//    }

    //---------------------------------------------Display function---------------------------------------------------//

    /*function that presents for the manager the employees that are available to work by given job type and shift*/
    public void displayAvailableEmployeesForJobType(String date, String shiftType, String jobTypeNumberString, int branch) {
        System.out.println("Available employees : ");
        try {
            System.out.println(service.displayAvailableEmployeesForJobType(date, shiftType, jobTypeNumberString, branch));
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /* function that print all shifts details for next week*/
    public void displayAllShiftsDetails(int branch){
        System.out.println(service.displayAllShiftsDetails(branch));
    }


    public void SalaryReportForAllEmployee(int branch){
        System.out.println(service.SalaryReportForAllEmployee(branch));
    }

    public void displayAllRegisterEmployee(int branch){
        System.out.println(service.displayAllRegisterEmployee(branch));
    }

    public void driversSalaryReport(){
        System.out.println(service.driversSalaryReport());
    }


    //---------------------------------------------Helper function---------------------------------------------------//

    /* function that ask for employee ID */
    public String getEmployeeId() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the employee's ID: ");
        String employeeID = scanner.nextLine();
        /* Check if the ID is not empty and contains only numeric characters*/
        while (!employeeID.matches("^[\\d]+$")) {
            System.out.print("You have entered a wrong ID, please enter again: ");
            employeeID = scanner.nextLine();
        }
        return employeeID;
    }

    /*function that gets a string and check if it is a positive int, if not asks for a user to enter again*/
    public Integer getPositiveInt(String str) {
        boolean validInt = false;
        int returnNum = 0;
        Scanner scanner = new Scanner(System.in);
        while (!validInt) {
            try {
                //check if it is an int
                returnNum = Integer.parseInt(str);
                // check if the int is positive
                if (returnNum < 0) {
                    System.out.print("You have entered a negative number, please enter a positive number : ");
                    str = scanner.nextLine();
                } else {
                    validInt = true;
                }
            }
            // if not an int
            catch (NumberFormatException e) {
                System.out.print("Please enter a positive number : ");
                str = scanner.nextLine();
            }
        }
        return returnNum;
    }

    /* function that ask for shift details*/
    public Pair<String, String>getShiftDetails() {
        // ask the user for the date of the wanted shift and check that the date is according to the format
        System.out.print("Enter the date of the shift you want (yyyy-mm-dd): ");
        Scanner scanner = new Scanner(System.in);
        String dateString = scanner.nextLine();
        while(!dateValidFormat(dateString)) {
            System.out.println("The date is Invalid format. Please enter a date in the format yyyy-mm-dd : ");
            dateString = scanner.nextLine();
        }
        // ask the user for the shift type and check that the input is correct
        System.out.print("Morning or Evening ? (m/e) : ");
        String shiftType = scanner.nextLine();
        while (!(shiftType.equals("m") || shiftType.equals("e"))) {
            System.out.println("You have entered an invalid answer, please enter (m/e) : ");
            shiftType = scanner.nextLine();
        }
        return new Pair<>(dateString,shiftType);
    }

    /* function to get job type number*/
    public String getJobType() {
        // ask for the job that he wants to add
        Scanner scanner = new Scanner(System.in);
        String jobTypeNumberString = "0";
        while (!jobTypeNumberString.matches("^[1-7]$")) {
            System.out.print("Enter the number of the job you want : \n" +
                    "1 - store keeper\n" +
                    "2 - cashier\n" +
                    "3 - shift manager\n" +
                    "4 - general employee\n" +
                    "5 - security\n" +
                    "6 - usher\n" +
                    "7 - cleaner\n");
            jobTypeNumberString = scanner.nextLine();
        }
        return jobTypeNumberString;
    }

    public boolean dateValidFormat(String date){
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /* function that ask the Hr manager for start and end time for a shift*/
    public String[] getHoursForShift() {
        String startTimeOfShift;
        String endTimeOfShift;
        Scanner scanner = new Scanner(System.in);
        // ask for start time
        System.out.print("Start time of the shift in the format hh:mm:ss :");
        startTimeOfShift = scanner.nextLine();

        while (!hourValidFormat(startTimeOfShift)) {
            System.out.println("The time is Invalid format. Please enter time in the format hh:mm:ss : ");
            startTimeOfShift = scanner.nextLine();
        }
        // ask for end time
        System.out.print("End time of the shift in the format hh:mm:ss :");
        endTimeOfShift = scanner.nextLine();

        while (!hourValidFormat(endTimeOfShift)) {
            System.out.println("The time is Invalid format. Please enter time in the format hh:mm:ss : ");
            endTimeOfShift = scanner.nextLine();
        }
        return new String[]{startTimeOfShift, endTimeOfShift};
    }

    public boolean hourValidFormat(String hour){
        try {
            LocalTime.parse(hour);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /* function that get employee that canceled and product */
    public Pair<String, String> getCashierCancellationData() {
        Scanner scanner = new Scanner(System.in);
        String employeeID = getEmployeeId();
        System.out.print("Enter the barcode of the cancelled product: ");
        String product = scanner.nextLine();
        return new Pair<>(product, employeeID);
    }

}
