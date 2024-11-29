 /**
 * Class: BSc. in Computing
 * Instructor: Maria Boyle
 * Description: Models an Employee
 * Date: 21/11/2024
 * @version 1.0
 * Author: Cashel Mc Daid
**/

    package ie.atu.hotel;

    import java.text.DecimalFormat;
    import java.util.ArrayList;
    	//Question (i)
    import java.io.Serializable;
    import javax.swing.JComboBox;
    import javax.swing.JDialog;
    import javax.swing.JOptionPane;
    import javax.swing.JTextField;

    	// Question (i)
    public class Employee extends Person implements Payable, Serializable {
    private double salary;
    private int number;
    private static int nextNumber = 10000;
    private static ArrayList<Integer> noRepeatNumbers = new ArrayList<>();
    private final double MAX_SALARY = 150000;

    public Employee() {
        super();
        salary = 0.0;
        number = getAvailableNumber();
    }

    public Employee(Name name, String phoneNo, Date dob, Date startDate, double salary) {
        super(name, phoneNo);
        this.salary = salary;
        number = getAvailableNumber();
    }
    
  

    private int getAvailableNumber() {
        if (!noRepeatNumbers.isEmpty()) {
            return noRepeatNumbers.remove(0);
        }
        return nextNumber++;
    }
    


    public static void repeatNumber(int number) {
        // making sure no duplicates
        for (int n : noRepeatNumbers) {
            if (n == number) {
                return; 
            }
        }
        noRepeatNumbers.add(number);
    }
   
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.00");
        return number + " " + name + " € " + df.format(salary) + ".";
    }

    @Override
    public boolean equals(Object obj) {
        Employee eObject;
        if (obj instanceof Employee) eObject = (Employee) obj;
        else return false;
        return this.number == eObject.number;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public static int getNextNumber() {
        return nextNumber;
    }

    public static void setNextNumber(int nextNumber) {
        Employee.nextNumber = nextNumber;
    }

    public static ArrayList<Integer> getRepeatNumbers() {
        return noRepeatNumbers;
    }

    public static void setRepeatNumbers(ArrayList<Integer> noRepeatNumbers) {
        Employee.noRepeatNumbers = noRepeatNumbers;
    }

    	// Question (iii)
    public boolean read() {
        JTextField txtEmployeeNumber = new JTextField(String.valueOf(number));
        txtEmployeeNumber.setEditable(false);
        JTextField txtFirstName = new JTextField();
        JTextField txtSurname = new JTextField();
        JTextField txtPhoneNumber = new JTextField();
        JTextField txtSalary = new JTextField();

        String[] title = {"Mr", "Ms", "Mrs", "Miss"};
        JComboBox<String> dropTitles = new JComboBox<>(title);

        Object[] combinedObj = {
            "Employee Number:", txtEmployeeNumber,
            "Title:", dropTitles,
            "First Name:", txtFirstName,
            "Surname:", txtSurname,
            "Phone Number:", txtPhoneNumber,
            "Salary:", txtSalary
        };

        JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        int selection = JOptionPane.showConfirmDialog(dialog, combinedObj, "ENTER EMPLOYEE DETAILS", JOptionPane.OK_CANCEL_OPTION);

        if (selection == JOptionPane.OK_OPTION) {
            if (txtFirstName.getText().trim().isEmpty() || txtSurname.getText().trim().isEmpty() ||
                txtPhoneNumber.getText().trim().isEmpty() || txtSalary.getText().trim().isEmpty())  {
                JOptionPane.showMessageDialog(null, "All fields are required: ", "Entering Input Error ", JOptionPane.ERROR_MESSAGE);
                repeatNumber(number);
                return false;
            }

            if (txtFirstName.getText().trim().length() > 20 || txtSurname.getText().trim().length() > 20) {
                JOptionPane.showMessageDialog(null, "First name and surname must not exceed 20 characters: ", "Entering Input Error ", JOptionPane.ERROR_MESSAGE);
                repeatNumber(number);
                return false;
            }
            
            if (txtFirstName.getText().trim().matches(".*\\d.*") || txtSurname.getText().trim().matches(".*\\d.*")) {
            	JOptionPane.showMessageDialog(null, "Names cannot contain numbers: ", "Entering Input Error ", JOptionPane.ERROR_MESSAGE); 
            	repeatNumber(number); 
            	return false; 
            
            }

            if (txtPhoneNumber.getText().trim().length()!= 10 || !txtPhoneNumber.getText().trim().matches("\\d{10}")) {
                JOptionPane.showMessageDialog(null, "Phone number must be exactly 10 digits: ", "Entering Input Error ", JOptionPane.ERROR_MESSAGE);
                repeatNumber(number);
                return false;
            }
            
            if (txtSalary.getText().trim().length() > 12 || !txtSalary.getText().trim().matches("\\d+(\\.\\d{1,2})?")) { 
            	JOptionPane.showMessageDialog(null, "Salary must be a number with up to 12 digits and up to 2 decimal places: ", "Entering Input Error ", JOptionPane.ERROR_MESSAGE); 
                repeatNumber(number); 
                return false; }

            this.name.setTitle(dropTitles.getSelectedItem().toString());
            this.name.setFirstName(capitalizeFirstLetter(txtFirstName.getText().trim()));
            this.name.setSurname(capitalizeFirstLetter(txtSurname.getText().trim()));
            this.phoneNumber = txtPhoneNumber.getText().trim();
            this.salary = Double.valueOf(txtSalary.getText().trim());

            return true;
        } else {
            repeatNumber(number);
            return false;
        }
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0,1).toUpperCase()+ input.substring(1).toLowerCase();
    }

    @Override
    public double calculatePay(double taxPercentage) {
        double pay = salary / 12;
        pay -= (pay * (taxPercentage /100));
        return pay ;
    }

    @Override
    public double incrementSalary(double incrementAmount) {
        salary += incrementAmount;
        if (salary > MAX_SALARY) salary = MAX_SALARY;
        return salary;
    }
}




