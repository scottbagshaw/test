/**
 * 
 * @author Scott + Omar
 * @date 03/01/17
 */
public class Item {
	//ANOTHER COMMENT FOR GITHUB!!!!!!!
	private int itemID;
	private static int counter;
	private int locationID;
	private int numNonPorous;

	
	public int getItemID() {
		return itemID;
	}
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	public int getLocationID() {
		return locationID;
	}
	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}
	public int getNumNonPorous() {
		return numNonPorous;
	}
	public void setNumNonPorous(int numNonPorous) {
		this.numNonPorous = numNonPorous;
	}
	
	// automatically generate a user ID using the counter
	public Item(int locationID, int numNonPorous) {
		this(counter, locationID, numNonPorous);
	}
	
	public Item(int itemID, int locationID, int numNonPorous) {
		this.itemID = itemID;
		
		// set counter to userID+1 rather than counter+1 because we always want to continue counting up from the last ID which was entered
		counter = itemID + 1;
		this.locationID = locationID;
		this.numNonPorous = numNonPorous;
	}
	

	
}
