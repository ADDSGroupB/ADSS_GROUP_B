package BusinessLayer;

import Utillity.Pair;
import Utillity.Response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderController {
    private HashMap<Integer, ArrayList<Order>> supplierOrders; //supplierId, Order

    public OrderController(){
        supplierOrders = new HashMap<>();
    }

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
//            // TODO - ������ ���� ���� ��� ������
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
            ArrayList<Pair<SupplierProduct,Integer>> productsToOrder =new ArrayList<>();

            for (int j = 0; j < suppliersProduct.size(); j++) {//run over all products to order from supplier
                int productId = suppliersProduct.get(j).getFirst();
                int amountToOrder = suppliersProduct.get(j).getSecond();

                SupplierProduct product = supplierToOrder.getProductById(productId);
                productsToOrder.add(new Pair<>(product, amountToOrder));
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

            Order newOrderForSupplier = new Order(supplierName, supplierAddress, supplierId, contactPhoneNumber, productsToOrder, priceBeforeDiscount, priceAfterDiscount,deliveyDate, branchId);
            if (!supplierOrders.containsKey(supplierId)) {
                supplierOrders.put(supplierId, new ArrayList<>());
            }
            supplierOrders.get(supplierId).add(newOrderForSupplier);
        }
        return new Response(0);

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
