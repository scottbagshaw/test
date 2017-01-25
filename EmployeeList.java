import java.util.ArrayList;


public class EmployeeList {
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	
	/**
	 * Adds an Employee to the arraylist of employees, but only if the ID of the new employee does not already exist 
	 * @param e
	 */
	public void add(Employee e) {
		//employees.add(e);
		int ID = e.getEmployeeID();
		
		Employee foundEmployee = getEmployees(ID);
		
		if (foundEmployee == null) {
			employees.add(e);
		} else {
			System.out.println("Employee ID " + ID + " was not added. This is a duplicate value.");
		}
		
	}
	
	
	public ArrayList<Employee> getEmployees() {
		return employees;
	}
	
	public Employee getEmployees(int employeeID) {
		for (Employee e : employees) {
			if (e.getEmployeeID() == employeeID) {
				return e;
			}
		}
		return null;
	}
	
	/**
	 * Checks the password string against the employee identified by employeeID
	 * @param employeeID the ID of the employee to login
	 * @param password a string of the password to check
	 * @return true if the password input matches the employee's password
	 */
	public boolean attemptLogin(int employeeID, String password) {
		int hashedPassword = password.hashCode();
		Employee e = getEmployees(employeeID);
		
		if(e.getPassword() == hashedPassword) {
			return true;
		}
		return false;
	}
	
}
