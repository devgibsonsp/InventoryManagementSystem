/**
 * RestockingResult.java
 * Author: Steven Gibson
 * Date Created: 4/3/2017
 * Last Modified: 4/3/2017
 * Description: Implements the result of restocking product to a location in database
 */
public class RestockingResult
{

    //-------Private Data Members----------------------------
    private ProductRec product;

    //-------------------------------------------------------

    /**
     * Constructs a RestockingResult object
     * @param product The product to be restocked in the database
     */
    public RestockingResult(ProductRec product)
    {
        this.product = product;
    } // END constructor

    //-------------------------------------------------------

    /**
     * Returns the object from the restocking result
     * @return ProductRec object
     */
    public ProductRec getProduct()
    {
        return this.product;
    } // END getProduct

    //-------------------------------------------------------

} // END class
