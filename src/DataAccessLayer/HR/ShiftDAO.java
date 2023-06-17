package DataAccessLayer.HR;

import BuisnessLayer.HR.JobType;
import BuisnessLayer.HR.Shift;
import BuisnessLayer.HR.ShiftType;
import DataAccessLayer.Database;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.*;


public class ShiftDAO {
    private static ShiftDAO instance = null;
    private HashMap<Integer, Set<Shift>> shifts;
    private Connection conn;

    private ShiftDAO() throws SQLException {
        //conn = Database.connect();
        shifts = new HashMap<>();
    }

    public static ShiftDAO getInstance(){
        if(instance ==null){
            try {
                instance = new ShiftDAO();
            }
            catch (SQLException e){
                System.out.println("SQL exception");
            }
        }
        return instance;
    }

    public Set<Shift> getAllShifts(int branchID) throws SQLException{
        try {
            conn = Database.connect();
            LocalDate currentDate = LocalDate.now();
            LocalDate startOfWeek = currentDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
            LocalDate endOfWeek = startOfWeek.plusDays(7); // add six days to get the end of the week
            String sql = "SELECT * FROM shifts WHERE branchID = ? AND shiftDate BETWEEN ? AND ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, branchID);
            stmt.setString(2, startOfWeek.toString());
            stmt.setString(3, endOfWeek.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String shiftDateString = rs.getString("shiftDate");
                    String shiftTypeString = rs.getString("shiftType");
                    int branchId = rs.getInt("branchID");
                    getShiftByBranchDateAndType(branchId, shiftDateString, shiftTypeString);
                }
            }
            Database.closeConnection(conn);
            return this.shifts.get(branchID);
        }
        catch (Exception e){
            System.out.println("Exception has occurred in getAllShift ShiftDAO.");
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }


