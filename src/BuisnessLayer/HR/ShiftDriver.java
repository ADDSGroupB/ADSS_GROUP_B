package BuisnessLayer.HR;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ShiftDriver extends AShift {

    public List<String> driversInShift;

    public ShiftDriver(LocalDate shiftDate, ShiftType shiftType, LocalTime startTimeOfShift, LocalTime endTimeOfShift) {
        super(shiftDate, shiftType, startTimeOfShift, endTimeOfShift);
        driversInShift = new ArrayList<>();
    }

    /* function that add driver to shift*/
    public boolean addDriverToShift(String driverId, Driver driver){
        this.driversInShift.add(driverId);
        return true;
    }

    /* function that remove driver from shift*/
    public boolean removeDriverFromShift(String driverId){
        if(!this.driversInShift.contains(driverId))
            return false;
        this.driversInShift.remove(driverId);
        return true;
    }

    /* function to display all the drivers in the shift */
    public String displayShiftDriver(Map<String, Driver> registeredDrivers){
        String output = "";
        output += "- Shift details:\n";
        output += "Day : " + this.shiftDate + ", " + " Type: " + this.shiftType + ", " + "start time : " + this.startTimeOfShift + ", " + "end time : " + this.endTimeOfShift + "\n";
        //display drivers in this shift - name and id
        for (String driverId : this.driversInShift) {
            Driver driverToPrint = registeredDrivers.get(driverId);
            // print this specific driver
            output += "Drivers in shift :\n";
            output += driverToPrint;
        }
        return output;
    }
}
