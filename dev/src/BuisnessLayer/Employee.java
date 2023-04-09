package BuisnessLayer;

import java.time.LocalDate;
import java.util.*;


public class Employee {
    public String employeeFullName;
    public String employeeID;
    private String bankAccountInformation;
    public String startOfEmploymentDate;
    public int salaryPerHour;
    public String employeePersonalDetails;
    public ArrayList<JobType> employeesAuthorizedJobs;
    public String termsOfEmployment;
    public Map<LocalDate, Set<ShiftType>> employeeSubmittingShifts;
    public Map<LocalDate, Set<Shift>> employeesAssignmentForShifts;
    public Integer shiftCountForWeek;
    private String employeePassword;

    /*constructor to create a new employee*/
    public Employee(String employeeFullName, String employeeID, String bankAccountInformation, String startOfEmploymentDate, int salaryPerHour,
                    String employeePersonalDetails, ArrayList<JobType> employeesAuthorizedJobs, String termsOfEmployment, String employeePassword){
        this.employeeFullName = employeeFullName;
        this.employeeID = employeeID;
        this.bankAccountInformation = bankAccountInformation;
        this.startOfEmploymentDate = startOfEmploymentDate;
        this.salaryPerHour = salaryPerHour;
        this.termsOfEmployment = termsOfEmployment;
        this.employeePersonalDetails = employeePersonalDetails;
        this.employeesAuthorizedJobs = employeesAuthorizedJobs;
        /* initialize the maps for the submitting shifts and assigned shifts of the employee*/
        this.employeeSubmittingShifts = new HashMap<>();
        this.employeesAssignmentForShifts = new HashMap<>();
        this.shiftCountForWeek = 0;
        this.employeePassword = employeePassword;
    }

    /* get function for employee's bank account information */
    public String getBankAccountInformation(){
        return bankAccountInformation;
    }

    public void setSalaryPerHour(int newSalary){
        this.salaryPerHour = newSalary;
    }
    public void setBankAccountInformation(String newBankAccountInformation){
        this.bankAccountInformation = newBankAccountInformation;
    }

    /* add personal details to the employee */
    public void addEmployeePersonalDetails(String new_PersonalDetails){
        this.employeePersonalDetails += ", " + new_PersonalDetails;
    }

    /* remove job from employee */
    public void addEmployeesAuthorizedJobs(JobType newJobType){
        if(!this.employeesAuthorizedJobs.contains(newJobType)){
            this.employeesAuthorizedJobs.add(newJobType);
        }
    }

    /* remove job from employee:
           first, check if the employee holds the job the user wants to remove, if so, remove it. If not, return false */
    public boolean removeEmployeesAuthorizedJobs(JobType removeJobType){
        if(this.employeesAuthorizedJobs.contains(removeJobType)){
            this.employeesAuthorizedJobs.remove(removeJobType);
            return true;
        }
        else {
            return false;
        }
    }

    /*
        functions that add and remove to a map (submitting shifts or assignment shifts) a shift that contains dateOfShift and shiftType
    */

    public boolean addShiftToShiftsMap(LocalDate dateOfShift, Shift shift){
        // add a new key (dateOfShift) with an empty list as the initial value if it doesn't exist yet
        if(!(employeesAssignmentForShifts.containsKey(dateOfShift))){
            employeesAssignmentForShifts.put(dateOfShift, new HashSet<>());
        }
        // add the shift type
        employeesAssignmentForShifts.get(dateOfShift).add(shift);
        return true;
    }

    public boolean addShiftToShiftTypeMap(LocalDate dateOfShift, ShiftType shift){
        // add a new key (dateOfShift) with an empty list as the initial value if it doesn't exist yet
        if(!(employeeSubmittingShifts.containsKey(dateOfShift))){
            employeeSubmittingShifts.put(dateOfShift, new HashSet<>());
        }
        // add the shift type
        employeeSubmittingShifts.get(dateOfShift).add(shift);
        return true;
    }

    public boolean removeShiftFromShiftsMap(LocalDate dateOfShift, Shift shift){
        // check if this shift - dateOfShift and ShiftType exist in the submitting shifts list
        if(!(employeesAssignmentForShifts.containsKey(dateOfShift)) || !(employeesAssignmentForShifts.get(dateOfShift).contains(shift))){
            return false;
        }
        // delete the shift type
        employeesAssignmentForShifts.get(dateOfShift).remove(shift);
        // after removal if the set of the shift type is empty, remove the date key
        if(employeesAssignmentForShifts.get(dateOfShift).isEmpty()){
            employeesAssignmentForShifts.remove(dateOfShift);
        }
        return true;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }
}