    public Shift getShiftByBranchDateAndType(int branchId, String shiftDateString, String shiftTypeString) throws SQLException {
        try {
            conn = Database.connect();
            Set<Shift> shiftsInBranch = shifts.get(branchId);
            if(shiftsInBranch!=null) {
                for (Shift shift : shiftsInBranch) {
                    if (shift.shiftDate.toString().equals(shiftDateString) && shift.shiftType.toString().toLowerCase().equals(shiftTypeString)) {
                        Database.closeConnection(conn);
                        return shift;
                    }
                }
            }

            String sql8 = "SELECT COUNT(*) FROM shifts WHERE branchID = ? AND shiftDate = ? AND shiftType = ?";
            PreparedStatement stmt8 = conn.prepareStatement(sql8);
            stmt8.setInt(1, branchId);
            stmt8.setString(2, shiftDateString);
            stmt8.setString(3,  shiftTypeString);
            ResultSet rs8 = stmt8.executeQuery();
            int count = rs8.getInt(1);
            if (count == 0) {
                Database.closeConnection(conn);
                return null;
            }

            String sql = "SELECT * FROM shifts WHERE branchID = ? AND shiftDate = ? AND shiftType = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, branchId);
            stmt.setString(2, shiftDateString);
            stmt.setString(3, shiftTypeString);
            ResultSet rs = stmt.executeQuery();

            LocalDate shiftDate = LocalDate.parse(rs.getString("shiftDate"));
            String shiftTypeString1 = rs.getString("shiftType");
            ShiftType shiftType = null;
            if(shiftTypeString1.equals("morning")){
                shiftType = ShiftType.MORNING;
            }
            else{
                shiftType = ShiftType.EVENING;
            }
            LocalTime startTimeOfShift = LocalTime.parse(rs.getString("startTime"));
            LocalTime endTimeOfShift = LocalTime.parse(rs.getString("endTime"));


            String sql1 = "SELECT * FROM wantedJobsInShift WHERE branchID = ? AND shiftDate = ? AND shiftType = ?";
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            stmt1.setInt(1, branchId);
            stmt1.setString(2, shiftDateString);
            stmt1.setString(3, shiftTypeString);
            ResultSet rs1 = stmt1.executeQuery();
            int cashierCount = 0;
            int storeKeeperCount = 0;
            int generalEmployeeCount = 0;
            int securityCount = 0;
            int usherCount = 0;
            int cleanerCount = 0;
            int shiftManagerCount = 0;
            if (rs1.isBeforeFirst()) {
                while (rs1.next()) {
                    if (rs1.getString("jobType").equals("cashier")) {
                        cashierCount = rs1.getInt("numberOfJobType");
                    }
                    if (rs1.getString("jobType").equals("storekeeper")) {
                        storeKeeperCount = rs1.getInt("numberOfJobType");
                    }
                    if (rs1.getString("jobType").equals("general_employee")) {
                        generalEmployeeCount = rs1.getInt("numberOfJobType");
                    }
                    if (rs1.getString("jobType").equals("security")) {
                        securityCount = rs1.getInt("numberOfJobType");
                    }
                    if (rs1.getString("jobType").equals("usher")) {
                        usherCount = rs1.getInt("numberOfJobType");
                    }
                    if (rs1.getString("jobType").equals("cleaner")) {
                        cleanerCount = rs1.getInt("numberOfJobType");
                    }
                    if (rs1.getString("jobType").equals("shift_manager")) {
                        shiftManagerCount = rs1.getInt("numberOfJobType");
                    }

                }
            }
            Shift shift = new Shift(shiftDate, shiftType, startTimeOfShift, endTimeOfShift, cashierCount, storeKeeperCount, generalEmployeeCount,
                    securityCount, usherCount, cleanerCount, shiftManagerCount);


            String sql2 = "SELECT employeeID, product FROM shiftCancellations WHERE branchID = ? AND shiftDate = ? AND shiftType = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setInt(1, branchId);
            stmt2.setString(2, shiftDateString);
            stmt2.setString(3, shiftTypeString);
            ResultSet rs2 = stmt2.executeQuery();
            Map<String, List<String>> shiftCancellations = new HashMap<>();
            if (rs2.isBeforeFirst()) {
                // Loop through the results and populate the HashMap
                while (rs2.next()) {
                    String employeeID = rs2.getString("employeeID");
                    String product = rs2.getString("product");
                    if (shiftCancellations.containsKey(employeeID)) {
                        shiftCancellations.get(employeeID).add(product);
                    } else {
                        List<String> productList = new ArrayList<>();
                        productList.add(product);
                        shiftCancellations.put(employeeID, productList);
                    }
                }
            }
            shift.cancellationInCashBox = shiftCancellations;


            String sql3 = "SELECT employeeID, jobType FROM employeeAssignedInShift WHERE branchID = ? AND shiftDate = ? AND shiftType = ?";
            PreparedStatement stmt3 = conn.prepareStatement(sql3);
            stmt3.setInt(1, branchId);
            stmt3.setString(2, shiftDateString);
            stmt3.setString(3, shiftTypeString);
            ResultSet rs3 = stmt3.executeQuery();
            // Create a HashMap to store the jobType and employee count data
            Map<JobType, Integer> assignedJobCountInShift = new HashMap<>();
            assignedJobCountInShift.put(JobType.SHIFT_MANAGER, 0);
            assignedJobCountInShift.put(JobType.CASHIER, 0);
            assignedJobCountInShift.put(JobType.STOREKEEPER, 0);
            assignedJobCountInShift.put(JobType.USHER, 0);
            assignedJobCountInShift.put(JobType.CLEANER, 0);
            assignedJobCountInShift.put(JobType.GENERAL_EMPLOYEE, 0);
            assignedJobCountInShift.put(JobType.SECURITY, 0);
            // Create a HashMap to store the employeeID and jobType data
            Map<String, JobType> employeesAndJobsInShift = new HashMap<>();

            // Loop through the results and populate the HashMaps
            if (rs3.isBeforeFirst()) {
                while (rs3.next()) {
                    String employeeID = rs3.getString("employeeID");
                    String jobTypeString = rs3.getString("jobType");
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

                    // Update the assignedJobCountInShift HashMap
                    if (assignedJobCountInShift.containsKey(jobType)) {
                        assignedJobCountInShift.put(jobType, assignedJobCountInShift.get(jobType) + 1);
                    } else {
                        assignedJobCountInShift.put(jobType, 1);
                    }
                    // Update the employeesAndJobsInShift HashMap
                    employeesAndJobsInShift.put(employeeID, jobType);
                }
            }
            shift.employeesAndJobsInShift = employeesAndJobsInShift;
            shift.assignedJobCountInShift = assignedJobCountInShift;


            // add a new key (dateOfShift) with an empty list as the initial value if it doesn't exist yet
            if (!(shifts.containsKey(branchId))) {
                shifts.put(branchId, new HashSet<>());
            }
            // add the shift
            shifts.get(branchId).add(shift);
            Database.closeConnection(conn);
            return shift;
        }
        catch (Exception e){
            System.out.println("Exception has occurred in getShiftByBranchDateAndType ShiftDAO.");
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }

    public boolean insert(int branchID, Shift shift) throws SQLException{
        Connection conn = null;
        try {
            conn = Database.connect();
            if (getShiftByBranchDateAndType(branchID, shift.shiftDate.toString(), shift.shiftType.toString().toLowerCase()) != null) {
                Database.closeConnection(conn);
                return false;
            }
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO shifts (branchID, shiftDate, shiftType, startTime, endTime) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, branchID);
            stmt.setString(2, shift.shiftDate.toString());
            stmt.setString(3, shift.shiftType.toString().toLowerCase());
            stmt.setString(4, shift.startTimeOfShift.toString());
            stmt.setString(5, shift.endTimeOfShift.toString());
            stmt.executeUpdate();

            if (shift.cancellationInCashBox != null) {
                for (String employeeID : shift.cancellationInCashBox.keySet()) {
                    List<String> cancellations = shift.cancellationInCashBox.get(employeeID);
                    for (String barcode : cancellations) {
                        PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO shiftCancellations (branchID, shiftDate, shiftType, product, employeeID) VALUES (?, ?, ?, ?, ?)");
                        stmt1.setInt(1, branchID);
                        stmt1.setString(2, shift.shiftDate.toString());
                        stmt1.setString(3, shift.shiftType.toString().toLowerCase());
                        stmt1.setString(4, barcode);
                        stmt1.setString(5, employeeID);
                        stmt1.executeUpdate();
                    }
                }
            }

            for (JobType jobType : shift.wantedJobCountInShift.keySet()) {
                int numberOfJobType = shift.wantedJobCountInShift.get(jobType);
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO wantedJobsInShift (branchID, shiftDate, shiftType, jobType, numberOfJobType) VALUES (?, ?, ?, ?, ?)");
                stmt2.setInt(1, branchID);
                stmt2.setString(2, shift.shiftDate.toString());
                stmt2.setString(3, shift.shiftType.toString().toLowerCase());
                stmt2.setString(4, jobType.toString().toLowerCase());
                stmt2.setInt(5, numberOfJobType);
                stmt2.executeUpdate();
            }

            if (shift.employeesAndJobsInShift != null){
                for (String employeeID : shift.employeesAndJobsInShift.keySet()) {
                    String jobTypeString = shift.employeesAndJobsInShift.get(employeeID).toString().toLowerCase();
                    PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO employeeAssignedInShift (branchID, shiftDate, shiftType, employeeID, jobType) VALUES (?, ?, ?, ?, ?)");
                    stmt3.setInt(1, branchID);
                    stmt3.setString(2, shift.shiftDate.toString());
                    stmt3.setString(3, shift.shiftType.toString().toLowerCase());
                    stmt3.setString(4, employeeID);
                    stmt3.setString(5, jobTypeString);
                    stmt3.executeUpdate();
                }
            }

            // add a new key (dateOfShift) with an empty list as the initial value if it doesn't exist yet
            if (!(shifts.containsKey(branchID))) {
                shifts.put(branchID, new HashSet<>());
            }
            // add the shift
            shifts.get(branchID).add(shift);
            Database.closeConnection(conn);
            return true;
        }
        catch (Exception e){
            System.out.println("Exception has occurred in insert ShiftDAO.");
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return false;
    }


    public boolean update(int branchID, Shift shift) throws SQLException {
        try{
        conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("UPDATE shifts SET startTime = ?, endTime = ? WHERE branchID = ? AND shiftDate = ? AND shiftType = ?");
        stmt.setString(1, shift.startTimeOfShift.toString());
        stmt.setString(2, shift.endTimeOfShift.toString());
        stmt.setInt(3, branchID);
        stmt.setString(4, shift.shiftDate.toString());
        stmt.setString(5, shift.shiftType.toString().toLowerCase());
        stmt.executeUpdate();

        if(shift.cancellationInCashBox!=null) {
            for (String employeeID : shift.cancellationInCashBox.keySet()) {
                List<String> cancellations = shift.cancellationInCashBox.get(employeeID);
                for (String barcode : cancellations) {
                    PreparedStatement stmt1 = conn.prepareStatement("SELECT employeeID FROM shiftCancellations WHERE branchID = ? AND shiftDate = ? AND shiftType = ? AND product = ? AND employeeID = ?");
                    stmt1.setInt(1, branchID);
                    stmt1.setString(2, shift.shiftDate.toString());
                    stmt1.setString(3, shift.shiftType.toString().toLowerCase());
                    stmt1.setString(4, barcode);
                    stmt1.setString(5, employeeID);
                    ResultSet rs1 = stmt1.executeQuery();
                    if (!rs1.next() || rs1.isAfterLast()) {
                        PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO shiftCancellations (branchID, shiftDate, shiftType, product, employeeID) VALUES (?, ?, ?, ?, ?)");
                        stmt2.setInt(1, branchID);
                        stmt2.setString(2, shift.shiftDate.toString());
                        stmt2.setString(3, shift.shiftType.toString().toLowerCase());
                        stmt2.setString(4, barcode);
                        stmt2.setString(5, employeeID);
                        stmt2.executeUpdate();
                    }
                }
            }
        }

        PreparedStatement stmt3 = conn.prepareStatement("SELECT * FROM shiftCancellations WHERE branchID = ? AND shiftDate = ? AND shiftType = ?");
        stmt3.setInt(1, branchID);
        stmt3.setString(2, shift.shiftDate.toString());
        stmt3.setString(3, shift.shiftType.toString().toLowerCase());
        ResultSet rs3 = stmt3.executeQuery();
        if (rs3.isBeforeFirst()) {
            while (rs3.next()) {
                String employeeID = rs3.getString("employeeID");
                String product = rs3.getString("product");
                if (!(shift.cancellationInCashBox.containsKey(employeeID) && shift.cancellationInCashBox.get(employeeID).contains(product))) {
                    PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM shiftCancellations WHERE branchID = ? AND shiftDate = ? AND shiftType = ? AND employeeID = ? AND product = ?");
                    deleteStmt.setInt(1, branchID);
                    deleteStmt.setString(2, shift.shiftDate.toString());
                    deleteStmt.setString(3, shift.shiftType.toString().toLowerCase());
                    deleteStmt.setString(4, employeeID);
                    deleteStmt.setString(5, product);
                    deleteStmt.executeUpdate();
                }
            }
        }

        for (JobType jobType : shift.wantedJobCountInShift.keySet()) {
            int count = shift.wantedJobCountInShift.get(jobType);
            String jobTypeString = jobType.toString().toLowerCase();
            PreparedStatement stmt4 = conn.prepareStatement("SELECT numberOfJobType FROM wantedJobsInShift WHERE branchID = ? AND shiftDate = ? AND shiftType = ? AND jobType = ?");
            stmt4.setInt(1, branchID);
            stmt4.setString(2, shift.shiftDate.toString());
            stmt4.setString(3, shift.shiftType.toString().toLowerCase());
            stmt4.setString(4, jobTypeString);
            ResultSet rs4 = stmt4.executeQuery();
            if (rs4.isBeforeFirst()) {
                if (rs4.next()) {
                    int numberOfJobType = rs4.getInt("numberOfJobType");
                    if (numberOfJobType != count) {
                        PreparedStatement stmt5 = conn.prepareStatement("UPDATE wantedJobsInShift SET numberOfJobType = ? WHERE branchID = ? AND shiftDate = ? AND shiftType = ? AND jobType = ?");
                        stmt5.setInt(1, count);
                        stmt5.setInt(2, branchID);
                        stmt5.setString(3, shift.shiftDate.toString());
                        stmt5.setString(4, shift.shiftType.toString().toLowerCase());
                        stmt5.setString(5, jobTypeString);
                        stmt5.executeUpdate();
                    }
                }
            }
        }

        if(shift.employeesAndJobsInShift !=null) {
            for (String employeeID : shift.employeesAndJobsInShift.keySet()) {
                PreparedStatement stmt6 = conn.prepareStatement("SELECT employeeID FROM employeeAssignedInShift WHERE branchID = ? AND shiftDate = ? AND shiftType = ? AND employeeID = ? AND jobType = ?");
                stmt6.setInt(1, branchID);
                stmt6.setString(2, shift.shiftDate.toString());
                stmt6.setString(3, shift.shiftType.toString().toLowerCase());
                stmt6.setString(4, employeeID);
                stmt6.setString(5, shift.employeesAndJobsInShift.get(employeeID).toString().toLowerCase());
                ResultSet rs6 = stmt6.executeQuery();
                if (!rs6.isBeforeFirst()) {
                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO employeeAssignedInShift (branchID, shiftDate, shiftType, employeeID, jobType) VALUES (?, ?, ?, ?, ?)");
                    stmt2.setInt(1, branchID);
                    stmt2.setString(2, shift.shiftDate.toString());
                    stmt2.setString(3, shift.shiftType.toString().toLowerCase());
                    stmt2.setString(4, employeeID);
                    stmt2.setString(5, shift.employeesAndJobsInShift.get(employeeID).toString().toLowerCase());
                    stmt2.executeUpdate();
                }

            }
        }

        PreparedStatement stmt7 = conn.prepareStatement("SELECT * FROM employeeAssignedInShift WHERE branchID = ? AND shiftDate = ? AND shiftType = ? ");
        stmt7.setInt(1, branchID);
        stmt7.setString(2, shift.shiftDate.toString());
        stmt7.setString(3, shift.shiftType.toString().toLowerCase());
        ResultSet rs7 = stmt7.executeQuery();
        if (rs7.isBeforeFirst()) {
                while (rs7.next()) {
                    String employeeID = rs7.getString("employeeID");
                    if (!(shift.employeesAndJobsInShift.containsKey(employeeID))) {
                        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM employeeAssignedInShift WHERE branchID = ? AND shiftDate = ? AND shiftType = ?");
                        deleteStmt.setInt(1, branchID);
                        deleteStmt.setString(2, shift.shiftDate.toString());
                        deleteStmt.setString(3, shift.shiftType.toString().toLowerCase());
                        deleteStmt.executeUpdate();
                    }
                }
            }
        Database.closeConnection(conn);
        return true;
        }
         catch (Exception e){
            System.out.println("Exception has occurred in update ShiftDAO.");
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
            stmt.executeUpdate("DELETE FROM shifts");
            stmt.executeUpdate("DELETE FROM wantedJobsInShift");
            stmt.executeUpdate("DELETE FROM shiftCancellations");
            stmt.executeUpdate("DELETE FROM employeeAssignedInShift");
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while deleting table shifts: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public HashMap<Integer, Set<Shift>> getShifts() {
        return shifts;
    }
}
