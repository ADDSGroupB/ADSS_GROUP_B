package DataAccessLayer.HR;

import BuisnessLayer.HR.JobType;
import BuisnessLayer.HR.ShiftType;
import BuisnessLayer.HR.Driver;
import DataAccessLayer.Database;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class DriverDAO {
    private static DriverDAO instance = null;
    public HashMap<String, Driver> drivers;
    private Connection conn;

    public DriverDAO() throws SQLException {
        //conn = Database.connect();
        drivers = new HashMap<>();
    }

    public static DriverDAO getInstance(){
        if(instance ==null){
            try {
                instance = new DriverDAO();
            }
            catch (SQLException e){
                System.out.println("SQL exception");
            }
        }
        return instance;
    }

    public HashMap<String, Driver> getAllDriver() throws SQLException{
        try {
            conn = Database.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM drivers");
            while (rs.next()) {
                String employeeId = rs.getString("employeeID");
                getDriverByID(employeeId);
            }
            Database.closeConnection(conn);
            return this.drivers;
        }
        catch (Exception e){
            System.out.println("Exception has occurred in getAllDriver DriverDAO.");
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        Database.closeConnection(conn);
        return null;
    }

    public Driver getDriverByID(String driverId) throws SQLException{
        try {
            conn = Database.connect();
            for (String employeeID : drivers.keySet()) {
                if (employeeID.equals(driverId)) {
                    Database.closeConnection(conn);
                    return drivers.get(driverId);
                }
            }

            String sql8 = "SELECT COUNT(*) FROM drivers WHERE employeeID = ?";
            PreparedStatement stmt8 = conn.prepareStatement(sql8);
            stmt8.setString(1, driverId);
            ResultSet rs8 = stmt8.executeQuery();
            int count = rs8.getInt(1);
            if (count == 0) {
                Database.closeConnection(conn);
                return null;
            }

            String sql = "SELECT * FROM drivers WHERE employeeID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, driverId);
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
            int driver_max_weight_allowed = rs.getInt("driver_max_weight_allowed");
            int driver_license = rs.getInt("driver_license");
            ArrayList<JobType> driverJobs = new ArrayList<>();
            Set<Integer> branches = new HashSet<>();
            Driver newDriver = new Driver(driver_max_weight_allowed, driver_license, employeeFullName,
                    employeeID, bankAccountInformation, startOfEmploymentDate, salaryPerHour,
                    employeePersonalDetails, driverJobs, termsOfEmployment, employeePassword, branches);
            newDriver.bonus = bonus;
            newDriver.shiftCountForWeek = shiftCountForWeek;
            //update maps
            Map<LocalDate, Set<ShiftType>> driverSubmittingShifts = new HashMap<>();

            String sql1 = "SELECT shiftDate, shiftType FROM employeeSubmittingShifts WHERE employeeID = ?";
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            stmt1.setString(1, driverId);
            ResultSet rs1 = stmt1.executeQuery();
            if (rs1.isBeforeFirst()) {
                while (rs1.next()) {
                    LocalDate shiftDate =LocalDate.parse(rs1.getString("shiftDate"));
                    String shiftTypeString1 = rs1.getString("shiftType");
                    ShiftType shiftType = null;
                    if(shiftTypeString1.equals("morning")){
                        shiftType = ShiftType.MORNING;
                    }
                    else{
                        shiftType = ShiftType.EVENING;
                    }                    if (driverSubmittingShifts.containsKey(shiftDate)) {
                        driverSubmittingShifts.get(shiftDate).add(shiftType);
                    } else {
                        Set<ShiftType> shiftTypeSet = new HashSet<>();
                        shiftTypeSet.add(shiftType);
                        driverSubmittingShifts.put(shiftDate, shiftTypeSet);
                    }
                }
            }
            newDriver.employeeSubmittingShifts = driverSubmittingShifts;

            Map<LocalDate, Set<ShiftType>> driverAssignmentForShifts = new HashMap<>();
            LocalDate currentDate = LocalDate.now();
            LocalDate startOfWeek = currentDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
            LocalDate endOfWeek = startOfWeek.plusDays(7); // add six days to get the end of the week
            String sql2 = "SELECT shiftDate, shiftType FROM driverAssignedInShift WHERE employeeID = ? AND shiftDate BETWEEN ? AND ?";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1, driverId);
            stmt2.setString(2, startOfWeek.toString());
            stmt2.setString(3, endOfWeek.toString());
            ResultSet rs2 = stmt2.executeQuery();
            if (rs2.isBeforeFirst()) {
                while (rs2.next()) {
                    LocalDate shiftDate =LocalDate.parse(rs2.getString("shiftDate"));
                    String shiftTypeString1 = rs2.getString("shiftType");
                    ShiftType shiftType = null;
                    if(shiftTypeString1.equals("morning")){
                        shiftType = ShiftType.MORNING;
                    }
                    else{
                        shiftType = ShiftType.EVENING;
                    }                    if (driverAssignmentForShifts.containsKey(shiftDate)) {
                        driverAssignmentForShifts.get(shiftDate).add(shiftType);
                    } else {
                        Set<ShiftType> shiftTypeSet = new HashSet<>();
                        shiftTypeSet.add(shiftType);
                        driverAssignmentForShifts.put(shiftDate, shiftTypeSet);
                    }
                }
            }
            newDriver.employeesAssignmentForShifts = driverAssignmentForShifts;

            // add the employee
            drivers.put(employeeID, newDriver);
            Database.closeConnection(conn);
            return newDriver;
        }
        catch (Exception e){
            System.out.println("Exception has occurred in getDriverByID DriverDAO.");
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        Database.closeConnection(conn);
        return null;
    }

    public boolean insert(Driver driver) throws SQLException{
        Connection conn = null;
        try {
            conn = Database.connect();
            if (getDriverByID(driver.employeeID) != null) {
                Database.closeConnection(conn);
                return false;
            }
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO drivers (employeeID, employeeFullName, bankAccountInformation, startOfEmploymentDate, salaryPerHour, bonus, " +
                    "employeePersonalDetails,termsOfEmployment,shiftCountForWeek,employeePassword, driver_max_weight_allowed, driver_license) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, driver.employeeID);
            stmt.setString(2, driver.employeeFullName);
            stmt.setString(3, driver.getBankAccountInformation());
            stmt.setString(4, driver.startOfEmploymentDate);
            stmt.setInt(5, driver.salaryPerHour);
            stmt.setInt(6, driver.bonus);
            stmt.setString(7, driver.employeePersonalDetails);
            stmt.setString(8, driver.termsOfEmployment);
            stmt.setInt(9, driver.shiftCountForWeek);
            stmt.setString(10, driver.getEmployeePassword());
            stmt.setDouble(11, driver.getDriver_max_weight_allowed());
            stmt.setInt(12, driver.getDriver_license());
            stmt.executeUpdate();

            for (LocalDate shiftDate : driver.employeeSubmittingShifts.keySet()) {
                Set<ShiftType> shiftTypeSet = driver.employeeSubmittingShifts.get(shiftDate);
                for (ShiftType shiftType : shiftTypeSet) {
                    PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO driverSubmittingShifts (employeeID, shiftDate, shiftType) VALUES (?, ?, ?)");
                    stmt1.setString(1, driver.employeeID);
                    stmt1.setString(2, shiftDate.toString());
                    stmt1.setString(3, shiftType.toString().toLowerCase());
                    stmt1.executeUpdate();
                }
            }

            // add the employee
            drivers.put(driver.employeeID, driver);
            Database.closeConnection(conn);
            return true;
        }
        catch (Exception e){
            System.out.println("Exception has occurred in insert DriverDAO.");
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return false;
    }

    public boolean update(Driver driver) throws SQLException{
        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("UPDATE drivers SET employeeFullName = ?, bankAccountInformation= ? , " +
                    "startOfEmploymentDate = ?, salaryPerHour = ?, bonus = ?, employeePersonalDetails = ?,termsOfEmployment = ?,shiftCountForWeek = ?," +
                    "employeePassword = ?, driver_max_weight_allowed = ?, driver_license= ? WHERE employeeID = ?");
            stmt.setString(1, driver.employeeFullName);
            stmt.setString(2, driver.getBankAccountInformation());
            stmt.setString(3, driver.startOfEmploymentDate);
            stmt.setInt(4, driver.salaryPerHour);
            stmt.setInt(5, driver.bonus);
            stmt.setString(6, driver.employeePersonalDetails);
            stmt.setString(7, driver.termsOfEmployment);
            stmt.setInt(8, driver.shiftCountForWeek);
            stmt.setString(9, driver.getEmployeePassword());
            stmt.setDouble(10, driver.getDriver_max_weight_allowed());
            stmt.setInt(11, driver.getDriver_license());
            stmt.setString(12, driver.employeeID);

            stmt.executeUpdate();

            for (LocalDate shiftDate : driver.employeeSubmittingShifts.keySet()) {
                Set<ShiftType> shiftTypeSet = driver.employeeSubmittingShifts.get(shiftDate);
                for (ShiftType shiftType : shiftTypeSet) {
                    PreparedStatement stmt4 = conn.prepareStatement("SELECT employeeID FROM employeeSubmittingShifts WHERE employeeID = ? AND shiftDate = ? AND shiftType = ?");
                    stmt4.setString(1, driver.employeeID);
                    stmt4.setString(2, shiftDate.toString());
                    stmt4.setString(3, shiftType.toString().toLowerCase());
                    ResultSet rs4 = stmt4.executeQuery();
                    if (!rs4.next() || rs4.isAfterLast()) {
                        PreparedStatement stmt5 = conn.prepareStatement("INSERT INTO employeeSubmittingShifts (employeeID, shiftDate, shiftType) VALUES (?, ?, ?)");
                        stmt5.setString(1, driver.employeeID);
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
            System.out.println("Exception has occurred.");
            System.out.println(e.getMessage() + " in update DriverDAO");
            Database.closeConnection(conn);
        }
        return false;
    }

    public void delete() throws SQLException{
        Statement stmt = null;
        try {
            conn = Database.connect();
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM drivers");
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while deleting table drivers: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }
}
