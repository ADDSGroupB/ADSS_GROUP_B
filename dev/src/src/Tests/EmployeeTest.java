package Tests;

import BuisnessLayer.HR.Employee;
import BuisnessLayer.HR.JobType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {
    private Employee employee;

    @BeforeEach
    void setUp() {
        ArrayList<JobType> jobTypes = new ArrayList<>();
        jobTypes.add(JobType.STOREKEEPER);
        Set<Integer> branches = new HashSet<>();
        branches.add(1);
        employee = new Employee("Linoy Bitan", "318305570", "Hapoalim", "22-04-2023", 35,"Student", jobTypes, "3 shifts","linoy", branches);
    }

    //getters
    @Test
    void getEmployeePassword() {
        assertEquals("linoy", employee.getEmployeePassword());
    }

    @Test
    void getEmployeeFullName(){
        assertEquals( "Linoy Bitan", employee.getEmployeeFullName());
    }

    @Test
    void getBonus() {
        assertEquals(0, employee.getBonus());
    }

    @Test
    void getBankAccount() {
        assertEquals("Hapoalim", employee.getBankAccountInformation());
    }

    @Test
    void getSalaryPerHour() {
        assertEquals(35, employee.getSalaryPerHour());
    }

    @Test
    void displayEmployeeJobs(){
        String output = "";
        output += "Employee's authorized jobs : \n" + "- " + "STOREKEEPER" + "\n";
        assertEquals(output, employee.displayAuthorizedJob());
    }

    //setters
    @Test
    void setBonus() {
        employee.setBonus(5);
        assertEquals(5, employee.getBonus());
    }

    @Test
    void setSalaryPerHour() {
        employee.setSalaryPerHour(100);
        assertEquals(100, employee.getSalaryPerHour());
    }


    @Test
    void addPersonalDetails() {
        employee.addEmployeePersonalDetails("mother");
        assertEquals("Student, mother", employee.getEmployeePersonalDetails());
    }
    @Test
    void addAuthorizedJob() {
        ArrayList<JobType> jobs = new ArrayList<>();
        jobs.add(JobType.STOREKEEPER);
        jobs.add(JobType.USHER);
        employee.addEmployeesAuthorizedJobs(JobType.USHER);
        assertEquals(jobs, employee.getEmployeesAuthorizedJobs());
    }
}
