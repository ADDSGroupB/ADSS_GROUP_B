package DataAccessLayer.HR;

import BuisnessLayer.HR.Employee;
import BuisnessLayer.HR.JobType;
import BuisnessLayer.HR.ShiftType;
import DataAccessLayer.Database;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class EmployeeDAO {

    private static EmployeeDAO instance = null;
    public HashMap<String, Employee> employees;
    private Connection conn;

    private EmployeeDAO() throws SQLException {
        conn = Database.connect();
        employees = new HashMap<>();
    }

    public static EmployeeDAO getInstance(){
        if(instance ==null){
            try {
                instance = new EmployeeDAO();
            }
            catch (SQLException e){
                System.out.println("SQL exception");
            }
        }
        return instance;
    }

    public HashMap<String, Employee> getAllEmployees() throws SQLException{
        try {
            conn = Database.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employees");
            while (rs.next()) {
                String employeeId = rs.getString("employeeID");
                getEmployeeByID(employeeId);
            }
            Database.closeConnection(conn);

        }
        catch (Exception e){
            System.out.println("Exception has occurred in getAllEmployees EmployeeDAO.");
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }

    public HashMap<String, Employee> getAllEmployeesInBranch(int branchID) throws SQLException{
        branchID= branchID+1;
        try {
            conn = Database.connect();
            String sql = "SELECT * FROM employees JOIN employeesInBranch ON employees.employeeID = employeesInBranch.employeeID WHERE employeesInBranch.branchID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, branchID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String employeeId = rs.getString("employeeID");
                getEmployeeByID(employeeId);
            }
            Database.closeConnection(conn);
            return this.employees;
        }
        catch (Exception e){
            System.out.println("Exception has occurred in getAllEmployeesInBranch EmployeeDAO.");
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }

    public Employee getEmployeeByID(String employeeId) throws SQLException{
        try {
            conn = Database.connect();
            for (String employeeID : employees.keySet()) {
                if (employeeID.equals(employeeId)) {
                    Database.closeConnection(conn);
                    return employees.get(employeeID);
                }
            }

            String sql8 = "SELECT COUNT(*) FROM employees WHERE employeeID = ?";
            PreparedStatement stmt8 = conn.prepareStatement(sql8);
            stmt8.setString(1, employeeId);
            ResultSet rs8 = stmt8.executeQuery();
            int count = rs8.getInt(1);
            if (count == 0) {
                Database.closeConnection(conn);
                return null;
            }

            String sql = "SELECT * FROM employees WHERE employeeID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            String employeeFullName = rs.getString("employeeFullName");
            String employeeID = rs.getString("employeeID");
            String bankAccountInformation = rs.getString("bankAccountInformation");
            String startOfEmploymentDate = rs.getString("startOfEmploymentDate");
            int salaryPerHour = (rs.getInt("salaryPerHour"));
            int bonus = rs.getInt("bonus");
            String employeePersonalDetails = (rs.getString("employeePersonalDetails"));
            String termsOfEmployment = rs.getString("termsOfEmployment");
            int shiftCountForWeek = rs.getInt("shiftCountForWeek");
            String employeePassword = rs.getString("employeePassword");

            String sql1 = "SELECT jobType FROM employeeAuthorizedJobs WHERE employeeID = ?";
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            stmt1.setString(1, employeeId);

            ResultSet rs1 = stmt1.executeQuery();
            ArrayList<JobType> employeesAuthorizedJobs = new ArrayList<>();
            while (rs1.next()) {
                JobType jobType = null;
                if (rs1.getString("jobType").equals("cashier")) {
                    jobType = JobType.CASHIER;
                }
                if (rs1.getString("jobType").equals("storekeeper")) {
                    jobType = JobType.STOREKEEPER;
                }
                if (rs1.getString("jobType").equals("general_employee")) {
                    jobType = JobType.GENERAL_EMPLOYEE;
                }
                if (rs1.getString("jobType").equals("security")) {
                    jobType = JobType.SECURITY;
                }
                if (rs1.getString("jobType").equals("usher")) {
                    jobType = JobType.USHER;
                }
                if (rs1.getString("jobType").equals("cleaner")) {
                    jobType = JobType.CLEANER;
                }
                if (rs1.getString("jobType").equals("shift_manager")) {
                    jobType = JobType.SHIFT_MANAGER;
                }
                employeesAuthorizedJobs.add(jobType);
            }


            String sql2 = "SELECT branchID FROM employeesInBranch WHERE employeeID = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1, employeeId);

            ResultSet rs2 = stmt2.executeQuery();
            Set<Integer> branches = new HashSet<>();
            while (rs2.next()) {
                int branch = rs2.getInt("branchID");
                branches.add(branch);
            }

            Employee employee = new Employee(employeeFullName, employeeID, bankAccountInformation, startOfEmploymentDate, salaryPerHour, employeePersonalDetails,
                    employeesAuthorizedJobs, termsOfEmployment, employeePassword, branches);
            employee.bonus = bonus;
            employee.shiftCountForWeek = shiftCountForWeek;
            //update maps
            Map<LocalDate, Set<ShiftType>> employeeSubmittingShifts = new HashMap<>();

            String sql3 = "SELECT shiftDate, shiftType FROM employeeSubmittingShifts WHERE employeeID = ?";
            PreparedStatement stmt3 = conn.prepareStatement(sql3);
            stmt3.setString(1, employeeId);
            ResultSet rs3 = stmt3.executeQuery();
            if (rs3.isBeforeFirst()) {
                while (rs3.next()) {
                    LocalDate shiftDate =LocalDate.parse(rs3.getString("shiftDate"));
                    String shiftTypeString1 = rs3.getString("shiftType");
                    ShiftType shiftType = null;
                    if(shiftTypeString1.equals("morning")){
                        shiftType = ShiftType.MORNING;
                    }
                    else{
                        shiftType = ShiftType.EVENING;
                    }
                    if (employeeSubmittingShifts.containsKey(shiftDate)) {
                        employeeSubmittingShifts.get(shiftDate).add(shiftType);
                    } else {
                        Set<ShiftType> shiftTypeSet = new HashSet<>();
                        shiftTypeSet.add(shiftType);
                        employeeSubmittingShifts.put(shiftDate, shiftTypeSet);
                    }
                }
            }
            employee.employeeSubmittingShifts = employeeSubmittingShifts;

            Map<LocalDate, Set<ShiftType>> employeesAssignmentForShifts = new HashMap<>();
            LocalDate currentDate = LocalDate.now();
            LocalDate startOfWeek = currentDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
            LocalDate endOfWeek = startOfWeek.plusDays(7); // add six days to get the end of the week
            String sql4 = "SELECT shiftDate, shiftType FROM employeeAssignedInShift WHERE employeeID = ? AND shiftDate BETWEEN ? AND ?";
            PreparedStatement stmt4 = conn.prepareStatement(sql4);
            stmt4.setString(1, employeeId);
            stmt4.setString(2, startOfWeek.toString());
            stmt4.setString(3, endOfWeek.toString());
            ResultSet rs4 = stmt4.executeQuery();
            if (rs4.isBeforeFirst()) {
                while (rs4.next()) {
                    String shiftDateStr = rs4.getString("shiftDate");
                    LocalDate shiftDate = LocalDate.parse(shiftDateStr);
                    String shiftTypeString1 = rs4.getString("shiftType");
                    ShiftType shiftType = null;
                    if(shiftTypeString1.equals("morning")){
                        shiftType = ShiftType.MORNING;
                    }
                    else{
                        shiftType = ShiftType.EVENING;
                    }
                    if (employeesAssignmentForShifts.containsKey(shiftDate)) {
                        employeesAssignmentForShifts.get(shiftDate).add(shiftType);
                    } else {
                        Set<ShiftType> shiftTypeSet = new HashSet<>();
                        shiftTypeSet.add(shiftType);
                        employeesAssignmentForShifts.put(shiftDate, shiftTypeSet);
                    }
                }
            }
            employee.employeesAssignmentForShifts = employeesAssignmentForShifts;

            // add the employee
            employees.put(employeeID, employee);
            Database.closeConnection(conn);
            return employee;
        }
        catch (Exception e){
            System.out.println("Exception has occurred in getEmployeeByID EmployeeDAO.");
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }

    public boolean insert(Employee employee) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = Database.connect();
            if (getEmployeeByID(employee.employeeID) != null) {
                Database.closeConnection(conn);
                return false;
            }

            stmt = conn.prepareStatement("INSERT INTO employees (employeeID, employeeFullName, bankAccountInformation, startOfEmploymentDate, salaryPerHour, bonus, " +
                    "employeePersonalDetails,termsOfEmployment,shiftCountForWeek,employeePassword) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, employee.employeeID);
            stmt.setString(2, employee.employeeFullName);
            stmt.setString(3, employee.getBankAccountInformation());
            stmt.setString(4, employee.startOfEmploymentDate);
            stmt.setInt(5, employee.salaryPerHour);
            stmt.setInt(6, employee.bonus);
            stmt.setString(7, employee.employeePersonalDetails);
            stmt.setString(8, employee.termsOfEmployment);
            stmt.setInt(9, employee.shiftCountForWeek);
            stmt.setString(10, employee.getEmployeePassword());
            stmt.executeUpdate();


            for (Integer branch : employee.branches) {
                PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO employeesInBranch (employeeID, branchID) VALUES (?, ?)");
                stmt1.setString(1, employee.employeeID);
                stmt1.setInt(2, branch);
                stmt1.executeUpdate();
            }


            for (JobType employeeJobType : employee.employeesAuthorizedJobs) {
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO employeeAuthorizedJobs (employeeID, jobType) VALUES (?, ?)");
                stmt2.setString(1, employee.employeeID);
                stmt2.setString(2, employeeJobType.toString().toLowerCase());
                stmt2.executeUpdate();
            }


            for (LocalDate shiftDate : employee.employeeSubmittingShifts.keySet()) {
                Set<ShiftType> shiftTypeSet = employee.employeeSubmittingShifts.get(shiftDate);
                for (ShiftType shiftType : shiftTypeSet) {
                    PreparedStatement stmt4 = conn.prepareStatement("INSERT INTO employeeSubmittingShifts (employeeID, shiftDate, shiftType) VALUES (?, ?, ?)");
                    stmt4.setString(1, employee.employeeID);
                    stmt4.setString(2, shiftDate.toString());
                    stmt4.setString(3, shiftType.toString().toLowerCase());
                    stmt4.executeUpdate();
                }
            }
            // add the employee
            employees.put(employee.employeeID, employee);
            return true;
        }
        catch (Exception e){
            System.out.println("Exception has occurred in insert EmployeeDAO.");
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
            return false;
        }
    }


    public boolean update(Employee employee) throws SQLException {
        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("UPDATE employees SET employeeFullName = ?, bankAccountInformation= ? , " +
                    "startOfEmploymentDate = ?, salaryPerHour = ?, bonus = ?, employeePersonalDetails = ?,termsOfEmployment = ?,shiftCountForWeek = ?," +
                    "employeePassword = ? WHERE employeeID = ?");
            stmt.setString(1, employee.employeeFullName);
            stmt.setString(2, employee.getBankAccountInformation());
            stmt.setString(3, employee.startOfEmploymentDate);
            stmt.setInt(4, employee.salaryPerHour);
            stmt.setInt(5, employee.bonus);
            stmt.setString(6, employee.employeePersonalDetails);
            stmt.setString(7, employee.termsOfEmployment);
            stmt.setInt(8, employee.shiftCountForWeek);
            stmt.setString(9, employee.getEmployeePassword());
            stmt.setString(10, employee.employeeID);
            stmt.executeUpdate();

            for (JobType employeeJobType : employee.employeesAuthorizedJobs) {
                PreparedStatement stmt1 = conn.prepareStatement("SELECT COUNT(jobType) FROM employeeAuthorizedJobs WHERE employeeID = ? AND jobType = ?");
                stmt1.setString(1, employee.employeeID);
                stmt1.setString(2, employeeJobType.toString().toLowerCase());
                ResultSet rs1 = stmt1.executeQuery();
                int count = rs1.getInt(1);
                if (count == 0) {
                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO employeeAuthorizedJobs (employeeID, jobType) VALUES (?, ?)");
                    stmt2.setString(1, employee.employeeID);
                    stmt2.setString(2, employeeJobType.toString().toLowerCase());
                    stmt2.executeUpdate();

                }
            }

            PreparedStatement stmt3 = conn.prepareStatement("SELECT * FROM employeeAuthorizedJobs WHERE employeeID = ?");
            stmt3.setString(1, employee.employeeID);
            ResultSet rs3 = stmt3.executeQuery();
            while (rs3.next()) {
                JobType jobType = null;
                if (rs3.getString("jobType").equals("cashier")) {
                    jobType = JobType.CASHIER;
                }
                if (rs3.getString("jobType").equals("storekeeper")) {
                    jobType = JobType.STOREKEEPER;
                }
                if (rs3.getString("jobType").equals("general_employee")) {
                    jobType = JobType.GENERAL_EMPLOYEE;
                }
                if (rs3.getString("jobType").equals("security")) {
                    jobType = JobType.SECURITY;
                }
                if (rs3.getString("jobType").equals("usher")) {
                    jobType = JobType.USHER;
                }
                if (rs3.getString("jobType").equals("cleaner")) {
                    jobType = JobType.CLEANER;
                }
                if (rs3.getString("jobType").equals("shift_manager")) {
                    jobType = JobType.SHIFT_MANAGER;
                }
                if (!(employee.employeesAuthorizedJobs.contains(jobType)) && jobType != null) {
                    PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM employeeAuthorizedJobs WHERE employeeID = ? AND jobType = ?");
                    deleteStmt.setString(1, employee.employeeID);
                    deleteStmt.setString(2, jobType.toString().toLowerCase());
                    deleteStmt.executeUpdate();
                }
            }

            for (LocalDate shiftDate : employee.employeeSubmittingShifts.keySet()) {
                Set<ShiftType> shiftTypeSet = employee.employeeSubmittingShifts.get(shiftDate);
                for (ShiftType shiftType : shiftTypeSet) {
                    PreparedStatement stmt4 = conn.prepareStatement("SELECT employeeID FROM employeeSubmittingShifts WHERE employeeID = ? AND shiftDate = ? AND shiftType = ?");
                    stmt4.setString(1, employee.employeeID);
                    stmt4.setString(2, shiftDate.toString());
                    stmt4.setString(3, shiftType.toString().toLowerCase());
                    ResultSet rs4 = stmt4.executeQuery();
                    if (!rs4.next() || rs4.isAfterLast()) {
                        PreparedStatement stmt5 = conn.prepareStatement("INSERT INTO employeeSubmittingShifts (employeeID, shiftDate, shiftType) VALUES (?, ?, ?)");
                        stmt5.setString(1, employee.employeeID);
                        stmt5.setString(2, shiftDate.toString());
                        stmt5.setString(3, shiftType.toString().toLowerCase());
                        stmt5.executeUpdate();

                    }
                }
            }

            Database.closeConnection(conn);
            return true;
        }
        catch (Exception e){
            System.out.println("Exception has occurred in update EmployeeDAO.");
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return false;
    }

    public void delete() throws SQLException{
        Statement stmt = null;
        try {
            conn = Database.connect();
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM employees");
            stmt.executeUpdate("DELETE FROM employeesInBranch");
            stmt.executeUpdate("DELETE FROM employeeAuthorizedJobs");
            stmt.executeUpdate("DELETE FROM employeesubmittingShifts");
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while deleting table employees: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }
}
