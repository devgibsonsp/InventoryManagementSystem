/**
 * PickingResult.java
 * Author: Steven Gibson
 * Date Created: 4/3/2017
 * Last Modified: 4/3/2017
 * Description: Implements the result of picking product from a location in database
 */
public class PickingResult
{

    //-------Private Data Members----------------------------
    private ProductRec product;

    //-------------------------------------------------------

    /**
     * Constructs a PickingResult object
     * @param product The product to be picked from the database
     */
    public PickingResult(ProductRec product)
    {
        this.product = product;
    } // END constructor

    //-------------------------------------------------------

    /**
     * Returns the object from the picking result
     * @return ProductRec object
     */
    public ProductRec getProduct()
    {
        return this.product;
    } // END getProduct

    //-------------------------------------------------------

} // END class