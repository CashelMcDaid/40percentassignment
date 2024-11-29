//**
// * Class: BSc. in Computing
// * Instructor: Maria Boyle
// * Description: serializes an Employee
// * Date: 21/11/2024
// * @author Cashel Mc Daid
// * @version 1.0
//**/

    package ie.atu.serialize;

    import java.io.*;
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Comparator;
    import javax.swing.JOptionPane;
    import ie.atu.hotel.Employee;

    public class EmployeeSerializer {
    private ArrayList<Employee> employees;
    private final String FILENAME = "employees.bin";
    private File employeesFile;
    private ArrayList<Integer> deletedNumbers;

    // Default Constructor
    	// Question (ii)
    public EmployeeSerializer() {//startt
        employees = new ArrayList<>();
        deletedNumbers = new ArrayList<>();
        employeesFile = new File(FILENAME);
        try {
            if (employeesFile.createNewFile()) {
                System.out.println("Created file: " + employeesFile.getName());
            } else {
                System.out.println("This file exists already.");
            }
        } catch (IOException e) {
            System.err.println("Issue / Error creating file: " + e.getMessage());
        }
        deserializeEmployees(); 
    }//end
    
    /////////////////////////////////////////////////////////////
    // Method Name : add()                                      //
    // Return Type : void                                       //
    // Parameters : None                                        //
    // Purpose : Reads one Employee record from the user        //
    //           and adds it to the ArrayList called employees  //
    /////////////////////////////////////////////////////////////

    // Question (iii)
    
    public void add() {//start
        int employeeNumber =getAvailableNumber();
        Employee employee= new Employee();
        employee.setNumber(employeeNumber); 

        if (!employee.read())  {
            JOptionPane.showMessageDialog(null, "Adding employee was cancelled: ", "INFO", JOptionPane.INFORMATION_MESSAGE) ;
            reuseNumber(employeeNumber);
            return;
        } else  {
            employees.add(employee);
            updateNextNumber();
            //Question (Iv)
            serializeEmployees(); 
        }
    }//end
    
    //Question (VIII)

    // My method to get the smallest available number
    private int getAvailableNumber() {//start
        int nextNumber= Employee.getNextNumber(); 

        // Looks for the smallest available number 
        for (int i = 10000; i < nextNumber; i++) {
            if (!isNumberInUse(i) && !deletedNumbers.contains(i)) {
                return i;
            }
        }

        // If no smaller number are there it uses the smallest number from deletedNumbers
        if (!deletedNumbers.isEmpty()) {
            int smallestDeletedNumber =Collections.min(deletedNumbers); // Find the smallest number in deletedNumbers
            deletedNumbers.remove(Integer.valueOf(smallestDeletedNumber)); // Remove it from deletedNumbers
            return smallestDeletedNumber;
        }
        
        return nextNumber;
    }//end

    private boolean isNumberInUse(int number) {//start
        return employees.stream().anyMatch(e -> e.getNumber() ==number);
    }//end

    private void reuseNumber(int number) {//start
        if (!deletedNumbers.contains(number)) {
            deletedNumbers.add(number);
        }
    }//end

    private void updateNextNumber() {
        if (!employees.isEmpty()) {
            Employee lastEmp = employees.get(employees.size() - 1);
            int lastNumber = lastEmp.getNumber();
            Employee.setNextNumber(lastNumber + 1);
        } else {
            Employee.setNextNumber(10000);
        }
    }

     ///////////////////////////////////////////////////////
     // Method Name : list()                              //
     // Return Type : void                                //
     // Parameters : None                                 //
     // Purpose : Lists all Employee records in employees //
     ///////////////////////////////////////////////////////
    
    public void list() {
        employees.sort(Comparator.comparingInt(Employee::getNumber));
        StringBuilder employeeList= new StringBuilder();

        if (!employees.isEmpty()) {
            for (Employee tmpEmployee : employees) {
                employeeList.append(tmpEmployee);
                employeeList.append("\n");
            }
            JOptionPane.showMessageDialog(null, employeeList.toString(), "EMPLOYEE LIST", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No Employees to the list ", "EMPLOYEE LIST", JOptionPane.INFORMATION_MESSAGE);
        }
    }

     	////////////////////////////////////////////////////////////////
    	// Method Name : view()                                       //
    	// Return Type : Employee                                     //
    	// Parameters : None                                          //
    	// Purpose : Displays the required Employee record on screen  //
    	//           And returns it, or null if not found             //
    	////////////////////////////////////////////////////////////////
    
    		// Question (V)
    public Employee view() {//start
        String input= JOptionPane.showInputDialog(null, "Enter Employee Number: ", "View Employee", JOptionPane.QUESTION_MESSAGE);

        if (input== null)   {
            JOptionPane.showMessageDialog(null, "Operation cancelled ", "INFO", JOptionPane.INFORMATION_MESSAGE) ;
            return null;
        }

        try {
            int employeeNumber = Integer.valueOf(input);

            for (Employee employee : employees) {
                if (employee.getNumber() == employeeNumber) {
                    JOptionPane.showMessageDialog(null, employee, "Employee Details", JOptionPane.INFORMATION_MESSAGE);
                    return employee;
                }
            }

            JOptionPane.showMessageDialog(null, "Employee not located: ", "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid/Incorrect Employee Number entered. Please enter a valid number: ", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e)  {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null ;
    }//end

    		////////////////////////////////////////////////////////////////
    		// Method Name : delete()                                     //
    		// Return Type : void                                         //
    		// Parameters : None                                          //
    		// Purpose : Deletes the required Employee record from        //
    		//           employees                                        //
    		//////////////////////////////////////////////////////////////
    
    //Question (VII)
    
    public void delete() {//start
        try {
            String input = JOptionPane.showInputDialog(null, "Enter an Employee Number to delete :", "Delete Employee", JOptionPane.QUESTION_MESSAGE) ;

            if (input == null) {
                JOptionPane.showMessageDialog(null, "Deleting Operation void/cancelled ", "Operation Cancelled", JOptionPane.INFORMATION_MESSAGE );
                return;
            }
            // Know can use parseInt but prefer valueOf as its universal and have to think less about it
            int employeeNumber = Integer.valueOf(input);

            boolean removed = employees.removeIf(employee -> employee.getNumber() == employeeNumber);

            if (removed) {
                reuseNumber(employeeNumber);
                JOptionPane.showMessageDialog(null, "Employee deleted successfully ", "Deleted Successfully ", JOptionPane.INFORMATION_MESSAGE);
                serializeEmployees();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid/Incorrect Employee number entered. Please enter a valid number: ", "Employee Not Found/Input Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid/Incorrect Employee Number entered. Please enter a valid number: ", "Input Error",JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//end

    		///////////////////////////////////////////////////////////////
    		// Method Name : edit()                                      //
    		// Return Type : void                                        //
      		// Parameters : None                                         //
    		// Purpose : Edits the required Employee record in employees //
      		///////////////////////////////////////////////////////////////
    
    			// Question (vi)
    
    public void edit() {//start
        try {
            String input = JOptionPane.showInputDialog(null, "Enter Employee Number to edit:  ", "Edit Employee", JOptionPane.QUESTION_MESSAGE);

            if (input == null) {
                JOptionPane.showMessageDialog(null, "Operation cancelled.", "INFO", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int employeeNumber = Integer.valueOf(input);

            Employee employee = null;
            for (Employee employ : employees) {
                if (employ.getNumber() ==employeeNumber) {
                    employee = employ;
                    break;
                }
            }

            if (employee == null) {
                JOptionPane.showMessageDialog(null, "Employee not found ", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(null, employee, "Current Employee Details", JOptionPane.INFORMATION_MESSAGE);

            String[] titles = { "Mr", "Mrs", "Ms", "Dr" };
            String editTitle = (String) JOptionPane.showInputDialog(null, "Select new title: ", "Edit the Title", JOptionPane.QUESTION_MESSAGE, null, titles, titles[0]);
            if (editTitle != null && !editTitle.trim().isEmpty()) employee.getName().setTitle(editTitle);

            String editFirstName = JOptionPane.showInputDialog(null, "Enter new first name: ", "Edit the First name", JOptionPane.QUESTION_MESSAGE);
            if (editFirstName != null && !editFirstName.trim().isEmpty()) employee.getName().setFirstName(editFirstName) ;

            String editSurname = JOptionPane.showInputDialog(null, "Enter new surname: ", "Edit the Surname", JOptionPane.QUESTION_MESSAGE);
            if (editSurname != null && !editSurname.trim().isEmpty()) employee.getName().setSurname(editSurname);

            String editPhone = JOptionPane.showInputDialog(null, "Enter new phone number: ", "Edit the Phone Number", JOptionPane.QUESTION_MESSAGE);
            if (editPhone != null && !editPhone.trim().isEmpty()) employee.setPhoneNumber(editPhone);

            String editSalary = JOptionPane.showInputDialog(null, "Enter new salary: ", "Edit the Salary", JOptionPane.QUESTION_MESSAGE);
            if (editSalary != null && !editSalary.trim().isEmpty()) {
                try {
                    double salary = Double.valueOf(editSalary);
                    employee.setSalary(salary);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid salary entered. Please enter a valid number: ", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            JOptionPane.showMessageDialog(null, employee, "Updated Employee Details", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Employee Number entered. Please enter a valid integer: ", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//end
    
    	///////////////////////////////////////////////////////
    	// Method Name : serializeEmployees()                //
    	// Return Type : void                                //
    	// Parameters : None                                 //
    	// Purpose : Serializes the employees ArrayList      //
    	//           to a file called employees.bin          //
    	///////////////////////////////////////////////////////

    	// Question (vi)
    public void serializeEmployees() {//start
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(employeesFile)))  {
            os.writeObject(employees);
            os.writeInt(Employee.getNextNumber());
             os.writeObject(deletedNumbers);
        } catch (IOException e) {
            System.out.println("Cannot write to " + employeesFile.getName() + ": " + e.getMessage());
        }
    }//end
    
    
    	///////////////////////////////////////////////////////
    	// Method Name : deserializeEmployees()              //
    	// Return Type : void                                //
    	// Parameters : None                                 //
    	// Purpose : Deserializes the employees ArrayList    //
    	//           from the file employees.bin             //
    	///////////////////////////////////////////////////////

    		// Question (vii)
    public void deserializeEmployees() {//start
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(employeesFile))) {
            employees = (ArrayList<Employee>) is.readObject();
            Employee.setNextNumber(is.readInt());
            deletedNumbers = (ArrayList<Integer>) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Cannot read from " + employeesFile.getName() + ": " + e.getMessage());
        }
    }//end

}

