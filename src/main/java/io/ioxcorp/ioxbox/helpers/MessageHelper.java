package io.ioxcorp.ioxbox.helpers;

import io.ioxcorp.ioxbox.data.format.CustomUser;
import net.dv8tion.jda.api.entities.Message;

import javax.security.auth.login.AccountNotFoundException;

public class MessageHelper {

    public static boolean hasPing(Message message) {
        return message.getMentionedUsers().isEmpty();
    }

    public static CustomUser getFirstMention(Message message) throws AccountNotFoundException {
        if (message.getMentionedUsers().stream().findFirst().isEmpty()) {
            throw new AccountNotFoundException("no ping found in message");
        } else {
            return new CustomUser(message.getMentionedUsers().stream().findFirst().get());
        }
    }
}
