import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;

public class ConsoleLogger {
    private static final String LOG_FILE = HealthCareSystem.DATA_DIR + "logfile.txt";
    private static PrintWriter logWriter;
    private static final DateTimeFormatter logTimestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    static {
        try {
            Files.createDirectories(Paths.get(HealthCareSystem.DATA_DIR));
            logWriter = new PrintWriter(new FileWriter(LOG_FILE, true));
        } catch (IOException e) {
            System.err.println("Failed to initialize log file: " + e.getMessage());
        }
    }

    public static void log(String message) {
        if (!isVisualElement(message)) {
            String timestamp = LocalDateTime.now().format(logTimestampFormatter);
            logWriter.print("[" + timestamp + "] " + message);
            logWriter.flush();
        }
    }

    public static void logln(String message) {
        if (!isVisualElement(message)) {
            String timestamp = LocalDateTime.now().format(logTimestampFormatter);
            logWriter.println("[" + timestamp + "] " + message);
            logWriter.flush();
        }
    }

    private static boolean isVisualElement(String message) {
        return message.equals(HealthCareSystem.BORDER) || message.equals(HealthCareSystem.SECTION) ||
                message.trim().isEmpty() || message.startsWith("  ") ||
                message.contains("=====") || message.contains("-----");
    }

    public static void close() {
        if (logWriter != null) {
            logWriter.close();
        }
    }
}