package io.ioxcorp.ioxbox.listeners.confirmation;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * runs through the {@link ConfirmationGetter ConfirmationGetters} contained in the static {@code CONFIRMATION_GETTERS} {@link java.util.HashMap} contained in the {@link ConfirmationGetter ConfirmationGetter} class and checks if we've gotten confirmation from the referenced users <br>
 * if so, closes that getter and saves the response
 * @author ioxom
 */
public final class ConfirmationGetterListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {

        //if we have no active confirmation getters do nothing
        if (ConfirmationGetter.getConfirmationGetters().isEmpty()) {
            return;
        }

        for (ConfirmationGetter getter : ConfirmationGetter.getConfirmationGetters().values()) {

            //with over 5 attempts we assume no
            if (getter.getAttempts() >= 5) {
                getter.setChannel(event.getChannel());
                getter.getLatch().countDown();
            //has to be the right person of course - we don't add any attempts for the wrong user
            } else if (!(event.getAuthor().getIdLong() == getter.getId())) {
                return;
            }

            //if our other checks passed handle the response
            final String messageContent = event.getMessage().getContentRaw();
            if (messageContent.equals("yes") || messageContent.equals("true") || messageContent.equals("of course")) {
                getter.setResponse(true);
                getter.setChannel(event.getChannel());
                getter.getLatch().countDown();
            } else if (messageContent.equals("false") || messageContent.equals("no") || /* will anyone ever say this? probably not */ messageContent.equals("absolutely not")) {
                getter.setResponse(false);
                getter.setChannel(event.getChannel());
                getter.getLatch().countDown();
            } else {
                getter.addAttempt();
            }
        }
    }
}
