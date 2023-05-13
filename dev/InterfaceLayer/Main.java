package InterfaceLayer;

import BusinessLayer.*;
import DataAccess.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws SQLException {

        CliDan cliDan = new CliDan();
        cliDan.categoryUI();
//        cliDan.productUI();;
        ProductsDao productsDao = new ProductsDaoImpl();
        ItemsDao itemsDao = new ItemsDaoImpl();
        DiscountsDao discountsDao = new DiscountDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
//
//        System.out.println("======================addNewDiscount===========================");
//        Product product1 = productsDao.getProductByID(1);
//        Product product2 = productsDao.getProductByID(2);
//        Product product3 = productsDao.getProductByID(3);
//        Product product4 = productsDao.getProductByID(4);
//        Category category1 = categoryDao.getCategoryByID(1);
//        Category category2 = categoryDao.getCategoryByID(2);
//        Category category3 = categoryDao.getCategoryByID(3);
//        Category category4 = categoryDao.getCategoryByID(4);
//
//        LocalDate SDate1 = LocalDate.of(2023, 4, 15);
//        LocalDate EDate1 = LocalDate.of(2023, 5, 13);
//        LocalDate SDate2 = LocalDate.of(2023, 3, 22);
//        LocalDate EDate2 = LocalDate.of(2023, 8, 29);
//        LocalDate SDate3 = LocalDate.of(2023, 2, 10);
//        LocalDate EDate3 = LocalDate.of(2023, 6, 24);
//        LocalDate SDate4 = LocalDate.of(2023, 4, 14);
//        LocalDate EDate4 = LocalDate.of(2023, 5, 4);
//        Discount discountProduct1 = discountsDao.addNewDiscount(1,SDate1,EDate1,15,null,product1);
//        Discount discountProduct2 = discountsDao.addNewDiscount(2,SDate2,EDate2,22,null,product2);
//        Discount discountProduct3 = discountsDao.addNewDiscount(3,SDate3,EDate3,16,null,product3);
//        Discount discountProduct4 = discountsDao.addNewDiscount(4,SDate4,EDate4,25,null,product4);
//        Discount discountProduct5 = discountsDao.addNewDiscount(1,SDate2,EDate4,8,null,product1);

//
//        Discount discountCategory1 = discountsDao.addNewDiscount(1,SDate1,EDate1,5,category1,null);
//        Discount discountCategory2 = discountsDao.addNewDiscount(2,SDate2,EDate2,20,category2,null);
//        Discount discountCategory3 = discountsDao.addNewDiscount(3,SDate3,EDate3,10,category3,null);
//        Discount discountCategory4 = discountsDao.addNewDiscount(4,SDate4,EDate4,40,category4,null);
//        Discount discountCategory5 = discountsDao.addNewDiscount(1,SDate1,EDate1,7,category1,null);

//        System.out.println("=================================================================================================================");

//        System.out.println("======================getDiscountByID===========================");
//        System.out.println("=========================   discount 1========================");
//        Discount discount1 = discountsDao.getDiscountByID(1);
//        System.out.println(discount1);
//        System.out.println("=================================================");
//        System.out.println("=========================   discount 2========================");
//        Discount discount2 = discountsDao.getDiscountByID(2);
//        System.out.println(discount2);
//        System.out.println("=================================================================================================================");

//        System.out.println("======================getAllDiscount===========================");
//
//        List<Discount> discounts = discountsDao.getAllDiscount();
//        for (Discount discount : discounts)
//        {
//            System.out.println(discount);
//            System.out.println("=================================================");
//        }
//        System.out.println("=================================================================================================================");
//        System.out.println("======================getAllDiscountByBranchID===========================");
//
//        List<Discount> discounts = discountsDao.getAllDiscountByBranchID(1);
//        for (Discount discount : discounts)
//        {
//            System.out.println(discount);
//            System.out.println("=================================================");
//        }
//        System.out.println("=================================================================================================================");
//        System.out.println("======================getLastDiscountOfProductInBranch===========================");
//
//        Discount discount = discountsDao.getLastDiscountOfProductInBranch(1,1);
//            System.out.println(discount);
//        System.out.println("=================================================================================================================");
//        System.out.println("======================getLastDiscountOfCategoryInBranch===========================");
//
//        Discount discount = discountsDao.getLastDiscountOfCategoryInBranch(1,1);
//        System.out.println(discount);
//        System.out.println("=================================================================================================================");
//        System.out.println("======================checkValidDiscount===========================");
//        for (int i = 1 ; i<11;i++)
//        {
//            System.out.println("Discount ID : " + i +" --- " +" Valid Or Not : " + discountsDao.checkValidDiscount(i));
//        }
//        System.out.println("=================================================================================================================");


//        System.out.println("========================= discountsMapFromDB========================");
//        Map<Integer, Discount> discountMap = discountsDao.getDiscountsMapFromDB();
//        for (Map.Entry<Integer, Discount> entry : discountMap.entrySet())
//        {
//            System.out.println(entry.getValue());
//            System.out.println("===================");
//        }

    }
}

