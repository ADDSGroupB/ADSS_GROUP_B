package BuisnessLayer.HR;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Shift extends AShift {

    public Map<String, JobType> employeesAndJobsInShift;
    public Map<JobType, Integer> assignedJobCountInShift;
    public Map<JobType, Integer> wantedJobCountInShift;
    public Map<String, List<String>> cancellationInCashBox;

    /* constructor for when the HR manager enters specific hours for the shift */
    public Shift(LocalDate shiftDate, ShiftType shiftType, LocalTime startTimeOfShift, LocalTime endTimeOfShift, int cashierCount, int storeKeeperCount,
                 int generalEmployeeCount, int securityCount, int usherCount, int cleanerCount, int shiftManagerCount) {
        super(shiftDate, shiftType, startTimeOfShift, endTimeOfShift);
        this.employeesAndJobsInShift = new HashMap<>();
        this.assignedJobCountInShift = new HashMap<>();
        this.cancellationInCashBox = new HashMap<>();
        // create map of the count of employees wanted in the shift defined by the HR manager
        this.wantedJobCountInShift = new HashMap<>();
        this.wantedJobCountInShift.put(JobType.CASHIER, cashierCount);
        this.wantedJobCountInShift.put(JobType.STOREKEEPER, storeKeeperCount);
        this.wantedJobCountInShift.put(JobType.GENERAL_EMPLOYEE, generalEmployeeCount);
        this.wantedJobCountInShift.put(JobType.SECURITY, securityCount);
        this.wantedJobCountInShift.put(JobType.USHER, usherCount);
        this.wantedJobCountInShift.put(JobType.CLEANER, cleanerCount);
        this.wantedJobCountInShift.put(JobType.SHIFT_MANAGER, shiftManagerCount);
    }


    /* add employees to the shift's employees map and add to the shift's job count*/
    public boolean addEmployeesAndJobsInShift(JobType jobType, Employee employeeToAdd){
        // check if the number of employees with this jobType is the maximum the HR manager defined
        if(this.assignedJobCountInShift.containsKey(jobType)){
            if(this.assignedJobCountInShift.get(jobType).equals(this.wantedJobCountInShift.get(jobType))){
                return false;
            }
        }
        // add employee to the shift
        this.employeesAndJobsInShift.put(employeeToAdd.employeeID, jobType);
        // update the jobType count
        if(!this.assignedJobCountInShift.containsKey(jobType)){
            this.assignedJobCountInShift.put(jobType, 1);
        }
        else {
            this.assignedJobCountInShift.put(jobType, this.assignedJobCountInShift.get(jobType) + 1);
        }
        // add the shift to the employee's assigned shifts
        employeeToAdd.addShiftToShiftsMap(this.shiftDate, this.shiftType);
        // add 1 to shiftCountForWeek
        employeeToAdd.shiftCountForWeek ++;
        return true;
    }

    /* remove employees from the shift's employees map and remove from the shift's job count*/
    public boolean removeEmployeesAndJobsInShift(Employee employeeToRemove){
        // check if the employee is in the shift
        boolean foundEmployee = checkIfEmployeeInShift(employeeToRemove.employeeID);
        if(!foundEmployee){
            return false;
        }
        // save the employee's job type in this shift
        JobType jobType = this.employeesAndJobsInShift.get(employeeToRemove.employeeID);
        // remove employee from this shift
        this.employeesAndJobsInShift.remove(employeeToRemove.employeeID);
        // update the count of the jobType in this shift
        if(this.assignedJobCountInShift.get(jobType) == 1){
            this.assignedJobCountInShift.remove(jobType);
        }
        else{
            this.assignedJobCountInShift.put(jobType, this.assignedJobCountInShift.get(jobType) - 1);
        }
        // remove the shift to the employee's assigned shifts
        employeeToRemove.removeShiftFromShiftsMap(this.shiftDate, this);
        // remove 1 from shiftCountForWeek
        employeeToRemove.shiftCountForWeek --;
        return true;
    }

    public boolean checkIfEmployeeInShift(String employeeId){
        for(String key : this.employeesAndJobsInShift.keySet()){
            if(key.equals(employeeId)){
                return true;
            }
        }
        return false;
    }

    /*functions that add and remove to the map of cancelled objects from the cash box with the employee that cancelled (he has to be a shift manager)*/

    public boolean addCancellationInCashBox(Employee employeeThatCancelled, String cancelledProduct){
        // check if the employee is authorized to be shift manager
        if(this.employeesAndJobsInShift.get(employeeThatCancelled.employeeID).equals(JobType.SHIFT_MANAGER)){
            // add a new key (employeeThatCancelled) with an empty list as the initial value if it doesn't exist yet
            if(!(this.cancellationInCashBox.containsKey(employeeThatCancelled.employeeID))){
                this.cancellationInCashBox.put(employeeThatCancelled.employeeID, new ArrayList<>());
            }
            // add the cancellation
            this.cancellationInCashBox.get(employeeThatCancelled.employeeID).add(cancelledProduct);
            return true;
        }
        return false;
    }


    public boolean deleteCancellationInCashBox(Employee employeeThatCancelled, String cancelledProduct){
        // check if this employee and item exist in the map
        if(!(this.cancellationInCashBox.containsKey(employeeThatCancelled.employeeID)) || !(this.cancellationInCashBox.get(employeeThatCancelled.employeeID).contains(cancelledProduct))){
            return false;
        }
        // delete the item type
        this.cancellationInCashBox.get(employeeThatCancelled.employeeID).remove(cancelledProduct);
        // after removal if the list of employee items is empty, remove the employee
        if(this.cancellationInCashBox.get(employeeThatCancelled.employeeID).isEmpty()){
            this.cancellationInCashBox.remove(employeeThatCancelled.employeeID);
        }
        return true;
    }

    /* set functions for the time of the shift */
    public void setShiftStartTime(LocalTime startTime){
        this.startTimeOfShift = startTime;
    }

    public void setShiftEndTime(LocalTime endTime){
        this.endTimeOfShift = endTime;
    }

    /* function to display all the employees and their jobs in the shift */
    public String displayShift(Map<String, Employee> registeredEmployees){
        String output = "";
        output += "- Shift details:\n";
        output += "Day : " + this.shiftDate + " Type: " + this.shiftType + " Start time : " + this.startTimeOfShift + " End time : " + this.endTimeOfShift + "\n";
        //display employees in this shift - name and id and their jobs
        for (String employeeIDToPrint : this.employeesAndJobsInShift.keySet()) {
            Employee employeeToPrint = registeredEmployees.get(employeeIDToPrint);
            // print this specific employee
            output += "Employees in shift : \n";
            output += "Employee ID : " + employeeToPrint.employeeID + " Full Name : " + employeeToPrint.employeeFullName +  " Job : " + this.employeesAndJobsInShift.get(employeeToPrint.employeeID) + "\n";
        }
        return output;
    }

    public Object[] getShiftData(Map<String, Employee> registeredEmployees) {
        Object[] rowData = new Object[5]; // Assuming there are 5 fields in total (modify as needed)

        rowData[0] = this.shiftDate;
        rowData[1] = this.shiftType;
        rowData[2] = this.startTimeOfShift;
        rowData[3] = this.endTimeOfShift;

        StringBuilder employeesInShift = new StringBuilder();
        for (String employeeIDToPrint : this.employeesAndJobsInShift.keySet()) {
            Employee employeeToPrint = registeredEmployees.get(employeeIDToPrint);
            employeesInShift.append("Employee ID: ").append(employeeToPrint.employeeID).append(", Full Name: ").append(employeeToPrint.employeeFullName).append(", Job: ").append(this.employeesAndJobsInShift.get(employeeToPrint.employeeID)).append("\n");
        }
        rowData[4] = employeesInShift.toString();

        return rowData;
    }

    public List<String> getEmployeesInShift(Map<String, Employee> registeredEmployees) {
        List<String> employees = new ArrayList<>();
        for (String employeeID : this.employeesAndJobsInShift.keySet()) {
            if (registeredEmployees.containsKey(employeeID)) {
                Employee employee = registeredEmployees.get(employeeID);
                String employeeName = employee.employeeFullName; // Get the employee name or any other relevant employee information
                employees.add(employeeName);
            }
        }
        return employees;
    }


    /* change the number of wanted employees in a specific job type*/
    public String setJobCount(JobType wantedJob, int jobCount){
        if(this.assignedJobCountInShift.containsKey(wantedJob) && (this.assignedJobCountInShift.get(wantedJob) > jobCount)){
            throw new RuntimeException( "You can't choose a number smaller than the number of employees assigned to this job.\n");
        }
        if(wantedJob.equals(JobType.SHIFT_MANAGER) && jobCount == 0){
            throw  new RuntimeException( "There has to be a shift manager.\n");
        }
        else{
            this.wantedJobCountInShift.put(wantedJob, jobCount);
        }
        return null;
    }

    public boolean isShiftValid(){
        return this.assignedJobCountInShift.equals(this.wantedJobCountInShift);
    }
}
