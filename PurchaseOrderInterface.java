import java.io.IOException;


public interface PurchaseOrderInterface {
	
	public void readTextFile(String filename, ItemStock items) throws IOException;
	
	public void notifyAccounts();
	
}
