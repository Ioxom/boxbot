package io.ioxcorp.ioxbox.listeners.confirmation;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ConfirmationGetterListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (ConfirmationGetter.confirmationGetters.isEmpty()) return;
        for (ConfirmationGetter confirmationGetter : ConfirmationGetter.confirmationGetters.values()) {

            //with over 5 attempts we assume no
            if (confirmationGetter.attempts > 5) {
                confirmationGetter.timedOut = true;
                ConfirmationGetter.channels.put(confirmationGetter.id, event.getChannel());
                confirmationGetter.latch.countDown();
            }

            //has to be the right person of course
            if (!(event.getAuthor().getIdLong() == confirmationGetter.id)) {
                return;
            }

            if (event.getMessage().getContentRaw().equals("yes") || event.getMessage().getContentRaw().equals("true")) {
                confirmationGetter.response = true;
                ConfirmationGetter.channels.put(confirmationGetter.id, event.getChannel());
                confirmationGetter.latch.countDown();
            } else if (event.getMessage().getContentRaw().equals("false") || event.getMessage().getContentRaw().equals("no")) {
                confirmationGetter.response = false;
                ConfirmationGetter.channels.put(confirmationGetter.id, event.getChannel());
                confirmationGetter.latch.countDown();
            } else {
                confirmationGetter.attempts ++;
            }
        }
    }
}