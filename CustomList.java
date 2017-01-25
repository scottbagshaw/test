import java.util.ArrayList;

/**
 * 
 * @author Scott + Omar
 * @date 03/01/17
 */
public abstract class CustomList {
	public int countElements(ArrayList<Object> List){
		return 0;
	}
	
	/**
	 * Adds element to the collection
	 * @param element
	 */
	public abstract void addElement(Object element);
	
	/**
	 * Removes element from collection using ID
	 * @param ID
	 */
	public abstract void removeElement(int ID);
	
	/**
	 * Returns a nicely formatted string of all elements in a collection
	 * @return Nicely formatted string of all elements in a collection
	 */
	public abstract String printAll();
	
}
