package io.ioxcorp.ioxbox.frame.logging;

public enum LogType {
    /**
     * appears in the console as [main]: message
     */
    MAIN,
    /**
     * appears in the console as [init]: message
     */
    INIT,
    /**
     * appears in the console as [err]: message
     */
    ERR,
    /**
     * appears in the console as [err/FATAL]: message
     */
    FATAL_ERR,
    /**
     * appears in the console as [cmd]: message
     */
    CMD,
    /**
     * appears in the console as [help]: message
     */
    HELP
}
