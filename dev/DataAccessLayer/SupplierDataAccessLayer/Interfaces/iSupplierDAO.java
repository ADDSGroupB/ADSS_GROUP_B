package DataAccessLayer.SupplierDataAccessLayer.Interfaces;

import BusinessLayer.SupplierBusinessLayer.Supplier;
import Utillity.Response;

import java.util.HashMap;

public interface iSupplierDAO {
    HashMap<Integer, Supplier> getAllSuppliers();
    Supplier getSupplierByID(int id);

    Response updateSupplierProductAmount(int supplierID, int productID, int amount);

    Integer getActiveSupplierById(Integer id);
    Response addSupplier(Supplier supplier);
    Response removeSupplier(int id);
    Response updateSupplierName(int id, String name);
    Response updateSupplierAddress(int id, String address);
    Response updateSupplierBankAccount(int id, String bankAccount);
    void printAllSuppliers();

    Response getSupplierNameById(Integer id);
}
