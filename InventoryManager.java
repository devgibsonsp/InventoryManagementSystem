import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * InventoryManager.java
 * Author: Steven Gibson
 * Date Created: 4/3/2017
 * Last Modified: 4/4/2017
 * Description: InventoryManager implements the front end applications of the database for use in picking & restocking
 */
public class InventoryManager
{
    //-------------------------------------------------------

    public static Lock sync = new ReentrantLock();

    public static InventoryDatabase database;

    //-------------------------------------------------------

    public static void main(String args[])
    {
        database = new InventoryDatabase();

        new Thread(new runner()).start();

    } // END main

    //-------------------------------------------------------

    static class runner implements Runnable
    {
        public void run()
        {
            Manager();
        } // END run
    } // END class

    //-------------------------------------------------------

    public static void Manager()
    {
        Scanner input = new Scanner(System.in);
        int select = -1;


        while(select != 6 )
        {
            DisplayMain();
            select = Integer.parseInt(input.nextLine());
            sync.lock(); // Locking thread to ensure database integrity
            switch(select)
            {
                case 0: DisplayAdd(); break;
                case 1: DisplayPick(); break;
                case 2: DisplayRestock(); break;
                case 3: DisplayViewProduct(); break;
                case 4: DisplayViewLocation(); break;
                case 5: DisplayRemove(); break;
                case 6: System.out.println("Quitting..."); break;
                case 123: System.out.println("Entering Test Mode...");
                          test(); break;
                default: System.out.println("Invalid Option");
            } // END switch
            sync.unlock(); // Unlocking thread after database has been edited

        } // END while

        //test();
    } // END Manager

    //-------------------------------------------------------

    /**********************************************************
     * Displays the options menu for the user
     **********************************************************/
    public static void DisplayMain()
    {
        System.out.println("Please Select an Option:");
        System.out.println("0: Add Product");
        System.out.println("1: Pick Product");
        System.out.println("2: Restock Product");
        System.out.println("3: view Product");
        System.out.println("4: view Location");
        System.out.println("5: Remove Product");
        System.out.println("6: Exit Program");
    } // END DisplayMain

    //-------------------------------------------------------

    /**********************************************************
     * User interface for adding items to database
     **********************************************************/
    public static void DisplayAdd()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the product to add to the system...");
        String productId = input.nextLine();


