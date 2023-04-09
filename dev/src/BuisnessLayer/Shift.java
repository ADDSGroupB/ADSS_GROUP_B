package BuisnessLayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Shift {
    public LocalDate shiftDate;
    public ShiftType shiftType;
    public Map<Employee, JobType> employeesAndJobsInShift;
    public Map<JobType, Integer> assignedJobCountInShift;
    public Map<JobType, Integer> wantedJobCountInShift;
    public Map<Employee, List<String>> cancellationInCashBox;
    public LocalTime startTimeOfShift;
    public LocalTime endTimeOfShift;

    /* constructor for when the HR manager enters specific hours for the shift */
    public Shift(LocalDate shiftDate, ShiftType shiftType, LocalTime startTimeOfShift, LocalTime endTimeOfShift, int cashierCount, int storeKeeperCount,
                 int generalEmployeeCount, int securityCount, int usherCount, int cleanerCount) {
        this.shiftDate = shiftDate;
        this.shiftType = shiftType;
        this.employeesAndJobsInShift = new HashMap<>();
        this.assignedJobCountInShift = new HashMap<>();
        this.cancellationInCashBox = new HashMap<>();
        this.startTimeOfShift = startTimeOfShift;
        this.endTimeOfShift = endTimeOfShift;
        // create map of the count of employees wanted in the shift defined by the HR manager
        this.wantedJobCountInShift = new HashMap<>();
        this.wantedJobCountInShift.put(JobType.CASHIER, cashierCount);
        this.wantedJobCountInShift.put(JobType.STOREKEEPER, storeKeeperCount);
        this.wantedJobCountInShift.put(JobType.GENERAL_EMPLOYEE, generalEmployeeCount);
        this.wantedJobCountInShift.put(JobType.SECURITY, securityCount);
        this.wantedJobCountInShift.put(JobType.USHER, usherCount);
        this.wantedJobCountInShift.put(JobType.CLEANER, cleanerCount);
        this.wantedJobCountInShift.put(JobType.SHIFT_MANAGER, 1);

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
        this.employeesAndJobsInShift.put(employeeToAdd, jobType);
        // update the jobType count
        if(!this.assignedJobCountInShift.containsKey(jobType)){
            this.assignedJobCountInShift.put(jobType, 1);
        }
        else {
            this.assignedJobCountInShift.put(jobType, this.assignedJobCountInShift.get(jobType) + 1);
        }
        // add the shift to the employee's assigned shifts
        employeeToAdd.addShiftToShiftsMap(this.shiftDate, this);
        // add 1 to shiftCountForWeek
        employeeToAdd.shiftCountForWeek ++;
        return true;
    }

    /* remove employees from the shift's employees map and remove from the shift's job count*/
    public boolean removeEmployeesAndJobsInShift(String employeeID){
        // check if the employee is in the shift
        Employee foundEmployee = checkIfEmployeeInShift(employeeID);
        if(foundEmployee == null){
            return false;
        }
        // save the employee's job type in this shift
        JobType jobType = this.employeesAndJobsInShift.get(foundEmployee);
        // remove employee from this shift
        this.employeesAndJobsInShift.remove(foundEmployee);
        // update the count of the jobType in this shift
        if(this.assignedJobCountInShift.get(jobType) == 1){
            this.assignedJobCountInShift.remove(jobType);
        }
        else{
            this.assignedJobCountInShift.put(jobType, this.assignedJobCountInShift.get(jobType) - 1);
        }
        // remove the shift to the employee's assigned shifts
        foundEmployee.removeShiftFromShiftsMap(this.shiftDate, this);
        // remove 1 from shiftCountForWeek
        foundEmployee.shiftCountForWeek --;
        return true;
    }

    public Employee checkIfEmployeeInShift(String employeeId){
        for(Employee key : this.employeesAndJobsInShift.keySet()){
            if(key.employeeID.equals(employeeId)){
                return key;
            }
        }
        return null;
    }

    /*functions that add and remove to the map of cancelled objects from the cash box with the employee that cancelled (he has to be a shift manager)*/

    public boolean addCancellationInCashBox(Employee employeeThatCancelled, String cancelledProduct){
        // check if the employee is authorized to be shift manager
        if(employeeThatCancelled.employeesAuthorizedJobs.contains(JobType.SHIFT_MANAGER)){
            // add a new key (employeeThatCancelled) with an empty list as the initial value if it doesn't exist yet
            if(!(this.cancellationInCashBox.containsKey(employeeThatCancelled))){
                this.cancellationInCashBox.put(employeeThatCancelled, new ArrayList<>());
            }
            // add the cancellation
            this.cancellationInCashBox.get(employeeThatCancelled).add(cancelledProduct);
            return true;
        }
        return false;
    }


    public boolean deleteCancellationInCashBox(Employee employeeThatCancelled, String cancelledProduct){
        // check if this employee and item exist in the map
        if(!(this.cancellationInCashBox.containsKey(employeeThatCancelled)) || !(this.cancellationInCashBox.get(employeeThatCancelled).contains(cancelledProduct))){
            return false;
        }
        // delete the item type
        this.cancellationInCashBox.get(employeeThatCancelled).remove(cancelledProduct);
        // after removal if the list of employee items is empty, remove the employee
        if(this.cancellationInCashBox.get(employeeThatCancelled).isEmpty()){
            this.cancellationInCashBox.remove(employeeThatCancelled);
        }
        return true;
    }

    /* set functions for the time of the shift */
    public void setShiftStartTime(LocalTime startTime){
        this.startTimeOfShift = startTime;
    }

    public void setShiftEndTime(LocalTime endTime){
        this.startTimeOfShift = endTime;
    }

    /* function to display all the employees and their jobs in the shift */
    public void displayShift(){
        System.out.println("- Shift details:");
        System.out.printf("Day : %s, Type: %s, start time : %s, end time : %s\n",this.shiftDate, this.shiftType, this.startTimeOfShift, this.endTimeOfShift);
        //display employees in this shift - name and id and their jobs
        for (Employee employeeToPrint : this.employeesAndJobsInShift.keySet()) {
            // print this specific employee
            System.out.println("Employees in shift :");
            System.out.printf("Employee ID : %s , Full Name : %s, Job : %s\n",
                    employeeToPrint.employeeID, employeeToPrint.employeeFullName, this.employeesAndJobsInShift.get(employeeToPrint));
        }
    }

    /* change the number of wanted employees in a specific job type*/
    public void setJobCount(JobType wantedJob, int jobCount){
        if(wantedJob.equals(JobType.SHIFT_MANAGER)){
            System.out.println("There has to be 1 shift manager, not less or more.");
            return;
        }
        if(this.assignedJobCountInShift.containsKey(wantedJob) && (this.assignedJobCountInShift.get(wantedJob) > jobCount)){
            System.out.print("You can't choose a number smaller than the number of employees assigned to this job.\n");
        }
        else{
            this.wantedJobCountInShift.put(wantedJob, jobCount);
        }
    }

    public boolean isShiftValid(){
        return this.assignedJobCountInShift.equals(this.wantedJobCountInShift);
    }
}
