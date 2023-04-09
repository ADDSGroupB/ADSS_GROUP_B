package BuisnessLayer;

import javafx.util.Pair;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class ShiftManagement {

    public Map<LocalDate, Set<Shift>> shiftsAssignmentForAWeek;

    public ShiftManagement() {
        this.shiftsAssignmentForAWeek = new HashMap<>();
    }

    /* HR manager function for creating shifts for a new week:
                search for the date of sunday of that week and create shifts for the whole week from sunday to friday,
                if the manager enters this function again and the shifts are already made (check if shiftsOrginizerForAWeek is not empty) return false
                while not all shifts of the week were creating:
                asking the HR for hours - start and end for the morning and evening shifts for this week, check if cancelled and the count of
                employee each job for each shift. Then add this shift to the map */
    public boolean createNewShiftsForNextWeek() {
        // search for the date of sunday of that week and create shifts for the whole week from sunday to friday
        LocalDate currentDate = LocalDate.now();
        LocalDate startOfWeek = currentDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(7); // add six days to get the end of the week
        // if the manager want to create shifts but the shifts are already made (check if shiftsOrginizerForAWeek is not empty) return false
        if (!shiftsAssignmentForAWeek.isEmpty()) {
            System.out.println("You have already defined the shifts, choose option 3 to edit");
            return false;
        }
        Scanner scanner = new Scanner(System.in);
        ShiftType shiftType;
        LocalDate shiftDate = startOfWeek;
        // ask for the time of the morning and evening shifts for the week
        LocalTime startTimeOfShiftMorning = null;
        LocalTime endTimeOfShiftMorning = null;
        LocalTime startTimeOfShiftEvening = null;
        LocalTime endTimeOfShiftEvening = null;
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                shiftType = ShiftType.MORNING;
            } else {
                shiftType = ShiftType.EVENING;
            }
            System.out.printf("Please enter the following details for this week's %s shifts : \n", shiftType);
            LocalTime[] result = getHoursForShift();
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
        while (shiftDate.isBefore(endOfWeek)) {
            // collect details from HR manager about the shifts for this day
            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    shiftType = ShiftType.MORNING;
                } else {
                    shiftType = ShiftType.EVENING;
                }
                System.out.printf("Do you want to cancel %s %s shift ? - (y/n) : ",shiftDate, shiftType);
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
                Integer cashierCount, storeKeeperCount, generalEmployeeCount, securityCount, usherCount, cleanerCount;
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

                // create this shift for this specific day and shift type
                Shift newAssignmentShiftByHrManager;
                if (i == 0) {
                    newAssignmentShiftByHrManager = new Shift(shiftDate, shiftType, startTimeOfShiftMorning, endTimeOfShiftMorning,
                            cashierCount, storeKeeperCount, generalEmployeeCount, securityCount, usherCount, cleanerCount);
                } else {
                    newAssignmentShiftByHrManager = new Shift(shiftDate, shiftType, startTimeOfShiftEvening, endTimeOfShiftEvening,
                            cashierCount, storeKeeperCount, generalEmployeeCount, securityCount, usherCount, cleanerCount);
                }

                //add to shift Assignment For AWeek
                addShiftToShiftsMap(newAssignmentShiftByHrManager, shiftDate, this.shiftsAssignmentForAWeek);
            }
            shiftDate = shiftDate.plusDays(1);
        }
        return true;
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

    /* function that ask the Hr manager for start and end time for a shift*/
    public LocalTime[] getHoursForShift() {
        LocalTime startTimeOfShift = null;
        LocalTime endTimeOfShift = null;
        Scanner scanner = new Scanner(System.in);
        // ask for start time
        System.out.print("Start time of the shift in the format hh:mm:ss :");
        String time = scanner.nextLine();

        while (startTimeOfShift == null) {
            try {
                startTimeOfShift = LocalTime.parse(time);
            } catch (DateTimeParseException e) {
                System.out.println("The time is Invalid format. Please enter time in the format hh:mm:ss : ");
                time = scanner.nextLine();
            }
        }
        // ask for end time
        System.out.print("End time of the shift in the format hh:mm:ss :");
        time = scanner.nextLine();

        while (endTimeOfShift == null) {
            try {
                endTimeOfShift = LocalTime.parse(time);
            } catch (DateTimeParseException e) {
                System.out.println("The time is Invalid format. Please enter time in the format hh:mm:ss : ");
                time = scanner.nextLine();
            }
        }
        return new LocalTime[]{startTimeOfShift, endTimeOfShift};
    }

    /* Function that collect data about shift from HR manager - date and shiftType , if the
     * shift doesn't exist, the Manager can't change and return false , else return the data
     * about the shift that collect in a pair */
    public Shift getShiftDayAndType() {
        // ask the user for the date of the wanted shift and check that the date is according to the format
        System.out.print("Enter the date of the shift you want to edit (yyyy-mm-dd): ");
        Scanner scanner = new Scanner(System.in);
        String dateString = scanner.nextLine();
        LocalDate date = null;
        while (date == null) {
            try {
                date = LocalDate.parse(dateString);
            } catch (DateTimeParseException e) {
                System.out.println("The date is Invalid format. Please enter a date in the format yyyy-mm-dd : ");
                dateString = scanner.nextLine();
            }
        }
        // ask the user for the shift type and check that the input is correct
        System.out.print("Morning or Evening ? (m/e) : ");
        String shiftType = scanner.nextLine();
        while (!(shiftType.equals("m") || shiftType.equals("e"))) {
            System.out.println("You have entered an invalid answer, please enter (m/e) : ");
            shiftType = scanner.nextLine();
        }
        ShiftType shiftTypeToSubmit;
        if (shiftType.equals("m")) {
            shiftTypeToSubmit = ShiftType.MORNING;
        } else {
            shiftTypeToSubmit = ShiftType.EVENING;
        }
        // if the shift doesn't exist, the Manager can't change
        if (!this.shiftsAssignmentForAWeek.containsKey(date)) {
            System.out.println("You didn't create the shift you are asking for");
            return null;
        }

        //find the specific shift and return the shift
        for (Shift shift : this.shiftsAssignmentForAWeek.get(date)) {
            if (shift.shiftType.equals(shiftTypeToSubmit)) {
                return shift;
            }
        }
        //the shift doesn't exist and the Manager can't change
        System.out.println("You didn't create the shift you are asking for");
        return null;
    }

    /* The function asks the Hr manager for new hours for a shift and change them */
    public boolean changeShiftTime() {
        // call to function that collect data about shift from HR manager - date and shiftType and check that the shift exist
        Shift shiftToChange = getShiftDayAndType();
        if (shiftToChange == null) {
            return false;
        }
        // change the shift's time
        LocalTime[] result = getHoursForShift();
        LocalTime startTimeOfShift = result[0];
        LocalTime endTimeOfShift = result[1];
        shiftToChange.setShiftStartTime(startTimeOfShift);
        shiftToChange.setShiftEndTime(endTimeOfShift);
        return true;
    }


    /* function that add to a map( assignment shifts) a shift that contains dateOfShift and shiftType */
    public boolean addShiftToShiftsMap(Shift newShift, LocalDate dateOfShift, Map<LocalDate, Set<Shift>> shiftsMap) {
        // add a new key (dateOfShift) with an empty list as the initial value if it doesn't exist yet
        if (!(shiftsMap.containsKey(dateOfShift))) {
            shiftsMap.put(dateOfShift, new HashSet<>());
        }
        // add the shift
        shiftsMap.get(dateOfShift).add(newShift);
        return true;
    }

    /*  HR manager function to edit a shift:
            1. present this current shift (employees (name and  id) and jobs)
            2. ask if he wants to add or remove employee and call to the function*/
    public boolean editAShift(Map<String, Employee> registeredEmployees) {
        // call to function that collect data about shift from HR manager - date and shiftType and check that the shift exist
        Shift shiftToChange = getShiftDayAndType();
        if (shiftToChange == null) {
            return false;
        }
        // present this current shift
        shiftToChange.displayShift();
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
            removeEmployeeFromShift(shiftToChange, employeesIDString);
        } else {
            addEmployeeToShift(shiftToChange, registeredEmployees);
        }
        return true;
    }

    /*  function for deleting employee from shift:
            1. ask for the employee id - search for the employee with the id to get the object to send to delete function
            2. delete employee
            5. present the shift
                    */
    public boolean removeEmployeeFromShift(Shift shiftToRemoveEmployee, String employeesIDString) {
        // find this specific employee and remove this employee from this shift
        boolean removeEmployee = shiftToRemoveEmployee.removeEmployeesAndJobsInShift(employeesIDString);
        if (!removeEmployee) {
            System.out.println("The employee you asked is not assigned to this shift.");
            displayAllShiftsDetails();
            return false;
        }
        // present the shift
        shiftToRemoveEmployee.displayShift();
        return true;
    }


    /* function for adding employee to shift:
            1. ask for the job that he wants to add
            2. present all the employees with that job that can work
            3. choose employee
            4. add employee to the shift - get the employee object with the id to send to the adding function
            5. add 1 to the employee's field of shiftCountForWeek
            6. present the shift*/
    public boolean addEmployeeToShift(Shift shiftToAddEmployee, Map<String, Employee> registeredEmployees) {
        //get job type of employee to add
        JobType wantedJob = getJobType();
        // check if HR didn't define he wants this job at creating the shift
        if(shiftToAddEmployee.wantedJobCountInShift.get(wantedJob) == 0)
        {
            System.out.println("You didn't ask for this job type for this shift.");
            return false;
        }
        // check if this job type fully staffed
        if(shiftToAddEmployee.wantedJobCountInShift.get(wantedJob).equals(shiftToAddEmployee.assignedJobCountInShift.get(wantedJob)))
        {
            System.out.println("You fully staffed this job for this shift.");
            return false;
        }
        Scanner scanner = new Scanner(System.in);
        // present all the employees with that job that can work
        if(!displayAvailableEmployeesForJobType(shiftToAddEmployee, wantedJob, registeredEmployees))
            return false;
        // choose employee
        System.out.print("Enter employee's ID you want to assign: ");
        String employeesIDString = scanner.nextLine();
        // find this employee and add this employee to this shift
        Employee find_employee = null;
        for (String employeeId : registeredEmployees.keySet()) {
            if (employeeId.equals(employeesIDString)) {
                find_employee = registeredEmployees.get(employeeId);
            }
        }
        if (find_employee == null) {
            System.out.println("The employee you searched for does not exist.");
            return false;
        }
        boolean addEmployee = checkIfEmployeeValidToWork(find_employee, shiftToAddEmployee);
        if(!addEmployee){
            System.out.println("This employee cannot work in this shift.");
            return false;
        }
        addEmployee = shiftToAddEmployee.addEmployeesAndJobsInShift(wantedJob, find_employee);
        if (!addEmployee) {
            System.out.println("The action cannot be done, you have reached the maximum of employees for this job.");
            return false;
        }
        // present the shift
        shiftToAddEmployee.displayShift();
        return true;
    }

    /*function that presents for the manager the employees that are available to work by given job type and shift*/
    public boolean displayAvailableEmployeesForJobType(Shift shift, JobType jobType, Map<String, Employee> registeredEmployees) {
        // iterate all the employees
        int flag = 0;
        System.out.println("Available employees : ");
        for (String employeeId : registeredEmployees.keySet()) {
            Employee find_employee = registeredEmployees.get(employeeId);
            // check if the employee is certified for the wanted job
            if (find_employee.employeesAuthorizedJobs.contains(jobType)) {
                // call the function to check if the employee is valid to work
                if (checkIfEmployeeValidToWork(find_employee, shift)) {
                    flag = 1;
                    System.out.printf("Name : %s, ID : %s\n", find_employee.employeeFullName, find_employee.employeeID);
                    System.out.println("--------------------------------------");
                }
            }
        }
        if (flag == 0) {
            System.out.println("There are no available employees for this shift.");
            return false;
        }
        return true;
    }

    /*function that checks if the given employee is valid to work in the given shift according to 3 constraints:
        - check if the given shift is in the given employees submitting shifts
        - check if employee is already works in other shift that day
        - check if employee is working six days */
    public boolean checkIfEmployeeValidToWork(Employee employee, Shift shift) {
        // if the employee submitted this shift
        if (employee.employeeSubmittingShifts.containsKey(shift.shiftDate) && employee.employeeSubmittingShifts.get(shift.shiftDate).contains(shift.shiftType)) {
            //check if employee is not already working in another shift that day
            if (!employee.employeesAssignmentForShifts.containsKey(shift.shiftDate)) {
                //checks if employee is working less than six days
                if (employee.shiftCountForWeek < 6) {
                    return true;
                }
            }
        }
        return false;
    }

    /* function that present all shifts for the next week - employees - name and id and their jobs */
    public void displayAllShiftsDetails() {
        for (LocalDate shiftDate : this.shiftsAssignmentForAWeek.keySet()) {
            // display hours of this day
            System.out.printf("Shift date : %s\n", shiftDate);
            Set<Shift> shitsForDay = this.shiftsAssignmentForAWeek.get(shiftDate);
            for (Shift shift : shitsForDay) {
                //print this specific shift
                shift.displayShift();
            }
            System.out.println("-----------------------------------------------------------");
        }
    }

    /*- function for adding cashier cancellation - by the shift manager*/
    public boolean addCashierCancellation() {
        //get the wanted shift
        Shift shiftToEdit = getShiftDayAndType();
        if (shiftToEdit == null) {
            return false;
        }
        // get cancellation data - employee and product
        Pair<String, Employee> returnValues = getCashierCancellationData(shiftToEdit);
        // check if there is problem with id of employee
        if(returnValues == null)
            return false;
        // add the cashier cancellation
        boolean addCancellation = shiftToEdit.addCancellationInCashBox(returnValues.getValue(), returnValues.getKey());
        if (!addCancellation) {
            System.out.println("The employee you entered is not a shift manager, other employees cannot cancel a product.");
            return false;
        }
        return true;
    }

    /* function for deleting cashier cancellation - by the shift manager*/
    public boolean deleteCashierCancellation() {
        //get the wanted shift
        Shift shiftToEdit = getShiftDayAndType();
        if (shiftToEdit == null) {
            return false;
        }
        // get cancellation data - employee and product
        Pair<String, Employee> returnValues = getCashierCancellationData(shiftToEdit);
        // check if there is problem with id of employee
        if(returnValues == null)
            return false;
        //delete the cashier cancellation
        boolean deleteCancellation = shiftToEdit.deleteCancellationInCashBox(returnValues.getValue(), returnValues.getKey());
        if (!deleteCancellation) {
            System.out.println("The employee or the product you enter do not exist in the cancellation history.");
            return false;
        }
        return true;
    }

    /* helper function that get employee that canceled and product */
    public Pair<String, Employee> getCashierCancellationData(Shift shiftToEdit) {
        Scanner scanner = new Scanner(System.in);
        // ask for the employee's ID
        System.out.print("Enter the employee's ID: ");
        String employeesIDString = scanner.nextLine();
        // find this specific employee
        Employee employeeThatCancelled = shiftToEdit.checkIfEmployeeInShift(employeesIDString);
        if (employeeThatCancelled == null) {
            System.out.println("The employee you asked is not assigned to this shift.");
            return null;
        }
        System.out.print("Enter the barcode of the cancelled product: ");
        String product = scanner.nextLine();
        return new Pair<>(product, employeeThatCancelled);
    }

    /* display all cancellation in cashier in specific shift:employees and products they cancelled*/
    public boolean displayCashierCancellation(){
        Shift shiftToEdit = getShiftDayAndType();
        if (shiftToEdit == null) {
            return false;
        }
        List<String> cancellations;
        // check if there is no cancelled products to display
        if (shiftToEdit.cancellationInCashBox.isEmpty()){
            System.out.println("There is no cancelled product yet");
            return false;
        }
        System.out.println("Product cancellations in shift: ");
        for(Employee employee : shiftToEdit.cancellationInCashBox.keySet()){
            System.out.printf("Employee ID: %s", employee.employeeID);
            cancellations = shiftToEdit.cancellationInCashBox.get(employee);
            System.out.println("Cancelled products:");
            for (String cancellation : cancellations) {
                System.out.printf("%s", cancellation);
            }
            System.out.println("-------------------------------------------------------------------------------\n");
        }
        return true;
    }

    /* function to change the number of employees of each job type */
    public void changeJobTypeCount() {
        Scanner scanner = new Scanner(System.in);
        Shift shiftToEdit = getShiftDayAndType();
        JobType wantedJob = getJobType();
        System.out.printf("How many %s do you want in this shift? ", wantedJob);
        String jobCount = scanner.nextLine();
        int jobCountInt = getPositiveInt(jobCount);
        shiftToEdit.setJobCount(wantedJob, jobCountInt);
    }

    /* function to get job type*/
    public JobType getJobType() {
        // ask for the job that he wants to add
        Scanner scanner = new Scanner(System.in);
        String jobTypeNumberString = "0";
        JobType wantedJob = null;
        while (!jobTypeNumberString.matches("^[1-7]$")) {
            System.out.print("Enter the number of the job you want to edit for this shift : \n" +
                    "1 - store keeper\n" +
                    "2 - cashier\n" +
                    "3 - shift manager\n" +
                    "4 - general employee\n" +
                    "5 - security\n" +
                    "6 - usher\n" +
                    "7 - cleaner\n");
            jobTypeNumberString = scanner.nextLine();
        }
        if (jobTypeNumberString.equals("1")) {
            wantedJob = JobType.STOREKEEPER;
        }
        if (jobTypeNumberString.equals("2")) {
            wantedJob = JobType.CASHIER;
        }
        if (jobTypeNumberString.equals("3")) {
            wantedJob = JobType.SHIFT_MANAGER;
        }
        if (jobTypeNumberString.equals("4")) {
            wantedJob = JobType.GENERAL_EMPLOYEE;
        }
        if (jobTypeNumberString.equals("5")) {
            wantedJob = JobType.SECURITY;
        }
        if (jobTypeNumberString.equals("6")) {
            wantedJob = JobType.USHER;
        }
        if (jobTypeNumberString.equals("7")) {
            wantedJob = JobType.CLEANER;
        }
        return wantedJob;
    }

    /* function that alerts if there is a shift without shift manager or not the count of employees the manager wanted 24 hours before the shift
    * this function will be used in the future with real dataBase for this HR system*/
    public void alertShiftNotValid() {
        for (LocalDate shiftDate : this.shiftsAssignmentForAWeek.keySet()) {
            Set<Shift> shitsForDay = this.shiftsAssignmentForAWeek.get(shiftDate);
            for (Shift shift : shitsForDay) {
                if (check24Hours(shift)) {
                    if (!shift.isShiftValid()) {
                        System.out.printf("The shift : %s %s is not valid.\n", shift.shiftDate, shift.shiftType);
                    }
                }
            }
        }
    }

    /*function to check if the time now is 24 hours or less before the wanted shift*/
    public boolean check24Hours(Shift shiftToChange) {
        LocalDateTime specificDateTimeOfShift = LocalDateTime.of(shiftToChange.shiftDate, shiftToChange.startTimeOfShift);
        LocalDateTime now = LocalDateTime.now();
        long hoursBetweenShiftAndNow = ChronoUnit.HOURS.between(now, specificDateTimeOfShift);
        return hoursBetweenShiftAndNow <= 24;
    }
}
