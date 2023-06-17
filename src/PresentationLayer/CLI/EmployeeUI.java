package PresentationLayer.CLI;

import BuisnessLayer.HR.Pair;
import ServiceLayer.HR.CompanyService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EmployeeUI {
    CompanyService service;

    public EmployeeUI() {
        service = new CompanyService();
    }

    public void employeeMenu() {
        System.out.println("Hello! Welcome to supermarket Lee!");
        String employeeID = "";
        boolean isRegistered = false;

        while (!isRegistered) {
            employeeID = getEmployeeId();
            try {
                service.checkIfEmployeesNotInCompany(employeeID);
                isRegistered = true;
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        Scanner scanner = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.println("Enter your choice: ");
            System.out.println("1. Submit available shift in the next week");
            System.out.println("2. Check expected transports in shift");
            System.out.println("3. Exit");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, please enter 1, 2 or 3.");
                scanner.next();
                continue;
            }
            switch (choice) {
                case 1:
                    submitAShiftForEmployee(employeeID);
                    break;
                case 2:
                    getTransportsForShift(employeeID);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice, please enter 1, 2 or 3.");
                    break;
            }
        }
    }


    // -------------------------------------------Employee functions--------------------------------------------------//


    /* function to display all transports in a shift to ShiftManger Or StoreKeeper*/
    public void getTransportsForShift(String employeeID){
        // ask for day and shift type
        Pair<String, String> returnValues =  getShiftDetails();
        //ask for branch
        Scanner scanner = new Scanner(System.in);
        String branchToAdd = "0";
        while (!(branchToAdd.matches("^[1-9]$") || branchToAdd.matches("10"))) {
            System.out.print("On what branch of the supermarket is the shift you are refering to? (1-10) : ");
            branchToAdd = scanner.next();
        }
        int branchInt = Integer.parseInt(branchToAdd);
        try {
            System.out.println(service.getTransportsForShift(returnValues.getKey(), returnValues.getValue(), branchInt, employeeID));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /* function for employees to submit a shift they can work */
    public void submitAShiftForEmployee(String employeeId) {
        Pair<String, String> returnValues =  getShiftDetails();
        try {
            service.submitAShiftForEmployee(employeeId, returnValues.getKey(), returnValues.getValue());
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Your shift has been successfully added");
    }


    //---------------------------------------------Helper function---------------------------------------------------//


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

}
