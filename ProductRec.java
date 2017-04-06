/**
 * ProductRec.java
 * Author: Steven Gibson
 * Date Created: 4/3/2017
 * Last Modified: 4/3/2017
 * Description: Defines a product type object for storage in database
 */
public class ProductRec
{

    //-------Private Data Members----------------------------
    private String productId;
    private String location;
    private int productLevel;

    //-------------------------------------------------------

    /**********************************************************
     * Constructs a 'ProductRec' object
     **********************************************************/
    public ProductRec(String productId, String location, int productLevel)
    {
        this.productId = productId;
        this.location = location;
        this.productLevel = productLevel;

    } // END constructor

    //-------Getters-----------------------------------------

    public String getProductId()
    {
        return this.productId;
    } // END getProductId

    public String getLocation()
    {
        return this.location;
    } // END getLocation

    public int getProductLevel()
    {
        return this.productLevel;
    } // END getProductLevel

    //-------Setters-----------------------------------------

    public void setProductLevel(int amt)
    {
        this.productLevel = amt;
    } // END setProductLevel

    //-------------------------------------------------------

} // END class
