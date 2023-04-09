package BuisnessLayer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class DataBase {

    Company companyData;
    public DataBase() {
        companyData = new Company();
    }
    public Company uploadAllDataBase(){
        createEmployees();
        createShifts();
        return this.companyData;
    }

    public Company uploadDataBaseEmployees(){
        createEmployees();
        return this.companyData;
    }

    public void createEmployees(){
        ArrayList<JobType> employeeJobs1 = new ArrayList<>(Arrays.asList(JobType.CASHIER, JobType.CLEANER));
        Employee employee1 = new Employee("Linoy Bitan", "1", "Bank Of Life",
                "2023-05-01", 100, "Student", employeeJobs1, "less is more", "1");
        ArrayList<JobType> employeeJobs2 = new ArrayList<>(Arrays.asList(JobType.SECURITY, JobType.CLEANER));
        Employee employee2 = new Employee("Netta Meiri", "2", "Bank Of Life",
                "2023-05-01", 50, "Student", employeeJobs2, "less is more", "2");
        ArrayList<JobType> employeeJobs3 = new ArrayList<>(Arrays.asList(JobType.SHIFT_MANAGER, JobType.STOREKEEPER));
        Employee employee3 = new Employee("Sarah Levy", "3", "Bank Of Life",
                "2023-05-01", 200, "Parent", employeeJobs3, "less is more", "3");
        ArrayList<JobType> employeeJobs4 = new ArrayList<>(Arrays.asList(JobType.USHER, JobType.SHIFT_MANAGER));
        Employee employee4 = new Employee("Itay Levy", "4", "Bank Of Life",
                "2023-05-01", 200, "Parent", employeeJobs4, "less is more", "4");
        ArrayList<JobType> employeeJobs5 = new ArrayList<>(Collections.singletonList(JobType.USHER));
        Employee employee5 = new Employee("Moshe Cohen", "5", "Bank Of Life",
                "2023-05-01", 90, "Student", employeeJobs5, "less is more", "5");
        companyData.registeredEmployeesInCompany.put("1", employee1);
        companyData.registeredEmployeesInCompany.put("2", employee2);
        companyData.registeredEmployeesInCompany.put("3", employee3);
        companyData.registeredEmployeesInCompany.put("4", employee4);
        companyData.registeredEmployeesInCompany.put("5", employee5);

        companyData.branchesList.get(0).employeeManagement.addNewEmployee(employee1);
        companyData.branchesList.get(0).employeeManagement.addNewEmployee(employee2);
        companyData.branchesList.get(0).employeeManagement.addNewEmployee(employee3);
        companyData.branchesList.get(0).employeeManagement.addNewEmployee(employee4);
        companyData.branchesList.get(0).employeeManagement.addNewEmployee(employee5);

        companyData.branchesList.get(1).employeeManagement.addNewEmployee(employee1);
        companyData.branchesList.get(1).employeeManagement.addNewEmployee(employee2);
        companyData.branchesList.get(1).employeeManagement.addNewEmployee(employee3);


        // search for the date of sunday of that week
        LocalDate currentDate = LocalDate.now();
        LocalDate startOfWeek = currentDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        employee1.employeeSubmittingShifts.put(startOfWeek,  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
        employee2.employeeSubmittingShifts.put(startOfWeek,  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
        employee3.employeeSubmittingShifts.put(startOfWeek,  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
        employee4.employeeSubmittingShifts.put(startOfWeek,  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));

        employee1.employeeSubmittingShifts.put(startOfWeek.plusDays(1),  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
        employee2.employeeSubmittingShifts.put(startOfWeek.plusDays(1),  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
        employee3.employeeSubmittingShifts.put(startOfWeek.plusDays(1),  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
        employee4.employeeSubmittingShifts.put(startOfWeek.plusDays(1),  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
        employee5.employeeSubmittingShifts.put(startOfWeek.plusDays(1),  new HashSet<>(Arrays.asList(ShiftType.MORNING, ShiftType.EVENING)));
    }

    public void createShifts(){
        LocalDate currentDate = LocalDate.now();
        LocalDate startOfWeek = currentDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        LocalTime startTimeofMorningShift = LocalTime.of(8,0);
        LocalTime endTimeofMorningShift = LocalTime.of(14,0);
        LocalTime startTimeofEveningShift = LocalTime.of(14,0);
        LocalTime endTimeofEveningShift = LocalTime.of(20,0);
        Shift shiftSundayMorning = new Shift(startOfWeek, ShiftType.MORNING, startTimeofMorningShift, endTimeofMorningShift,
                1, 0,0,0,0,1);
        Shift shiftSundayEvening = new Shift(startOfWeek, ShiftType.EVENING, startTimeofEveningShift, endTimeofEveningShift,
                0, 0,0,0,0,0);
        Shift shiftMondayMorning = new Shift(startOfWeek.plusDays(1), ShiftType.MORNING, startTimeofMorningShift, endTimeofMorningShift,
                0, 1,0,0,1,2);

        companyData.branchesList.get(0).shiftManagement.shiftsAssignmentForAWeek.put(startOfWeek, new HashSet<>(Arrays.asList(shiftSundayMorning, shiftSundayEvening)));
        companyData.branchesList.get(0).shiftManagement.shiftsAssignmentForAWeek.put(startOfWeek.plusDays(1), new HashSet<>(Collections.singletonList(shiftMondayMorning)));
    }
}
