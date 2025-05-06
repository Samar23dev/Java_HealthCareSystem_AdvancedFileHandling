import java.io.Serializable;

public class Patient extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    public String dob, phone;
    public Patient(String username, String password, String name, String dob, String phone) {
        super(username, password, name);
        this.dob = dob;
        this.phone = phone;
    }
    public static void registerPatient() {
        var scanner=HealthCareSystem.scanner;
        var users=HealthCareSystem.users;
        System.out.print("\nEnter Username: ");
        String username = scanner.nextLine();
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            ConsoleLogger.logln("Registration failed - username exists: " + username);
            HealthCareSystem.printMessage("Username already exists!");
            return;
        }
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();
        users.add(new Patient(username, password, name, dob, phone));
        ConsoleLogger.logln("New patient registered: " + username);
        HealthCareSystem.printMessage("Registration successful! You can now login.");
    }
    public String getDob() { return dob; }
    public String getPhone() { return phone; }
   
}