import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class WotsGUI extends JFrame {
	// GUI components
	private JFrame mainFrame;
	private static JFrame loginFrame;
	private static JFrame extraFrame;
	private JTextField extraInputText;
	private static JLabel headerLabel = new JLabel();
	private JTextArea statusLabel;
	private JPanel controlPanel;
	private JTextField textField;
	private JScrollPane scroll;
	private JPanel controlPanel2;

	private static CustomerOrders customerOrders = new OrderList();
	private static ItemStock items = new ItemList();
	private static Location locations = new Location();
	private static PurchaseOrderInterface purchaseOrder = new PurchaseOrder();
	private static JTextField usernameField;
	private static JPasswordField passwordField;
	private static EmployeeList employees = new EmployeeList();
	private static int employeeID;
	private static JTextField registerNameField;
	private static JPasswordField registerPassField;
	private static JFrame registerWindow;
	private static JLabel registerMessage;

	public WotsGUI() {
		prepareGUI();
	}

	/**
	 * This is the main application window where the user can select options
	 */
	private void prepareGUI() {
		mainFrame = new JFrame("Warehouse Management System");
		mainFrame.setSize(600, 450);
		mainFrame.setLayout(new GridLayout(3, 1));
		String employeeName = employees.getEmployees(employeeID).getName();
		String formattedName = ""
				+ Character.toUpperCase(employeeName.charAt(0))
				+ employeeName.substring(1);
		headerLabel = new JLabel(
				"<html><span style=\" font-size:10px; \">Welcome to the Warehouse Management System, "
						+ formattedName
						+ ".</span> <br>"
						+ "<span style=\"color:red; font-size:11px; font-weight:900 \">Please hurry up and get to work!</span></html>",
				JLabel.CENTER);
		statusLabel = new JTextArea();
		statusLabel.setEditable(false);
		scroll = new JScrollPane(statusLabel);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textField = new JTextField();
		textField.setEnabled(false);
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(0, 4, 5, 5));
		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.setVisible(true);
		mainFrame.add(scroll);

	}

	/**
	 * Adds butttons for the main window
	 */
	private void showEvent() {
		JButton printAll = new JButton("Print all orders");
		JButton printSOrder = new JButton("Print a specific order");
		JButton printUOrder = new JButton("Print all unassigned orders");
		JButton printAOrder = new JButton("Print all assigned orders");
		JButton checkoutOrder = new JButton("Checkout an order");
		JButton completeOrder = new JButton("Complete order");
		JButton inputStock = new JButton("Input a purchase order");
		JButton printItems = new JButton("Print items");
		JButton printCompItems = new JButton("Print completed orders");

		printAll.setActionCommand("PAll");
		printSOrder.setActionCommand("PS");
		printUOrder.setActionCommand("PU");
		printAOrder.setActionCommand("PA");
		checkoutOrder.setActionCommand("checkout");
		completeOrder.setActionCommand("complete");
		inputStock.setActionCommand("stock");
		printItems.setActionCommand("Print items");
		printCompItems.setActionCommand("printCompItems");

		printAll.addActionListener(new BCL());
		printSOrder.addActionListener(new BCL());
		printUOrder.addActionListener(new BCL());
		printAOrder.addActionListener(new BCL());
		checkoutOrder.addActionListener(new BCL());
		completeOrder.addActionListener(new BCL());
		inputStock.addActionListener(new BCL());
		printItems.addActionListener(new BCL());
		printCompItems.addActionListener(new BCL());

		controlPanel.add(printAll);
		controlPanel.add(printSOrder);
		controlPanel.add(printUOrder);
		controlPanel.add(printAOrder);
		controlPanel.add(checkoutOrder);
		controlPanel.add(completeOrder);
		controlPanel.add(printCompItems);
		controlPanel.add(new JPanel());
		controlPanel.add(inputStock);
		controlPanel.add(printItems);
	}

	/**
	 * This class handles the operations which require an input before pressing
	 * submit (e.g. printing a specific order using an input order ID)
	 * 
	 * @author Scott + Omar
	 *
	 */
	private class submitted implements ActionListener {
		public void actionPerformed(ActionEvent af) {
			String command = af.getActionCommand();
			switch (command) {
			case "PS":
				try {
					int orderID = Integer.valueOf(extraInputText.getText());
					statusLabel.setText(customerOrders.printOrder(orderID));
				} catch (Exception e) {
					statusLabel.setText("ENTER A VALID ID FOOL!!!!");
				}
				break;
			case "checkout":
				try {
					statusLabel.setText(customerOrders.checkoutOrder(
							employeeID, items));
				} catch (Exception e) {
					statusLabel.setText("ENTER A VALID ID FOOL!!!!");
				}
				break;
			case "complete":
				try {
					int orderID = Integer.valueOf(extraInputText.getText());
					customerOrders.updateDeliveredInDatabase(orderID);
					statusLabel.setText(customerOrders.completeOrder(orderID,
							employeeID));
				} catch (Exception e) {
					statusLabel.setText("ENTER A VALID ID FOOL!!!!");
				}
				break;
			}
		}
	}

	/**
	 * Whenever an input is required, a new window is generated using this
	 * method
	 * 
	 * @param nameOfField
	 *            What should be printed so the user knows what to input?
	 * @param actionCommand
	 *            What is the action command of pressing submit for that input?
	 */
	private void makeNewWindow(String nameOfField, String actionCommand) {
		extraFrame = new JFrame();
		extraFrame.setLayout(new GridLayout(3, 1));
		extraFrame.setSize(400, 200);
		JLabel headerLabel = new JLabel("Please enter " + nameOfField);
		extraInputText = new JTextField();
		JButton submitButton = new JButton("Submit");

		submitButton.setActionCommand(actionCommand);
		submitButton.addActionListener(new submitted());

		extraFrame.add(headerLabel);
		extraFrame.add(extraInputText);
		extraFrame.add(submitButton);
		extraFrame.setVisible(true);
	}

	/**
	 * This class handles the logic for button pressing on the main window
	 * 
	 * @author Scott + Omar
	 *
	 */
	private class BCL implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			String command = ae.getActionCommand();
			int orderID = 0;
			switch (command) {
			case "PAll":
				statusLabel.setText(customerOrders.printAll());
				break;
			case "PS":
				makeNewWindow("order ID.", command);
				break;
			case "PU":
				statusLabel.setText(customerOrders.printUnassignedOrders());
				break;
			case "PA":
				statusLabel.setText(customerOrders.printAssignedOrders());
				break;
			case "checkout":
				statusLabel.setText(customerOrders.checkoutOrder(employeeID,
						items));
				try {
					items.updateDatabase();
				} catch(Exception exp) {
					statusLabel.setText("There was a problem checking out this order. Please try again.");
				}
				break;
			case "complete":
				makeNewWindow("order ID.", command);
				break;
			case "stock":
				JFileChooser fileChooser = new JFileChooser();
				File workingDirectory = new File(System.getProperty("user.dir"));
				fileChooser.setCurrentDirectory(workingDirectory);
				int result = fileChooser.showOpenDialog(mainFrame);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String s = selectedFile.getName();
					try {
						purchaseOrder.readTextFile(s, items);
						statusLabel.setText("Stock imported!");
						
						try {
							items.updateDatabase();
						} catch(Exception exp) {
							statusLabel.setText("There was a problem checking out this order. Please try again.");
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				break;
			case "Print items":
				statusLabel.setText(items.printAll());
				break;
			case "printCompItems":
				statusLabel.setText(customerOrders.printCompletedOrders());
			}
		}
	}

	/**
	 * Automatically populate the warehouse/employees using hard-coded values
	 */
	private static void popdata() {
		Item gnome = new Item(1, 5);
		Item redGnome = new Item(4, 10);
		PorousableItem greenGnome = new PorousableItem(6, 10, 30, 4);
		Item blueGnome = new Item(7, 2);
		items.addElement(gnome);
		items.addElement(redGnome);
		items.addElement(greenGnome);
		items.addElement(blueGnome);
		int[] oneIDs = { gnome.getItemID() };
		int[] oneQuant = { 1 };
		int[] onePQuant = { 0 };
		Order one = new Order(oneIDs, oneQuant, onePQuant);
		int[] twoIDs = { gnome.getItemID(), greenGnome.getItemID() };
		int[] twoQuant = { 2, 2 };
		int[] twoPQuant = { 0, 2 };
		Order two = new Order(twoIDs, twoQuant, twoPQuant);
		Order a = new Order(twoIDs, twoQuant, twoPQuant);
		Order b = new Order(twoIDs, twoQuant, twoPQuant);
		Order c = new Order(twoIDs, twoQuant, twoPQuant);
		Order d = new Order(twoIDs, twoQuant, twoPQuant);
		Order e = new Order(twoIDs, twoQuant, twoPQuant);
		Order f = new Order(twoIDs, twoQuant, twoPQuant);
		Order g = new Order(twoIDs, twoQuant, twoPQuant);
		Order h = new Order(twoIDs, twoQuant, twoPQuant);
		Order i = new Order(twoIDs, twoQuant, twoPQuant);
		Order j = new Order(twoIDs, twoQuant, twoPQuant);
		Order k = new Order(twoIDs, twoQuant, twoPQuant);
		Order l = new Order(twoIDs, twoQuant, twoPQuant);
		Order m = new Order(twoIDs, twoQuant, twoPQuant);

		customerOrders.addElement(one);
		customerOrders.addElement(two);
		customerOrders.addElement(a);
		customerOrders.addElement(b);
		customerOrders.addElement(c);
		customerOrders.addElement(d);
		customerOrders.addElement(e);
		customerOrders.addElement(f);
		customerOrders.addElement(g);
		customerOrders.addElement(h);
		customerOrders.addElement(i);
		customerOrders.addElement(j);
		customerOrders.addElement(k);
		customerOrders.addElement(l);
		customerOrders.addElement(m);

		Employee e1 = new Employee("Paul", "tome");
		Employee e2 = new Employee("Barry", "toyou");
		employees.add(e1);
		employees.add(e2);

	}

	/**
	 * Retrieves data from the database
	 */
	private static void popdataFromDB() {
		try {
			WotsDB.popFromDB(items);
			WotsDB.popFromDB(customerOrders);
			WotsDB.popFromDB(locations);
			WotsDB.popFromDB(employees);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Creates and opens a login window
	 */
	private static void loginWindow() {
		loginFrame = new JFrame("Please login");
		loginFrame.setLayout(new GridLayout(5, 1));
		loginFrame.setSize(400, 200);
		headerLabel.setText("Please enter username and password or register");
		usernameField = new JTextField();
		passwordField = new JPasswordField();
		JButton submitButton = new JButton("Login");
		JButton registerButton = new JButton("Register new account");

		submitButton.setActionCommand("login");
		registerButton.setActionCommand("register");

		submitButton.addActionListener(new LoginClass());
		registerButton.addActionListener(new LoginClass());
		// registerButton.addActionListener(new RegisterClass());

		loginFrame.add(headerLabel);
		loginFrame.add(usernameField);
		loginFrame.add(passwordField);
		loginFrame.add(submitButton);
		loginFrame.add(registerButton);
		loginFrame.setVisible(true);
	}

	/**
	 * This handles events for both logging in, and registering a new user It
	 * opens main application window when the login successful It opens
	 * registration window when user selects this
	 * 
	 * @author Scott + Omar
	 *
	 */
	private static class LoginClass implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			String command = ae.getActionCommand();

			switch (command) {
			case "register":
				makeRegistrationWindow();
				break;
			case "registerSubmitted":
				registerNewUser();
				break;
			case "login":
				attemptLogin();
				break;
			}
		}

		private void makeRegistrationWindow() {
			registerWindow = new JFrame("Registration Window");
			registerWindow.setSize(400, 220);
			registerWindow.setLayout(new GridLayout(4, 1));
			registerMessage = new JLabel();
			registerMessage
					.setText("Please enter your name, and make a secure password");
			registerNameField = new JTextField();
			registerPassField = new JPasswordField();

			JButton registerSubmit = new JButton("Submit");

			registerSubmit.setActionCommand("registerSubmitted");
			registerSubmit.addActionListener(new LoginClass());

			registerWindow.add(registerMessage);
			registerWindow.add(registerNameField);
			registerWindow.add(registerPassField);
			registerWindow.add(registerSubmit);
			registerWindow.setVisible(true);
		}

		private void registerNewUser() {
			try {
				String name = registerNameField.getText();
				String password = registerPassField.getText();

				if (name.length() == 0 || password.length() == 0) {
					registerMessage
							.setText("You must enter your name and password...");
					return;
				}

				Employee e = new Employee(name, password);

				// add Employee to the local employees object, and to the
				// database so it's available the next time the program runs
				try {
					WotsDB.addNewEmployeeToDB(e);
					employees.add(e);
					registerMessage
							.setText("<html>Registration successful.<br> Your ID is "
									+ e.getEmployeeID()
									+ ". <br>You MUST remember this. Now close the window and login.</html>");
				} catch (Exception expn) {
					registerMessage
							.setText("Sorry, the registration could not be complete. Please try again.");
				}

			} catch (Exception e) {
				registerMessage.setText("ENTER VALID INPUTS!!!");
			}
		}

		private void attemptLogin() {
			try {
				int eID = Integer.valueOf(usernameField.getText());
				String pw = passwordField.getText();
				if (employees.attemptLogin(eID, pw)) {
					employeeID = Integer.valueOf(usernameField.getText());
					loginFrame.setVisible(false);

					// show the GUI
					WotsGUI sD = new WotsGUI();
					sD.showEvent();
				} else {
					headerLabel.setText("WRONG USERNAME OR PASSWORD FOOL");
				}
			} catch (Exception e) {
				headerLabel
						.setText("You must enter your employee ID in the first box");
			}
		}

	}

	public static void main(String[] args) {
		// popdata();
		popdataFromDB();
		loginWindow();
	}

}
