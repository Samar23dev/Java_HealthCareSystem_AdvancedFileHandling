public class Admin extends User {
    public Admin(String username, String password, String name) {
        super(username, password, name);
    }

    public static void viewAllDoctors() {
        String SECTION = HealthCareSystem.SECTION;
        int COL_ID = HealthCareSystem.COL_ID;
        int COL_SPEC = HealthCareSystem.COL_SPEC;
        int COL_NAME = HealthCareSystem.COL_NAME;
        int COL_FEES = HealthCareSystem.COL_FEES;
        int BORDER_WIDTH = HealthCareSystem.BORDER_WIDTH;
        System.out.println("\n" + SECTION);
        System.out.println(HealthCareSystem.centerString("DOCTORS LIST", BORDER_WIDTH));
        System.out.println(SECTION);
        System.out.printf("  %-" + COL_ID + "s %-" + COL_NAME + "s %-" + COL_SPEC + "s %" + COL_FEES + "s%n", "ID",
                "Name", "Specialization", "Fees");
        System.out.println(SECTION);
        int id = 1;
        for (User user : HealthCareSystem.users) {
            if (user instanceof Doctor d) {
                System.out.printf(
                        "  %-" + COL_ID + "d %-" + COL_NAME + "s %-" + COL_SPEC + "s $%-" + (COL_FEES - 1) + ".0f%n",
                        id++, d.getName(), d.getSpecialization(), d.getFees());
            }
        }
        System.out.println(SECTION + "\n");
        ConsoleLogger.logln("Viewed all doctors");
    }
}