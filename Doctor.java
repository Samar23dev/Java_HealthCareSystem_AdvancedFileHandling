public class Doctor extends User {
    public String specialization;
    public double fees;

    public Doctor(String username, String password, String name, String specialization, double fees) {
        super(username, password, name);
        this.specialization = specialization;
        this.fees = fees;
    }

    public static void addDoctor() {
        var scanner = HealthCareSystem.scanner;
        var users = HealthCareSystem.users;
        System.out.print("\nEnter Username: ");
        String username = scanner.nextLine();
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            ConsoleLogger.logln("Add doctor failed - username exists: " + username);
            HealthCareSystem.printMessage("Username already exists!");
            return;
        }
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Specialization: ");
        String spec = scanner.nextLine();
        System.out.print("Enter Consultation Fee: ");
        double fee = Double.parseDouble(scanner.nextLine());
        users.add(new Doctor(username, password, name, spec, fee));
        ConsoleLogger.logln("New doctor added: " + username);
        HealthCareSystem.printMessage("Doctor added successfully!");
    }

    public String getSpecialization() {
        return specialization;
    }

    public double getFees() {
        return fees;
    }
}