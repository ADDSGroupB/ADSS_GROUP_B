package DataAccessLayer;

import BusinessLayer.Supplier;
import Utillity.Response;

import java.util.HashMap;

public interface iSupplierDAO {
    HashMap<Integer, Supplier> getAllSuppliers();
    Supplier getSupplierByID(int id);
    Response addSupplier(Supplier supplier);
    Response removeSupplier(int id);
    Response updateSupplierName(int id, String name);
    Response updateSupplierAddress(int id, String address);
    Response updateSupplierBankAccount(int id, String bankAccount);
    void printAllSuppliers();
}
