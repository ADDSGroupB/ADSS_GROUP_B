package BuisnessLayer.TransportManager;

public class Product {

    private String product_name;
    private String type;
    private String manufacturer;
    private String product_code;
    private String category;
    private String sub_category;
    private double size;

    /**
     * Constructs a new Product object with the given parameters.
     *
     * @param product_name: the name of the product
     * @param type: the type of the product
     * @param manufacturer: the manufacturer of the product
     * @param product_code: the code of the product
     * @param category: the category of the product
     * @param sub_category: the sub-category of the product
     * @param size: the size of the product
     */
    public Product(String product_code, String product_name, String type, String manufacturer, String category, String sub_category, double size) {

        this.product_code = product_code;
        this.product_name = product_name;
        this.type = type;
        this.manufacturer = manufacturer;
        this.category = category;
        this.sub_category = sub_category;
        this.size = size;
    }

    /**
     * Gets the name of the product.
     *
     * Return the name of the product
     */
    public String getName() {

        return product_name;
    }
    /**
     * Gets the manufacturer of the product.
     *
     * Return the manufacturer of the product
     */
    public String getManufacturer() {

        return manufacturer;
    }

    /**
     * Returns a string representation of the Product object.
     *
     * Return a string representation of the Product object
     */
    @Override
    public String toString() {
        return "Product{" +
                "product_name='" + product_name + '\'' +
                ", type='" + type + '\'' +
                ", product_code='" + product_code + '\'' +
                '}';
    }


    /**
     * Gets the code of the product.
     *
     * Return the code of the product
     */
    public String getProduct_code() {

        return product_code;
    }

    /**
     * Gets the category of the product.
     *
     * Return the category of the product
     */
    public String getCategory() {

        return category;
    }

    /**
     * Gets the sub-category of the product.
     *
     * Return: the sub-category of the product
     */
    public String getSub_category() {

        return sub_category;
    }

    /**
     * Gets the size of the product.
     *
     * Return: the size of the product
     */
    public double getSize() {

        return size;
    }

    /**
     * Gets the type of the product.
     *
     * Return: the type of the product
     */
    public String getType() {

        return type;
    }
}
