package io.ioxcorp.ioxbox.helpers;

public final class MessageHelper {
    public static String getAsPing(Object id) {
        if (!(id instanceof String || id instanceof Long)) {
            throw new IllegalArgumentException("parameter id must be String or Long");
        } else {
            return "<@" + id + ">";
        }
    }
}
