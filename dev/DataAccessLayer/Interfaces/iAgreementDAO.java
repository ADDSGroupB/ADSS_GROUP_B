package DataAccessLayer.Interfaces;

import BusinessLayer.Agreement;
import BusinessLayer.SupplierProduct;
import Utillity.Response;

import java.util.HashMap;

public interface iAgreementDAO {
    Agreement getAgreementByID(int id);
    Response addAgreement(int supplierID, Agreement agreement);
    Response addAgreementWithDiscount(int supplierID, Agreement agreement);
    Response removeAgreement(int supplierID);
    Response updateAgreement(int supplierID, String paymentType, boolean selfSupply, String supplyMethod, int supplyTime);
    Response updatePaymentType(int supplierID, String paymentType);
    Response updateSelfSupply(int supplierID, boolean selfSupply);
    Response updateSupplyMethod(int supplierID, String supplyMethod);
    Response updateSupplyTime(int supplierID, int supplyTime);
}
