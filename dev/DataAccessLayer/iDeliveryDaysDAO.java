package DataAccessLayer;

import BusinessLayer.Agreement;
import Utillity.Response;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public interface iDeliveryDaysDAO {
    public ArrayList<DayOfWeek> getDeliveryDays(int id);
    public Response addDeliveryDays(int supplierID, ArrayList<DayOfWeek> days);
    public Response removeDeliveryDays(int supplierID);
    public Response updateDeliveryDays(int supplierID, ArrayList<DayOfWeek> days);
}
