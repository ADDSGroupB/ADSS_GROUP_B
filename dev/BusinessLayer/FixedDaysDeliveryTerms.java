package BusinessLayer;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

public class FixedDaysDeliveryTerms implements DeliveryTerm{
    private ArrayList<DayOfWeek> supplyDays;

    public FixedDaysDeliveryTerms(ArrayList<DayOfWeek> supplyDays) {
        this.supplyDays = supplyDays;
    }

    public String getSupplyDays() {
        String days = "The supply days are: ";
        for(DayOfWeek day : this.supplyDays)
            days += day + ", ";
        return days.substring(0, days.length()-3);
    }

    public void addSupplyDay(DayOfWeek day) {
        if(!this.supplyDays.contains(day))
            this.supplyDays.add(day);
    }


    @Override
    public LocalDate getDeliveryDate() {
        LocalDate today = LocalDate.now();
        LocalDate closestDate = null;
        long daysUntil = Long.MAX_VALUE;

        for (DayOfWeek dayOfWeek : supplyDays) {
            LocalDate nextDate = today.with(TemporalAdjusters.next(dayOfWeek));
            long days = ChronoUnit.DAYS.between(today, nextDate);
            if (days < daysUntil) {
                closestDate = nextDate;
                daysUntil = days;
            }
        }
        return closestDate;
    }
}
