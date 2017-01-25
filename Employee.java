public class Employee {
	private int employeeID;
	private static int counter;
	private String name;
	private int password; //hashed password using .hashCode() 

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// This constructor automatically generates an employeeID based on the counter (which is incremented in the below constructor)
	public Employee(String name, String password) {
		this(counter, name, password);
	}
	

	public Employee(int employeeID, String name, String password) {
		this.employeeID = employeeID;

		// set counter to employeeID+1 rather than counter+1 because we always want to continue counting up from the last ID which was entered
		counter = employeeID + 1; 
		this.name = name;
		this.password = password.hashCode();
	}
	
	// This constructor accepts a password which is already hashed (hence int)
	// this can only happen when retrieving from a database, therefore we will always have an employeeID in this case
	public Employee(int employeeID, String name, int hashedPassword) {
		this.employeeID = employeeID;
		counter = employeeID + 1;
		this.name = name;
		this.password = hashedPassword;
	}
	
	public int getPassword() {
		return password; // returns the hashed password
	}

}
