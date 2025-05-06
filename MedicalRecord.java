import java.time.*;
import java.util.*;
import java.io.Serializable;

public class MedicalRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id, appointmentId, diagnosis, prescription, notes;
    private Doctor doctor;
    private Patient patient;
    private LocalDate date;
    static   String SECTION = HealthCareSystem.SECTION;
    static    int COL_ID=HealthCareSystem.COL_ID;
    static    int COL_SPEC=HealthCareSystem.COL_SPEC;
    static    int COL_NAME=HealthCareSystem.COL_NAME;
    static    int COL_FEES=HealthCareSystem.COL_FEES;
    static    int BORDER_WIDTH=HealthCareSystem.BORDER_WIDTH;
    static int COL_PROBLEM=HealthCareSystem.COL_PROBLEM;
    public MedicalRecord(String id, String appointmentId, Doctor doctor, Patient patient, 
                       String diagnosis, String prescription, String notes, LocalDate date) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.doctor = doctor;
        this.patient = patient;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.notes = notes;
        this.date = date;
    }
    public static void createMedicalRecord() {
        Doctor doctor = (Doctor) HealthCareSystem.currentUser;
        List<Appointment> pendingApts = HealthCareSystem.appointments.stream()
                .filter(a -> a.getDoctor().equals(doctor) && a.getStatus().equals("Pending"))
                .toList();
        if (pendingApts.isEmpty()) {
            HealthCareSystem.printMessage("No pending appointments!");
            return;
        }
        System.out.println("\n" + SECTION);
        System.out.println(HealthCareSystem.centerString("SELECT APPOINTMENT", BORDER_WIDTH));
        System.out.println(SECTION);
        System.out.printf("  %-" + COL_ID + "s %-" + COL_NAME + "s %-" + COL_PROBLEM + "s %-" + HealthCareSystem.COL_DATETIME + "s%n",
                "ID", "Patient", "Problem", "Date/Time");
        System.out.println(SECTION);
        int id = 1;
        for (Appointment apt : pendingApts) {
            System.out.printf(
                    "  %-" + COL_ID + "d %-" + COL_NAME + "s %-" + COL_PROBLEM + "s %-" + HealthCareSystem.COL_DATETIME + "s%n", id++,
                    apt.getPatient().getName(), apt.getProblem(), apt.getDateTime().format(HealthCareSystem.dateTimeFormatter));
        }
        System.out.println(SECTION);
        System.out.print("Enter appointment ID: ");
        int choice = Integer.parseInt(HealthCareSystem.scanner.nextLine()) - 1;
        if (choice < 0 || choice >= pendingApts.size()) {
            HealthCareSystem.printMessage("Invalid appointment ID!");
            return;
        }
        Appointment selected = pendingApts.get(choice);
        System.out.print("Enter diagnosis: ");
        String diag = HealthCareSystem.scanner.nextLine();
        System.out.print("Enter prescription: ");
        String pres = HealthCareSystem.scanner.nextLine();
        System.out.print("Enter notes: ");
        String notes = HealthCareSystem.scanner.nextLine();
        selected.setStatus("Completed");
        HealthCareSystem.records.add(new MedicalRecord(UUID.randomUUID().toString(), selected.getId(), doctor,
                (Patient) selected.getPatient(), diag, pres, notes, LocalDate.now()));
        ConsoleLogger.logln("Created medical record for appointment: " + selected.getId());
        HealthCareSystem.printMessage("Medical record created and appointment marked as completed!");
    }
    public static void displayMedicalRecords(Patient patient) {
        

        System.out.println("\n" + SECTION);
        System.out.println(HealthCareSystem.centerString("MEDICAL RECORDS FOR " + patient.getName().toUpperCase(), BORDER_WIDTH));
        boolean found = false;
        for (MedicalRecord rec : HealthCareSystem.records) {
            if (rec.getPatient().equals(patient)) {
                System.out.println(SECTION);
                System.out.printf("  %-15s: %s%n", "Record ID", rec.getId());
                System.out.printf("  %-15s: %s%n", "Date", rec.getDate().format(HealthCareSystem.dateFormatter));
                System.out.printf("  %-15s: %s%n", "Doctor", rec.getDoctor().getName());
                System.out.printf("  %-15s: %s%n", "Diagnosis", rec.getDiagnosis());
                System.out.printf("  %-15s: %s%n", "Prescription", rec.getPrescription());
                System.out.printf("  %-15s: %s%n", "Notes", rec.getNotes());
                found = true;
            }
        }
        if (!found) {
            HealthCareSystem.printMessage("No medical records found for this patient!");
        } else {
            System.out.println(SECTION + "\n");
        }
        ConsoleLogger.logln("Viewed medical records for patient: " + patient.getUsername());
    }
    public String getId() { return id; }
    public String getAppointmentId() { return appointmentId; }
    public Doctor getDoctor() { return doctor; }
    public Patient getPatient() { return patient; }
    public String getDiagnosis() { return diagnosis; }
    public String getPrescription() { return prescription; }
    public String getNotes() { return notes; }
    public LocalDate getDate() { return date; }
}