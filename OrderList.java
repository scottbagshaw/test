import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * @author Scott + Omar
 * @date 03/01/17
 */
public class OrderList extends CustomList implements CustomerOrders {
	private ArrayList<Order> order = new ArrayList<Order>();
	private ArrayList<Order> deliveredOrders = new ArrayList<Order>();

	
	public void addElement(Object order) {
		Order o = (Order) order;
		this.order.add(o);
	}
	
	public void addDeliveredElement(Object order) {
		Order o = (Order) order;
		deliveredOrders.add(o);
	}

	public void removeElement(int orderID) {
		Iterator<Order> itr = order.iterator();
		while (itr.hasNext()) {
			Order order = itr.next();
			if (order.getOrderID() == orderID) {
				itr.remove();
			}
		}
	}

	public String completeOrder(int orderID, int employeeID) {
		Order temp = getOrder(orderID);
		
		if(temp == null || temp.getAssignedEmployee() != employeeID) {
			return "Order ID not found";
		}
		
		this.deliveredOrders.add(temp);
		removeElement(orderID);
		return "Order completed!";
	}
	
	/**
	 * Updates the delivered status in the database to match whatever is in deliveredOrders
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void updateDeliveredInDatabase(int orderID) throws ClassNotFoundException, SQLException {
		WotsDB.completeOrder(orderID);
	}

	private Order getFirstUnassigned(ItemStock items) {
		for (Order o : order) {
			if (o.getAssignedEmployee() == -1 && checkInStock(o, items)) {
				return o;
			}
		}
		// no unassigned orders in this case
		return null;
	}

	private boolean checkInStock(Order o, ItemStock items) {
		// compares the amount of porousable and nonporousable items with the
		// amounts of porousable and non porousable items in stock.
		for (int i = 0; i < o.getItemIDs().length; i++) {
			Item item = items.getItem(o.getItemIDs()[i]); // returns item object
															// with ith item ID
			if (item instanceof PorousableItem) {
				PorousableItem pItem = (PorousableItem) item; // casts item as
																// porousable
																// item
				if (o.getQtyPRequired()[i] > pItem.getNumPorousItem()) {
					return false;
				}
			}
			if (o.getQtyRequired()[i] > item.getNumNonPorous()) {
				return false;
			}

		}
		return true;
	}

	private void removeStock(Order o, ItemStock items) {
		for (int i = 0; i < o.getItemIDs().length; i++) {
			Item item = items.getItem(o.getItemIDs()[i]);
			item.setNumNonPorous(item.getNumNonPorous() - o.getQtyRequired()[i]);
			if (item instanceof PorousableItem) {
				PorousableItem pItem = (PorousableItem) item;
				pItem.setNumPorousItem(pItem.getNumPorousItem()
						- o.getQtyPRequired()[i]);
			}
		}
	}

	public String checkoutOrder(int employeeID, ItemStock items) {
		Order o = getFirstUnassigned(items);
		String s = "";
		if (o != null) {
			o.setAssignedEmployee(employeeID);
			removeStock(o, items);
			s = s + "Please process the following order: ";
			s = s + printOrder(o.getOrderID());
			s = s + "The recommended order of selection is as follows: \n";
			s = s + mapRoute(o.getOrderID(), items);
			return s;

		} else {
			return "No unassigned orders with all items in stock!";
		}

	}

	/*
	 * Makes sure the same location ID is not in the list more than once, as this is not compatible with mapping the route in Location
	 */
	public int[] removeDuplicates(int[] locationIDs) {
		Set<Integer> lIds = new TreeSet<Integer>();
		for (int i = 0; i < locationIDs.length; i++) {
			lIds.add(locationIDs[i]);
		}
		int[] uniqueLocIDs = new int[lIds.size()];
		int j = 0;
		for(int i : lIds){
			uniqueLocIDs[j] = i;
			j++;						
		}
		return uniqueLocIDs;
	}

	private String mapRoute(int orderID, ItemStock stock) {
		Order o = getOrder(orderID);
		int[] locationIDs = new int[o.getItemIDs().length];
		for (int i = 0; i < o.getItemIDs().length; i++) {
			Item item = stock.getItem(o.getItemIDs()[i]);
			locationIDs[i] = item.getLocationID();
		}
		;
		int [] uniqueLocationIDs = removeDuplicates(locationIDs);
		Location warehouse = new Location();
		return warehouse.mapRoute(uniqueLocationIDs);
	}

	public ArrayList<Order> getOrder() {
		return order;
	}

	public ArrayList<Order> getDeliveredOrders() {
		return deliveredOrders;
	}

	public Order getOrder(int orderID) {
		for (Order o : order) {
			if (o.getOrderID() == orderID) {
				return o;
			}
		}
		return null;
	}

	public void setOrder(ArrayList<Order> order) {
		this.order = order;
	}

	// Prints order
	public String printOrder(int orderID) {
		for (Order o : order) {
			if (o.getOrderID() == orderID) {
				String s = nameOrder(o);
				// Adds info for each item in this order to string s
				for (int i = 0; i < o.getItemIDs().length; i++) {
					s = s + "Item ID: " + o.getItemIDs()[i]
							+ " Porous Quantity Required: "
							+ o.getQtyPRequired()[i]
							+ " Non Porous Quantity Required: "
							+ o.getQtyRequired()[i] + "\n";
				}
				return s;
			}
		}
		return "This is not a valid order ID.";

	}

	private String nameOrder(Order o) {
		// Returns a line of the order.
		return "Order ID: " + String.valueOf(o.getOrderID()) + " Employee ID: "
				+ String.valueOf(o.getAssignedEmployee()) + "\n";

	}

	public String printAll() {
		String s = "";
		for (Order o : order) {
			s = s + nameOrder(o);
		}
		return s;

	};

	public String printUnassignedOrders() {
		String s = "";
		for (Order o : order) {
			if (o.getAssignedEmployee() == -1) {
				s += nameOrder(o);
			}
		}
		return s;
	}

	public String printAssignedOrders() {
		String s = "";
		for (Order o : order) {
			if (o.getAssignedEmployee() != -1) {
				s += nameOrder(o);
			}
		}
		return s;
	}
	
	public String printCompletedOrders() {
		String s = "";
		for (Order o : deliveredOrders) {
			if (o.getAssignedEmployee() != -1) {
				s += nameOrder(o);
			}
		}
		return s;
	}

}
