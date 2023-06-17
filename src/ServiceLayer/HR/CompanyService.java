package ServiceLayer.HR;

import BuisnessLayer.HR.CompanyController;
import BuisnessLayer.HR.JobType;
import BuisnessLayer.HR.Pair;
import BuisnessLayer.HR.Shift;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CompanyService {
    private CompanyController companyController;

    public CompanyService() {
        companyController = new CompanyController();
    }

    public void setCompany(CompanyController companyController){
        this.companyController = companyController;
    }

    //-----------------------------function for the Service layer--------------------------//
    public void checkIfEmployeesInCompany(String employeeID) {
        companyController.checkIfEmployeesInCompany(employeeID);
    }

    public void checkIfEmployeesNotInCompany(String employeeID) {
        companyController.checkIfEmployeesNotInCompany(employeeID);
    }

    public void registerNewEmployee(String employeeFullName, String employeeID, String bankAccountInformation, String startOfEmploymentDate, int salaryPerHour,
                                    String employeePersonalDetails, ArrayList<JobType> employeesAuthorizedJobs, String termsOfEmployment, String employeePassword, Set<Integer> branchList) {
        companyController.registerNewEmployee(employeeFullName, employeeID, bankAccountInformation, startOfEmploymentDate, salaryPerHour,
                employeePersonalDetails, employeesAuthorizedJobs, termsOfEmployment, employeePassword, branchList);

    }

    public void addEmployeeToBranch(String employeeID, int branch) {
        companyController.addEmployeeToBranch(employeeID, branch);
    }

    public void registerNewDriver(double max_weight_allowed_insert_for_new_driver, int driver_license_insert_for_new_driver, String employeeFullName, String employeeID, String bankAccountInformation, String startOfEmploymentDate, int salaryPerHour,
                                  String employeePersonalDetails, String termsOfEmployment, String employeePassword) {
        companyController.registerNewDriver(max_weight_allowed_insert_for_new_driver, driver_license_insert_for_new_driver, employeeFullName,
                employeeID, bankAccountInformation, startOfEmploymentDate, salaryPerHour,
                employeePersonalDetails, termsOfEmployment, employeePassword);
    }

    public boolean CheckEmployeePassword(String employeeID, String employeePassword) {
        return companyController.CheckEmployeePassword(employeeID, employeePassword);
    }


    public void submitAShiftForEmployee(String employeeId, String date, String shiftType) {
        companyController.submitAShiftForEmployee(employeeId, date, shiftType);
    }

    public void changeJobTypeCount(String date, String shiftType, String jobTypeNumberString, int branch, int jobCountInt) {
        companyController.changeJobTypeCount(date, shiftType, jobTypeNumberString, branch, jobCountInt);
    }

    public void changeShiftTime(String date, String shiftType, String[] hours, int branch) {
        companyController.changeShiftTime(date, shiftType, hours, branch);
    }

    public String displayShift(String date, String shiftType, int branch) {
        return companyController.displayShift(date, shiftType, branch);
    }

    public List<String> getEmployeesInShift(String date, String shiftType, int branch) {
        return companyController.getEmployeesInShift(date, shiftType, branch);
    }

    public Pair<Shift, JobType> checkJobToAdd(String date, String shiftType, String jobTypeNumberString, int branch) {
        return companyController.checkJobToAdd(date, shiftType, jobTypeNumberString, branch);
    }

    public void addEmployeeToShift(String employeesIDString, JobType wantedJob, Shift shiftToAddEmployee, int branch) {
        companyController.addEmployeeToShift(employeesIDString, wantedJob, shiftToAddEmployee, branch);
    }

    public void removeEmployeeFromShift(String date, String shiftType, String employeesIDString, int branch) {
        companyController.removeEmployeeFromShift(date, shiftType, employeesIDString, branch);
    }

    public void isShiftValid(String date, String shiftType, int branch) {
        companyController.isShiftValid(date, shiftType, branch);
    }

    public void setSalaryPerHourForEmployee(String employeeID, int newSalary) {
        companyController.setSalaryPerHourForEmployee(employeeID, newSalary);
    }

    public void setPersonalDetailsForEmployee(String employeeID, String details) {
        companyController.setPersonalDetailsForEmployee(employeeID, details);
    }

    public void setBankAccountForEmployee(String employeeID, String newBankInfo) {
        companyController.setBankAccountForEmployee(employeeID, newBankInfo);
    }

    public void checkIfEmployeeExist(String employeeID) {
        companyController.checkIfEmployeeExist(employeeID);
    }

    public void displayEmployeesJobs(String employeeID) {
        companyController.displayEmployeesJobs(employeeID);
    }
    public List<String> getEmployeesJobs(String employeeID) {
        return companyController.getEmployeesJobs(employeeID);
    }

    public void AddJobTypeToEmployee(String employeeID, String jobType) {
        companyController.AddJobTypeToEmployee(employeeID, jobType);
    }

    public void removeJobTypeFromEmployee(String employeeID, String jobType) {
        companyController.removeJobTypeFromEmployee(employeeID, jobType);
    }

    public String displayCashierCancellation(String date, String shiftType, int branch) {
        return companyController.displayCashierCancellation(date, shiftType, branch);
    }

    public void addOrDeleteCashierCancellation(String shiftDate, String shiftType, String employeeID, String barcode, int branch, String addOrDelete) {
        companyController.addOrDeleteCashierCancellation(shiftDate, shiftType, employeeID, barcode, branch, addOrDelete);
    }

    public String displayAllShiftsDetails(int branch) {
        return companyController.displayAllShiftsDetails(branch);
    }

    public Object[][]  getAllShiftsDetails(int branch) {
        return companyController.getAllShiftsDetails(branch);
    }

    public String displayAllRegisterEmployee(int branch) {
        return companyController.displayAllRegisterEmployee(branch);
    }

    public String displayAllRegisterEmployee2(int branch) {
        return companyController.displayAllRegisterEmployee2(branch);
    }

    public String displayAvailableEmployeesForJobType(String date, String shiftType, String jobTypeNumberString, int branch) {
        return companyController.displayAvailableEmployeesForJobType(date, shiftType, jobTypeNumberString, branch);
    }
    public List<String> getAvailableEmployeesForJobType(String date, String shiftType, String jobTypeNumberString, int branch) {
        return companyController.getAvailableEmployeesForJobType(date, shiftType, jobTypeNumberString, branch);
    }

    public LocalDate[] currDateAndEndOfWeek(int branch){
        return companyController.currDateAndEndOfWeek(branch);
    }

    public String SalaryReportForAllEmployee(int branch){
        return companyController.SalaryReportForAllEmployee(branch);
    }

    public void checkIfShiftsAlreadyMade(int branch){
        companyController.checkIfShiftsAlreadyMade(branch);
    }

    public void createNewShiftsForNextWeek(String shiftType, LocalDate shiftDate, String startTimeOfShiftMorning, String endTimeOfShiftMorning, String startTimeOfShiftEvening, String endTimeOfShiftEvening,
                                           int cashierCount, int storeKeeperCount, int generalEmployeeCount, int securityCount, int usherCount, int cleanerCount, int shiftManagerCount, int branch){
        companyController.createNewShiftsForNextWeek(shiftType, shiftDate, startTimeOfShiftMorning, endTimeOfShiftMorning, startTimeOfShiftEvening, endTimeOfShiftEvening,
                cashierCount, storeKeeperCount, generalEmployeeCount, securityCount, usherCount, cleanerCount, shiftManagerCount, branch);
    }

    public void addBonusToEmployee(String employeeID, int bonus){
        companyController.addBonusToEmployee(employeeID, bonus);
    }
    public void updateDriverMaxWeightAllowed(String employeeID, double max_weight_allowed_insert_for_new_driver){
        companyController.updateDriverMaxWeightAllowed(employeeID, max_weight_allowed_insert_for_new_driver);
    }
    public void updateDriverLicense(String employeeID, int driver_license_insert_for_new_driver){
        companyController.updateDriverLicense(employeeID, driver_license_insert_for_new_driver);
    }

    public String driversSalaryReport(){
        return companyController.driversSalaryReport();
    }

    public String  getTransportsForShift(String date, String shiftType, int branch, String employeeID){
        return companyController.getTransportsForShift(date, shiftType, branch, employeeID);
    }

    public void resetDataBaseAssignment1(){
        companyController.resetDataBaseAssignment1();
    }

    public void resetDataBaseAssignment2(){
        companyController.resetDataBaseAssignment2();
    }

    public boolean isEmployee(String id){
        return companyController.isEmployee(id);
    }

    public boolean isDriver(String id){
        return companyController.isDriver(id);
    }
}
