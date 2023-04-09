package BuisnessLayer;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

public class EmployeeManagement {

    public Map<String, Employee> registeredEmployees;

    public EmployeeManagement() {
        this.registeredEmployees = new HashMap<>();
    }

    /* Add employee to registeredEmployees map*/
    public void addNewEmployee(Employee registerNewEmployee) {
        // add the new employee to the registeredEmployee map
        this.registeredEmployees.put(registerNewEmployee.employeeID, registerNewEmployee);
    }

    /* function to return specific employee from registeredEmployees. if not exist return null*/
    public Employee getEmployeeById(String employeeId) {
        return this.registeredEmployees.get(employeeId);
    }

    /* function to add a bonus to employee salary */
    public int addBonusToEmployee(Employee employee) {
        // ask if he wants to add or remove employee and call to the function
        System.out.print("Do you want to add a bonus to this employee's week salary ? -(y/n) : ");
        Scanner scanner = new Scanner(System.in);
        String AddBonusOrNot = scanner.nextLine();
        while (!(AddBonusOrNot.equals("y") || AddBonusOrNot.equals("n"))) {
            System.out.println("You have entered an invalid answer, please enter (y/n) : ");
            AddBonusOrNot = scanner.nextLine();
        }
        int bonusToAdd = 0;
        // check the input if the HR want to add bonus to employee
        if (AddBonusOrNot.equals("y")) {
            // add the bonus to employee
            System.out.print("How much bonus do you want to add ? ");
            String bonus = scanner.nextLine();
            bonusToAdd = getPositiveInt(bonus);
        }
        return bonusToAdd;
    }

    /* function to calculate and print the salary report for all employee (check for each employee how many shifts and their hours and multiply the salary)*/
    public void SalaryReportForAllEmployee() {
        for (String employeeID : this.registeredEmployees.keySet()) {
            // find this employee
            Employee findEmployee = getEmployeeById(employeeID);
            // calculate salary, check for bonus and print the salary report for each employee
            int salaryBeforeBonus = weekWorkHoursEmployee(findEmployee) * findEmployee.salaryPerHour;
            System.out.printf("Employee: %s, Id: %s, Salary: %s\n", findEmployee.employeeFullName, findEmployee.employeeID, salaryBeforeBonus);
            // ask for bonus for this employee
            int bonus = addBonusToEmployee(findEmployee);
            int salaryAfterBonus = salaryBeforeBonus + bonus;
            if (bonus != 0) {
                System.out.println("Employee salary after bonus : " + salaryAfterBonus);
            }
        }
    }

    /* function that calculate total hours that employee work this week*/
    public int weekWorkHoursEmployee(Employee employee) {
        int hourCounter = 0;
        for (LocalDate shiftDate : employee.employeesAssignmentForShifts.keySet()) {
            Set<Shift> shiftSet = employee.employeesAssignmentForShifts.get(shiftDate);
            for (Shift shift : shiftSet) {
                hourCounter += Duration.between(shift.startTimeOfShift, shift.endTimeOfShift).toHours();
            }
        }
        return hourCounter;
    }

    /* function to add a job type to a specific employee */
    public boolean AddJobTypeToEmployee() {
        //get employee ID
        String employeeID = getEmployeeId();
        Employee findEmployee = getEmployeeById(employeeID);
        displayAuthorizedJob(findEmployee);
        //get the job type to add
        JobType jobTypeToAdd = getEmployeeJobType();
        // add this job type to employee
        if (findEmployee == null) {
            System.out.println("The employee you have enter does not exist.");
            return false;
        }
        findEmployee.addEmployeesAuthorizedJobs(jobTypeToAdd);
        displayAuthorizedJob(findEmployee);
        return true;
    }

    /* function to remove a job type to a specific employee */
    public void removeJobTypeFromEmployee() {
        //get employee ID
        String employeeID = getEmployeeId();
        Employee findEmployee = getEmployeeById(employeeID);
        displayAuthorizedJob(findEmployee);
        JobType jobTypeToRemove = getEmployeeJobType();
        // remove this job type from employee - not allow to remove general employee
        if (jobTypeToRemove == JobType.GENERAL_EMPLOYEE) {
            System.out.println("All employees have to be general employees");
        }
        boolean remove = findEmployee.removeEmployeesAuthorizedJobs(jobTypeToRemove);
        if (!remove) {
            System.out.println("The employee is not authorized to this job.");
        }
        displayAuthorizedJob(findEmployee);
    }

