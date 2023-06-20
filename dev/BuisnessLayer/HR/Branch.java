package BuisnessLayer.HR;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Branch {
    public int thisBranchID;
    public ShiftManagement shiftManagement;

    public Branch(int branchID){
        thisBranchID = branchID;
        shiftManagement = new ShiftManagement(branchID);
    }

    /* function to calculate and print the salary report for all employee (check for each employee how many shifts and their hours and multiply the salary)*/
    public String SalaryReportForAllEmployee(Map<String, Employee> registeredEmployees) {
        String salaryReport = "";
        for (String employeeID : registeredEmployees.keySet()) {
            // find this employee
            Employee findEmployee = registeredEmployees.get(employeeID);
            // calculate salary, with bonus and return the salary report for each employee
            int salary = weekWorkHoursEmployee(findEmployee) * (findEmployee.salaryPerHour + findEmployee.getBonus());
            salaryReport += "Employee: " + findEmployee.employeeFullName + " Id: " + findEmployee.employeeID + " Salary: " + salary +"\n";
        }
        return salaryReport;
    }

    /* function that calculate total hours that employee work this week*/
    public int weekWorkHoursEmployee(Employee employee) {
        Shift shift = null;
        int hourCounter = 0;
        for (LocalDate shiftDate : employee.employeesAssignmentForShifts.keySet()) {
            try {
                Set<ShiftType> shiftTypeSet = employee.employeesAssignmentForShifts.get(shiftDate);
                String shiftType = null;
                if(shiftTypeSet.contains(ShiftType.MORNING)){
                    shiftType = "morning";
                }
                else{
                    shiftType = "evening";
                }
                shift = this.shiftManagement.shiftDAO.getShiftByBranchDateAndType(thisBranchID, shiftDate.toString(), shiftType);
            }
            catch (SQLException e){
                System.out.println("SQL exception");
                System.out.println(e.getMessage());
            }
            if(shift!=null) {
                hourCounter += Duration.between(shift.startTimeOfShift, shift.endTimeOfShift).toHours();
            }
        }
        return hourCounter;
    }

    /*function that return shift by date and time*/
    public Shift getShiftByDateAndTime(LocalDate date, LocalTime time){
        Set<Shift> shitsForDay = new HashSet<>();
        try {
            shitsForDay = this.shiftManagement.shiftDAO.getAllShifts(thisBranchID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        Shift shiftToFind = null;
        if (shitsForDay != null) {
            for (Shift shift : shitsForDay) {
                // check if there is morning or evening shift
                if (shift.shiftDate.equals(date) && (time.isBefore(shift.endTimeOfShift) || time.equals(shift.endTimeOfShift)) && (time.isAfter(shift.startTimeOfShift) || time.equals(shift.startTimeOfShift))) {
                    shiftToFind = shift;
                    break;
                }
            }
        }
        return shiftToFind;
    }

    /*function that return set of shift by date*/
    public Set<Shift> getShiftByDate(LocalDate date){
        Set<Shift> shitsForBranch = new HashSet<>();
        try {
            shitsForBranch = this.shiftManagement.shiftDAO.getAllShifts(thisBranchID);
        }
        catch (SQLException e){
            System.out.println("SQL exception");
        }
        Set<Shift> shiftsForDay = new HashSet<>();
        for (Shift shift : shitsForBranch) {
            // check if there is morning or evening shift
            if (shift.shiftDate.equals(date)){
                shiftsForDay.add(shift);
            }
        }
        return shiftsForDay;
    }

    /* function to present all employees */
    public String displayAllRegisterEmployee(Map<String, Employee> registeredEmployees) {
        String output = "";
        for (String employeeID : registeredEmployees.keySet()) {
            Employee employeeToPrint = registeredEmployees.get(employeeID);
            // print this specific employee
            output += "Employee ID : " + employeeToPrint.employeeID + "," +  " Full Name : " + employeeToPrint.employeeFullName + "\n";
            output += employeeToPrint.displayAuthorizedJob();
            output += "-------------------------------------------------------------------------------\n";
        }
        return output;
    }

    public String displayAllRegisterEmployee2(Map<String, Employee> registeredEmployees) {
        String output = "";
        for (String employeeID : registeredEmployees.keySet()) {
            // find this employee
            Employee employeeToPrint = registeredEmployees.get(employeeID);
            // calculate salary, with bonus and return the salary report for each employee
            output += "Employee: " + employeeToPrint.employeeFullName + " Id: " + employeeToPrint.employeeID + " Jobs: ";
            StringBuilder jobsString = new StringBuilder();
            for (JobType job : employeeToPrint.employeesAuthorizedJobs) {
                jobsString.append(job).append(", ");
            }
            // Remove the trailing comma and space if jobsString is not empty
            if (jobsString.length() > 0) {
                jobsString.setLength(jobsString.length() - 2);
            }
            output += jobsString.toString() + "\n";
        }
        return output;
    }

}
