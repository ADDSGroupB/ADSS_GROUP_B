package BusinessLayer.InventoryBusinessLayer;

public class Branch {
    private final int BranchID;
    private String BranchName;
    private static int MinItemsInShelf = 20;
    private static int MaxItemsInShelf = 50;

    private BranchReportManager branchReportManager;

    public Branch(int branchID,String name) {
        this.BranchID = branchID;
        this.BranchName = name;// get
        this.branchReportManager = new BranchReportManager();// get
    }
    @Override
    public String toString(){
        return "** BranchID : " + BranchID + " ** " + "Branch Name: " + BranchName + " **\n";
    }
    public int getMinItemsInShelf(){ return MinItemsInShelf;}
    public int getMaxItemsInShelf(){ return MaxItemsInShelf;}
    public int getBranchID() {
        return BranchID;
    }
    public String getBranchName() {
        return BranchName;
    }
    public BranchReportManager getBranchReportManager() {
        return branchReportManager;
    }

}
