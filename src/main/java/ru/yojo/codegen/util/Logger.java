package ru.yojo.codegen.util;

/**
 * Simple logger for the YOJO generator.
 * Centralizes logging to make it easier to replace with a proper logging framework later.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class Logger {

    /**
     * Log levels.
     */
    public enum Level {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    private final String className;
    private Level minimumLevel = Level.INFO;

    /**
     * Creates a logger for the given class.
     *
     * @param clazz the class to log for
     */
    public Logger(Class<?> clazz) {
        this.className = clazz.getSimpleName();
    }

    /**
     * Sets the minimum log level. Messages below this level will be suppressed.
     *
     * @param level the minimum level to log
     */
    public void setMinimumLevel(Level level) {
        this.minimumLevel = level;
    }

    /**
     * Logs a debug message.
     *
     * @param message the message
     */
    public void debug(String message) {
        if (minimumLevel.ordinal() <= Level.DEBUG.ordinal()) {
            System.out.println("[DEBUG] " + className + ": " + message);
        }
    }

    /**
     * Logs an info message.
     *
     * @param message the message
     */
    public void info(String message) {
        if (minimumLevel.ordinal() <= Level.INFO.ordinal()) {
            System.out.println("[INFO] " + className + ": " + message);
        }
    }

    /**
     * Logs a warning message.
     *
     * @param message the message
     */
    public void warn(String message) {
        if (minimumLevel.ordinal() <= Level.WARN.ordinal()) {
            System.err.println("[WARN] " + className + ": " + message);
        }
    }

    /**
     * Logs an error message.
     *
     * @param message the message
     */
    public void error(String message) {
        if (minimumLevel.ordinal() <= Level.ERROR.ordinal()) {
            System.err.println("[ERROR] " + className + ": " + message);
        }
    }

    /**
     * Logs an error message with exception.
     *
     * @param message the message
     * @param e the exception
     */
    /**
     * Logs an error message with exception details.
     *
     * @param message the message
     * @param e the exception
     */
    public void error(String message, Throwable e) {
        error(message + " - " + e.getClass().getSimpleName() + ": " + e.getMessage());
    }
}
