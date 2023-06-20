package DataAccessLayer.HR;
import BuisnessLayer.HR.ShiftDriver;
import BuisnessLayer.HR.ShiftType;
import DataAccessLayer.Database;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class ShiftDriverDAO {
    private static ShiftDriverDAO instance = null;
    private HashMap<LocalDate, Set<ShiftDriver>> driverShifts ;
    private Connection conn;

    private ShiftDriverDAO() throws SQLException {
        driverShifts = new HashMap<>();
    }

    public static ShiftDriverDAO getInstance(){
        if(instance ==null){
            try {
                instance = new ShiftDriverDAO();
            }
            catch (SQLException e){
                System.out.println("SQL exception");
            }
        }
        return instance;
    }


    public HashMap<LocalDate, Set<ShiftDriver>> getAllDriverShifts() throws SQLException {
        try {
            conn = Database.connect();
            // search for the date of sunday of that week and create shifts for the whole week from sunday to friday
            LocalDate currentDate = LocalDate.now();
            LocalDate startOfWeek = currentDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
            LocalDate endOfWeek = startOfWeek.plusDays(7); // add six days to get the end of the week

            String sql = "SELECT * FROM shiftsDriver WHERE shiftDate BETWEEN ? AND ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, startOfWeek.toString());
            stmt.setString(2, endOfWeek.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String shiftDateString = rs.getString("shiftDate");
                    getDriverShiftsByDate(shiftDateString);
                }
            }
            Database.closeConnection(conn);
        }
        catch (Exception e){
            System.out.println("Exception has occurred in getAllDriverShifts ShiftDriverDAO.");
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return this.driverShifts;

    }

    public Set<ShiftDriver> getDriverShiftsByDate(String shiftDateString) throws SQLException{
        try {
            conn = Database.connect();
            LocalDate shiftDateLocalDate = LocalDate.parse(shiftDateString);
            if (driverShifts.get(shiftDateLocalDate) != null) {
                Database.closeConnection(conn);
                return driverShifts.get(shiftDateLocalDate);
            }

            Set<ShiftDriver> shiftDriverByDate = new HashSet<>();

            String sql8 = "SELECT COUNT(*) FROM shiftsDriver WHERE shiftDate = ?";
            PreparedStatement stmt8 = conn.prepareStatement(sql8);
            stmt8.setString(1, shiftDateString);
            ResultSet rs8 = stmt8.executeQuery();
            int count = rs8.getInt(1);
            if (count == 0) {
                Database.closeConnection(conn);
                return null;
            }

            String sql = "SELECT * FROM shiftsDriver WHERE shiftDate = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, shiftDateString);
            ResultSet rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    LocalDate shiftDate =LocalDate.parse(rs.getString("shiftDate"));
                    String shiftTypeString1 = rs.getString("shiftType");
                    ShiftType shiftType = null;
                    if(shiftTypeString1.equals("morning")){
                        shiftType = ShiftType.MORNING;
                    }
                    else{
                        shiftType = ShiftType.EVENING;
                    }
                    LocalTime startTimeOfShift = LocalTime.parse(rs.getString("startTimeOfShift"));
                    LocalTime endTimeOfShift = LocalTime.parse(rs.getString("endTimeOfShift"));

                    ShiftDriver shift = new ShiftDriver(shiftDate, shiftType, startTimeOfShift, endTimeOfShift);
                    shiftDriverByDate.add(shift);
                    // add the shift
                    
                    // add a new key (dateOfShift) with an empty list as the initial value if it doesn't exist yet
                    if (!(driverShifts.containsKey(shiftDateLocalDate))) {
                        driverShifts.put(shiftDateLocalDate, new HashSet<>());
                    }
                    // add the shift
                    driverShifts.get(shiftDateLocalDate).add(shift);

                }
            }

            // Create a HashMap to store the drivers in shift data
            for (ShiftDriver shift : shiftDriverByDate) {
                List<String> driversInShift = new ArrayList<>();
                String sql3 = "SELECT * FROM driverAssignedInShift WHERE shiftDate = ? AND shiftType = ?";
                PreparedStatement stmt3 = conn.prepareStatement(sql3);
                stmt3.setString(1, shiftDateString);
                stmt3.setString(2, shift.shiftType.toString().toLowerCase());
                ResultSet rs3 = stmt3.executeQuery();

                // Loop through the results and populate the HashMaps
                if (rs3.isBeforeFirst()) {
                    while (rs3.next()) {
                        String employeeID = rs3.getString("employeeID");

                        // Update the driversInShift HashMap
                        driversInShift.add(employeeID);
                    }
                }
                shift.driversInShift = driversInShift;

            }
            Database.closeConnection(conn);
            return shiftDriverByDate;
        }
        catch (Exception e){
            System.out.println("Exception has occurred in getDriverShiftsByDate Shoft.");
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }


    public boolean insert(ShiftDriver shiftDriver) throws SQLException{
        Connection conn = null;
        try {
            conn = Database.connect();
            boolean checkIfExist = false;
            Set<ShiftDriver> shiftDriverByDate = getDriverShiftsByDate(shiftDriver.shiftDate.toString());
            if(shiftDriverByDate != null) {
                for (ShiftDriver shiftDriver1 : shiftDriverByDate) {
                    if (shiftDriver1.shiftType == shiftDriver.shiftType) {
                        checkIfExist = true;
                        break;
                    }
                }
            }
            if (checkIfExist) {
                Database.closeConnection(conn);
                return false;
            }

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO shiftsDriver (shiftDate, shiftType, startTimeOfShift, endTimeOfShift) VALUES (?, ?, ?, ?)");
            stmt.setString(1, shiftDriver.shiftDate.toString());
            stmt.setString(2, shiftDriver.shiftType.toString().toLowerCase());
            stmt.setString(3, shiftDriver.startTimeOfShift.toString());
            stmt.setString(4, shiftDriver.endTimeOfShift.toString());
            stmt.executeUpdate();

            for (String employeeID : shiftDriver.driversInShift) {
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO driverAssignedInShift (shiftDate, shiftType, employeeID) VALUES (?, ?, ?)");
                stmt2.setString(1, shiftDriver.shiftDate.toString());
                stmt2.setString(2, shiftDriver.shiftType.toString().toLowerCase());
                stmt2.setString(3, employeeID);
                stmt2.executeUpdate();
            }
            // add a new key (dateOfShift) with an empty list as the initial value if it doesn't exist yet
            if (!(this.driverShifts.containsKey(shiftDriver.shiftDate))) {
                driverShifts.put(shiftDriver.shiftDate, new HashSet<>());
            }
            // add the shift
            driverShifts.get(shiftDriver.shiftDate).add(shiftDriver);

            Database.closeConnection(conn);
            return true;
        }
        catch (Exception e){
            System.out.println("Exception has occurred.");
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return false;
    }


    public boolean update(ShiftDriver shiftDriver) throws SQLException{
        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("UPDATE shiftsDriver SET startTimeOfShift = ?, endTimeOfShift = ? WHERE shiftDate = ? AND shiftType = ?");
            stmt.setString(1, shiftDriver.startTimeOfShift.toString());
            stmt.setString(2, shiftDriver.endTimeOfShift.toString());
            stmt.setString(3, shiftDriver.shiftDate.toString());
            stmt.setString(4, shiftDriver.shiftType.toString().toLowerCase());
            stmt.executeUpdate();

            for (String employeeID : shiftDriver.driversInShift) {
                PreparedStatement stmt6 = conn.prepareStatement("SELECT employeeID FROM driverAssignedInShift WHERE shiftDate = ? AND shiftType = ? AND employeeID = ?");
                stmt6.setString(1, shiftDriver.shiftDate.toString());
                stmt6.setString(2, shiftDriver.shiftType.toString().toLowerCase());
                stmt6.setString(3, employeeID);
                ResultSet rs6 = stmt6.executeQuery();
                if (!rs6.next() || rs6.isAfterLast()) {
                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO driverAssignedInShift (shiftDate, shiftType, employeeID) VALUES (?, ?, ?)");
                    stmt2.setString(1, shiftDriver.shiftDate.toString());
                    stmt2.setString(2, shiftDriver.shiftType.toString().toLowerCase());
                    stmt2.setString(3, employeeID);
                    stmt2.executeUpdate();
                }
            }

            PreparedStatement stmt7 = conn.prepareStatement("SELECT * FROM driverAssignedInShift WHERE shiftDate = ? AND shiftType = ? ");
            stmt7.setString(1, shiftDriver.shiftDate.toString());
            stmt7.setString(2, shiftDriver.shiftType.toString().toLowerCase());
            ResultSet rs7 = stmt7.executeQuery();
            if (rs7.isBeforeFirst()) {
                while (rs7.next()) {
                    String employeeID = rs7.getString("employeeID");
                    if (!(shiftDriver.driversInShift.contains(employeeID))) {
                        rs7.deleteRow();
                    }
                }
            }
            Database.closeConnection(conn);
            return true;
        }
        catch (Exception e){
            System.out.println("Exception has occurred.");
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
            stmt.executeUpdate("DELETE FROM shiftsDriver");
            stmt.executeUpdate("DELETE FROM driverAssignedInShift");
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while deleting table shiftsDriver: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }
}
