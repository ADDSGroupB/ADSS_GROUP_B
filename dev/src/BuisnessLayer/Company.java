package BuisnessLayer;

import javafx.util.Pair;

import javax.swing.text.StyledEditorKit;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class Company {
    public ArrayList<Branch> branchesList;
    public Map<String, Employee> registeredEmployeesInCompany;

    public Company() {
        registeredEmployeesInCompany = new HashMap<>();
        branchesList = new ArrayList<>(10);
        branchesList.add(new Branch());
        branchesList.add(new Branch());
        branchesList.add(new Branch());
        branchesList.add(new Branch());
        branchesList.add(new Branch());
        branchesList.add(new Branch());
        branchesList.add(new Branch());
        branchesList.add(new Branch());
        branchesList.add(new Branch());
        branchesList.add(new Branch());
    }
    /* register employee add to wanted branches :
        - when creating the employee, always put in the jobType general worker
        - jobTypes - allow only from the enum  */
    public boolean registerNewEmployee() {
        //collecting details about the new employee and check the validation od the details
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter employee's full name: ");
        String employeeName = scanner.nextLine();
        //get employee ID
        String employeeID = branchesList.get(0).employeeManagement.getEmployeeId();
        // check if this employee already in the company
        if (registeredEmployeesInCompany.get(employeeID) != null){
            System.out.println("This employee already registered");
            return false;
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
        int salaryPerHour = branchesList.get(0).employeeManagement.getPositiveInt(salaryPerHourString);
        System.out.print("Enter the employee's terms of employment: ");
        String termsOfEmployment = scanner.nextLine();
        System.out.print("Enter the employee's personal details: ");
        String employeePersonalDetails = scanner.nextLine();
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
        System.out.print("Enter the employee's password for the system: ");
        String employeePassword = scanner.nextLine();
        // create this new employee
        Employee registerNewEmployee = new Employee(employeeName, employeeID, bankInformation, startOfEmploymentDate, salaryPerHour, employeePersonalDetails, employeesAuthorizedJobs,
                termsOfEmployment, employeePassword);
        this.registeredEmployeesInCompany.put(registerNewEmployee.employeeID, registerNewEmployee);
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
            this.branchesList.get(branchInt).employeeManagement.addNewEmployee(registerNewEmployee);
            branchToAdd = "0";
            System.out.print("Do you want to add to another branch? (y/n) : ");
            flag = scanner.nextLine();
            while (!(flag.equals("y") || flag.equals("n"))) {
                System.out.println("You have entered an invalid answer, please enter (y/n) : ");
                flag = scanner.nextLine();
            }
        }
        System.out.println("The employee has been registered.");
        return true;
    }

    /* Login function for employee */
    public Pair<Boolean, String> logInEmployee(){
        Scanner scanner = new Scanner(System.in);
        String employeeID = branchesList.get(0).employeeManagement.getEmployeeId();
        // check if this employee is not registered in the company
        if(this.registeredEmployeesInCompany.get(employeeID) == null){
            System.out.println("You are not a registered employee in the company");
            return null;
        }
        System.out.print("Enter your password: ");
        String employeePassword = scanner.nextLine();
        // Check if this is the correct password of the employee
        boolean loginCheck = this.registeredEmployeesInCompany.get(employeeID).getEmployeePassword().equals(employeePassword);
        if(!loginCheck) {
            System.out.println("You entered a wrong password");
            return null;
        }
        return new Pair<>(loginCheck,employeeID);
    }

    /* Login function for HR manager */
    public boolean logInHR(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your password: ");
        String employeePassword = scanner.nextLine();
        if(employeePassword.equals("LN")){
            return true;
        }
        System.out.println("You entered a wrong password.");
        return false;
    }

    /* function for employees to submit a shift they can work at and add the shift to the employee's shiftSubmission
        the function checks that it is before thursday 12:00 when the shift submission for next week is closing
        if the shift doesn't exist, the employee can't submit */
    public boolean submitAShiftForEmployee(String employeeId) {
        // ask the user for the date of the wanted shift and check that the date is according to the format
        System.out.print("Enter the date of the shift you want to add for next week(yyyy-mm-dd): ");
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

        //check if the date of the shift is only in the next week sunday - saturday
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDate nowDate = LocalDate.now();
        LocalDate startOfWeek = nowDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        //print massage if not a valid date
        if(!(!date.isAfter(endOfWeek) && !date.isBefore(startOfWeek))){
            System.out.println("You can only submit shifts for the next week");
            return false;
        }

        // add the shift to the submitting shifts
        Employee wantedEmployee = branchesList.get(0).employeeManagement.getEmployeeById(employeeId);
        wantedEmployee.addShiftToShiftTypeMap(date, shiftTypeToSubmit);
        System.out.println("Your shift has been successfully added");
        return true;
    }
}
