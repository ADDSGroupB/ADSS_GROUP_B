package BuisnessLayer;

public class Branch {

    public EmployeeManagement employeeManagement;
    public ShiftManagement shiftManagement;

    public Branch(){
        employeeManagement = new EmployeeManagement();
        shiftManagement = new ShiftManagement();
    }
}
