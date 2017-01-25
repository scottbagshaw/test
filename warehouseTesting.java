

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * 
 * @author Scott + Omar
 * @date 03/01/17
 */
public class warehouseTesting {
	
	@Test
	public void printOrder() {
		int[] a = {1};
		Order o = new Order(a,a,a);
		OrderList ol = new OrderList();		
		ol.addElement(o);
		String str1 = ol.printAll();
		String str2 = ol.printOrder(o.getOrderID());
		String goID = String.valueOf(o.getOrderID()); 
		assertEquals("Order ID: " + goID + " Employee ID: -1\n", str1);
		assertEquals("Order ID: " + goID + " Employee ID: -1\n" + "Item ID: 1 Porous Quantity Required: 1 Non Porous Quantity Required: 1\n", str2);
	}
	
	@Test
	public void printOrders(){
		int[] a = {1};
		Order o = new Order(a,a,a);
		Order o1 = new Order(a,a,a);
		OrderList ol = new OrderList();
		ol.addElement(o);
		ol.addElement(o1);
		String str1 = ol.printAll();
		String goID = String.valueOf(o.getOrderID());
		String goneID = String.valueOf(o1.getOrderID()); 
		assertEquals("Order ID: " + goID + " Employee ID: -1\n"  + "Order ID: " + goneID + " Employee ID: -1\n", str1);
		
	}
	
	@Test
	public void testAssignedOrders(){
		int[] a = {1};
		Order o = new Order(a,a,a);
		o.setAssignedEmployee(1);
		Order o1 = new Order(a,a,a);
		OrderList ol = new OrderList();
		ol.addElement(o);
		ol.addElement(o1);
		String goID = String.valueOf(o.getOrderID());
		String goneID = String.valueOf(o1.getOrderID());
		String str1 = ol.printUnassignedOrders();
		String str2 = ol.printAssignedOrders();
		assertEquals("Order ID: " + goneID + " Employee ID: -1\n", str1);
		assertEquals("Order ID: " + goID + " Employee ID: 1\n", str2);
		
	}
	
	@Test
	public void itemsAddedToList() {
		Item it = new Item(1,1);
		ItemList it2 = new ItemList();
		it2.addElement(it);
		ArrayList<Item> arrList = it2.getItem();
		assertEquals(1, arrList.size());
	}
	
	@Test
	public void ordersAddedToList() {
		int[] a = {1};
		Order  o = new Order(a,a,a);
		OrderList ol = new OrderList();
		ol.addElement(o);
		ArrayList<Order> arrList = ol.getOrder();
		assertEquals(1, arrList.size());
	}
	
	@Test
	public void itemRemovedFromList() {
		Item it = new Item(1,3);
		ItemList it2 = new ItemList();
		it2.addElement(it);
		it2.removeElement(it.getItemID());
		ArrayList<Item> arrList = it2.getItem();
		assertEquals(0, arrList.size());
	}
	
