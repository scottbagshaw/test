import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * @author Scott + Omar
 * @date 03/01/17
 *
 */
public class ItemList extends CustomList implements ItemStock {
	private ArrayList<Item> item = new ArrayList<Item>();
	
	
	public void addElement(Object item) {
		Item newItem = (Item) item;
		this.item.add(newItem);
	}

	public void removeElement(int itemID) {
		Iterator<Item> itr = item.iterator();
		while (itr.hasNext()) {
			Item item = itr.next();
			if (item.getItemID() == itemID) {
				itr.remove();
			}
		}
	}
	
	/*
	 * This method just passes the entire collection to a database class, which then iterators through the whole list and updates each item accordingly
	 * It would be more efficient to only update the database when stock is added/removed rather than iterating through the whole list
	 */
	public void updateDatabase() throws ClassNotFoundException, SQLException {
		WotsDB.updateStockDB(item);
	}


	public void removeStock(int itemID, int amount, boolean porous) {
		Item item = getItem(itemID);
		if (item instanceof PorousableItem) {
			PorousableItem pItem = (PorousableItem) item;
			if (porous) {
				if (amount <= pItem.getNumPorousItem()) {
					pItem.setNumPorousItem(pItem.getNumPorousItem() - amount);
				} else {
					System.out.println("Not enough items in stock to do this.");
				}

			} else {
				if (amount <= pItem.getNumNonPorous()) {
					pItem.setNumNonPorous(pItem.getNumNonPorous() - amount);
				} else {
					System.out.println("Not enough items in stock to do this.");
				}
			}

		} else {
			if (porous) {
				System.out.println("Error, This item cannot be poroused");
			} else {
				if (amount <= item.getNumNonPorous()) {
					item.setNumNonPorous(item.getNumNonPorous() - amount);
				} else {
					System.out.println("Not enough items in stock to do this.");
				}
			}

		}
	}

	public void addStock(int itemID, int amount, boolean porous) {
		Item item = getItem(itemID);
		
		if (item instanceof PorousableItem) {
			PorousableItem pItem = (PorousableItem) item;
			if (porous) {
				pItem.setNumPorousItem(pItem.getNumPorousItem() + amount);
			} else {
				pItem.setNumNonPorous(pItem.getNumNonPorous() + amount);
			}
		} else {
			if (porous) {
				System.out.println("Error, This item cannot be poroused");
			} else {
				item.setNumNonPorous(item.getNumNonPorous() + amount);
			}
		}
	}


	public ArrayList<Item> getItem() {
		return item;
	}

	public void setItem(ArrayList<Item> item) {
		this.item = item;
	}

	public Item getItem(int itemID) {
		for (Item i : item) {
			if (i.getItemID() == itemID) {
				return i;
			}
		}
		return null;
	}
	
	public String printAll(){
		String s = "Item ID: LocationID: #Non Porous: #Porousable: % Required Porous \n";
		for (Item i : item){
			if(i instanceof PorousableItem){
				PorousableItem p = (PorousableItem) i;
				s = s + "      "+ String.valueOf(p.getItemID()) + "               " + String.valueOf(p.getLocationID()) + "                    " + String.valueOf(p.getNumNonPorous()) + "                        " + String.valueOf(p.getNumPorousItem()) + "                       " + String.valueOf(p.getPercentagePorousRequired()) + "\n";
			} else {
				s = s + "      "+ String.valueOf(i.getItemID()) + "               " + String.valueOf(i.getLocationID()) + "                    " +  String.valueOf(i.getNumNonPorous()) + "\n";}
			}
			return s;	
	}

}
