import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class WotsDB {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/wots";
	private static final String USER = "root";
	private static final String PASS = "123456";
	private static java.sql.Connection conn = null;
	private static java.sql.Statement stmt = null;
	
	// in popFromDB(CustomerOrders) multiple statements are needed at the same time, so we need another stmt
	private static java.sql.Statement stmt2 = null;

	private static void openConnection() throws ClassNotFoundException,
			SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = conn.createStatement();
	}

	/**
	 * Gets data from the item table, and adds this to ItemStock items
	 * 
	 * @param items
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void popFromDB(ItemStock items) throws SQLException,
			ClassNotFoundException {
		openConnection();
		stmt = conn.createStatement();
		String getItem = "SELECT * FROM item";

		ResultSet rs = stmt.executeQuery(getItem);

		try {
			while (rs.next()) {
				int itemID = rs.getInt("ItemID");
				int locationID = rs.getInt("locationID");
				int numNonPorous = rs.getInt("NumberNonPorous");
				int numPorous = rs.getInt("NumberPorous");
				int percPorousReq = rs.getInt("PercentagePorousReq");
				boolean porousable = rs.getBoolean("Porousable");

				Item it = null;
				if (porousable) {
					it = new PorousableItem(itemID, locationID, numNonPorous,
							percPorousReq, numPorous);
				} else {
					it = new Item(itemID, locationID, numNonPorous);
				}
				items.addElement(it); // add the item to the itemList
			}
		} finally {
			rs.close(); // always close the connection
		}
	}

	/**
	 * Gets data from the orderline table, and inserts into CustomerOrders
	 * orders
	 * 
	 * @param orders
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void popFromDB(CustomerOrders orders) throws SQLException,
			ClassNotFoundException {
		openConnection();
		stmt = conn.createStatement();

		/*
		 * Orderline contains a row with one orderId and one itemID we need to
		 * find how many items each order contains they are ordered by orderID
		 * because this implies ordering by oldest first (so this is selected
		 * first) The second stmt is used in this case
		 */
		String getNumItems = "SELECT ol.orders_orderID orderID, count(ol.item_itemID) numItems, o.delivered delivered, o.employeeID eID FROM orderline ol JOIN orders o on ol.orders_orderID=o.orderID GROUP BY orderID ORDER BY orderID;";
		ResultSet rs = stmt.executeQuery(getNumItems);
		stmt2 = conn.createStatement();

		// A nested loop... once to make the arrays containing item IDs, and one
		// to populate them

		try {
			while (rs.next()) {
				int[] itemIDs = new int[rs.getInt("numItems")];
				int[] qtyReq = new int[rs.getInt("numItems")];
				int[] qtyPorousReq = new int[rs.getInt("numItems")];

				String getItemDetails = "SELECT * FROM orderline WHERE orders_orderID = "
						+ rs.getInt("orderID") + ";";
				ResultSet rs2 = stmt2.executeQuery(getItemDetails);

				try {
					for (int i = 0; i < itemIDs.length; i++) {
						rs2.next();
						itemIDs[i] = rs2.getInt("Item_ItemID");
						qtyReq[i] = rs2.getInt("QuantityRequired");
						qtyPorousReq[i] = rs2.getInt("QuantityPRequired");
					}
					int employeeID = rs.getInt("eID");
					boolean delivered = rs.getBoolean("delivered");
					Order o = new Order(itemIDs, qtyReq, qtyPorousReq);
					o.setAssignedEmployee(employeeID);
					
					
					if(delivered) {
						orders.addDeliveredElement(o);
					} else {
						orders.addElement(o);
					}
					
				} finally {
					rs2.close(); // always close the connection
				}
			}
		} finally {
			rs.close(); // always close the connection
		}
	}

	/**
	 * Gets data from the location table, and inserts into Location loc
	 * 
	 * @param items
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void popFromDB(Location locationObj) throws SQLException,
			ClassNotFoundException {
		openConnection();
		stmt = conn.createStatement();

		// get number of location IDs in the table
		String getNumLocations = "SELECT COUNT(locationID) numLocs FROM Location";
		ResultSet rs = stmt.executeQuery(getNumLocations);
		rs.next();
		int numLocs = rs.getInt("numLocs");
		
		// The location ID's must start from 0, and the coordinate at position 0 must be {0,0}
		int[] locationIDs = new int[numLocs + 1];
		int[][] coords = new int[numLocs + 1][2];
		locationIDs[0] = 0;
		coords[0][0] = 0;
		coords[0][1] = 0;

		rs.close();

		// get the actual data
		String getLocations = "SELECT * FROM Location";
		rs = stmt.executeQuery(getLocations);

		try {
			for (int i = 1; i < numLocs + 1; i++) {
				rs.next();
				locationIDs[i] = rs.getInt("locationID");
				coords[i][0] = rs.getInt("rowNumber");
				coords[i][1] = rs.getInt("columnNumber");
			}
			locationObj.setLocationID(locationIDs);
			locationObj.setCoords(coords);
		} finally {
			rs.close(); // always close down the connection
			closeConnection();
		}

	}

	/**
	 * Populates the EmployeeList employees using data from the employees table
	 * in the database
	 * 
	 * @param employees
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void popFromDB(EmployeeList employees) throws SQLException,
			ClassNotFoundException {
		openConnection();
		stmt = conn.createStatement();

		// get number of location IDs in the table
		String getEmployees = "SELECT * FROM employees";
		ResultSet rs = stmt.executeQuery(getEmployees);

		try {
			String name;
			int hashedPassword;
			int employeeID;
			Employee e;

			while (rs.next()) {
				employeeID = rs.getInt("EmployeeListID");
				name = rs.getString("name");
				hashedPassword = rs.getInt("password");
				e = new Employee(employeeID, name, hashedPassword);
				employees.add(e);
			}
		} finally {
			rs.close(); // always close down the connection
			closeConnection();
		}

	}

	/**
	 * Adds the new employee to the employees table in the database
	 * 
	 * @param e
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void addNewEmployeeToDB(Employee e) throws SQLException,
			ClassNotFoundException {
		openConnection();
		stmt = conn.createStatement();

		try {
			String insertEmployee = "INSERT INTO employees VALUES("
					+ e.getEmployeeID() + ", '" + e.getName() + "',"
					+ e.getPassword() + ");";
			stmt.executeUpdate(insertEmployee);
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	/**
	 * Iterates through the Item collection, and updates the table to match the quantities in the Item objects
	 */
	public static void updateStockDB(ArrayList<Item> items)
			throws ClassNotFoundException, SQLException {
		// The method loops through all of items, and updates the values in the
		// table with the values in items
		openConnection();
		stmt = conn.createStatement();
		String str;
		try {
			for (Item i : items) {
				if (i instanceof PorousableItem) {
					PorousableItem p = (PorousableItem) i;
					str = "UPDATE item SET NumberNonPorous="
							+ p.getNumNonPorous() + ", NumberPorous="
							+ p.getNumPorousItem() + " WHERE itemID="
							+ i.getItemID() + ";";
				} else {
					str = "UPDATE item SET NumberNonPorous="
							+ i.getNumNonPorous() + " WHERE itemID="
							+ i.getItemID() + ";";
				}
				stmt.executeUpdate(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}

	}
	
	public static void completeOrder(int orderID) throws ClassNotFoundException, SQLException {
		openConnection();
		stmt = conn.createStatement();
		
		String str = "UPDATE orders SET delivered=" + 1 + " WHERE orderID = " + orderID + ";";
		
		try {
			stmt.executeUpdate(str);
		} finally {
			closeConnection();
		}
	}

	private static void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}