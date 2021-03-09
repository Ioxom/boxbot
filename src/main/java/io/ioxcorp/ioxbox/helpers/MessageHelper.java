package io.ioxcorp.ioxbox.helpers;

public final class MessageHelper {
    private MessageHelper() {

    }

    /**
     * get the specified id as a discord-formatted ping
     * @param id the id of the user to return as a ping
     * @return the ping
     */
    public static String getAsPing(final Object id) {
        if (!(id instanceof String || id instanceof Long)) {
            throw new IllegalArgumentException("parameter id must be String or Long");
        } else {
            return "<@" + id + ">";
        }
    }
}
