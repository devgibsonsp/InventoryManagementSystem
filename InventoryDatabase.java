import java.util.concurrent.ConcurrentHashMap;

/**
 * InventoryDatabase.java
 * Author: Steven Gibson
 * Date Created: 4/3/2017
 * Last Modified: 4/4/2017
 * Description: InventoryDatabase stores products in a database for use in picking and restocking items in a warehouse
 * Assumptions:
 *          -Each product will be stored at one and only one named location within the warehouse.
 *          -Inventory adjustments may be additive (restocks) or subtractive (picks).
 *          -No additional product information needs to be tracked beyond location and level.
 */
public class InventoryDatabase implements InventoryManagementSystem
{

    //-------Private Data Members----------------------------

    // Map to store products in database <product ID, product>
    private ConcurrentHashMap<String, ProductRec> productMap;

    // Map to store locations in database <location, product>
    private ConcurrentHashMap<String,ProductRec> locMap;

    //-------------------------------------------------------

    /**********************************************************
     * Constructs an 'InventoryDatabase' object
     **********************************************************/
    public InventoryDatabase()
    {
        // Creating the hashmap for the database
        this.productMap = new ConcurrentHashMap<>();
        this.locMap = new ConcurrentHashMap<>();
    } // END constructor

    //-------------------------------------------------------

    /**********************************************************
     * View product by product ID
     * @param productId The ID of the product to view
     * @return ProductRec product with associated productId
     **********************************************************/
    public ProductRec viewProduct(String productId)
    {
        // If the product exists in the database, return it
        if(this.productMap.containsKey(productId))
            return this.productMap.get(productId);

        // Otherwise, return an invalid product with invalid product level
        ProductRec invalidProduct = new ProductRec("Product Not Found", "Invalid Location", -1);

        return invalidProduct;

    } // END viewProduct

    //-------------------------------------------------------


    /**********************************************************
     * View product by location
     * @param location of the product to view
     * @return ProductRec product with associated location
     **********************************************************/
    public ProductRec viewLocation(String location)
    {
        if(this.locMap.containsKey(location))
            return this.locMap.get(location);

        // Otherwise, return an invalid product with invalid product level
        ProductRec invalidProduct = new ProductRec("Product Not Found", "Invalid Location", -1);

        return invalidProduct;

    } // END viewLocation

    /**********************************************************
     * Add product to the database
     * @param product The product to be added to the database
     * @return Boolean value indicating success or failure to add
     **********************************************************/
    public Boolean addProduct(ProductRec product)
    {


        // If product isn't already present and the product level is at least 0, return true
        if(!this.productMap.containsKey(product.getProductId()) && product.getProductLevel() >= 0 && !locMap.containsKey(product.getLocation()))
        {
            this.productMap.put(product.getProductId(), product);
            this.locMap.put(product.getLocation(), product);
            return true;

        } // END if

        // Otherwise, return false
        return false;

    } // END addProduct

    //-------------------------------------------------------

    /***********************************************************
     * Remove product from the database
     * @param product The product to be removed from the database
     * @return Boolean value indicating success or failure to remove
     ***********************************************************/
    public Boolean removeProduct(String productId)
    {
        // If the product exists in the database, remove it, return true
        if(this.productMap.containsKey(productId))
        {
            this.locMap.remove(this.productMap.get(productId).getLocation());
            this.productMap.remove(productId);


            return true;

        } // END if

        // Otherwise, return false
        return false;

    } // END removeProduct

    //-------------------------------------------------------

    /***********************************************************
     * Updates values at associated locations in database
     * @param productId The ID of the product to update
     * @param product The product to be updated
     ***********************************************************/
    private void updateProduct(String productId, ProductRec product)
    {
        // Putting the new updated product into the database
        this.productMap.put(productId, product);
        this.locMap.put(this.productMap.get(productId).getLocation(), product);

    } // END UpdateProduct

    //-------------------------------------------------------

    /***********************************************************
     * Deduct 'amountToPick' of the given 'productId' from inventory.
     * @param productId The ID of the product to pick
     * @param amountToPick The quantity of the product to pick
     * @return PickingResult object with updated item
     ***********************************************************/
    public PickingResult pickProduct(String productId, int amountToPick)
    {

        ProductRec product;

        // If the product is located in the database
        if(this.productMap.containsKey(productId))
        {
            product = this.productMap.get(productId);

            // If the amount to pick is less than or equal to the product level
            if(amountToPick <= this.productMap.get(productId).getProductLevel() && amountToPick >= 1)
            {
                product.setProductLevel(product.getProductLevel() - amountToPick);
                this.updateProduct(productId, product);

            } // END if

        } // END if
        else // If the product is not present in the database
            product = new ProductRec("Product Not Found", "Invalid Location", -1);

        // Storing and returning the picking result
        PickingResult result = new PickingResult(product);
        return result;

    } // END PickingResult

    //-------------------------------------------------------

    /**
     * Add 'amountToRestock' of the given productId to inventory.
     * @param productId The ID of the product to restock
     * @param amountToRestock The quantity of the product to restock
     * @return RestockingResult object with updated item
     */
    public RestockingResult restockProduct(String productId, int amountToRestock)
    {
        ProductRec product;

        // If the product is located in the database
        if(this.productMap.containsKey(productId))
        {
            product = this.productMap.get(productId);

            // If the amount to pick is less than or equal to the product level
            if(amountToRestock > 0)
            {
                product.setProductLevel(product.getProductLevel() + amountToRestock);
                this.updateProduct(productId, product);

            } // END if

        }  // END if
        else // If the product is not present in the database
            product = new ProductRec("Product Not Found", "Invalid Location", -1);

        // Storing and returning the restocking result
        RestockingResult result = new RestockingResult(product);
        return result;

    } // END RestockingResult

    //-------------------------------------------------------

} // END class
