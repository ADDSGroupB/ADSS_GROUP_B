package DataAccessLayer.Interfaces;

import BusinessLayer.Agreement;
import Utillity.Response;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public interface iDeliveryDaysDAO {
    ArrayList<DayOfWeek> getDeliveryDays(int id);
    Response addDeliveryDays(int supplierID, ArrayList<DayOfWeek> days);
    Response removeDeliveryDays(int supplierID);
    Response updateDeliveryDays(int supplierID, ArrayList<DayOfWeek> days);
}
