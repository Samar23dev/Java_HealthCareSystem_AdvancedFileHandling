import java.io.Serializable;
import java.time.*;
import java.util.*;

public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id, problem = "General Checkup", status;
    private Doctor doctor;
    private Patient patient;
    private LocalDateTime dateTime;

    public Appointment(String id, Doctor doctor, Patient patient, LocalDateTime dateTime) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.dateTime = dateTime;
        this.status = "Pending";
    }

    public static void bookAppointment() {
        String SECTION = HealthCareSystem.SECTION;
        int COL_ID = HealthCareSystem.COL_ID;
        int COL_SPEC = HealthCareSystem.COL_SPEC;
        int COL_NAME = HealthCareSystem.COL_NAME;
        int COL_FEES = HealthCareSystem.COL_FEES;
        int BORDER_WIDTH = HealthCareSystem.BORDER_WIDTH;

        Patient patient = (Patient) HealthCareSystem.currentUser;
        List<Doctor> doctorList = HealthCareSystem.users.stream()
                .filter(u -> u instanceof Doctor)
                .map(u -> (Doctor) u)
                .toList();
        System.out.println("\n" + SECTION);
        System.out.println(HealthCareSystem.centerString("AVAILABLE DOCTORS", BORDER_WIDTH));
        System.out.println(SECTION);
        System.out.printf(
                "  %-" + HealthCareSystem.COL_ID + "s %-" + HealthCareSystem.COL_NAME + "s %-"
                        + HealthCareSystem.COL_SPEC + "s %" + HealthCareSystem.COL_FEES + "s%n",
                "ID",
                "Name", "Specialization", "Fees");
        System.out.println(SECTION);
        int id = 1;
        for (Doctor d : doctorList) {
            System.out.printf(
                    "  %-" + COL_ID + "d %-" + COL_NAME + "s %-" + COL_SPEC + "s $%-" + (COL_FEES - 1) + ".0f%n", id++,
                    d.getName(), d.getSpecialization(), d.getFees());
        }
        System.out.println(SECTION);
        System.out.print("Enter doctor ID: ");
        int choice = Integer.parseInt(HealthCareSystem.scanner.nextLine()) - 1;
        if (choice < 0 || choice >= doctorList.size()) {
            HealthCareSystem.printMessage("Invalid doctor ID!");
            return;
        }
        Doctor selected = doctorList.get(choice);
        System.out.print("Enter problem description: ");
        String problem = HealthCareSystem.scanner.nextLine();
        LocalDateTime now = LocalDateTime.now();
        Appointment apt = new Appointment(UUID.randomUUID().toString(), selected, patient, now.plusDays(1));
        apt.setProblem(problem);
        HealthCareSystem.appointments.add(apt);
        ConsoleLogger.logln("New appointment booked with doctor: " + selected.getUsername());
        System.out.println("\n" + SECTION);
        System.out.println(HealthCareSystem.centerString("APPOINTMENT DETAILS", BORDER_WIDTH));
        System.out.printf("  %-15s: %s%n", "Doctor", selected.getName());
        System.out.printf("  %-15s: %s%n", "Date", apt.getDateTime().format(HealthCareSystem.dateTimeFormatter));
        System.out.printf("  %-15s: %s%n", "Problem", apt.getProblem());
        System.out.printf("  %-15s: %s%n", "Status", apt.getStatus());
        System.out.println(SECTION);
        HealthCareSystem.printMessage("Appointment booked successfully!");
    }

    public String getId() {
        return id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getProblem() {
        return problem;
    }

    public String getStatus() {
        return status;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}