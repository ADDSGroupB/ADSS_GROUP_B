package DataAccessLayer.InventoryDataAccessLayer.SupplierDataAccessLayer.Interfaces;

import BusinessLayer.InventoryBusinessLayer.SupplierBusinessLayer.Agreement;
import Utillity.Response;

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
