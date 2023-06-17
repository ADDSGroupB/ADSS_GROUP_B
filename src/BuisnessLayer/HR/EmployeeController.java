package BuisnessLayer.HR;

import DataAccessLayer.HR.EmployeeDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeController {
    public EmployeeDAO employeeDAO;

    public EmployeeController() {
        //this.registeredEmployeesInCompany = new HashMap<>();

        this.employeeDAO = EmployeeDAO.getInstance();

    }

    /* function to return specific employee from registeredEmployees. if not exist return null*/
    public Employee getEmployeeById(String employeeId) {
        Employee employee = null;
        try {
            employee = this.employeeDAO.getEmployeeByID(employeeId);
            return this.employeeDAO.getEmployeeByID(employeeId);
        }
        catch (SQLException e){
            System.out.println("SQL exception");;
        }
        return null;
    }

    public void AddJobTypeToEmployee(String employeeID, String jobType) {
        Employee findEmployee = getEmployeeById(employeeID);
        JobType jobTypeToAdd = convertToJobType(jobType);
        // add this job type to employee
        findEmployee.addEmployeesAuthorizedJobs(jobTypeToAdd);
        try {
            this.employeeDAO.update(findEmployee);
        }
        catch (SQLException e){
            System.out.println("SQL exception");;
        }

    }

    public void displayEmployeesJobs(String employeeID){
        Employee findEmployee = getEmployeeById(employeeID);
        if(findEmployee == null){
            throw new RuntimeException("Employee doesn't exist");
        }
        findEmployee.displayAuthorizedJob();
    }

    public List<String> getEmployeesJobs(String employeeID){
        Employee findEmployee = getEmployeeById(employeeID);
        if(findEmployee == null){
            throw new RuntimeException("Employee doesn't exist");
        }
        List<String> authorizedJobNames = new ArrayList<>();
        for (JobType jobType : findEmployee.employeesAuthorizedJobs) {
            authorizedJobNames.add(jobType.toString());
        }
        return authorizedJobNames;
    }

    /* function to remove a job type to a specific employee */
    public void removeJobTypeFromEmployee(String employeeID, String jobType) {
        Employee findEmployee = getEmployeeById(employeeID);
        JobType jobTypeToRemove = convertToJobType(jobType);
        // remove this job type from employee - not allow to remove general employee
        if (jobTypeToRemove == JobType.GENERAL_EMPLOYEE) {
            throw new RuntimeException("All employees have to be general employees");
        }
        boolean remove = findEmployee.removeEmployeesAuthorizedJobs(jobTypeToRemove);
        if (!remove) {
            throw new RuntimeException("The employee is not authorized to this job.");
        }
        try {
            this.employeeDAO.update(findEmployee);
        }
        catch (SQLException e){
            System.out.println("SQL exception");;
        }
    }


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
        if (jobTypeNumberString.equals("7")){
            wantedJob = JobType.CLEANER;
        }
        return wantedJob;
    }



}