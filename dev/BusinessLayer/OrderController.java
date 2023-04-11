package BusinessLayer;

import Utillity.Pair;
import Utillity.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderController {
    private HashMap<Integer, Order> supplierOrders; //supplierId, Order

    public OrderController(){
        supplierOrders = new HashMap<>();
    }

    public void createOrder(Response res) {
        ArrayList<ArrayList<Pair<SupplierProduct,Integer>>> supplierProductsAndAmount = res.getSupplyLists();
        ArrayList<Supplier> supplierArrayList = res.getSuppliersInOrder();
        for(int i =0; i < supplierArrayList.size(); i++){
            Supplier currSupplier = supplierArrayList.get(i);
            int supplierId = currSupplier.getSupplierId();
            String supplierName = currSupplier.getName();
            String supplierAdress = currSupplier.getAddress();
            String contactPhoneNumber = supplierArrayList.get(i).getContacts().get(0).getPhoneNumber();//assume that every supplier have contact.
            ArrayList<Pair<Integer,Integer>> suuplierProductsToCalculate = new ArrayList<>();
            for(int j =0; j < supplierProductsAndAmount.get(i).size(); j++){
                int productId = supplierProductsAndAmount.get(i).get(j).getFirst().getProductID();
                int amountToOrder = supplierProductsAndAmount.get(i).get(j).getSecond();
                Pair<Integer,Integer> pair = new Pair<>(productId,amountToOrder);
                suuplierProductsToCalculate.add(pair);
            }
            ArrayList<Pair<SupplierProduct,Integer>> productsToOrder = supplierProductsAndAmount.get(i);
            double priceAfterDiscount = currSupplier.calculatePriceAfterDiscount(suuplierProductsToCalculate);
            int amountToorder = currSupplier.getTotalAmount(suuplierProductsToCalculate);
            if (currSupplier.getTotalOrderDiscountPerOrderPrice()!=null&&currSupplier.getTotalDiscountInPrecentageForOrder()!=null) {
                if (priceAfterDiscount > currSupplier.getTotalOrderDiscountPerOrderPrice().getFirst()) {
                    priceAfterDiscount = priceAfterDiscount - currSupplier.getTotalOrderDiscountPerOrderPrice().getSecond();
                }
                if (amountToorder > currSupplier.getTotalDiscountInPrecentageForOrder().getFirst()) {
                    priceAfterDiscount = ((100 - currSupplier.getTotalDiscountInPrecentageForOrder().getSecond()) / 100) * priceAfterDiscount;
                }
            }
            double priceBeforeDiscount = currSupplier.calculatePriceBeforeDiscount(suuplierProductsToCalculate);
            Order newOrderForSupplier = new Order(supplierName, supplierAdress, supplierId, contactPhoneNumber, productsToOrder, priceBeforeDiscount, priceAfterDiscount);
            //System.out.println(newOrderForSupplier);
            supplierOrders.put(supplierId, newOrderForSupplier);

        }
    }

    public void PrintOrders() {
        if(!supplierOrders.isEmpty()){
            for (Map.Entry<Integer, Order> entry : supplierOrders.entrySet()) {
                Integer supplierId = entry.getKey();
                Order order = entry.getValue();
                System.out.println(order);
                System.out.println("\n");
            }
        }
        else{
            System.out.println("can't make an order");
        }
    }
}
