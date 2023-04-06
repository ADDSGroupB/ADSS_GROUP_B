package BusinessLayer;

import java.util.ArrayList;
import java.util.HashMap;

public class SupplierController {
    private HashMap<Integer, Supplier> suppliers;

    public SupplierController(){
        this.suppliers = new HashMap<>();
    }
    public Supplier getSupplier(int supplierID)
    {
        return suppliers.get(supplierID);
    }

}