package DataAccessLayer;

import BusinessLayer.Supplier;
import Utillity.Response;

import java.util.HashMap;

public interface iSupplierDAO {
    public HashMap<Integer, Supplier> getAllSuppliers();
    public Supplier getSupplierByID(int id);
    public Response addSupplier(Supplier supplier);
    public Response removeSupplier(int id);
    public Response updateSupplierName(int id, String name);
    public Response updateSupplierAddress(int id, String address);
    public Response updateSupplierBankAccount(int id, String bankAccount);
}
