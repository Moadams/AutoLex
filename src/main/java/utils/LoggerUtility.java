package main.java.utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import main.java.controller.FileProcessor;

public class LoggerUtility {
    private static final String LOG_FILE = "logs/log.txt";
    private final FileProcessor fileProcessor;

    public LoggerUtility(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    public void log(String level, String message) {
        try{
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String content = level + "" + timeStamp + ": " + message + "\n";
            fileProcessor.appendToFile(LOG_FILE, content);
            
        } catch (IOException e) {
            System.err.println("Logging failed: " + e.getMessage());
        }
    }

    public void logInfo(String message) {
        log("INFO", message);
    }

    public void logError(String message, Throwable throwable) {
        log("ERROR", message + " | Exception: " + throwable.getMessage());
    }

    public void logWarning(String message) {
        log("WARNING", message);
    }
}