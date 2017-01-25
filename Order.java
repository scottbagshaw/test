/**
 * 
 * @author Scott + Omar
 * @date 03/01/17
 */
public class Order implements AccountsCommunication {
	private int orderID;
	private static int counter;
	private int assignedEmployee = -1;
	private int[] itemIDs;
	private int[] qtyRequired;
	private int[] qtyPRequired;


	public void notifyAccounts() {
		// send email to accounts
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
	
	// automatically assigns orderID based on a counter
	public Order(int[] itemIDs, int[] qtyRequired, int[] qtyPRequired) {
		this(counter, itemIDs, qtyRequired, qtyPRequired);
	}
	
	// allows a customer orderID to be inserted
	public Order(int orderID, int[] itemIDs, int[] qtyRequired, int[] qtyPRequired) {
		this.orderID = orderID;
		counter = orderID + 1;
		this.itemIDs = itemIDs;
		this.qtyRequired = qtyRequired;
		this.qtyPRequired = qtyPRequired;

	}

	public int getAssignedEmployee() {
		return assignedEmployee;
	}

	public void setAssignedEmployee(int assignedEmployee) {
		this.assignedEmployee = assignedEmployee;
	}

	public int[] getItemIDs() {
		return itemIDs;
	}

	public void setItemIDs(int[] itemIDs) {
		this.itemIDs = itemIDs;
	}

	public int[] getQtyRequired() {
		return qtyRequired;
	}

	public void setQtyRequired(int[] qtyRequired) {
		this.qtyRequired = qtyRequired;
	}

	public int[] getQtyPRequired() {
		return qtyPRequired;
	}

	public void setQtyPRequired(int[] qtyPRequired) {
		this.qtyPRequired = qtyPRequired;
	}

}
