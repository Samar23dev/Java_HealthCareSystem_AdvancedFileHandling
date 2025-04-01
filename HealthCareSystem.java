import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class HealthCareSystem {
    public static String DATA_DIR = "./data/";
    public static String USERS_FILE = DATA_DIR + "users.txt";
    public static String APPOINTMENTS_FILE = DATA_DIR + "appointments.txt";
    public static String RECORDS_FILE = DATA_DIR + "records.txt";
    public static Scanner scanner = new Scanner(System.in);
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static int BORDER_WIDTH = 90;
    public static String BORDER = "=".repeat(BORDER_WIDTH);
    public static String SECTION = "-".repeat(BORDER_WIDTH);
    public static int COL_ID = 5, COL_USERNAME = 15, COL_NAME = 20, COL_SPEC = 20;
    public static int COL_FEES = 8, COL_DOB = 12, COL_PHONE = 15, COL_PROBLEM = 20;
    public static int COL_STATUS = 12, COL_DATETIME = 19;
    public static User currentUser = null;
    public static List<User> users = new ArrayList<>();
    public static List<Appointment> appointments = new ArrayList<>();
    public static List<MedicalRecord> records = new ArrayList<>();

    public static void main(String[] args) {
        try {
            initializeSystem();
            loadData();
            boolean running = true;
            while (running) {
                if (currentUser == null) {
                    showLoginMenu();
                } else if (currentUser instanceof Admin) {
                    showAdminMenu();
                } else if (currentUser instanceof Doctor) {
                    showDoctorMenu();
                } else if (currentUser instanceof Patient) {
                    showPatientMenu();
                }
                String choice = scanner.nextLine();
                ConsoleLogger.logln("User input: " + choice);
                if (choice.equals("0")) {
                    running = false;
                } else {
                    processChoice(choice);
                }
            }
            saveData();
            ConsoleLogger.logln("System shutdown");
        } finally {
            ConsoleLogger.close();
        }
    }

    public static void initializeSystem() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            if (!Files.exists(Paths.get(USERS_FILE))) {
                Files.write(Paths.get(USERS_FILE), Arrays.asList(
                        "# TYPE|USERNAME|PASSWORD|NAME|SPECIALIZATION|FEES|DOB|PHONE",
                        "ADMIN|admin|admin123|Admin User",
                        "DOCTOR|drsahil|sahil123|Dr. Sahil J. Dulani|Cardiology|150",
                        "DOCTOR|drsanjay|sanjay123|Dr. Sanjay K. Singh|Pediatrics|120",
                        "PATIENT|pat1|pat123|Rahul K.|1990-05-15|123-456-7890"));
                ConsoleLogger.logln("Initialized users file");
            }
            if (!Files.exists(Paths.get(APPOINTMENTS_FILE))) {
                Files.write(Paths.get(APPOINTMENTS_FILE), Collections.singletonList(
                        "# ID|DOCTOR_USERNAME|PATIENT_USERNAME|DATETIME|PROBLEM|STATUS"));
                ConsoleLogger.logln("Initialized appointments file");
            }
            if (!Files.exists(Paths.get(RECORDS_FILE))) {
                Files.write(Paths.get(RECORDS_FILE), Collections.singletonList(
                        "# ID|DOCTOR_USERNAME|PATIENT_USERNAME|APPOINTMENT_ID|DIAGNOSIS|PRESCRIPTION|NOTES|DATE"));
                ConsoleLogger.logln("Initialized records file");
            }
        } catch (IOException e) {
            ConsoleLogger.logln("Error initializing system: " + e.getMessage());
        }
    }

    public static void loadData() {
        try {
            List<String> userLines = Files.readAllLines(Paths.get(USERS_FILE));
            for (String line : userLines) {
                if (line.startsWith("#"))
                    continue;
                String[] parts = line.split("\\|");
                switch (parts[0]) {
                    case "ADMIN":
                        users.add(new Admin(parts[1], parts[2], parts[3]));
                        break;
                    case "DOCTOR":
                        users.add(new Doctor(parts[1], parts[2], parts[3], parts[4], Double.parseDouble(parts[5])));
                        break;
                    case "PATIENT":
                        users.add(new Patient(parts[1], parts[2], parts[3], parts[4], parts[5]));
                        break;
                }
            }
            ConsoleLogger.logln("Loaded " + users.size() + " users");
            if (Files.exists(Paths.get(APPOINTMENTS_FILE))) {
                List<String> aptLines = Files.readAllLines(Paths.get(APPOINTMENTS_FILE));
                for (String line : aptLines) {
                    if (line.startsWith("#"))
                        continue;
                    String[] parts = line.split("\\|");
                    Doctor doc = (Doctor) findUser(parts[1]);
                    Patient pat = (Patient) findUser(parts[2]);
                    if (doc != null && pat != null) {
                        Appointment apt = new Appointment(parts[0], doc, pat,
                                LocalDateTime.parse(parts[3], dateTimeFormatter));
                        apt.setProblem(parts[4]);
                        apt.setStatus(parts[5]);
                        appointments.add(apt);
                    }
                }
                ConsoleLogger.logln("Loaded " + appointments.size() + " appointments");
            }
            if (Files.exists(Paths.get(RECORDS_FILE))) {
                List<String> recLines = Files.readAllLines(Paths.get(RECORDS_FILE));
                for (String line : recLines) {
                    if (line.startsWith("#"))
                        continue;
                    String[] parts = line.split("\\|");
                    Doctor doc = (Doctor) findUser(parts[1]);
                    Patient pat = (Patient) findUser(parts[2]);
                    if (doc != null && pat != null) {
                        records.add(new MedicalRecord(parts[0], parts[3], doc, pat, parts[4], parts[5], parts[6],
                                LocalDate.parse(parts[7])));
                    }
                }
                ConsoleLogger.logln("Loaded " + records.size() + " medical records");
            }
        } catch (IOException e) {
            ConsoleLogger.logln("Error loading data: " + e.getMessage());
        }
    }

    public static User findUser(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    public static void saveData() {
        try {
            List<String> userLines = new ArrayList<>();
            userLines.add("# TYPE|USERNAME|PASSWORD|NAME|SPECIALIZATION|FEES|DOB|PHONE");
            for (User user : users) {
                if (user instanceof Admin) {
                    userLines.add("ADMIN|" + user.getUsername() + "|" + user.getPassword() + "|" + user.getName());
                } else if (user instanceof Doctor d) {
                    userLines.add("DOCTOR|" + d.getUsername() + "|" + d.getPassword() + "|" + d.getName() + "|"
                            + d.getSpecialization() + "|" + d.getFees());
                } else if (user instanceof Patient p) {
                    userLines.add("PATIENT|" + p.getUsername() + "|" + p.getPassword() + "|" + p.getName() + "|"
                            + p.getDob() + "|" + p.getPhone());
                }
            }
            Files.write(Paths.get(USERS_FILE), userLines);
            List<String> aptLines = new ArrayList<>();
            aptLines.add("# ID|DOCTOR_USERNAME|PATIENT_USERNAME|DATETIME|PROBLEM|STATUS");
            for (Appointment apt : appointments) {
                aptLines.add(apt.getId() + "|" + apt.getDoctor().getUsername() + "|" + apt.getPatient().getUsername()
                        + "|" + apt.getDateTime().format(dateTimeFormatter) + "|" + apt.getProblem() + "|"
                        + apt.getStatus());
            }
            Files.write(Paths.get(APPOINTMENTS_FILE), aptLines);
            List<String> recLines = new ArrayList<>();
            recLines.add("# ID|DOCTOR_USERNAME|PATIENT_USERNAME|APPOINTMENT_ID|DIAGNOSIS|PRESCRIPTION|NOTES|DATE");
            for (MedicalRecord rec : records) {
                recLines.add(rec.getId() + "|" + rec.getDoctor().getUsername() + "|" + rec.getPatient().getUsername()
                        + "|" + rec.getAppointmentId() + "|" + rec.getDiagnosis() + "|" + rec.getPrescription() + "|"
                        + rec.getNotes() + "|" + rec.getDate().format(dateFormatter));
            }
            Files.write(Paths.get(RECORDS_FILE), recLines);
        } catch (IOException e) {
            ConsoleLogger.logln("Error saving data: " + e.getMessage());
        }
    }

    public static void showLoginMenu() {
        System.out.println("\n" + SECTION);
        System.out.println(centerString("HEALTHCARE MANAGEMENT SYSTEM", BORDER_WIDTH));
        System.out.println(SECTION);
        System.out.println("  1. Login");
        System.out.println("  2. Register as Patient");
        System.out.println("  0. Exit");
        System.out.println(SECTION);
        System.out.print("Enter your choice: ");
        ConsoleLogger.logln("Displayed login menu");
    }

    public static void showAdminMenu() {
        System.out.println("\n" + SECTION);
        System.out.println(centerString("ADMIN MENU", BORDER_WIDTH));
        System.out.println(SECTION);
        System.out.println("  1. Add Doctor");
        System.out.println("  2. View All Doctors");
        System.out.println("  3. View All Patients");
        System.out.println("  4. View All Appointments");
        System.out.println("  5. Logout");
        System.out.println("  0. Exit");
        System.out.println(SECTION);
        System.out.print("Enter your choice: ");
        ConsoleLogger.logln("Displayed admin menu");
    }

    public static void showDoctorMenu() {
        System.out.println("\n" + SECTION);
        System.out.println(centerString("DOCTOR MENU", BORDER_WIDTH));
        System.out.println(SECTION);
        System.out.println("  1. View My Appointments");
        System.out.println("  2. Create Medical Record");
        System.out.println("  3. View Patient Records");
        System.out.println("  4. Logout");
        System.out.println("  0. Exit");
        System.out.println(SECTION);
        System.out.print("Enter your choice: ");
        ConsoleLogger.logln("Displayed doctor menu");
    }

    public static void showPatientMenu() {
        System.out.println("\n" + SECTION);
        System.out.println(centerString("PATIENT MENU", BORDER_WIDTH));
        System.out.println(SECTION);
        System.out.println("  1. View Profile");
        System.out.println("  2. View Doctors");
        System.out.println("  3. Book Appointment");
        System.out.println("  4. View Medical Records");
        System.out.println("  5. View Appointments");
        System.out.println("  6. Logout");
        System.out.println("  0. Exit");
        System.out.println(SECTION);
        System.out.print("Enter your choice: ");
        ConsoleLogger.logln("Displayed patient menu");
    }

    public static String centerString(String s, int width) {
        if (s.length() >= width)
            return s;
        int padding = (width - s.length()) / 2;
        return " ".repeat(padding) + s;
    }

    public static void printMessage(String message) {
        System.out.println("\n" + BORDER);
        System.out.println(centerString(message, BORDER_WIDTH));
        System.out.println(BORDER + "\n");
        ConsoleLogger.logln(message);
    }

    public static String formatCell(String content, int width) {
        if (content.length() > width) {
            return content.substring(0, width - 3) + "...";
        }
        return String.format("%-" + width + "s", content);
    }

    public static void processChoice(String choice) {
        if (currentUser == null) {
            switch (choice) {
                case "1" -> login();
                case "2" -> Patient.registerPatient();
                default -> printMessage("Invalid choice!");
            }
        } else if (currentUser instanceof Admin) {
            switch (choice) {
                case "1" -> Doctor.addDoctor();
                case "2" -> Admin.viewAllDoctors();
                case "3" -> viewAllPatients();
                case "4" -> viewAllAppointments();
                case "5" -> logout();
                default -> printMessage("Invalid choice!");
            }
        } else if (currentUser instanceof Doctor) {
            switch (choice) {
                case "1" -> viewDoctorAppointments();
                case "2" -> MedicalRecord.createMedicalRecord();
                case "3" -> viewPatientRecords();
                case "4" -> logout();
                default -> printMessage("Invalid choice!");
            }
        } else if (currentUser instanceof Patient) {
            switch (choice) {
                case "1" -> viewProfile();
                case "2" -> Admin.viewAllDoctors();
                case "3" -> Appointment.bookAppointment();
                case "4" -> viewMedicalRecords();
                case "5" -> viewPatientAppointments();
                case "6" -> logout();
                default -> printMessage("Invalid choice!");
            }
        }
    }

    public static void login() {
        System.out.print("\nEnter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        currentUser = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst().orElse(null);
        if (currentUser != null) {
            ConsoleLogger.logln("User " + username + " logged in");
            printMessage("Login successful! Welcome, " + currentUser.getName() + "!");
        } else {
            ConsoleLogger.logln("Failed login attempt for username: " + username);
            printMessage("Invalid username or password!");
        }
    }

    public static void logout() {
        ConsoleLogger.logln("User " + currentUser.getUsername() + " logged out");
        currentUser = null;
        printMessage("Logged out successfully!");
    }

    public static void viewProfile() {
        Patient patient = (Patient) currentUser;
        System.out.println("\n" + SECTION);
        System.out.println(centerString("YOUR PROFILE", BORDER_WIDTH));
        System.out.println(SECTION);
        System.out.printf("  %-15s: %s%n", "Username", patient.getUsername());
        System.out.printf("  %-15s: %s%n", "Name", patient.getName());
        System.out.printf("  %-15s: %s%n", "Date of Birth", patient.getDob());
        System.out.printf("  %-15s: %s%n", "Phone", patient.getPhone());
        System.out.println(SECTION + "\n");
        ConsoleLogger.logln("Patient viewed profile");
    }

    public static void viewAllPatients() {
        System.out.println("\n" + SECTION);
        System.out.println(centerString("PATIENTS LIST", BORDER_WIDTH));
        System.out.println(SECTION);
        System.out.printf("  %-" + COL_ID + "s %-" + COL_USERNAME + "s %-" + COL_NAME + "s %-" + COL_DOB + "s %-"
                + COL_PHONE + "s%n", "ID", "Username", "Name", "DOB", "Phone");
        System.out.println(SECTION);
        int id = 1;
        for (User user : users) {
            if (user instanceof Patient p) {
                System.out.printf("  %-" + COL_ID + "d %-" + COL_USERNAME + "s %-" + COL_NAME + "s %-" + COL_DOB
                        + "s %-" + COL_PHONE + "s%n", id++, p.getUsername(), p.getName(), p.getDob(), p.getPhone());
            }
        }
        System.out.println(SECTION + "\n");
        ConsoleLogger.logln("Viewed all patients");
    }

    public static void viewAllAppointments() {
        System.out.println("\n" + SECTION);
        System.out.println(centerString("ALL APPOINTMENTS", BORDER_WIDTH));
        System.out.println(SECTION);
        System.out.printf("  %-" + COL_ID + "s %-" + COL_NAME + "s %-" + COL_NAME + "s %-" + COL_DATETIME + "s %-"
                + COL_STATUS + "s%n", "ID", "Doctor", "Patient", "Date/Time", "Status");
        System.out.println(SECTION);
        int id = 1;
        for (Appointment apt : appointments) {
            System.out.printf(
                    "  %-" + COL_ID + "d %-" + COL_NAME + "s %-" + COL_NAME + "s %-" + COL_DATETIME + "s %-"
                            + COL_STATUS + "s%n",
                    id++, formatCell(apt.getDoctor().getName(), COL_NAME),
                    formatCell(apt.getPatient().getName(), COL_NAME), apt.getDateTime().format(dateTimeFormatter),
                    apt.getStatus());
        }
        System.out.println(SECTION + "\n");
        ConsoleLogger.logln("Viewed all appointments");
    }

    public static void viewDoctorAppointments() {
        Doctor doctor = (Doctor) currentUser;
        System.out.println("\n" + SECTION);
        System.out.println(centerString("MY APPOINTMENTS", BORDER_WIDTH));
        System.out.println(SECTION);
        System.out.printf("  %-" + COL_ID + "s %-" + COL_NAME + "s %-" + COL_PROBLEM + "s %-" + COL_DATETIME + "s %-"
                + COL_STATUS + "s%n", "ID", "Patient", "Problem", "Date/Time", "Status");
        System.out.println(SECTION);
        int id = 1;
        boolean found = false;
        for (Appointment apt : appointments) {
            if (apt.getDoctor().equals(doctor)) {
                System.out.printf(
                        "  %-" + COL_ID + "d %-" + COL_NAME + "s %-" + COL_PROBLEM + "s %-" + COL_DATETIME + "s %-"
                                + COL_STATUS + "s%n",
                        id++, formatCell(apt.getPatient().getName(), COL_NAME),
                        formatCell(apt.getProblem(), COL_PROBLEM), apt.getDateTime().format(dateTimeFormatter),
                        apt.getStatus());
                found = true;
            }
        }
        if (!found) {
            printMessage("No appointments found!");
        } else {
            System.out.println(SECTION + "\n");
        }
        ConsoleLogger.logln("Doctor viewed appointments");
    }

    public static void viewPatientAppointments() {
        Patient patient = (Patient) currentUser;
        System.out.println("\n" + SECTION);
        System.out.println(centerString("YOUR APPOINTMENTS", BORDER_WIDTH));
        System.out.println(SECTION);
        System.out.printf("  %-" + COL_ID + "s %-" + COL_NAME + "s %-" + COL_SPEC + "s %-" + COL_DATETIME + "s %-"
                + COL_STATUS + "s%n", "ID", "Doctor", "Specialization", "Date/Time", "Status");
        System.out.println(SECTION);
        int id = 1;
        boolean found = false;
        for (Appointment apt : appointments) {
            if (apt.getPatient().equals(patient)) {
                System.out.printf(
                        "  %-" + COL_ID + "d %-" + COL_NAME + "s %-" + COL_SPEC + "s %-" + COL_DATETIME + "s %-"
                                + COL_STATUS + "s%n",
                        id++, formatCell(apt.getDoctor().getName(), COL_NAME),
                        formatCell(apt.getDoctor().getSpecialization(), COL_SPEC),
                        apt.getDateTime().format(dateTimeFormatter), apt.getStatus());
                found = true;
            }
        }
        if (!found) {
            printMessage("No appointments found!");
        } else {
            System.out.println(SECTION + "\n");
        }
        ConsoleLogger.logln("Patient viewed appointments");
    }

    public static void viewPatientRecords() {
        System.out.println("\n" + SECTION);
        System.out.println(centerString("SELECT PATIENT", BORDER_WIDTH));
        System.out.println(SECTION);
        System.out.printf("  %-" + COL_ID + "s %-" + COL_USERNAME + "s %-" + COL_NAME + "s%n", "ID", "Username",
                "Name");
        System.out.println(SECTION);
        int id = 1;
        List<Patient> patientList = users.stream()
                .filter(u -> u instanceof Patient)
                .map(u -> (Patient) u)
                .toList();
        for (Patient p : patientList) {
            System.out.printf("  %-" + COL_ID + "d %-" + COL_USERNAME + "s %-" + COL_NAME + "s%n", id++,
                    p.getUsername(), p.getName());
        }
        System.out.println(SECTION);
        System.out.print("Enter patient ID: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        if (choice < 0 || choice >= patientList.size()) {
            printMessage("Invalid patient ID!");
            return;
        }
        MedicalRecord.displayMedicalRecords(patientList.get(choice));
    }

    public static void viewMedicalRecords() {
        Patient patient = (Patient) currentUser;
        MedicalRecord.displayMedicalRecords(patient);
    }

}