        if(database.viewProduct(productId).getProductLevel() < 0)
        {
            System.out.println("Enter the location for the product...");
            String location = input.nextLine();

            if(database.viewLocation(location).getProductLevel() < 0)
            {
                System.out.println("Enter the product level...");
                int productLevel = Integer.parseInt(input.nextLine());

                ProductRec productToAdd = new ProductRec(productId, location, productLevel);

                if (database.addProduct(productToAdd))
                    System.out.println(productLevel + " Product(s) " + productId + " Successfully added to location " + location);

            } // END if
            else
                System.out.println("Location " + location + " Already contains a product");

        } // END if
        else
            System.out.println("Product " + productId + " Already exists at location " + database.viewProduct(productId).getLocation());

    } // END DisplayAdd

    //-------------------------------------------------------

    /**********************************************************
     * User interface for picking items from database
     **********************************************************/
    public static void DisplayPick()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the product to pick from the system...");
        String productId = input.nextLine();

        ProductRec product = database.viewProduct(productId);

        // If the product is valid
        if(product.getProductLevel() >= 0)
        {
            System.out.println("Enter an amount of product to pick...");

            int pickAmount = Integer.parseInt(input.nextLine());

            if((product.getProductLevel() - pickAmount) < 0)
                System.out.println("There is not enough product to pick");
            else if(pickAmount > 0) // if valid product
            {
                PickingResult pickProd = database.pickProduct(productId, pickAmount);

                System.out.println(pickAmount + " " + productId + "(s) picked from location " + pickProd.getProduct().getLocation());
                System.out.println(pickProd.getProduct().getProductLevel() + " " + productId + "(s) remaining");
            } // END else if
            else
                System.out.println(pickAmount + " is not a valid amount to pick");

        } // END if
        else
            System.out.println("Product " + productId + " Does not exist in the system");

    } // END DisplayPick

    //-------------------------------------------------------

    /**********************************************************
     * User interface for restocking items in database
     **********************************************************/
    public static void DisplayRestock()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the product to restock from the system...");
        String productId = input.nextLine();

        ProductRec product = database.viewProduct(productId);

        // If the product is valid
        if(product.getProductLevel() >= 0)
        {
            System.out.println("Enter an amount of product to restock...");

            int restockAmount = Integer.parseInt(input.nextLine());

            if(restockAmount > 0)
            {
                RestockingResult restockProd = database.restockProduct(productId, restockAmount);

                System.out.println(restockAmount + " " + productId + "(s) restocked at location " + restockProd.getProduct().getLocation());
                System.out.println(restockProd.getProduct().getProductLevel() + " " + productId + "(s) available");
            }
            else
                System.out.println(restockAmount + " is not a valid amount to restock");

        }
        else
            System.out.println("Product " + productId + " Does not exist in the system");



    } // END DisplayRestock

    //-------------------------------------------------------

    /**********************************************************
     * User interface for displaying item by location
     **********************************************************/
    public static void DisplayViewLocation()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the location to view from the system...");
        String location = input.nextLine();

        ProductRec product = database.viewLocation(location);


        System.out.println("Product ID: " + database.viewProduct(product.getProductId()).getProductId());
        System.out.println("Product Location: " + database.viewProduct(product.getProductId()).getLocation());
        System.out.println("Product Level: " + database.viewProduct(product.getProductId()).getProductLevel());

    }

    //-------------------------------------------------------

    /**********************************************************
     * User interface for adding item by item ID
     **********************************************************/
    public static void DisplayViewProduct()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the product to view from the system...");
        String productId = input.nextLine();

        ProductRec product = database.viewProduct(productId);

        System.out.println("Product ID: " + database.viewProduct(product.getProductId()).getProductId());
        System.out.println("Product Location: " + database.viewProduct(product.getProductId()).getLocation());
        System.out.println("Product Level: " + database.viewProduct(product.getProductId()).getProductLevel());

    }  // END DisplayView

    //-------------------------------------------------------

    /**********************************************************
     * User interface for removing item from database
     **********************************************************/
    public static void DisplayRemove()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the product to remove from the system...");
        String productId = input.nextLine();


        if(database.viewProduct(productId).getProductLevel() >= 0)
        {

            database.removeProduct(productId);

            System.out.println("Product " + productId + " Successfully removed from system");

        }
        else
            System.out.println("Product " + productId + " Does not exist in the system");

    } // END DisplayRemove

    //-------------------------------------------------------

    /**********************************************************
     * Test of the InventoryDatabase class methods
     **********************************************************/
    public static void test()
    {
        ProductRec productTest0 = new ProductRec("Mop", "B1", 12);
        ProductRec productTest1 = new ProductRec("Plunger", "B2", 2);
        ProductRec productTest2 = new ProductRec("Broom", "B3", 22);

        InventoryDatabase database2 = new InventoryDatabase();

        database2.addProduct(productTest0);
        database2.addProduct(productTest2);

        System.out.println("-------------Test 0: Testing Add & view");
        System.out.println("Product ID: " + database2.viewProduct(productTest0.getProductId()).getProductId());
        System.out.println("Product Location: " + database2.viewProduct(productTest0.getProductId()).getLocation());
        System.out.println("Product Level: " + database2.viewProduct(productTest0.getProductId()).getProductLevel());
        System.out.println("");

        System.out.println("-------------Test 1: Testing viewing invalid");
        System.out.println("Product ID: " + database2.viewProduct(productTest1.getProductId()).getProductId());
        System.out.println("Product Location: " + database2.viewProduct(productTest1.getProductId()).getLocation());
        System.out.println("Product Level: " + database2.viewProduct(productTest1.getProductId()).getProductLevel());
        System.out.println("");

        database2.removeProduct(productTest0.getProductId());
        System.out.println("-------------Test 2: Testing delete");
        System.out.println("Product ID: " + database2.viewProduct(productTest0.getProductId()).getProductId());
        System.out.println("Product Location: " + database2.viewProduct(productTest0.getProductId()).getLocation());
        System.out.println("Product Level: " + database2.viewProduct(productTest0.getProductId()).getProductLevel());
        System.out.println("");

        System.out.println("-------------Test 3: Testing pickProduct with valid amount");
        System.out.println("Product ID: " + database2.viewProduct(productTest2.getProductId()).getProductId());
        System.out.println("Product Location: " + database2.viewProduct(productTest2.getProductId()).getLocation());
        System.out.println("Product Level: " + database2.viewProduct(productTest2.getProductId()).getProductLevel());
        System.out.println("-------------Picking 12 products...");
        database2.pickProduct("Broom", 12);
        System.out.println("Product ID: " + database2.viewProduct(productTest2.getProductId()).getProductId());
        System.out.println("Product Location: " + database2.viewProduct(productTest2.getProductId()).getLocation());
        System.out.println("Product Level: " + database2.viewProduct(productTest2.getProductId()).getProductLevel());
        System.out.println("");

        System.out.println("-------------Test 4: Testing pickProduct with invalid amount");
        System.out.println("Product ID: " + database2.viewProduct(productTest2.getProductId()).getProductId());
        System.out.println("Product Location: " + database2.viewProduct(productTest2.getProductId()).getLocation());
        System.out.println("Product Level: " + database2.viewProduct(productTest2.getProductId()).getProductLevel());
        System.out.println("-------------Picking 12 products...");
        database2.pickProduct("Broom", 12);
        System.out.println("Product ID: " + database2.viewProduct(productTest2.getProductId()).getProductId());
        System.out.println("Product Location: " + database2.viewProduct(productTest2.getProductId()).getLocation());
        System.out.println("Product Level: " + database2.viewProduct(productTest2.getProductId()).getProductLevel());
        System.out.println("");

        System.out.println("-------------Test 5: Testing pickProduct with invalid product");
        System.out.println("Product ID: " + database.viewProduct(productTest1.getProductId()).getProductId());
        System.out.println("Product Location: " + database.viewProduct(productTest1.getProductId()).getLocation());
        System.out.println("Product Level: " + database.viewProduct(productTest1.getProductId()).getProductLevel());
        System.out.println("-------------Picking 12 products...");
        database.pickProduct("Plunger", 12);
        System.out.println("Product ID: " + database.viewProduct(productTest1.getProductId()).getProductId());
        System.out.println("Product Location: " + database.viewProduct(productTest1.getProductId()).getLocation());
        System.out.println("Product Level: " + database.viewProduct(productTest1.getProductId()).getProductLevel());
        System.out.println("");

        System.out.println("-------------Test 6: Testing RestockProduct with valid amount");
        System.out.println("Product ID: " + database.viewProduct(productTest2.getProductId()).getProductId());
        System.out.println("Product Location: " + database.viewProduct(productTest2.getProductId()).getLocation());
        System.out.println("Product Level: " + database.viewProduct(productTest2.getProductId()).getProductLevel());
        System.out.println("-------------Picking 12 products...");
        database.restockProduct("Broom", 100);
        System.out.println("Product ID: " + database.viewProduct(productTest2.getProductId()).getProductId());
        System.out.println("Product Location: " + database.viewProduct(productTest2.getProductId()).getLocation());
        System.out.println("Product Level: " + database.viewProduct(productTest2.getProductId()).getProductLevel());
        System.out.println("");

        System.out.println("-------------Test 7: Testing RestockProduct with invalid amount");
        System.out.println("Product ID: " + database.viewProduct(productTest2.getProductId()).getProductId());
        System.out.println("Product Location: " + database.viewProduct(productTest2.getProductId()).getLocation());
        System.out.println("Product Level: " + database.viewProduct(productTest2.getProductId()).getProductLevel());
        System.out.println("-------------Picking 12 products...");
        database.restockProduct("Broom", -100);
        System.out.println("Product ID: " + database.viewProduct(productTest2.getProductId()).getProductId());
        System.out.println("Product Location: " + database.viewProduct(productTest2.getProductId()).getLocation());
        System.out.println("Product Level: " + database.viewProduct(productTest2.getProductId()).getProductLevel());
        System.out.println("");

    } // END test

    //-------------------------------------------------------

} // END class
