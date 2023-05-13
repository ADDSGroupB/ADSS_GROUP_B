package BusinessLayer;
import java.util.Comparator;

public class SuppliersComparator implements Comparator<Supplier> {
    @Override
    public int compare(Supplier s1, Supplier s2) {

        int s1Days = s1.getSupplierClosestDaysToDelivery();
        int s2Days = s2.getSupplierClosestDaysToDelivery();
        return Integer.compare(s1Days, s2Days);
    }
}
