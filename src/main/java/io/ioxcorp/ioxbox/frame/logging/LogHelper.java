package io.ioxcorp.ioxbox.frame.logging;

import io.ioxcorp.ioxbox.Main;

public final class LogHelper {
    private LogHelper() {

    }

    private static String replaceNewlines(final LogType type, final String message) {
        return String.join("\n" + type.getValue() + ": ", message.split("\n"));
    }

    /**
     * @param type the {@link LogType} the message should be
     * @param message the message
     * @return the message formatted as what should be sent in the console
     */
    public static String getLogMessage(final LogType type, final String message) {
        if (type == LogType.FATAL_ERR) {
            return replaceNewlines(type, "\n" + message + "\nclosing ioxbox. you can view this message in " + Main.FRAME.getFileLogger().getFileName() + ".");
        } else {
            return replaceNewlines(type, "\n" + message);
        }
    }

    /**
     * handles all {@link LogType LogTypes} excluding {@link LogType#CMD}
     * @param loggerType the type of logger to use: {@link LoggerType#WRITER WRITER} writes only to a file and {@link LoggerType#CONSOLE CONSOLE} writes to the console and a file
     * @param logType the type of log to display before the sent message
     * @param message the message to be displayed after the type
     * @author ioxom
     */
    public static void handleNormalLogs(final LoggerType loggerType, final LogType logType, final String message) {
        if (logType == LogType.FATAL_ERR) {
            throwFatalError(message);
        }

        String logMessage = getLogMessage(logType, message);
        if (loggerType == LoggerType.WRITER) {
            Main.FRAME.getFileLogger().write(logMessage);
        } else if (loggerType == LoggerType.CONSOLE) {
            writeToConsoleAndFrame(logMessage);
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

    /**
     * exits ioxbox after giving ample time to read the message
     * @param message the message to send before killing the process
     */
    private static void throwFatalError(final String message) {
        writeToConsoleAndFrame(getLogMessage(LogType.FATAL_ERR, message));
        try {
            Thread.sleep(5000);
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void writeToConsoleAndFrame(final String message) {
        Main.FRAME.getFileLogger().write(message);
        Main.FRAME.getConsole().append(message);
    }
}
