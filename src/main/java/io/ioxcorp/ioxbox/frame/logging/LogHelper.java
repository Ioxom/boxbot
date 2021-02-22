package io.ioxcorp.ioxbox.frame.logging;

import io.ioxcorp.ioxbox.Main;

public class LogHelper {
    public static String replaceNewlines(LogType type, String message) {
        String delimiter = null;
        switch (type) {
            case CMD:
                delimiter = "\n[cmd]: ";
                break;
            case ERR:
                delimiter = "\n[err]: ";
                break;
            case INIT:
                delimiter = "\n[init]: ";
                break;
            case MAIN:
                delimiter = "\n[main]: ";
                break;
            case FATAL_ERR:
                delimiter = "\n[err/FATAL]: ";
                break;
        }
        return String.join(delimiter, message.split("\n"));
    }

    /**
     * @param type the {@link LogType} the message should be
     * @param message the message
     * @return the message formatted as what should be sent in the console
     */
    public static String getLogMessage(LogType type, String message) {
        String formattedMessage;
        switch (type) {
            case FATAL_ERR:
                formattedMessage = "\n[err/FATAL]: " + replaceNewlines(type, message) + "\n[err/FATAL]: closing ioxbox. you can view this message in " + Main.frame.getFileLogger().getFileName() + ".";
                break;
            case MAIN:
                formattedMessage = "\n[main]: " + replaceNewlines(type, message);
                break;
            case INIT:
                formattedMessage = "\n[init]: " + replaceNewlines(type, message);
                break;
            case ERR:
                formattedMessage = "\n[err]: " + replaceNewlines(type, message);
                break;
            case CMD:
                formattedMessage = "\n[cmd]: " + replaceNewlines(type, message);
                break;
            default:
                throw new IllegalArgumentException("unhandled type in LogType sent to LogHelper#getLogMessage(LogType, String)");
        }

        return formattedMessage;
    }

    /**
     * handles all {@link LogType LogTypes} excluding {@link LogType#CMD}
     * @param loggerType the type of logger to use: {@link LoggerType#WRITER WRITER} writes only to a file and {@link LoggerType#CONSOLE CONSOLE} writes to the console and a file
     * @param logType the type of log to display before the sent message
     * @param message the message to be displayed after the type
     * @author ioxom
     */
    public static void handleNormalLogs(LoggerType loggerType, LogType logType, String message) {
        if (logType == LogType.FATAL_ERR) {
            throwFatalError(message);
        }

        message = getLogMessage(logType, message);
        if (loggerType == LoggerType.WRITER) {
            Main.frame.getFileLogger().write(message);
        } else if (loggerType == LoggerType.CONSOLE) {
            Main.frame.getConsole().append(message);
            Main.frame.getFileLogger().write(message);
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
     * exists ioxbox after giving ample time to read the message
     * @param message the message to send before killing the process
     */
    public static void throwFatalError(String message) {
        message = getLogMessage(LogType.FATAL_ERR, message);
        Main.frame.log(LogType.FATAL_ERR, message);
        try {
            Thread.sleep(5000);
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