    /* function that get employee ID */
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

    public JobType getEmployeeJobType() {
        Scanner scanner = new Scanner(System.in);
        String jobTypeNumberString = "0";
        JobType wantedJob = null;
        while (!jobTypeNumberString.matches("^[1-7]$")) {
            System.out.print("Enter the number of the job you want to add/remove :\n" +
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
        if (jobTypeNumberString.equals("7")){
            wantedJob = JobType.CLEANER;
        }
        return wantedJob;
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

    /* function that changes the salary Per hour for a specific employee */
    public void setSalaryPerHourForEmployee() {
        // get id of employee to change
        String employeeID = getEmployeeId();
        //findEmployee
        Employee findEmployee = getEmployeeById(employeeID);
        //ask for the salary to change
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new employee's salary per hour: ");
        String salaryPerHourString = scanner.nextLine();
        int newSalaryPerHour = getPositiveInt(salaryPerHourString);
        //change the salary
        findEmployee.setSalaryPerHour(newSalaryPerHour);
    }

    /* function to add personal details to a specific employee*/
    public void setPersonalDetailsForEmployee() {
        // get id of employee to change
        String employeeID = getEmployeeId();
        //findEmployee
        Employee findEmployee = getEmployeeById(employeeID);
        //ask for the personal details to add
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the employee's personal details to add : ");
        String newEmployeePersonalDetails = scanner.nextLine();
        ;
        //add the personal details
        findEmployee.addEmployeePersonalDetails(newEmployeePersonalDetails);
    }

    /* function to change bank account to a specific employee*/
    public void setBankAccountForEmployee() {
        // get id of employee to change
        String employeeID = getEmployeeId();
        //findEmployee
        Employee findEmployee = getEmployeeById(employeeID);
        //ask for the Bank account details to change
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new employee's bank information: ");
        String newBankInformation = scanner.nextLine();
        while (newBankInformation.isEmpty()) {
            System.out.print("Bank account details cannot be empty, please enter again: ");
            newBankInformation = scanner.nextLine();
        }
        //change the BankInformation
        findEmployee.setBankAccountInformation(newBankInformation);
    }

    /* function to present all employees */
    public void displayAllRegisterEmployee() {
        for (String employeeID : this.registeredEmployees.keySet()) {
            Employee employeeToPrint = this.registeredEmployees.get(employeeID);
            // print this specific employee
            System.out.printf("Employee ID : %s , Full Name : %s\n",
                    employeeToPrint.employeeID, employeeToPrint.employeeFullName);
            displayAuthorizedJob(employeeToPrint);
            System.out.println("-------------------------------------------------------------------------------");
        }
    }

    /* display employees authorized jobs*/
    public void displayAuthorizedJob(Employee employeeToPrint){
        // print Employee's authorized
        System.out.println("Employee's authorized jobs : ");
        for (int i = 0; i < employeeToPrint.employeesAuthorizedJobs.size(); i++) {
            System.out.printf("- %s\n", employeeToPrint.employeesAuthorizedJobs.get(i));
        }
    }
    /*function to delete this employee from all his assignment shifts and then delete him from registeredEmployees*/

    public boolean deleteRegisteredEmployee() {
        // there is no employee to remove
        if(this.registeredEmployees.isEmpty())
            return false;
        // get employee to delete
        String employeeID = getEmployeeId();
        Employee employeeToDelete = getEmployeeById(employeeID);
        // check if the employee doesn't exist
        if(employeeToDelete == null)
        {
            System.out.println("This employee doesn't exist.");
            return false;
        }
        // delete this employee from all shifts that he registered
        for (LocalDate shiftDate : employeeToDelete.employeesAssignmentForShifts.keySet()) {
            Set<Shift> shiftSetValue = employeeToDelete.employeesAssignmentForShifts.get(shiftDate);
            for (Shift shift : shiftSetValue) {
                shift.removeEmployeesAndJobsInShift(employeeID);
            }
        }
        // delete employee from registered employee map
        this.registeredEmployees.remove(employeeID, employeeToDelete);
        System.out.printf("%s has been deleted\n", employeeToDelete.employeeFullName);
        return true;
    }
}