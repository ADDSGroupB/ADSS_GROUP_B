package InterfaceLayer;
import BusinessLayer.Item;
import BusinessLayer.StatusEnum;
import DataAccessLayer.*;

import java.sql.SQLException;
public class Main {
    public static void main(String[] args) throws SQLException {
        Cli cli = new Cli();
        cli.Start();
//        ProductsDao productsDao = new ProductsDaoImpl();
//        ItemsDao itemsDao = new ItemsDaoImpl();
//        BranchesDao branchesDao = new BranchesDaoImpl();
//        CategoryDao categoryDao = new CategoryDaoImpl();
//        DiscountsDao discountsDao = new DiscountDaoImpl();
//        ProductMinAmountDao productMinAmountDao = new ProductMinAmountDaoImpl();
//
//        Item item = itemsDao.getItemByID(1);
//        item.setStatusType(StatusEnum.Storage);
//        itemsDao.updateItemStatus(1,"Storage");
//        System.out.println(item);
//        System.out.println("========================================");
//
//        itemsDao.updateItemStatus(1,"KK");
//        System.out.println("============= After Change Status ===========================");
//
//        Item item1 = itemsDao.getItemByID(1);
//        System.out.println(item);
//        System.out.println("========================================");
    }
}

