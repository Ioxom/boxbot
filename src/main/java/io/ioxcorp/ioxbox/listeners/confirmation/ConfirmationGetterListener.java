package io.ioxcorp.ioxbox.listeners.confirmation;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * runs through the {@link ConfirmationGetter ConfirmationGetters} contained in the static {@code confirmationGetters} {@link java.util.HashMap} contained in the {@link ConfirmationGetter ConfirmationGetter} class and checks if we've gotten confirmation from the referenced users <br>
 * if so, closes that getter and saves the response
 * @author ioxom
 */
public class ConfirmationGetterListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (ConfirmationGetter.confirmationGetters.isEmpty()) return;
        for (ConfirmationGetter confirmationGetter : ConfirmationGetter.confirmationGetters.values()) {

            //with over 5 attempts we assume no
            if (confirmationGetter.attempts >= 5) {
                confirmationGetter.setChannel(event.getChannel());
                confirmationGetter.getLatch().countDown();
            }

            //has to be the right person of course
            if (!(event.getAuthor().getIdLong() == confirmationGetter.getId())) {
                return;
            }

            //if our other checks passed handle the response
            if (event.getMessage().getContentRaw().equals("yes") || event.getMessage().getContentRaw().equals("true")) {
                confirmationGetter.setResponse(true);
                confirmationGetter.setChannel(event.getChannel());
                confirmationGetter.getLatch().countDown();
            } else if (event.getMessage().getContentRaw().equals("false") || event.getMessage().getContentRaw().equals("no")) {
                confirmationGetter.setResponse(false);
                confirmationGetter.setChannel(event.getChannel());
                confirmationGetter.getLatch().countDown();
            } else {
                confirmationGetter.attempts ++;
            }
        }
    }
}