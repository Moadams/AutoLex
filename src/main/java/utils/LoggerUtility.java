package main.java.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtility {
    private static final String LOG_FILE = "logs/log.txt";

    public static void log(String level, String message) {
        try(PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true));) {
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.printf("%s: %s %s\n", timeStamp, message);
        } catch (IOException e) {
            System.err.println("Logging failed: " + e.getMessage());
        }
    }

    public static void logInfo(String message) {
        log("INFO", message);
    }

    public static void logError(String message, Throwable throwable) {
        log("ERROR", message + " | Exception: " + throwable.getMessage());
    }

    public static void logWarning(String message) {
        log("WARNING", message);
    }
}