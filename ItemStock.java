import java.sql.SQLException;
import java.util.ArrayList;


public interface ItemStock {
	
	// NB addStock and removeStock must be two separate methods since they have different checks
	// it is not just a case of changing a +ve sign to a -ve sign
	/**
	 * Adds quantity of stock to existing items in the warehouse
	 * @param itemID The ID of the item to add items for
	 * @param amount The number of items to add
	 * @param porous True if these items are poroused, otherwise false
	 */
	public void addStock(int itemID, int amount, boolean porous);
	
	/**
	 * Removes quantity of stock from existing items in the warehouse, but only if this does not cause the qty to fall become negative
	 * @param itemID The ID of the item to remove items for
	 * @param amount The number of items to remove
	 * @param porous True if these items are poroused, otherwise false
	 */
	public void removeStock(int itemID, int amount, boolean porous);

	/**
	 * Returns an Item object which matches the ID passed
	 * @param i the item ID of the object to find
	 * @return the Item found, or null if no item was found with that ID
	 */
	public Item getItem(int i);
	
	/**
	 * Gets an arraylist of items 
	 * @return ArrayList<Item>
	 */
	public ArrayList<Item> getItem();

	/**
	 * Adds a brand new item to the collection
	 * @param item The item to add
	 */
	public void addElement(Object item);
	
	/**
	 * Gives a string of all items in the warehouse, which can then be printed for example
	 * @return String of all items in the warehouse
	 */
	public String printAll();
	
	/**
	 * Updates the database using the current quantity values in the collection
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void updateDatabase() throws ClassNotFoundException, SQLException;
	
}
