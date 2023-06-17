package Tests;

import BuisnessLayer.HR.*;
import BuisnessLayer.TransportManager.*;
import DataAccessLayer.Database;
import DataAccessLayer.HR.ShiftDAO;
import DataAccessLayer.TransportManager.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class Tests {

    TransportController transportController;
    CompanyController companyController;

    @BeforeEach
    void setUp() {
        try {
            companyController = new CompanyController();
            transportController = new TransportController();
        }
        catch (SQLException e) {
            System.out.println("Failed in setUp transport controller test: " + e.getMessage());

        }
    }

    @AfterEach
    void tearDown() {
    }



    @Test
    void display_available_drivers_controller() {

        // TODO: need to add shifsbyDate to data base, shiftsdriver, driverAssignedInShift
        // TODO: need to change the return of the functions to string

        ArrayList<JobType> jobs_type = new ArrayList<>();
        Set<Integer> set_branches = new HashSet<Integer>();
        Driver driver = new Driver(3000, 3, "Noam Barak", "318305570",
                "Bank Leumi, 36987", "17-10-2021", 50,
                "Student", jobs_type, "Regular terms Of Employment", "noamb12", set_branches);

        //add driver to the table

        LocalDate date = LocalDate.of(2023, 7, 26);
        LocalTime time = LocalTime.of(19, 0, 0);
        Connection conn = null;
        try {
            companyController.driverController.driverDAO.insert(driver);

            conn = Database.connect();

            PreparedStatement stmt4 = conn.prepareStatement("SELECT employeeID FROM employeeSubmittingShifts WHERE employeeID = ? AND shiftDate = ? AND shiftType = ?");
            stmt4.setString(1,"318305570");
            stmt4.setString(2, "2023-07-26");
            stmt4.setString(3, "evening");
            ResultSet rs4 = stmt4.executeQuery();
            if (!rs4.next() || rs4.isAfterLast()) {
                PreparedStatement stmt5 = conn.prepareStatement("INSERT INTO employeeSubmittingShifts (employeeID, shiftDate, shiftType) VALUES (?, ?, ?)");
                stmt5.setString(1, driver.employeeID);
                stmt5.setString(2, "2023-07-26");
                stmt5.setString(3, "evening");
                stmt5.executeUpdate();

            }

            ShiftType shiftType = ShiftType.EVENING;
            Set<ShiftType> shiftTypeSet = new HashSet<>();
            shiftTypeSet.add(shiftType);
            driver.employeeSubmittingShifts.put(date, shiftTypeSet);

            Database.closeConnection(conn);

            companyController.driverController.driverDAO.update(driver);

            String result = transportController.display_available_drivers_controller(date, time, 3000, 3);

            String expectedResult = "" +
                    "Driver's Name: '" + "Noam Barak" + '\'' +
                    ", ID: = '" + "318305570" + '\'' +
                    ", Max weight allowed: = " + "3000.0" +
                    ", License: = " + "3" +
                    "\n---------------------------------------------------------------------" + "\n";

            assertEquals(expectedResult, result);

            conn = Database.connect();

            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM drivers WHERE employeeID = ?");
            deleteStmt.setString(1, driver.employeeID);
            deleteStmt.executeUpdate();

            PreparedStatement deleteStmt1 = conn.prepareStatement("DELETE FROM employeeSubmittingShifts WHERE shiftDate = ? AND shiftType = ? AND employeeID = ?");
            deleteStmt1.setString(1, "2023-07-26");
            deleteStmt1.setString(2, "evening");
            deleteStmt1.setString(3, driver.employeeID);

            deleteStmt1.executeUpdate();
            Database.closeConnection(conn);

            companyController.driverController.driverDAO.drivers.remove(driver.employeeID, driver);

        }
        catch(SQLException e){
            System.out.println("SQL exception display_available_drivers_controller");
            System.out.println(e.getMessage());
        }
    }

    @Test
    void add_driver_to_shift_controller() {

        // TODO: add delete function to DriveDAO - from drives and driverSubmittingShifts
        // TODO: need to add - driver to data base , (treat shiftsDriver table)
        // TODO: need to remove all of this data at the end
        try {

            companyController.registerNewDriver(3000, 3, "Noam Barak", "208753698",
                    "Bank Leumi, 36987", "17-10-2021", 50,
                    "Student", "Regular terms Of Employment", "noamb12");

            Driver driver = companyController.driverController.driverDAO.getDriverByID("208753698");

            LocalDate date = LocalDate.of(2023, 7, 25);
            LocalTime time = LocalTime.of(15, 0, 0);

            Connection conn = null;
            conn = Database.connect();

            PreparedStatement stmt4 = conn.prepareStatement("SELECT employeeID FROM employeeSubmittingShifts WHERE employeeID = ? AND shiftDate = ? AND shiftType = ?");
            stmt4.setString(1,"208753698");
            stmt4.setString(2, "2023-07-25");
            stmt4.setString(3, "evening");
            ResultSet rs4 = stmt4.executeQuery();
            if (!rs4.next() || rs4.isAfterLast()) {
                PreparedStatement stmt5 = conn.prepareStatement("INSERT INTO employeeSubmittingShifts (employeeID, shiftDate, shiftType) VALUES (?, ?, ?)");
                stmt5.setString(1, driver.employeeID);
                stmt5.setString(2, "2023-07-25");
                stmt5.setString(3, "evening");
                stmt5.executeUpdate();
            }

            ShiftType shiftType = ShiftType.EVENING;
            Set<ShiftType> shiftTypeSet = new HashSet<>();
            shiftTypeSet.add(shiftType);
            driver.employeeSubmittingShifts.put(date, shiftTypeSet);

            ShiftDriver shiftDriver = new ShiftDriver(date, shiftType, LocalTime.parse("15:00:00") , LocalTime.parse("23:00:00"));
            companyController.driverController.shiftDriverDAO.insert(shiftDriver);

            Database.closeConnection(conn);

            boolean result = transportController.add_driver_to_shift_controller(driver.employeeID, date, time);
            assertTrue(result, "Failed in add_driver_to_shift_controller Test, got false and not true ");

            conn = Database.connect();

            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM drivers WHERE employeeID = ?");
            deleteStmt.setString(1, driver.employeeID);
            deleteStmt.executeUpdate();

            PreparedStatement deleteStmt1 = conn.prepareStatement("DELETE FROM employeeSubmittingShifts WHERE shiftDate = ? AND shiftType = ? AND employeeID = ?");
            deleteStmt1.setString(1, "2023-07-25");
            deleteStmt1.setString(2, "evening");
            deleteStmt1.setString(3, driver.employeeID);
            deleteStmt1.executeUpdate();

            PreparedStatement deleteStmt2 = conn.prepareStatement("DELETE FROM driverAssignedInShift WHERE shiftDate = ? AND shiftType = ? AND employeeID = ?");
            deleteStmt2.setString(1, "2023-07-25");
            deleteStmt2.setString(2, "evening");
            deleteStmt2.setString(3, driver.employeeID);
            deleteStmt2.executeUpdate();

            Database.closeConnection(conn);

        }
        catch(SQLException e){
            System.out.println("SQL exception add_driver_to_shift_controller");
            System.out.println(e.getMessage());
        }
    }

    @Test
    void check_if_storeKeeper_assigned_false() {
        try {
            LocalDate date = LocalDate.of(2023, 7, 27);
            LocalTime time = LocalTime.of(10, 0, 0);
            LocalTime startTime = LocalTime.of(8, 0, 0);
            LocalTime endTime = LocalTime.of(15, 0, 0);
            ShiftType shiftType = ShiftType.MORNING;

            Shift shift = new Shift(date, shiftType, startTime, endTime, 1, 1, 1, 1, 1, 1, 1);

            ShiftDAO shiftDao = companyController.branchesList.get(0).shiftManagement.getShiftDAO();
            shiftDao.insert(1, shift);

            boolean check = transportController.check_if_storeKeeperA_assigned(1, date, time);
            assertFalse(check);

            Connection conn = null;
            conn = Database.connect();

            PreparedStatement deleteStmt1 = conn.prepareStatement("DELETE FROM shifts WHERE branchID = ? AND shiftDate = ? AND shiftType = ?");
            deleteStmt1.setInt(1, 1);
            deleteStmt1.setString(2, "2023-07-27");
            deleteStmt1.setString(3, "morning");
            deleteStmt1.executeUpdate();

            PreparedStatement deleteStmt2 = conn.prepareStatement("DELETE FROM wantedJobsInShift WHERE branchID = ? AND shiftDate = ? AND shiftType = ?");
            deleteStmt2.setInt(1, 1);
            deleteStmt2.setString(2, "2023-07-27");
            deleteStmt2.setString(3, "morning");
            deleteStmt2.executeUpdate();

            Database.closeConnection(conn);

        }
        catch(SQLException e){
            System.out.println("SQL exception check storekeeper");
            System.out.println(e.getMessage());
        }
    }

    @Test
    void check_if_storeKeeper_assigned_true() {
        try {
            ArrayList<JobType> jobs_type = new ArrayList<>();
            jobs_type.add(JobType.STOREKEEPER);
            Set<Integer> set_branches = new HashSet<Integer>();
            set_branches.add(1);

            companyController.registerNewEmployee("100", "100", "100", "100", 100, "100", jobs_type, "100", "100", set_branches);
            companyController.registerNewEmployee("200", "200", "200", "200", 200, "200", jobs_type, "200", "200", set_branches);

            LocalDate date = LocalDate.of(2023, 7, 28);
            LocalTime time = LocalTime.of(10, 0, 0);
            LocalTime startTime1 = LocalTime.of(15, 0, 0);
            LocalTime endTime1 = LocalTime.of(23, 0, 0);
            ShiftType shiftType1 = ShiftType.EVENING;

            Shift shift = new Shift(date, shiftType1, startTime1, endTime1, 1, 1, 1, 1, 1, 1, 1);

            ShiftDAO shiftDao = companyController.branchesList.get(0).shiftManagement.getShiftDAO();
            shiftDao.insert(1, shift);

            LocalTime startTime2 = LocalTime.of(8, 0, 0);
            LocalTime endTime2 = LocalTime.of(15, 0, 0);
            ShiftType shiftType2 = ShiftType.MORNING;

            Shift shift2 = new Shift(date, shiftType2, startTime2, endTime2, 1, 1, 1, 1, 1, 1, 1);
            shiftDao.insert(1, shift2);

            shiftDao.getShifts().get(1).remove(shift);
            shiftDao.getShifts().get(1).remove(shift2);

            Connection conn = null;
            conn = Database.connect();

            PreparedStatement stmt4 = conn.prepareStatement("SELECT employeeID FROM employeeSubmittingShifts WHERE employeeID = ? AND shiftDate = ? AND shiftType = ?");
            stmt4.setString(1,"100");
            stmt4.setString(2, "2023-07-28");
            stmt4.setString(3, "evening");
            ResultSet rs4 = stmt4.executeQuery();
            if (!rs4.next() || rs4.isAfterLast()) {
                PreparedStatement stmt5 = conn.prepareStatement("INSERT INTO employeeSubmittingShifts (employeeID, shiftDate, shiftType) VALUES (?, ?, ?)");
                stmt5.setString(1, "100");
                stmt5.setString(2, "2023-07-28");
                stmt5.setString(3, "evening");
                stmt5.executeUpdate();
            }

            PreparedStatement stmt8 = conn.prepareStatement("SELECT employeeID FROM employeeSubmittingShifts WHERE employeeID = ? AND shiftDate = ? AND shiftType = ?");
            stmt8.setString(1,"200");
            stmt8.setString(2, "2023-07-28");
            stmt8.setString(3, "morning");
            ResultSet rs8 = stmt8.executeQuery();
            if (!rs8.next() || rs8.isAfterLast()) {
                PreparedStatement stmt9 = conn.prepareStatement("INSERT INTO employeeSubmittingShifts (employeeID, shiftDate, shiftType) VALUES (?, ?, ?)");
                stmt9.setString(1, "200");
                stmt9.setString(2, "2023-07-28");
                stmt9.setString(3, "morning");
                stmt9.executeUpdate();
            }

            PreparedStatement stmt5 = conn.prepareStatement("SELECT employeeID FROM employeeAssignedInShift WHERE branchID = ? AND employeeID = ? AND shiftDate = ? AND shiftType = ?");
            stmt5.setInt(1,1);
            stmt5.setString(2, "100");
            stmt5.setString(3, "2023-07-28");
            stmt5.setString(4, "evening");
            ResultSet rs5 = stmt5.executeQuery();
            if (!rs5.next() || rs5.isAfterLast()) {
                PreparedStatement stmt6 = conn.prepareStatement("INSERT INTO employeeAssignedInShift (branchID, shiftDate, shiftType, employeeID, jobType) VALUES (?, ?, ?, ?, ?)");
                stmt6.setInt(1, 1);
                stmt6.setString(2, "2023-07-28");
                stmt6.setString(3, "evening");
                stmt6.setString(4, "100");
                stmt6.setString(5, "storekeeper");
                stmt6.executeUpdate();
            }

            PreparedStatement stmt10 = conn.prepareStatement("SELECT employeeID FROM employeeAssignedInShift WHERE branchID = ? AND employeeID = ? AND shiftDate = ? AND shiftType = ?");
            stmt10.setInt(1,1);
            stmt10.setString(2, "200");
            stmt10.setString(3, "2023-07-28");
            stmt10.setString(4, "morning");
            ResultSet rs10 = stmt10.executeQuery();
            if (!rs10.next() || rs10.isAfterLast()) {
                PreparedStatement stmt11 = conn.prepareStatement("INSERT INTO employeeAssignedInShift (branchID, shiftDate, shiftType, employeeID, jobType) VALUES (?, ?, ?, ?, ?)");
                stmt11.setInt(1, 1);
                stmt11.setString(2, "2023-07-28");
                stmt11.setString(3, "morning");
                stmt11.setString(4, "200");
                stmt11.setString(5, "storekeeper");
                stmt11.executeUpdate();
            }
            Database.closeConnection(conn);

            Map<JobType, Integer> assignedJobCountInShift = new HashMap<>();
            assignedJobCountInShift.put(JobType.STOREKEEPER, 1);
            shift.assignedJobCountInShift = assignedJobCountInShift;
            shift2.assignedJobCountInShift = assignedJobCountInShift;

            Map<String, JobType> employeesAndJobsInShift1 = new HashMap<>();
            Map<String, JobType> employeesAndJobsInShift2 = new HashMap<>();
            employeesAndJobsInShift1.put("100", JobType.STOREKEEPER);
            employeesAndJobsInShift2.put("200", JobType.STOREKEEPER);
            shift.employeesAndJobsInShift = employeesAndJobsInShift1;
            shift2.employeesAndJobsInShift = employeesAndJobsInShift2;

            shiftDao.update(1, shift);
            shiftDao.update(1, shift2);
            shiftDao.getShifts().get(1).add(shift);
            shiftDao.getShifts().get(1).add(shift2);



            boolean check = transportController.check_if_storeKeeperA_assigned(1, date, time);
            assertTrue(check);
            //Connection conn = null;

            conn = Database.connect();

            PreparedStatement deleteStmt1 = conn.prepareStatement("DELETE FROM shifts WHERE branchID = ? AND shiftDate = ?");
            deleteStmt1.setInt(1, 1);
            deleteStmt1.setString(2, "2023-07-28");
            deleteStmt1.executeUpdate();

            PreparedStatement deleteStmt2 = conn.prepareStatement("DELETE FROM wantedJobsInShift WHERE branchID = ? AND shiftDate = ?");
            deleteStmt2.setInt(1, 1);
            deleteStmt2.setString(2, "2023-07-28");
            deleteStmt2.executeUpdate();

            PreparedStatement deleteStmt3 = conn.prepareStatement("DELETE FROM employeeSubmittingShifts WHERE employeeID = ? AND shiftDate = ?");
            deleteStmt3.setString(1, "100");
            deleteStmt3.setString(2, "2023-07-28");
            deleteStmt3.executeUpdate();

            PreparedStatement deleteStmt15 = conn.prepareStatement("DELETE FROM employeeSubmittingShifts WHERE employeeID = ? AND shiftDate = ?");
            deleteStmt15.setString(1, "200");
            deleteStmt15.setString(2, "2023-07-28");
            deleteStmt15.executeUpdate();

            PreparedStatement deleteStmt4 = conn.prepareStatement("DELETE FROM employeeAssignedInShift WHERE branchID = ? AND shiftDate = ?");
            deleteStmt4.setInt(1, 1);
            deleteStmt4.setString(2, "2023-07-28");
            deleteStmt4.executeUpdate();

            PreparedStatement deleteStmt5 = conn.prepareStatement("DELETE FROM employeeAuthorizedJobs WHERE employeeID = ?");
            deleteStmt5.setString(1, "100");
            deleteStmt5.executeUpdate();

            PreparedStatement deleteStmt12 = conn.prepareStatement("DELETE FROM employeeAuthorizedJobs WHERE employeeID = ?");
            deleteStmt12.setString(1, "200");
            deleteStmt12.executeUpdate();

            PreparedStatement deleteStmt6 = conn.prepareStatement("DELETE FROM employeesInBranch WHERE employeeID = ?");
            deleteStmt6.setString(1, "100");
            deleteStmt6.executeUpdate();

            PreparedStatement deleteStmt13 = conn.prepareStatement("DELETE FROM employeesInBranch WHERE employeeID = ?");
            deleteStmt13.setString(1, "200");
            deleteStmt13.executeUpdate();

            PreparedStatement deleteStmt7 = conn.prepareStatement("DELETE FROM employees WHERE employeeID = ?");
            deleteStmt7.setString(1, "100");
            deleteStmt7.executeUpdate();

            PreparedStatement deleteStmt14 = conn.prepareStatement("DELETE FROM employees WHERE employeeID = ?");
            deleteStmt14.setString(1, "200");
            deleteStmt14.executeUpdate();

            Database.closeConnection(conn);

        }
        catch(SQLException e){
            System.out.println("SQL exception check storekeeper");
            System.out.println(e.getMessage());
        }
    }


}