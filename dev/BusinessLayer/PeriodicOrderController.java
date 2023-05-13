package BusinessLayer;

import DataAccessLayer.PeriodicOrderDAO;
import DataAccessLayer.SupplierDAO;
import DataAccessLayer.SupplierProductDAO;
import Utillity.Pair;
import Utillity.Response;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PeriodicOrderController {
    private PeriodicOrderDAO periodicOrderDAO;
    private SupplierProductDAO supplierProductDAO;
    private SupplierDAO supplierDAO;
    private static int id;

    public PeriodicOrderController( ) {
        this.periodicOrderDAO = new PeriodicOrderDAO();
        this.supplierProductDAO = new SupplierProductDAO();
        this.supplierDAO = new SupplierDAO();
        id = periodicOrderDAO.getLastPeriodicOrderID() + 1;
    }

    public Response createPeriodicOrder(int supplierID, int branchID, DayOfWeek fixedDay, ArrayList<SupplierProduct> itemsInOrder)
    {
        HashMap<Integer, SupplierProduct> supplierProducts = supplierProductDAO.getAllSupplierProductsByID(supplierID);
        // Check if the supplier supply all the products in the list, if one of the isn't supplied by him send informing response
        for(SupplierProduct productInOrder : itemsInOrder)
            if(supplierProducts.get(productInOrder.getProductID()) == null)
                return new Response("The supplier with the ID: " + supplierID + " not supplying the product with the ID: " + productInOrder.getProductID());
        PeriodicOrder periodicOrder = new PeriodicOrder(id++, supplierID, branchID, fixedDay, itemsInOrder);
        return periodicOrderDAO.addPeriodicOrder(periodicOrder);
    }

    public Response removePeriodicOrder(int orderID) { return periodicOrderDAO.removePeriodicOrder(orderID); }

    public HashMap<Integer, PeriodicOrder> getAllPeriodicOrders() { return periodicOrderDAO.getAllPeriodicOrders(); }

    public PeriodicOrder getPeriodicOrderByID(int orderID) { return periodicOrderDAO.getPeriodicOrderByID(orderID); }

    public HashMap<Integer, PeriodicOrder> getPeriodicOrdersFromSupplier(int supplierID) { return periodicOrderDAO.getPeriodicOrdersFromSupplier(supplierID); }

    public HashMap<Integer, PeriodicOrder> getPeriodicOrdersToBranch(int branchID){ return periodicOrderDAO.getPeriodicOrdersToBranch(branchID); }

    public HashMap<Integer, PeriodicOrder> getAllPeriodicOrderForToday() { return periodicOrderDAO.getAllPeriodicOrderForToday(); }

    public Response executePeriodicOrder(int periodicOrderID)
    {
        PeriodicOrder periodicOrder = periodicOrderDAO.getPeriodicOrderByID(periodicOrderID);
        if(periodicOrder == null)
            return new Response("Periodic Order Creation Fails, Reason: periodicOrderID Is Not Exists");
        Supplier supplierToOrder = supplierDAO.getSupplierByID(periodicOrder.getSupplierID());
        ArrayList<SupplierProduct> productsInOrder = periodicOrder.getItemsInOrder();
        ArrayList<SupplierProduct> productsToOrder = new ArrayList<>();
        //Update the products amount for the order - checks the amount that the supplier got
        for(SupplierProduct productInOrder : productsInOrder)
        {
            SupplierProduct productInSupplier = supplierProductDAO.getSupplierProduct(periodicOrder.getSupplierID(), productInOrder.getProductID());
            if(productInSupplier == null || productInSupplier.getAmount() == 0) continue;
            if(productInSupplier.getAmount() < productInOrder.getAmount()) productsToOrder.add(new SupplierProduct(productInSupplier));
            else productsToOrder.add(new SupplierProduct(productInOrder));
        }
        return new Response(0); // TODO: Remove
        // TODO: Make all this functions from the DAO (if it to hard make new method in the supplier to support.
//            double priceAfterDiscount = supplierToOrder.calculatePriceAfterDiscount(suppliersProduct);
//            int totalAmountToOrder = supplierToOrder.getTotalAmount(suppliersProduct);
//            if (supplierToOrder.getTotalOrderDiscountPerOrderPrice() != null && supplierToOrder.getTotalDiscountInPrecentageForOrder() != null) {
//                if (priceAfterDiscount > supplierToOrder.getTotalOrderDiscountPerOrderPrice().getFirst()) {
//                    priceAfterDiscount = priceAfterDiscount - supplierToOrder.getTotalOrderDiscountPerOrderPrice().getSecond();
//                }
//                if (totalAmountToOrder > supplierToOrder.getTotalDiscountInPrecentageForOrder().getFirst()) {
//                    priceAfterDiscount = ((100 - supplierToOrder.getTotalDiscountInPrecentageForOrder().getSecond()) / 100) * priceAfterDiscount;
//                }
//            }
//            double priceBeforeDiscount = supplierToOrder.calculatePriceBeforeDiscount(suppliersProduct);
//
//            LocalDate deliveyDate = LocalDate.now().plusDays(supplierToOrder.getSupplierClosestDaysToDelivery());//create the arrival date
//
//            Order newOrderForSupplier = new Order(id++, supplierName, supplierAddress, supplierId, contactPhoneNumber, productsToOrder, priceBeforeDiscount, priceAfterDiscount,deliveyDate, branchId);
//            if (!supplierOrders.containsKey(supplierId)) {
//                supplierOrders.put(supplierId, new ArrayList<>());
//            }
//            supplierOrders.get(supplierId).add(newOrderForSupplier);
//            supplierOrderDAO.addOrder(newOrderForSupplier);
//        }
//        return new Response(0);
    }
}