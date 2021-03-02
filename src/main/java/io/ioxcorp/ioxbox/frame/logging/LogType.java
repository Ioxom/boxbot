package io.ioxcorp.ioxbox.frame.logging;

public enum LogType {
    /**
     * appears in the console as [main]: message
     */
    MAIN("[main]"),
    /**
     * appears in the console as [init]: message
     */
    INIT("[init]"),
    /**
     * appears in the console as [err]: message
     */
    ERR("[err]"),
    /**
     * appears in the console as [err/FATAL]: message
     */
    FATAL_ERR("[err/FATAL]"),
    /**
     * appears in the console as [cmd]: message
     */
    CMD("[cmd]"),
    /**
     * appears in the console as [help]: message
     */
    HELP("[help]");

    private final String value;

    LogType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
