package DataAccessLayer;

import BusinessLayer.Agreement;
import BusinessLayer.SupplierProduct;
import Utillity.Response;

import java.util.HashMap;

public interface iAgreementDAO {
    public Agreement getAgreementByID(int id);
    public Response addAgreement(int supplierID, Agreement agreement);
    public Response removeAgreement(int supplierID);
    public Response updateAgreement(int supplierID, String paymentType, boolean selfSupply, String supplyMethod, int supplyTime);
    public Response updatePaymentType(int supplierID, String paymentType);
    public Response updateSelfSupply(int supplierID, boolean selfSupply);
    public Response updateSupplyMethod(int supplierID, String supplyMethod);
    public Response updateSupplyTime(int supplierID, int supplyTime);
}
