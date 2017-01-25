/**
 * 
 * @author Scott + Omar
 * @date 03/01/17
 */
public class PorousableItem extends Item {
	private int percentagePorousRequired;
	private int numPorousItem;

	public int getPercentagePorousRequired() {
		return percentagePorousRequired;
	}

	public void setPercentagePorousRequired(int percentagePorousRequired) {
		this.percentagePorousRequired = percentagePorousRequired;
	}

	public int getNumPorousItem() {
		return numPorousItem;
	}

	public void setNumPorousItem(int numPorousItem) {
		this.numPorousItem = numPorousItem;
	}

	// itemID is automatically generated in the super constructor
	public PorousableItem(int locationID, int numNonPorous, int percentagePorousRequired, int numPorousItem) {
		super(locationID, numNonPorous);
		this.percentagePorousRequired = percentagePorousRequired;
		this.numPorousItem = numPorousItem;
	}
	
	// An itemID is specified (e.g. from a database)
	public PorousableItem(int itemID, int locationID, int numNonPorous, int percentagePorousRequired, int numPorousItem) {
		super(itemID, locationID, numNonPorous);
		this.percentagePorousRequired = percentagePorousRequired;
		this.numPorousItem = numPorousItem;
		
	}

	
	
	
	
}