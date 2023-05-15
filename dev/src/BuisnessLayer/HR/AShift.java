package BuisnessLayer.HR;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class AShift {
    public LocalDate shiftDate;
    public ShiftType shiftType;
    public LocalTime startTimeOfShift;
    public LocalTime endTimeOfShift;

    public AShift(LocalDate shiftDate, ShiftType shiftType, LocalTime startTimeOfShift, LocalTime endTimeOfShift){
        this.shiftDate = shiftDate;
        this.shiftType = shiftType;
        this.endTimeOfShift = endTimeOfShift;
        this.startTimeOfShift = startTimeOfShift;
    }
}
