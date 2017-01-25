import java.sql.SQLException;


public interface CustomerOrders {
	
	/**
	 * Gets the first unassigned order which has items in stock, and assigns this to the employee
	 * It removes the required stock level, and calls another method to find the shortest route between all items
	 * This route is returned as a string
	 * @param employeeID The ID to assign to the item
	 * @param items ItemStock should contain a field with a collection of Item objects
	 * @return String showing coordinate pairs which gives the shortest route to pickup all items and return to the start position
	 */
	public String checkoutOrder(int employeeID, ItemStock items);
	
	/**
	 * Gets a string to summarise all orders in the collection
	 * @return String (well formatted) of all orders in the collection 
	 */
	public String printAll();
	
	/**
	 * Gets contents of a particular order
	 * @param orderID the order ID to check
	 * @return String of the contents of this order 
	 */
	public String printOrder(int orderID);
	
	/**
	 * Gets a string to summarise all unassigned orders in the collection
	 * @return String (well formatted) of all unassigned orders in the collection 
	 */
	public String printUnassignedOrders();
	
	/**
	 * Gets a string to summarise all assigned orders in the collection
	 * @return String (well formatted) of all assigned orders in the collection 
	 */
	public String printAssignedOrders();
	
	/**
	 * Completes the order by changing the status of the order, but only when this order was checked out by the employee identified by employeeID
	 * @param orderID the orderID to checkout
	 * @param employeeID the employeeID of the person completing the order. This is to ensure there is no foul play by staff
	 * @return String containing a motivational message of congratulations to the values employee (useful in a GUI implementation)
	 */
	public String completeOrder(int orderID, int employeeID);
	
	/**
	 * Adds an order to the collection
	 * @param order An Order object
	 */
	public void addElement(Object order);

	/**
	 * Adds element to delivered orders
	 * @param order
	 */
	public void addDeliveredElement(Object order);
	
	/**
	 * Prints completed orders only
	 * @return
	 */
	public String printCompletedOrders();
	
	public void updateDeliveredInDatabase(int orderID) throws ClassNotFoundException, SQLException;
}
