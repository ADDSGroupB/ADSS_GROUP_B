package BuisnessLayer.HR;

import BuisnessLayer.TransportManager.TransportController;
import DataAccessLayer.HR.ShiftDAO;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class ShiftManagement {

    ShiftDAO shiftDAO;
    int branchID;

    public ShiftManagement(int branchID) {
        this.branchID = branchID;
        this.shiftDAO = ShiftDAO.getInstance();
    }

    /*check if the manager enters this function again and the shifts are already made (check if shiftsOrginizerForAWeek is not empty) return false */
    public void checkIfShiftsAlreadyMade(){
        Set<Shift> allShifts = new HashSet<>();
        try {
            allShifts = this.shiftDAO.getAllShifts(branchID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");;
        }
        // if the manager wants to create shifts but the shifts are already made (check if shiftsAssignmentForAWeek is not empty) return false
        if (allShifts!=null) {
            throw new RuntimeException("You have already defined the shifts, choose option 3 to edit");
        }
    }

    /* HR manager function for creating shifts for a new week:
               while not all shifts of the week were creating:
               asking the HR for hours - start and end for the morning and evening shifts for this week, check if cancelled and the count of
               employee each job for each shift. Then add this shift to the map */
    public void createNewShiftsForNextWeek(String shiftType, LocalDate shiftDate, String startTimeOfShiftMorning, String endTimeOfShiftMorning, String startTimeOfShiftEvening, String endTimeOfShiftEvening,
                                              int cashierCount, int storeKeeperCount, int generalEmployeeCount, int securityCount, int usherCount, int cleanerCount, int shiftManagerCount){

        LocalTime startLocalTimeOfShiftMorning = LocalTime.parse(startTimeOfShiftMorning);
        LocalTime endLocalTimeOfShiftMorning = LocalTime.parse(endTimeOfShiftMorning);
        LocalTime startLocalTimeOfShiftEvening = LocalTime.parse(startTimeOfShiftEvening);
        LocalTime endLocalTimeOfShiftEvening = LocalTime.parse(endTimeOfShiftEvening);
        ShiftType shiftType1;
        // create this shift for this specific day and shift type
        Shift newAssignmentShiftByHrManager;
        if (shiftType.equals("morning")) {
            shiftType1 = ShiftType.MORNING;
            newAssignmentShiftByHrManager = new Shift(shiftDate, shiftType1, startLocalTimeOfShiftMorning, endLocalTimeOfShiftMorning,
                    cashierCount, storeKeeperCount, generalEmployeeCount, securityCount, usherCount, cleanerCount, shiftManagerCount);
        }
        else {
            shiftType1 = ShiftType.EVENING;
            newAssignmentShiftByHrManager = new Shift(shiftDate, shiftType1, startLocalTimeOfShiftEvening, endLocalTimeOfShiftEvening,
                    cashierCount, storeKeeperCount, generalEmployeeCount, securityCount, usherCount, cleanerCount, shiftManagerCount);
        }
        //add to shift Assignment For AWeek
        try {
            this.shiftDAO.insert(branchID, newAssignmentShiftByHrManager);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
    }


    /* Function that collect data about shift from HR manager - date and shiftType , if the
     * shift doesn't exist, the Manager can't change and return false , else return the data
     * about the shift that collect in a pair */
    public Shift getShiftFromDetails(String shiftType, LocalDate date) {
        String shiftTypeToSubmit;
        if (shiftType.equals("m")) {
            shiftTypeToSubmit = "morning";
        } else {
            shiftTypeToSubmit = "evening";
        }
        Shift shift = null;
        try {
            shift = this.shiftDAO.getShiftByBranchDateAndType(branchID, date.toString(), shiftTypeToSubmit);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        // if the shift doesn't exist, the Manager can't change
        if (shift == null) {
            throw  new RuntimeException("The shift you are asking for wasn't created");
        }
        return shift;
    }

    /* The function get the Hr manager new hours for a shift and change them */
    public boolean changeShiftTime(String date, String shiftType, String[] hours) {
        LocalDate shiftDate = LocalDate.parse(date);
        Shift shiftToChange = getShiftFromDetails(shiftType, shiftDate);
        if (shiftToChange == null) {
            return false;
        }
        // change the shift's time
        LocalTime startTimeOfShift = LocalTime.parse(hours[0]);
        LocalTime endTimeOfShift = LocalTime.parse(hours[1]);
        shiftToChange.setShiftStartTime(startTimeOfShift);
        shiftToChange.setShiftEndTime(endTimeOfShift);
        try {
            this.shiftDAO.update(branchID, shiftToChange);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
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

    /*  function for deleting employee from shift:
            1. ask for the employee id - search for the employee with the id to get the object to send to delete function
            2. delete employee
            5. present the shift
                    */
    public boolean removeEmployeeFromShift(String date, String shiftType, Employee employee) {
        //find the shift
        LocalDate shiftDate = LocalDate.parse(date);
        Shift shiftToRemoveEmployee = getShiftFromDetails(shiftType, shiftDate);
        // find this specific employee and remove this employee from this shift
        if (employee == null) {
            throw new RuntimeException("The employee you searched for does not exist.");
        }
        boolean removeEmployee = shiftToRemoveEmployee.removeEmployeesAndJobsInShift(employee);
        if (!removeEmployee) {
            throw new RuntimeException("The employee you asked is not assigned to this shift.");
        }
        try {
            this.shiftDAO.update(branchID, shiftToRemoveEmployee);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return true;
    }


    /* function that check the job type to add to this shift */
    public Pair<Shift, JobType> checkJobToAdd(String date, String shiftType, Map<String, Employee> registeredEmployees, String jobTypeNumberString) {
        //get job type of employee to add
        JobType wantedJob = convertToJobType(jobTypeNumberString);
        //find the shift
        LocalDate shiftDate = LocalDate.parse(date);
        Shift shiftToAddEmployee = getShiftFromDetails(shiftType, shiftDate);
        // check if HR didn't define he wants this job at creating the shift
        if(shiftToAddEmployee.wantedJobCountInShift.get(wantedJob) == 0)
        {
            throw new RuntimeException("You didn't ask for this job type for this shift.");
        }
        // check if this job type fully staffed
        if(shiftToAddEmployee.wantedJobCountInShift.get(wantedJob).equals(shiftToAddEmployee.assignedJobCountInShift.get(wantedJob)))
        {
            throw new RuntimeException("You fully staffed this job for this shift.");
        }
        // present all the employees with that job that can work
        if(Objects.equals(displayAvailableEmployeesForJobType(date, shiftType, jobTypeNumberString, registeredEmployees), ""))
            throw new RuntimeException("There aren't available employees.");
        return new Pair<>(shiftToAddEmployee,wantedJob);
    }

    /*function that choose employee and add employee to the shift - get the employee object with the id to send to the adding function
      add 1 to the employee's field of shiftCountForWeek and present the shift*/
    public boolean addEmployeeToShift(Employee employee, JobType wantedJob, Shift shiftToAddEmployee){
        if (employee == null) {
            throw new RuntimeException("The employee you searched for does not exist.");
        }
        boolean addEmployee = checkIfEmployeeValidToWork(employee, shiftToAddEmployee);
        if(!addEmployee){
            throw new RuntimeException("This employee cannot work in this shift.");
        }

        if(!employee.employeesAuthorizedJobs.contains(wantedJob)){
            throw new RuntimeException("This employee is not qualified for this job");
        }

        addEmployee = shiftToAddEmployee.addEmployeesAndJobsInShift(wantedJob, employee);
        if (!addEmployee) {
            throw new RuntimeException("The action cannot be done, you have reached the maximum of employees for this job.");
        }
        try {
            this.shiftDAO.update(branchID, shiftToAddEmployee);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        return true;
    }

    /*function that presents for the manager the employees that are available to work by given job type and shift*/
    public String displayAvailableEmployeesForJobType(String date, String shiftType, String jobTypeNumberString, Map<String, Employee> registeredEmployees) {
        LocalDate shiftDate = LocalDate.parse(date);
        Shift shift = getShiftFromDetails(shiftType, shiftDate);
        JobType jobType = convertToJobType(jobTypeNumberString);
        // iterate all the employees
        int flag = 0;
        String output = "";
        for (String employeeId : registeredEmployees.keySet()) {
            Employee find_employee = registeredEmployees.get(employeeId);
            // check if the employee is certified for the wanted job
            if (find_employee.employeesAuthorizedJobs.contains(jobType)) {
                // call the function to check if the employee is valid to work
                if (checkIfEmployeeValidToWork(find_employee, shift)) {
                    flag = 1;
                    output += "Name : " + find_employee.employeeFullName + " ID : " + find_employee.employeeID + "\n";
                    output += "--------------------------------------"+"\n";
                }
            }
        }
        if (flag == 0) {
            throw new RuntimeException("There are no available employees for this shift.");
        }
        return output;
    }

    public List<String> getAvailableEmployeesForJobType(String date, String shiftType, String jobTypeNumberString, Map<String, Employee> registeredEmployees) {
        LocalDate shiftDate = LocalDate.parse(date);
        Shift shift = getShiftFromDetails(shiftType, shiftDate);
        JobType jobType = convertToJobType(jobTypeNumberString);

        List<String> employeeList = new ArrayList<>();

        for (String employeeId : registeredEmployees.keySet()) {
            Employee find_employee = registeredEmployees.get(employeeId);

            if (find_employee.employeesAuthorizedJobs.contains(jobType) && checkIfEmployeeValidToWork(find_employee, shift)) {
                String employeeInfo = "Name: " + find_employee.employeeFullName + " ID: " + find_employee.employeeID;
                employeeList.add(employeeInfo);
            }
        }

        if (employeeList.isEmpty()) {
            throw new RuntimeException("There are no available employees for this shift.");
        }

        return employeeList;
    }

    /*function that checks if the given employee is valid to work in the given shift according to 3 constraints:
        - check if the given shift is in the given employees submitting shifts
        - check if employee is already works in other shift that day
        - check if employee is working six days */
    public boolean checkIfEmployeeValidToWork(Employee employee, Shift shift) {
        // if the employee submitted this shift
        if (employee.employeeSubmittingShifts.containsKey(shift.shiftDate) && employee.employeeSubmittingShifts.get(shift.shiftDate).contains(shift.shiftType)) {
            //check if employee is not already working in another sghift that day
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
    public String displayAllShiftsDetails(Map<String, Employee> registeredEmployees) {
        Set<Shift> allShifts = new HashSet<>();
        try {
            allShifts = this.shiftDAO.getAllShifts(branchID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");;
        }
        String output = "";
        if(allShifts != null) {
            for (Shift shift : allShifts) {
                //print this specific shift
                output += shift.displayShift(registeredEmployees);
                output += "-----------------------------------------------------------------------------------------------------" + "\n";
            }
        }
        return output;
    }

    public Object[][] getAllShiftsDetails(Map<String, Employee> registeredEmployees) {
        Set<Shift> allShifts = new HashSet<>();
        try {
            allShifts = this.shiftDAO.getAllShifts(branchID);
        } catch (SQLException e) {
            System.out.println("SQL exception");
        }

        Object[][] data = new Object[allShifts.size()][];

        int rowIndex = 0;
        for (Shift shift : allShifts) {
            data[rowIndex] = shift.getShiftData(registeredEmployees);
            rowIndex++;
        }

        return data;
    }



    public String displayShift(String date, String shiftType, Map<String, Employee> registeredEmployees) {
        LocalDate shiftDate = LocalDate.parse(date);
        Shift shift = getShiftFromDetails(shiftType, shiftDate);
        return shift.displayShift(registeredEmployees);
    }

    public List<String> displayEmployeesInShift(String date, String shiftType, Map<String, Employee> registeredEmployees) {
        LocalDate shiftDate = LocalDate.parse(date);
        Shift shift = getShiftFromDetails(shiftType, shiftDate);
        List<String> employeesInShift = new ArrayList<>();
        for(String employeeID : shift.employeesAndJobsInShift.keySet()){
            Employee findEmployee = registeredEmployees.get(employeeID);
            String employeeInfo = "Name: " + findEmployee.employeeFullName + " " + " ID: " + findEmployee.employeeID + " " + "Job: " + shift.employeesAndJobsInShift.get(employeeID).toString().toLowerCase() + "\n";
            employeesInShift.add(employeeInfo);
        }
        return employeesInShift;
    }

    /*- function for adding or delinting cashier cancellation - by the shift manager*/
    public void addOrDeleteCashierCancellation(String date, String shiftType, String employeeID, String barcode, String addOrDelete, Map<String, Employee> registeredEmployees) {
        //get the wanted shift
        LocalDate shiftDate = LocalDate.parse(date);
        Shift shiftToEdit = getShiftFromDetails(shiftType, shiftDate);
        if (shiftToEdit == null) {
            throw new RuntimeException("The shift you asked for does not exist.");
        }
        boolean employeeInShift = shiftToEdit.checkIfEmployeeInShift(employeeID);
        if (!employeeInShift) {
            throw new RuntimeException("The employee you asked is not assigned to this shift.");
        }
        Employee employeeThatCancelled = registeredEmployees.get(employeeID);
        // add or remove the cashier cancellation
        if(addOrDelete.equals("A")) {
            boolean addCancellation = shiftToEdit.addCancellationInCashBox(employeeThatCancelled, barcode);
            if (!addCancellation) {
                throw new RuntimeException("The employee you entered is not a shift manager, other employees cannot cancel a product.");
            }
        }
        else{
            boolean deleteCancellation = shiftToEdit.deleteCancellationInCashBox(employeeThatCancelled, barcode);
            if (!deleteCancellation) {
                throw new RuntimeException("The employee or the product you enter do not exist in the cancellation history.");
            }
        }
        try {
            this.shiftDAO.update(branchID, shiftToEdit);
        }
        catch(SQLException e){
            System.out.println("SQL exception");
        }
    }

    /* display all cancellation in cashier in specific shift:employees and products they cancelled*/
    public String displayCashierCancellation(String date, String shiftType){
        LocalDate shiftDate = LocalDate.parse(date);
        Shift shiftToEdit = getShiftFromDetails(shiftType, shiftDate);
        if (shiftToEdit == null) {
            throw new RuntimeException("The shift you asked for does not exist.");
        }
        List<String> cancellations;
        // check if there is no cancelled products to display
        if (shiftToEdit.cancellationInCashBox.isEmpty()){
            throw new RuntimeException("There is no cancelled product yet");
        }
        String output = "";
        output += "Product cancellations in shift: \n";
        for(String employeeID : shiftToEdit.cancellationInCashBox.keySet()){
            output += "Employee ID: " + employeeID + "\n";
            cancellations = shiftToEdit.cancellationInCashBox.get(employeeID);
            output += "Cancelled products:\n";
            for (String cancellation : cancellations) {
                output += cancellation + "\n";
            }
            output += "-------------------------------------------------------------------------------\n";
        }
        return output;
    }

    /* function to change the number of employees of each job type */
    public void changeJobTypeCount(String date, String shiftType, String jobTypeNumberString, int jobCountInt) {
        LocalDate shiftDate = LocalDate.parse(date);
        Shift shiftToEdit = getShiftFromDetails(shiftType, shiftDate);
        JobType wantedJob = convertToJobType(jobTypeNumberString);
        String str = shiftToEdit.setJobCount(wantedJob, jobCountInt);
        try {
            this.shiftDAO.update(branchID, shiftToEdit);
        }
        catch(SQLException e){
            System.out.println("SQL exception");
        }
    }

    /* function to get job type*/
    public JobType convertToJobType(String jobTypeNumberString) {
        JobType wantedJob = null;
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

    /*function that help to execute transport - that check if there is storekeeper assign for transport*/
    public boolean checkIfStoreKeeperAssignedInBranch(LocalDate date, LocalTime time){
        //this day is not in shift assignment
        Set<Shift> allShifts = new HashSet<>();
        try {
            allShifts = this.shiftDAO.getAllShifts(branchID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");;
        }
        Set<Shift> shitsForDay = new HashSet<>();
        boolean check = false;
        for(Shift shift : allShifts){
            if (shift.shiftDate.equals(date)) {
                check = true;
                shitsForDay.add(shift);
            }
        }
        if(!check){
            return false;
        }
        boolean isMorning = false;
        boolean isEvening = false;
        Shift morningShift=null;
        Shift eveningShift=null;
        for (Shift shift : shitsForDay) {
            // check if there is morning or evening shift
            if(shift.shiftType.equals(ShiftType.MORNING)){
                morningShift = shift;
                if ( (time.isBefore(shift.endTimeOfShift) || time.equals(shift.endTimeOfShift)) && (time.isAfter(shift.startTimeOfShift) || time.equals(shift.startTimeOfShift))){
                    // morning shift
                    isMorning = true;
                }
            }
            if(shift.shiftType.equals(ShiftType.EVENING)){
                eveningShift = shift;
                if ( (time.isBefore(shift.endTimeOfShift) || time.equals(shift.endTimeOfShift)) && (time.isAfter(shift.startTimeOfShift) || time.equals(shift.startTimeOfShift))){
                    // evening shift
                    isEvening = true;
                }
            }
        }
        // if there is morning shift check that storekeeper assign to morning and evening shift this day
        if(isMorning && (morningShift != null)){
            if(eveningShift != null){
                return morningShift.assignedJobCountInShift.get(JobType.STOREKEEPER) > 0 && eveningShift.assignedJobCountInShift.get(JobType.STOREKEEPER) > 0;
            }
        }
        // if there is evening shift check that storekeeper assign to evening shift this day
        else if (isEvening && (eveningShift != null)) {
            return eveningShift.assignedJobCountInShift.get(JobType.STOREKEEPER) > 0;
        }
        return false;
    }

    // check if a given shift is valid - has the wanted count of employees assignment
    public void isShiftValid(String date, String shiftType){
        LocalDate shiftDate = LocalDate.parse(date);
        Shift shift = getShiftFromDetails(shiftType, shiftDate);
        if(!shift.wantedJobCountInShift.equals(shift.assignedJobCountInShift)){
            throw new RuntimeException("The shift is not valid.");
        }
    }


    /* function that check for specific shift and check is an employee is Shift Manger Or StoreKeeper in this shift */
    public String getTransportsForShift(String date, String shiftType, String employeeID, int branch, Map<String, Employee> registeredEmployees) {
        // find this specific shift
        LocalDate shiftDate = LocalDate.parse(date);
        Shift shiftToChange = getShiftFromDetails(shiftType, shiftDate);
        for (String employeeId : shiftToChange.employeesAndJobsInShift.keySet()) {
            Employee employee = registeredEmployees.get(employeeId);
            if (employee.employeeID.equals(employeeID) && (shiftToChange.employeesAndJobsInShift.get(employee.employeeID).equals(JobType.SHIFT_MANAGER) ||
                    shiftToChange.employeesAndJobsInShift.get(employee.employeeID).equals(JobType.STOREKEEPER))) {
                // get the transport for this shift
                String transports = TransportController.displayTransportsForShift(shiftDate, shiftToChange.startTimeOfShift, shiftToChange.endTimeOfShift, branch);
                if (Objects.equals(transports, "")) {
                    throw new RuntimeException("There are no transportations");
                }
                else return transports;
            }
        }
        throw new RuntimeException("You are not in this shift / not a storekeeper or shift manager.");
    }

    public LocalDate[] currDateAndEndOfWeek() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startOfWeek = currentDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(7); // add six days to get the end of the week
        return new LocalDate[]{startOfWeek, endOfWeek};
    }

    public ShiftDAO getShiftDAO() {
        return shiftDAO;
    }
}
