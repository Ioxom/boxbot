package io.ioxcorp.ioxbox.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

//yes, this is horrific
//however no one but me is working on this so I get to it when I can
public class ConfirmationGetterListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (ConfirmationGetter.confirmationGetters == null || ConfirmationGetter.confirmationGetters.isEmpty()) return;
        for (ConfirmationGetter confirmationGetter : ConfirmationGetter.confirmationGetters.values()) {
            System.out.println("got message");
            System.out.println("h" + event.getAuthor().getIdLong());
            System.out.println("b" + confirmationGetter.id);

            if (confirmationGetter.attempts > 5) {
                System.out.println("too many attempts");
                confirmationGetter.timedOut = true;
                ConfirmationGetter.channels.put(confirmationGetter.id, event.getChannel());
                confirmationGetter.latch.countDown();
            }

            if (!(event.getAuthor().getIdLong() == confirmationGetter.id)) {
                System.out.println("incorrect author");
                return;
            }

            if (event.getMessage().getContentRaw().equals("yes") || event.getMessage().getContentRaw().equals("true")) {
                System.out.println("got true response");
                confirmationGetter.response = true;
                ConfirmationGetter.channels.put(confirmationGetter.id, event.getChannel());
                confirmationGetter.latch.countDown();
            } else if (event.getMessage().getContentRaw().equals("false") || event.getMessage().getContentRaw().equals("no")) {
                System.out.println("got false response");
                ConfirmationGetter.channels.put(confirmationGetter.id, event.getChannel());
                confirmationGetter.response = false;
                confirmationGetter.latch.countDown();
            } else {
                System.out.println("got bad response");
            }
        }
    }
}
