package BusinessLayer;

import java.time.LocalDate;

public class DaysFromOrderDeliveryTerms implements DeliveryTerm{
    private int deliveryDaysAmount;

    public DaysFromOrderDeliveryTerms(int deliveryDaysAmount) {
        this.deliveryDaysAmount = deliveryDaysAmount;
    }

    public int getDeliveryDaysAmount() {
        return deliveryDaysAmount;
    }

    public void setDeliveryDaysAmount(int deliveryDaysAmount) {
        this.deliveryDaysAmount = deliveryDaysAmount;
    }


    @Override
    public LocalDate getDeliveryDate() {
        return LocalDate.now().plusDays(deliveryDaysAmount);
    }
}