	@Test
	public void orderRemovedFromList() {
		int[] a = {1};
		Order  o = new Order(a,a,a);
		OrderList ol = new OrderList();
		ol.addElement(o);
		ol.removeElement(o.getOrderID());
		ArrayList<Order> arrList = ol.getOrder();
		assertEquals(0, arrList.size());
	}

	
	@Test
	public void reamFromFile() {
		Item i1 = new Item(1, 0);
		PorousableItem pi1 = new PorousableItem(1, 0, 50, 0);
		ItemList items = new ItemList();
		items.addElement(i1);
		items.addElement(pi1);
		
		PurchaseOrder p = new PurchaseOrder();
		
		// override the item ID's to match those in the file test.txt
		i1.setItemID(1);
		pi1.setItemID(2);
		
		/*
		 * file test.txt contains:
		 * 1 2
		 * 2 2
		 */
		try {
			p.readTextFile("test.txt", items);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(2, i1.getNumNonPorous());
		assertEquals(1, pi1.getNumNonPorous());
		assertEquals(1, pi1.getNumPorousItem());
	}
	
	@Test
	public void inputStock() {
		Item it1 = new Item(2,1);
		PorousableItem pi1 = new PorousableItem(1,2,40,2);
		int itemID = it1.getItemID();
		int pItemID = pi1.getItemID();
		ItemList il = new ItemList();
		il.addElement(it1);
		il.addElement(pi1);
		
		PurchaseOrder p = new PurchaseOrder();
		p.inputStock(itemID, 1, false, il);
		p.inputStock(pItemID, 1, false, il);
		p.inputStock(pItemID, 1, true, il);
		
		assertEquals(2, it1.getNumNonPorous());
		assertEquals(3, pi1.getNumNonPorous());
		assertEquals(3, pi1.getNumPorousItem());
	}
	
	@Test
	public void notifyAccounts() {
		//fail("Not yet implemented");
	}
	
	@Test
	public void showRequiredPorous() {
		PorousableItem it = new PorousableItem(1,0,40,0);
		it.setPercentagePorousRequired(50);
		int itemID = it.getItemID();
		ItemList il = new ItemList();
		il.addElement(it);
		
		PurchaseOrder p = new PurchaseOrder();
		int porousRequired = p.calculatePorousRequired(itemID, 2, il);
		System.out.println(porousRequired);
		assertEquals(1, porousRequired);
		
		
		// 0 percentage required
		it.setPercentagePorousRequired(0);
		porousRequired = p.calculatePorousRequired(itemID, 2, il);
		assertEquals(0, porousRequired);
		
		
		// not a round number
		it.setPercentagePorousRequired(50);
		porousRequired = p.calculatePorousRequired(itemID, 3, il);
		assertEquals(2, porousRequired);
		
	}
	
	@Test
	public void showRequiredPorous2() {
		// some items already in stock
		PorousableItem it = new PorousableItem(1,1,50,5);
		int itemID = it.getItemID();
		ItemList il = new ItemList();
		il.addElement(it);
		
		PurchaseOrder p = new PurchaseOrder();
		int porousRequired = p.calculatePorousRequired(itemID, 2, il);
		System.out.println(porousRequired);
		assertEquals(0, porousRequired);
	}

	@Test
	public void checkoutOrder() {
		Item item1 = new Item(2,3);
		
		ItemStock items = new ItemList();
		items.addElement(item1);
		
		Employee e = new Employee("Bill", "1234");
		PorousableItem it = new PorousableItem(1,3,1,3);
		items.addElement(it);
		int[] a = {item1.getItemID(),it.getItemID()};
		int[] b = {1,3};
		Order o = new Order(a,b,b);
		//int[] itemID = {1};
		//int[] itemID = {it.getItemID()};
		//int[] qty = {2};
		//int orderID = o.getOrderID();
		int employeeID = e.getEmployeeID();
				
		OrderList ol = new OrderList();
		ol.addElement(o);
		
		ol.checkoutOrder(employeeID, items);
		assertEquals(employeeID, o.getAssignedEmployee());
		assertEquals(2, item1.getNumNonPorous());
		assertEquals(0, it.getNumNonPorous());
		assertEquals(0, it.getNumPorousItem());
	}
	
	@Test
	public void checkoutOrderOutOfStock() {
		Item item1 = new Item(2,3);
		ItemList items = new ItemList();
		items.addElement(item1);
		
		Employee e = new Employee("Bill", "1234");
		
		// testing something
		char[] pwChar = {'1','2','3','4'};
		String str = "1234";
		
		System.out.println(pwChar.hashCode());
		System.out.println(str.hashCode());
		
		//
		
		PorousableItem it = new PorousableItem(1,3,1,3);
		items.addElement(it);
		int[] a = {item1.getItemID(),it.getItemID()};
		int[] b = {1,4};
		Order o = new Order(a,b,b);
		//int[] itemID = {1};
		//int[] itemID = {it.getItemID()};
		//int[] qty = {2};
		//int orderID = o.getOrderID();
		int employeeID = e.getEmployeeID();
				
		OrderList ol = new OrderList();
		ol.addElement(o);
		
		ol.checkoutOrder(employeeID, items);
		
		assertEquals(-1, o.getAssignedEmployee());
		assertEquals(3, item1.getNumNonPorous());
		assertEquals(3, it.getNumNonPorous());
		assertEquals(3, it.getNumPorousItem());
	}
	
		
	@Test
	public void mapRoute() {
		Location warehouse = new Location();
		int[] locationID = {1, 2, 3, 4, 5, 6, 7, 8, 9 };
		warehouse.mapRoute(locationID);
	}
	
	@Test
	public void removeStock() {
		Item it1 = new Item(2,1);
		PorousableItem pi1 = new PorousableItem(1,2,50,3);
		int itemID = it1.getItemID();
		int pItemID = pi1.getItemID();
		
		ItemList il = new ItemList();
		il.addElement(it1);
		il.addElement(pi1);
		
		il.removeStock(itemID, 1, false);
		il.removeStock(pItemID, 1, false);
		il.removeStock(pItemID, 1, true);
		
		assertEquals(0, it1.getNumNonPorous());
		assertEquals(1, pi1.getNumNonPorous());
		assertEquals(2, pi1.getNumPorousItem());
	}
	
	@Test
	public void completeOrder(){
		Employee e = new Employee("bill", "1234");
				
		int[] a = {1};
		Order o = new Order(a,a,a);
		OrderList order = new OrderList();
		order.addElement(o);
		order.completeOrder(o.getOrderID(), e.getEmployeeID());
		int ordersize = order.getOrder().size();
		int dordersize = order.getDeliveredOrders().size();
		assertEquals(0, ordersize);
		assertEquals(1, dordersize);
	}
	
	@Test
	public void addStock() {
		Item it1 = new Item(1,1);
		PorousableItem pi1 = new PorousableItem(1,1,1,1);
		int itemID = it1.getItemID();
		int pItemID = pi1.getItemID();
		
		ItemList il = new ItemList();
		il.addElement(it1);
		il.addElement(pi1);
		
		il.addStock(itemID, 1, false);
		il.addStock(pItemID, 1, false);
		il.addStock(pItemID, 1, true);
		
		assertEquals(2, it1.getNumNonPorous());
		assertEquals(2, pi1.getNumNonPorous());
		assertEquals(2, pi1.getNumPorousItem());
	}

	@Test
	public void counter(){
		// Use automatic ID generation
		int[] a = {1};
		Order o = new Order(a,a,a);
		Order o1 = new Order(a,a,a);
		
		Employee e = new Employee("Barry", "1234");
		Employee e1 = new Employee("Bill", "1234");
		
		Item i1 = new Item(1, 1);
		Item i2 = new Item(1, 1);
		
		PorousableItem pi1 = new PorousableItem(1,1,1,1);
		PorousableItem pi2 = new PorousableItem(1,1,1,1);
		
		assertEquals(o.getOrderID() + 1, o1.getOrderID());
		assertEquals(e.getEmployeeID() + 1, e1.getEmployeeID());
		assertEquals(i1.getItemID()+1, i2.getItemID());
		assertEquals(pi1.getItemID()+1, pi2.getItemID());
		
		// Check automatic ID generation works after an overriden one
		Order o2 = new Order(o1.getOrderID()+50,a,a,a);
		Order o3 = new Order(a,a,a);
		assertEquals(o2.getOrderID()+1, o3.getOrderID());
		
		Employee e2 = new Employee(e1.getEmployeeID() + 1, "a", "1");
		Employee e3 = new Employee("a","1");
		assertEquals(e2.getEmployeeID()+1, e3.getEmployeeID());

		Item i3 = new Item(i2.getItemID()+50,1,1);
		Item i4 = new Item(1,1);
		assertEquals(i3.getItemID()+1, i4.getItemID());
		
		PorousableItem pi3 = new PorousableItem(pi2.getItemID()+50,1,1,1,1);
		PorousableItem pi4 = new PorousableItem(1, 1, 1, 1);
		assertEquals(pi3.getItemID()+1, pi4.getItemID());
		
	}
	
}
