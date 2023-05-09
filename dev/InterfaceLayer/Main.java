package InterfaceLayer;

import BusinessLayer.*;
import DataAccess.*;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {
//        Cli cli = new Cli();
//        cli.start();

        ProductsDao productsDao = new ProductsDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        BranchesDao branchesDao = new BranchesDaoImpl();

//        Branch branch1 = branchesDao.getBranchByID(1);
//        System.out.println("Branch ID: " + branch1.getBranchID());
//        System.out.println("Branch Name: " + branch1.getBranchName());
//        Branch branch5 = branchesDao.getBranchByID(5);
//        System.out.println("Branch ID: " + branch5.getBranchID());
//        System.out.println("Branch Name: " + branch5.getBranchName());


//        List<Branch> currBranches = branchesDao.getAllBranches();
//        for (Branch branch : currBranches) {
//            System.out.println("Branch ID: " + branch.getBranchID());
//            System.out.println("Branch Name: " + branch.getBranchName());
//        }
//        branchesDao.addBranch("Super8"); // add new branch
//        branchesDao.addBranch("Super9"); // add new branch
//        branchesDao.addBranch("Super10"); // add new branch
//        branchesDao.addBranch("Super11"); // add new branch


//        System.out.println("-------------------------------------------------------------------------");
//        List<Branch> currBranchess = branchesDao.getAllBranches();
//        for (Branch branch : currBranchess) {
//            System.out.println("Branch ID: " + branch.getBranchID());
//            System.out.println("Branch Name: " + branch.getBranchName());
//        }
//
//        Branch branch1 = new Branch("Super1");
//        Branch branch2 = new Branch("Super2");
//        Branch branch3 = new Branch("Super3");
//        Branch branch4 = new Branch("Super4");
//        Branch branch5 = new Branch("Super5");
//
//        branchesDao.addBranch(branch1);
//        branchesDao.addBranch(branch2);
//        branchesDao.addBranch(branch3);
//        branchesDao.addBranch(branch4);
//        branchesDao.addBranch(branch5);
//
//        // Motzari Halav
//        Category DairyProducts = new Category("Dairy products");
//        // type - Milk , Cottage .... -- sub category for motzari halav
//        Category MilkCat = new Category("Milk");
//        Category cottage = new Category("Cottage");
//        Category CreamCheese = new Category("Cream Cheese");
//        Category YellowCheese = new Category("Yellow Cheese");
//
//        // SUB SUB category for MotzariHalav - 3% fat , 5% fat .....
//
//        Category fat1 = new Category("1% fat");
//        Category fat3 = new Category("3% fat");
//        Category fat5 = new Category("5% fat");
//        Category fat9 = new Category("9% fat");
//
//        // vegetables
//        Category vegetables = new Category("vegetables");
//        // sub category for vegetables
//        Category Onions = new Category("Onions");
//        Category Potato = new Category("Potato");
//
//        // subsub category for vegetables
//        Category GreenOnions = new Category("Green Onions");
//        Category WhiteOnions = new Category("White Onions");
//        Category RedPotato = new Category("Red Potato");
//
//        //Fruits
//        Category fruits = new Category("Fruits");
//
//        // sub category for Fruits
//
//        Category apples = new Category("Apples");
//        // subsub category for Fruits
//        Category redApples = new Category("Red Apples");
//        Category greenApples = new Category("Green Apples");
//
//        Product p1 = new Product("Milk 3%","Tara" , 500,DairyProducts,MilkCat,fat3);
//        Product p2 = new Product("Cottage 5% ","Tnova" , 250,DairyProducts,cottage,fat5);
//        Product p3 = new Product("White Onion","vegandfruit" , 20,vegetables,Onions,WhiteOnions);
//        Product p4 = new Product("Green Onion","vegandfruit" , 20,vegetables,Onions,GreenOnions);
//        Product p5 = new Product("Red Potato","vegandfruit" , 10,vegetables,Potato,RedPotato);
//        Product p6 = new Product("Red Apple","vegandfruit" , 150,fruits,apples,redApples);
//        Product p7 = new Product("Green Apple","vegandfruit" , 200,fruits,apples,greenApples);
//
//        productsDao.addProduct(p1);
//        productsDao.addProduct(p2);
//        productsDao.addProduct(p3);
//        productsDao.addProduct(p4);
//        productsDao.addProduct(p5);
//        productsDao.addProduct(p6);
//        productsDao.addProduct(p7);
//        categoryDao.addCategory(DairyProducts.getCategoryName());
//        categoryDao.addCategory(MilkCat.getCategoryName());
//        categoryDao.addCategory(cottage.getCategoryName());
//        categoryDao.addCategory(CreamCheese.getCategoryName());
//        categoryDao.addCategory(YellowCheese.getCategoryName());
//        categoryDao.addCategory(fat1.getCategoryName());
//        categoryDao.addCategory(fat3.getCategoryName());
//        categoryDao.addCategory(fat5.getCategoryName());
//        categoryDao.addCategory(fat9.getCategoryName());
//        categoryDao.addCategory(vegetables.getCategoryName());
//        categoryDao.addCategory(Onions.getCategoryName());
//        categoryDao.addCategory(Potato.getCategoryName());
//        categoryDao.addCategory(GreenOnions.getCategoryName());
//        categoryDao.addCategory(WhiteOnions.getCategoryName());
//        categoryDao.addCategory(RedPotato.getCategoryName());
//        categoryDao.addCategory(fruits.getCategoryName());
//        categoryDao.addCategory(apples.getCategoryName());
//        categoryDao.addCategory(redApples.getCategoryName());
//        categoryDao.addCategory(greenApples.getCategoryName());



//        Category category = categoryDao.getCategoryByID(10);
//        System.out.println(category.toString());
//          List<Product> products = productsDao.getAllProducts();
//          for (Product p : products)
//          {
//              System.out.println(p);
//              System.out.println("----------");
//          }
//        System.out.println("-------------------------------------------------------------------------------");
//
//        List<Category> categories = categoryDao.getAllCategories();
//        for (Category c : categories)
//        {
//            System.out.println(c);
//            for (Product p : c.getProductsInCategory())
//            {
//                System.out.println(p);
//                System.out.println("----------");
//            }
//            System.out.println("----------");
//        }
//         Category category = categoryDao.getCategoryByID(1);
//         System.out.println(category.toString());

    }
}

