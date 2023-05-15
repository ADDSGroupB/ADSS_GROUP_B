package BusinessLayer.SupplierBusinessLayer;

import ServiceLayer.SupplierServiceLayer.SupplierProductService;
import Utillity.Response;

import java.util.HashMap;
import java.util.Map;

public class ProductController {

    private HashMap<Integer, HashMap<Integer, Integer>> productIdAndCatalogId; // <productId: <supplierId: catalogId>>


    public ProductController(){
        productIdAndCatalogId = new HashMap<>();
    }



    public HashMap<Integer, SupplierProduct> createSupllyingProducts(HashMap<Integer, SupplierProductService> supllyingProducts, int supplierId) {
        HashMap<Integer, SupplierProduct> supplierProduct = new HashMap<>();
        for (Map.Entry<Integer, SupplierProductService> entry : supllyingProducts.entrySet()) {
            int productId = entry.getKey();
            SupplierProductService product = entry.getValue();
            int catatalogNumber = product.getCatalogNumber();
            SupplierProduct supplierProduct1 = new SupplierProduct(product.getName(), supplierId, product.getProductId(), product.getCatalogNumber(), product.getPrice(), product.getAmount(), product.getDiscountPerAmount(), product.getManufacturer(), product.getExpirationDays(), product.getWeight());
            supplierProduct.put(productId, supplierProduct1);
            if (productIdAndCatalogId.containsKey(productId)) {
                productIdAndCatalogId.get(productId).put(supplierId, catatalogNumber);
            } else {
                HashMap<Integer, Integer> toAdd = new HashMap<>();
                toAdd.put(supplierId, catatalogNumber);
                productIdAndCatalogId.put(productId, toAdd);
            }

        }
        return supplierProduct;
    }

    public Response removeSupplierProducts(int id) {
        int candidate = 0;
        for (Map.Entry<Integer, HashMap<Integer, Integer>> entry : productIdAndCatalogId.entrySet()) {
                HashMap<Integer, Integer> product =  entry.getValue();
                if(product.containsKey(id)){
                    candidate = 1;
                    product.remove(id);
                }
            }
        if(candidate == 1){
            return new Response(id);
        }
        return new Response("The user doesn't have any products yet");
    }

    public void addProductToSupplier(int supplierID,String name, int productId, int catalogNumber, double price, HashMap<Integer, Double> discountPerAmount) {
        if(productIdAndCatalogId.containsKey(productId)){
            productIdAndCatalogId.get(productId).put(supplierID, catalogNumber);
            }
         else {
            HashMap<Integer, Integer> toAdd = new HashMap<>();
            toAdd.put(supplierID, catalogNumber);
            productIdAndCatalogId.put(productId, toAdd);
            }
    }

    public Response removeProductToSupplier(int supplierID, int itemIdToDelete) {
        if(productIdAndCatalogId.containsKey(itemIdToDelete)){
            productIdAndCatalogId.get(itemIdToDelete).remove(supplierID);
            return new Response(supplierID);
        }
        return new Response("can not delete item with id: " + itemIdToDelete + " beacuse it is not exist in the supplier's products List");
    }

    public void editItemCatalodNumber(int supplierId, int productId, int newCatalogNumber) {
        HashMap<Integer, Integer> p =productIdAndCatalogId.get(productId); //supplierId, catalogNumber
        if (p.containsKey(supplierId)){
            p.remove(supplierId);
            p.put(supplierId, newCatalogNumber);
        }

    }
}