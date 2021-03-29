package io.ioxcorp.ioxbox.frame.logging;

import io.ioxcorp.ioxbox.Main;

/**
 * useful static methods for logging
 * @author ioxom
 */
public final class LogHelper {
    private LogHelper() {

    }

    /**
     * replaces \n with a corresponding header: i.e. [main]: yes\n no with the {@link LogType} of {@link LogType#MAIN MAIN} would become [main]: yes\n[main]:  no<br>
     * note: running a string through this method twice will produce bad results like "\n[main]: \n[main]: original message (if you run through twice)
     * @param type the {@link LogType} the message should be
     * @param message the message text
     * @return the formatted string
     */
    private static String replaceNewlines(final LogType type, final String message) {
        return String.join("\n" + type.getValue() + ": ", message.split("\n"));
    }

    /**
     * @param type the {@link LogType} the message should be
     * @param message the message
     * @return the message formatted as what should be sent in the console
     */
    public static String getLogMessage(final LogType type, final String message) {
        boolean addNewline = !Main.FRAME.getConsole().getText().isEmpty();
        if (type == LogType.FATAL_ERR) {
            return replaceNewlines(type, (addNewline ? "\n" : "") + message + "\nclosing ioxbox. you can view this message in " + Main.FRAME.getFileLogger().getFileName() + ".");
        } else {
            return replaceNewlines(type, (addNewline ? "\n" : "") + message);
        }
    }

    /**
     * handles all {@link LogType LogTypes} excluding {@link LogType#CMD}
     * @param loggerType the type of logger to use: {@link LoggerType#WRITER WRITER} writes only to a file and {@link LoggerType#CONSOLE CONSOLE} writes to the console and a file
     * @param logType the type of log to display before the sent message
     * @param message the message to be displayed after the type
     */
    public static void handleNormalLogs(final LoggerType loggerType, final LogType logType, final String message) {
        //special handling for FATAL_ERR
        if (logType == LogType.FATAL_ERR) {
            throwFatalError(message);
        //we need a CustomUser "author" object
        } else if (logType == LogType.CMD) {
            throw new IllegalArgumentException("LogType#CMD cannot be passed to LogHelper#handleNormalLogs(LoggerType, LogType, String)");
        } else {
            String logMessage = getLogMessage(logType, message);
            if (loggerType == LoggerType.WRITER) {
                Main.FRAME.getFileLogger().write(logMessage);
            } else if (loggerType == LoggerType.CONSOLE) {
                writeToConsoleAndFile(logMessage);
            }
        }
    }

    public enum LoggerType {
        /**
         * this logger writes to a file stored in /logs/
         */
        WRITER,
        /**
         * this logger logs to the console
         */
        CONSOLE
    }

    private static final int MILLISECONDS_BEFORE_QUITTING = 5000;

    /**
     * exits ioxbox after giving ample time to read the message
     * @param message the message to send before killing the process
     */
    private static void throwFatalError(final String message) {
        writeToConsoleAndFile(getLogMessage(LogType.FATAL_ERR, message));
        try {
            Thread.sleep(MILLISECONDS_BEFORE_QUITTING);
            Main.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * writes to both {@link Main Main's} console and its associated log file
     * @param message the message to write
     */
    private static void writeToConsoleAndFile(final String message) {
        Main.FRAME.getFileLogger().write(message);
        Main.FRAME.getConsole().append(message);
    }
}
