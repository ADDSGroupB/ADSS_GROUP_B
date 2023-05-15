package SupplierBusinessLayer;

import SupplierDataAccessLayer.*;
import Utillity.Pair;
import Utillity.Response;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class OrderController {
    private final HashMap<Integer, ArrayList<Order>> supplierOrders; //supplierId, Order TODO: Remove this
    private final SupplierOrderDAO supplierOrderDAO;
    private final PeriodicOrderDAO periodicOrderDAO;
    private final SupplierProductDAO supplierProductDAO;
    private final SupplierDAO supplierDAO;
    private static int id;

    public OrderController(){
        supplierOrders = new HashMap<>();
        supplierOrderDAO = new SupplierOrderDAO();
        periodicOrderDAO = new PeriodicOrderDAO();
        supplierProductDAO = new SupplierProductDAO();
        supplierDAO = new SupplierDAO();
        id = supplierOrderDAO.getLastOrderID() + 1;
    }

//    public static void main(String[] args) {
//        OrderController orderController = new OrderController();
//        HashMap<Integer, Integer> productsToAdd = new HashMap<>();
//        productsToAdd.put(5, 5);
//        productsToAdd.put(29, 5);
//        orderController.addProductsToOrder(1, productsToAdd);
//    }

    public HashMap<Integer, Order> getNoneCollectedOrdersForToday(int branchID) { return supplierOrderDAO.getNoneCollectedOrdersForToday(branchID); }
    public HashMap<Integer, Order> getOrdersFromSupplier(int supplierID) { return supplierOrderDAO.getOrdersFromSupplier(supplierID); }
    public HashMap<Integer, Order> getOrdersToBranch(int branchID) { return supplierOrderDAO.getOrdersToBranch(branchID); }
    public HashMap<Integer, Order> getAllOrderForToday() { return supplierOrderDAO.getAllOrderForToday(); }
    public Response markOrderAsCollected(int orderID) { return supplierOrderDAO.markOrderAsCollected(orderID); }

//    public void createOrder(Response res) {
//        ArrayList<ArrayList<Pair<SupplierProduct,Integer>>> supplierProductsAndAmount = res.getSupplyLists();
//        ArrayList<Supplier> supplierArrayList = res.getSuppliersInOrder();
//        for(int i =0; i < supplierArrayList.size(); i++){
//            Supplier currSupplier = supplierArrayList.get(i);
//            int supplierId = currSupplier.getSupplierId();
//            String supplierName = currSupplier.getName();
//            String supplierAdress = currSupplier.getAddress();
//            String contactPhoneNumber = supplierArrayList.get(i).getContacts().get(0).getPhoneNumber();//assume that every supplier have contact.
//            ArrayList<Pair<Integer,Integer>> suuplierProductsToCalculate = new ArrayList<>();
//            for(int j =0; j < supplierProductsAndAmount.get(i).size(); j++){
//                int productId = supplierProductsAndAmount.get(i).get(j).getFirst().getProductID();
//                int amountToOrder = supplierProductsAndAmount.get(i).get(j).getSecond();
//                Pair<Integer,Integer> pair = new Pair<>(productId,amountToOrder);
//                suuplierProductsToCalculate.add(pair);
//            }
//            ArrayList<Pair<SupplierProduct,Integer>> productsToOrder = supplierProductsAndAmount.get(i);
//            double priceAfterDiscount = currSupplier.calculatePriceAfterDiscount(suuplierProductsToCalculate);
//            int amountToorder = currSupplier.getTotalAmount(suuplierProductsToCalculate);
//            if (currSupplier.getTotalOrderDiscountPerOrderPrice()!=null&&currSupplier.getTotalDiscountInPrecentageForOrder()!=null) {
//                if (priceAfterDiscount > currSupplier.getTotalOrderDiscountPerOrderPrice().getFirst()) {
//                    priceAfterDiscount = priceAfterDiscount - currSupplier.getTotalOrderDiscountPerOrderPrice().getSecond();
//                }
//                if (amountToorder > currSupplier.getTotalDiscountInPrecentageForOrder().getFirst()) {
//                    priceAfterDiscount = ((100 - currSupplier.getTotalDiscountInPrecentageForOrder().getSecond()) / 100) * priceAfterDiscount;
//                }
//            }
//            double priceBeforeDiscount = currSupplier.calculatePriceBeforeDiscount(suuplierProductsToCalculate);
//            Order newOrderForSupplier = new Order(supplierName, supplierAdress, supplierId, contactPhoneNumber, productsToOrder, priceBeforeDiscount, priceAfterDiscount);
//            //System.out.println(newOrderForSupplier);
//
//            // TODO - להוסיף מקום לספק בעת היצירה
//            if(!supplierOrders.containsKey(supplierId)){
//                supplierOrders.put(supplierId, new ArrayList<>());
//            }
//            supplierOrders.get(supplierId).add(newOrderForSupplier);
//
//        }
//    }

    public Response createOrderByShortage(ArrayList<ArrayList<Supplier>> sortedSuppliers, int branchId, HashMap<Integer, Integer> shortage) {
//     for (int i = 0; i < sortedSuppliers.size(); i++) {
//         if (sortedSuppliers.get(i).size() == 0)
//             return new Response("can not supply all products");
        //}///////////////////////////////////////////
        if(sortedSuppliers.isEmpty()){
            return new Response("order creation was failed");
        }

        HashMap<Supplier, ArrayList<Pair<Integer, Integer>>> orderList = createOrderList(sortedSuppliers,shortage);
        //order list contains <supplier, list:<prod id, amount to supply> .....
        for (Map.Entry<Supplier, ArrayList<Pair<Integer, Integer>>> entry : orderList.entrySet()) {
            Supplier supplierToOrder = entry.getKey();
            ArrayList<Pair<Integer, Integer>> suppliersProduct = entry.getValue();
            int supplierId = supplierToOrder.getSupplierId();
            String supplierName = supplierToOrder.getName();
            String supplierAddress = supplierToOrder.getAddress();
            String contactPhoneNumber = supplierToOrder.getContactPhoneNumber();
//            ArrayList<Pair<SupplierProduct,Integer>> productsToOrder =new ArrayList<>();
            ArrayList<SupplierProduct> productsToOrder = new ArrayList<>();

            for (Pair<Integer, Integer> integerIntegerPair : suppliersProduct) {//run over all products to order from supplier
                int productId = integerIntegerPair.getFirst();
                int amountToOrder = integerIntegerPair.getSecond();

                SupplierProduct product = new SupplierProduct(supplierToOrder.getProductById(productId));
                product.setAmount(amountToOrder);
                productsToOrder.add(product);
                //Update the amount in supplier
//                Response res = supplierDAO.updateSupplierProductAmount(supplierToOrder.getSupplierId(), product.getProductID(), supplierToOrder.getProductById(productId).getAmount() - product.getAmount());
//                if(res.errorOccurred()) return res;
//                productsToOrder.add(new Pair<>(product, amountToOrder));
            }
            double priceAfterDiscount = supplierToOrder.calculatePriceAfterDiscount(suppliersProduct);
            int totalAmountToOrder = supplierToOrder.getTotalAmount(suppliersProduct);
            if (supplierToOrder.getTotalOrderDiscountPerOrderPrice() != null && supplierToOrder.getTotalDiscountInPrecentageForOrder() != null) {
                if (priceAfterDiscount > supplierToOrder.getTotalOrderDiscountPerOrderPrice().getFirst()) {
                    priceAfterDiscount = priceAfterDiscount - supplierToOrder.getTotalOrderDiscountPerOrderPrice().getSecond();
                }
                if (totalAmountToOrder > supplierToOrder.getTotalDiscountInPrecentageForOrder().getFirst()) {
                    priceAfterDiscount = ((100 - supplierToOrder.getTotalDiscountInPrecentageForOrder().getSecond()) / 100) * priceAfterDiscount;
                }
            }
            double priceBeforeDiscount = supplierToOrder.calculatePriceBeforeDiscount(suppliersProduct);

            LocalDate deliveyDate = LocalDate.now().plusDays(supplierToOrder.getSupplierClosestDaysToDelivery());//create the arrival date

            Order newOrderForSupplier = new Order(id++, supplierName, supplierAddress, supplierId, contactPhoneNumber, productsToOrder, priceBeforeDiscount, priceAfterDiscount,deliveyDate, branchId);
            if (!supplierOrders.containsKey(supplierId)) {
                supplierOrders.put(supplierId, new ArrayList<>());
            }
            supplierOrders.get(supplierId).add(newOrderForSupplier);
            Response response = supplierOrderDAO.addOrder(newOrderForSupplier);
            if(response.errorOccurred()) return response;
        }
        return new Response(0);

    }

    public Response updateProductsInOrder(int orderID, HashMap<Integer, Integer> productsToAdd)
    {
        Order order = supplierOrderDAO.getOrderByID(orderID);
        if(order == null)
            return new Response("Periodic Order Updating Fails, Reason: OrderID Is Not Exists");
        if(Math.abs(ChronoUnit.DAYS.between(order.getDeliveryDate(), LocalDate.now())) <= 1)
            return new Response("Periodic Order Updating Fails, Reason: There is one day or less until the delivery");
        for(int productID : productsToAdd.keySet())
            if (supplierProductDAO.getSupplierProduct(order.getSupplierId(), productID) == null)
                return new Response("The supplier with the ID: " + order.getSupplierId() + " not supplying the product with the ID: " + productID);
        Supplier supplierToOrder = supplierDAO.getSupplierByID(order.getSupplierId());
        ArrayList<SupplierProduct> productsInOrder = order.getItemsInOrder();
        TreeSet<SupplierProduct> productsToOrder = new TreeSet<>(Comparator.comparingInt(SupplierProduct::getProductID));
//        ArrayList<SupplierProduct> productsToOrder = new ArrayList<>(order.getItemsInOrder());
        for (Map.Entry<Integer, Integer> productAndAmount : productsToAdd.entrySet())
        {
            int productID = productAndAmount.getKey();
            int amount = productAndAmount.getValue();
            SupplierProduct productInSupplier = supplierProductDAO.getSupplierProduct(order.getSupplierId(), productID);
            if(productInSupplier == null || productInSupplier.getAmount() == 0) continue;
            SupplierProduct product = new SupplierProduct(productInSupplier);
            if(productInSupplier.getAmount() > amount) product.setAmount(amount);
            productsToOrder.add(product);
        }
        //Update the products amount for the order - checks the amount that the supplier got
        for(SupplierProduct productInOrder : productsInOrder)
        {
            SupplierProduct productInSupplier = supplierProductDAO.getSupplierProduct(order.getSupplierId(), productInOrder.getProductID());
            if(productInSupplier == null || productInSupplier.getAmount() == 0) continue;
            if(productInSupplier.getAmount() < productInOrder.getAmount())
                productsToOrder.add(new SupplierProduct(productInSupplier));
            else
                productsToOrder.add(new SupplierProduct(productInOrder));
        }
        ArrayList<SupplierProduct> productsList = new ArrayList<>(productsToOrder);
        double priceAfterDiscount = supplierToOrder.calculatePriceAfterDiscountNew(productsList);
        int totalAmountToOrder = supplierToOrder.getTotalAmountNew(productsList);
        if (supplierToOrder.getTotalOrderDiscountPerOrderPrice() != null && supplierToOrder.getTotalDiscountInPrecentageForOrder() != null)
        {
            if (priceAfterDiscount > supplierToOrder.getTotalOrderDiscountPerOrderPrice().getFirst())
                priceAfterDiscount = priceAfterDiscount - supplierToOrder.getTotalOrderDiscountPerOrderPrice().getSecond();
            if (totalAmountToOrder > supplierToOrder.getTotalDiscountInPrecentageForOrder().getFirst())
                priceAfterDiscount = ((100 - supplierToOrder.getTotalDiscountInPrecentageForOrder().getSecond()) / 100) * priceAfterDiscount;
        }
        double priceBeforeDiscount = supplierToOrder.calculatePriceBeforeDiscountNew(productsList);
        Order updatedOrderForSupplier = new Order(order, productsList, priceBeforeDiscount, priceAfterDiscount);
        Response response = supplierOrderDAO.removeOrder(orderID);
        if(response.errorOccurred()) return response;
        response = supplierOrderDAO.addOrder(updatedOrderForSupplier);
        if(response.errorOccurred()) return response;
        return new Response(updatedOrderForSupplier.getOrderID());
    }

    public Response removeProductsFromOrder(int orderID, ArrayList<Integer> productsToRemove)
    {
        Order order = supplierOrderDAO.getOrderByID(orderID);
        if(order == null)
            return new Response("Periodic Order Updating Fails, Reason: OrderID Is Not Exists");
        if(Math.abs(ChronoUnit.DAYS.between(order.getDeliveryDate(), LocalDate.now())) <= 1)
            return new Response("Periodic Order Updating Fails, Reason: There is one day or less until the delivery");
        boolean found = false;
        for(int productID : productsToRemove) {
            for (SupplierProduct supplierProduct : order.getItemsInOrder())
            {
                if (supplierProduct.getProductID() == productID) {
                    found = true;
                    break;
                }
            }
            if(!found) return new Response("In this order there is no such product with the ID: " + productID);
            else found = false;
        }

        Supplier supplierToOrder = supplierDAO.getSupplierByID(order.getSupplierId());
        ArrayList<SupplierProduct> productsToOrder = new ArrayList<>(order.getItemsInOrder());
        for (int productID : productsToRemove)
            productsToOrder.removeIf(product -> product.getProductID() == productID);
        //Update the products amount for the order - checks the amount that the supplier got
        double priceAfterDiscount = supplierToOrder.calculatePriceAfterDiscountNew(productsToOrder);
        int totalAmountToOrder = supplierToOrder.getTotalAmountNew(productsToOrder);
        if (supplierToOrder.getTotalOrderDiscountPerOrderPrice() != null && supplierToOrder.getTotalDiscountInPrecentageForOrder() != null)
        {
            if (priceAfterDiscount > supplierToOrder.getTotalOrderDiscountPerOrderPrice().getFirst())
                priceAfterDiscount = priceAfterDiscount - supplierToOrder.getTotalOrderDiscountPerOrderPrice().getSecond();
            if (totalAmountToOrder > supplierToOrder.getTotalDiscountInPrecentageForOrder().getFirst())
                priceAfterDiscount = ((100 - supplierToOrder.getTotalDiscountInPrecentageForOrder().getSecond()) / 100) * priceAfterDiscount;
        }
        double priceBeforeDiscount = supplierToOrder.calculatePriceBeforeDiscountNew(productsToOrder);
        Order updatedOrderForSupplier = new Order(order, productsToOrder, priceBeforeDiscount, priceAfterDiscount);
        Response response = supplierOrderDAO.removeOrder(orderID);
        if(response.errorOccurred()) return response;
        if(priceAfterDiscount == 0){
            response = supplierOrderDAO.addOrder(updatedOrderForSupplier);
            if (response.errorOccurred()) return response;
        }
        return new Response(updatedOrderForSupplier.getOrderID());
    }



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
            if(productInSupplier.getAmount() < productInOrder.getAmount())
            {
                //Update the amount in supplier
                productsToOrder.add(new SupplierProduct(productInSupplier));
//                Response res = supplierDAO.updateSupplierProductAmount(supplierToOrder.getSupplierId(), productInSupplier.getProductID(), 0);
//                if(res.errorOccurred()) return res;
            }
            else
            {
                productsToOrder.add(new SupplierProduct(productInOrder));
                //Update the amount in supplier
//                Response res = supplierDAO.updateSupplierProductAmount(supplierToOrder.getSupplierId(), productInOrder.getProductID(), productInSupplier.getAmount() - productInOrder.getAmount());
//                if(res.errorOccurred()) return res;
            }

        }
        double priceAfterDiscount = supplierToOrder.calculatePriceAfterDiscountNew(productsToOrder);
        int totalAmountToOrder = supplierToOrder.getTotalAmountNew(productsToOrder);
        if (supplierToOrder.getTotalOrderDiscountPerOrderPrice() != null && supplierToOrder.getTotalDiscountInPrecentageForOrder() != null)
        {
            if (priceAfterDiscount > supplierToOrder.getTotalOrderDiscountPerOrderPrice().getFirst())
                priceAfterDiscount = priceAfterDiscount - supplierToOrder.getTotalOrderDiscountPerOrderPrice().getSecond();
            if (totalAmountToOrder > supplierToOrder.getTotalDiscountInPrecentageForOrder().getFirst())
                priceAfterDiscount = ((100 - supplierToOrder.getTotalDiscountInPrecentageForOrder().getSecond()) / 100) * priceAfterDiscount;
        }
        double priceBeforeDiscount = supplierToOrder.calculatePriceBeforeDiscountNew(productsToOrder);
        LocalDate deliveryDate = LocalDate.now().plusDays(supplierToOrder.getSupplierClosestDaysToDelivery());//create the arrival date
        Order newOrderForSupplier = new Order(id++, supplierToOrder.getName(), supplierToOrder.getAddress(), supplierToOrder.getSupplierId(), supplierToOrder.getContactPhoneNumber(), productsToOrder, priceBeforeDiscount, priceAfterDiscount,deliveryDate, periodicOrder.getBranchID());
        Response response = supplierOrderDAO.addOrder(newOrderForSupplier);
        if(response.errorOccurred()) return response;
        return new Response(newOrderForSupplier.getOrderID());
    }

    public HashMap<Supplier, ArrayList<Pair<Integer, Integer>>> createOrderList (ArrayList<ArrayList<Supplier>> sortedSuppliers, HashMap<Integer, Integer> shortage){
        int i = 0;
        HashMap<Supplier, ArrayList<Pair<Integer, Integer>>> orderList = new HashMap<>();//sup<productId,amount>
        for (Map.Entry<Integer, Integer> entry : shortage.entrySet()) {
            int amountToSupply = entry.getValue();
            int productId = entry.getKey();
            for (int j = 0; j < sortedSuppliers.get(i).size(); j++) { //i for product. j for suppliers that supply product i
                if (amountToSupply > 0) {
                    int supplierAmount = sortedSuppliers.get(i).get(j).getAmountByProduct(productId);
                    if (amountToSupply <= supplierAmount) {
                        Pair<Integer, Integer> pair = new Pair<>(productId, amountToSupply);
                        if (orderList.containsKey(sortedSuppliers.get(i).get(j))) {
                            orderList.get(sortedSuppliers.get(i).get(j)).add(pair);
                        }
                        else {
                            ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
                            list.add(pair);
                            orderList.put(sortedSuppliers.get(i).get(j), list);
                        }
                        break;
                    }
                    else //(amountToSupply > supplierAmount)
                    {
                        amountToSupply = amountToSupply - supplierAmount;
                        Pair<Integer, Integer> pair2 = new Pair<>(productId, supplierAmount);
                        if (orderList.containsKey(sortedSuppliers.get(i).get(j))) {
                            orderList.get(sortedSuppliers.get(i).get(j)).add(pair2);
                        } else {
                            ArrayList<Pair<Integer, Integer>> list2 = new ArrayList<>();
                            list2.add(pair2);
                            orderList.put(sortedSuppliers.get(i).get(j), list2);
                        }
                    }
                }
            }
            if(i < sortedSuppliers.size()){
                i++;
            }
        }
        return orderList;
    }


    public Order getOrderByID(int orderID) { return supplierOrderDAO.getOrderByID(orderID); }


    public void PrintOrders() {
        if(!supplierOrders.isEmpty()){
            for (Map.Entry<Integer, ArrayList<Order>> entry : supplierOrders.entrySet()) {
                ArrayList<Order> orderArrayList = entry.getValue();
                for(Order order: orderArrayList)
                    System.out.println(order);
                System.out.println("\n");
            }
        }
        else{
            System.out.println("can't make an order");
        }
    }
}
