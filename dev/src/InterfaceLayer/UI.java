package InterfaceLayer;

import BuisnessLayer.Branch;
import BuisnessLayer.Company;
import BuisnessLayer.DataBase;
import BuisnessLayer.Employee;
import BuisnessLayer.Pair;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class UI {

    Company company;
    DataBase dataBase;

    /* Main menu for user that allowing to register as employee or HR or exit
     * this menu calling to HR or employee menu accordingly */
    public UI() {
        company = new Company();
        dataBase = new DataBase();
    }

    public void uploadDataBaseMenu(){
        Scanner scanner = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.println("Do you want to upload data base to the system? ");
            System.out.println("1. Upload employees and shifts");
            System.out.println("2. Upload employees");
            System.out.println("3. No upload");
            System.out.println("4. Exit");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, please enter 1-4.");
                scanner.next();
                continue;
            }
            switch (choice) {
                case 1:
                    company = dataBase.uploadAllDataBase();
                    menu();
                    return;
                case 2:
                    company = dataBase.uploadDataBaseEmployees();
                    menu();
                    return;
                case 3:
                    menu();
                    return;
                default:
                    System.out.println("Invalid choice, please enter 1-4.");
                    break;
            }
        }
    }
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello! Welcome to supermarket Lee!");
        int choice;
        while (true) {
            System.out.println("Enter your choice: ");
            System.out.println("1. Login as HR manager");
            System.out.println("2. Login as employee");
            System.out.println("3. Exit");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, please enter 1, 2 or 3.");
                scanner.next();
                continue;
            }
            boolean checkLogIn;
            switch (choice) {
                case 1:
                    checkLogIn = company.logInHR();
                    if (checkLogIn) {
                        hrMenu();
                    }
                    break;
                case 2:
                    Pair<Boolean, String> returnValues = company.logInEmployee();
                    if (returnValues == null)
                        break;
                    checkLogIn = returnValues.getKey();
                    String employeeID = returnValues.getValue();
                    if (checkLogIn) {
                        employeeMenu(employeeID);
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, please enter 1, 2 or 3.");
                    break;
            }
        }
    }

    public void employeeMenu(String employeeID) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.println("Enter your choice: ");
            System.out.println("1. Submit available shift in the next week");
            System.out.println("2. Exit");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, please enter 1, 2 or 3.");
                scanner.next();
                continue;
            }
            switch (choice) {
                case 1:
                    company.submitAShiftForEmployee(employeeID);
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid choice, please enter 1, 2 or 3.");
                    break;
            }
        }
    }

    public void hrMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.println("Enter your choice: ");
            System.out.println("1. Register new employee");
            System.out.println("2. Choose a branch");
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
                    company.registerNewEmployee();
                    break;
                case 2:
                    String branchToAdd = "0";
                    while (!(branchToAdd.matches("^[1-9]$") || branchToAdd.matches("10"))) {
                        System.out.print("On what branch of the supermarket do you want to work? (1-10) : ");
                        branchToAdd = scanner.next();
                    }
                    int branchInt = Integer.parseInt(branchToAdd) - 1;
                    hrBranchMenu(branchInt);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice, please enter 1, 2 or 3.");
                    break;
            }
        }
    }

    public void hrBranchMenu(int branch) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        Branch thisBranch = company.branchesList.get(branch);
        while (true) {
            int choice1 = 0;
            int choice2 = 0;
            int choice3 = 0;
            System.out.println("Enter your choice: ");
            System.out.println("1. Create shifts for next week");
            System.out.println("2. Assign/remove employee from shift");
            System.out.println("3. Edit shift details");
            System.out.println("4. Edit employee details");
            System.out.println("5. Display next week's shifts details");
            System.out.println("6. Cancelled product");
            System.out.println("7. Employees salary report");
            System.out.println("8. Display all employees");
            System.out.println("9. Delete employee");
            System.out.println("10. Exit");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, please enter 1 - 10");
                scanner.next();
                continue;
            }
            switch (choice) {
                case 1:
                    thisBranch.shiftManagement.createNewShiftsForNextWeek();
                    break;
                case 2:
                    Map<String, Employee> registers = thisBranch.employeeManagement.registeredEmployees;
                    thisBranch.shiftManagement.editAShift(registers);
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
                                thisBranch.shiftManagement.changeShiftTime();
                                break;
                            case 2:
                                thisBranch.shiftManagement.changeJobTypeCount();
                            case 3:
                                break;
                            default:
                                System.out.println("Invalid choice, please enter 1, 2 or 3.");
                                break;
                        }
                    }
                    break;
                case 4:
                    // add and remove job type to employee, set salary, add personal details, change bank account
                    while (choice3 != 6) {
                        System.out.println("Enter your choice: ");
                        System.out.println("1. Add job authorization");
                        System.out.println("2. Remove job authorization");
                        System.out.println("3. Change salary per hour");
                        System.out.println("4. Change back account details");
                        System.out.println("5. Add personal details");
                        System.out.println("6. Exit");
                        try {
                            choice3 = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid choice, please enter 1 - 6");
                            scanner.next();
                            continue;
                        }
                        switch (choice3) {
                            case 1:
                                thisBranch.employeeManagement.AddJobTypeToEmployee();
                                break;
                            case 2:
                                thisBranch.employeeManagement.removeJobTypeFromEmployee();
                                break;
                            case 3:
                                thisBranch.employeeManagement.setSalaryPerHourForEmployee();
                                break;
                            case 4:
                                thisBranch.employeeManagement.setBankAccountForEmployee();
                                break;
                            case 5:
                                thisBranch.employeeManagement.setPersonalDetailsForEmployee();
                                break;
                            case 6:
                                break;
                            default:
                                System.out.println("Invalid choice, please enter 1-6.");
                                break;
                        }
                    }
                    break;
                case 5:
                    thisBranch.shiftManagement.displayAllShiftsDetails();
                    break;
                case 6:
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
                                thisBranch.shiftManagement.displayCashierCancellation();
                                break;
                            case 2:
                                thisBranch.shiftManagement.addCashierCancellation();
                                break;
                            case 3:
                                thisBranch.shiftManagement.deleteCashierCancellation();
                                break;
                            case 4:
                                break;
                            default:
                                System.out.println("Invalid choice, please enter 1-4.");
                                break;
                        }
                    }
                    break;
                case 7:
                    thisBranch.employeeManagement.SalaryReportForAllEmployee();
                    break;
                case 8:
                    thisBranch.employeeManagement.displayAllRegisterEmployee();
                    break;
                case 9:
                    thisBranch.employeeManagement.deleteRegisteredEmployee();
                    break;
                case 10:
                    return;
                default:
                    System.out.println("Invalid choice, please enter 1-10.");
                    break;

            }

        }
    }

}






















/*תפריט:
עדכון משמרת - הוספה או מחיקת עובד
שינוי שעות של משמרת
יצירת מבנה משמרות
call the function that check which shift is not valid (24 before)
/*System.out.print("These are the shifts details for next week: ");
        call to function that prints all the shifts*/


/*
ToDo
3פונקציות
LOGIN
MANU
TEST
מסמך אפיון
לבדוק במעבדות*/