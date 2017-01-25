import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * 
 * @author Scott + Omar
 * @date 03/01/17
 */
public class PurchaseOrder implements PurchaseOrderInterface {
	private String purchaseOrderID;
	
	/*
	 * File format: 
	 * itemID1 qty1
	 * itemID2 qty2
	 */
	public void readTextFile(String filename, ItemStock items) throws IOException {
		 File input = new File(filename);
		 Scanner s = new Scanner(input);
		 
		 while(s.hasNextLine()) {
			int itemID = Integer.parseInt(s.next());
			int qty = Integer.parseInt(s.next());
			
			Item it = items.getItem(itemID);
			
			if (it instanceof PorousableItem) {
				// find how much of the items to porous
				int porousRequired = calculatePorousRequired(itemID, qty, items);
				int nonPorousRequired = qty - porousRequired;
				inputStock(itemID, porousRequired, true, items);
				inputStock(itemID, nonPorousRequired, false, items);
			} else {
				inputStock(itemID, qty, false, items); // no items need porousing
			}
		 }
	}
	
	public void inputStock(int itemID, int qty, boolean isPoroused, ItemStock items){
		items.addStock(itemID, qty, isPoroused);
	}
	
	public void notifyAccounts(){
		
	}
	
	
	// Finds how much of the item delivery to porous to achieve the required percentage 
	public int calculatePorousRequired(int itemID, int qtyArrived, ItemStock items) {
		Item it = items.getItem(itemID);
		PorousableItem pItem = (PorousableItem) it;
		int totalStock = qtyArrived + it.getNumNonPorous() + pItem.getNumPorousItem();
		
		int desiredPorous = (int) Math.round((pItem.getPercentagePorousRequired())*(1.0/100)*totalStock);
		int porousRequired = desiredPorous - pItem.getNumPorousItem();
		
		if(porousRequired > 0) {
			return porousRequired;
		} else {
			return 0;
		}
		
	}
	

	
}
