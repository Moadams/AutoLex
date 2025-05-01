package main.java.utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.controller.FileProcessor;

public class LoggerUtility {
    private static final Logger logger = Logger.getLogger(LoggerUtility.class.getName());
    private static final String LOG_FILE = "logs/log.txt";
    private final FileProcessor fileProcessor;

    public LoggerUtility(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    public void log(Level level, String message) {
        try{
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String content = level + ": " + timeStamp + ": " + message + "\n";
            fileProcessor.appendToFile(LOG_FILE, content);
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Logging failed: " + e.getMessage());
            
        }
    }

    public void logInfo(String message) {
        log(Level.INFO, message);
    }

    public void logError(String message, Throwable throwable) {
        log(Level.SEVERE, message + " | Exception: " + throwable.getMessage());
    }

    public void logWarning(String message) {
        log(Level.WARNING, message);
    }
}