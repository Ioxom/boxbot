package io.ioxcorp.ioxbox.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import static io.ioxcorp.ioxbox.listeners.ConfirmationGetter.confirmationGetter;

public class ConfirmationGetterListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        System.out.println("got message");
        System.out.println("h" + event.getAuthor().getIdLong());
        System.out.println("b" + confirmationGetter.id);

        if (confirmationGetter.attempts > 5) {
            System.out.println("too many attempts");
            confirmationGetter.timedOut = true;
            ConfirmationGetter.channel = event.getChannel();
            confirmationGetter.latch.countDown();
        }

        if (!(event.getAuthor().getIdLong() == confirmationGetter.id)) {
            System.out.println("incorrect author");
            return;
        }

        if (event.getMessage().getContentRaw().equals("yes") || event.getMessage().getContentRaw().equals("true")) {
            System.out.println("got true response");
            confirmationGetter.response = true;
            ConfirmationGetter.channel = event.getChannel();
            confirmationGetter.latch.countDown();
        } else if (event.getMessage().getContentRaw().equals("false") || event.getMessage().getContentRaw().equals("no")) {
            System.out.println("got false response");
            ConfirmationGetter.channel = event.getChannel();
            confirmationGetter.response = false;
            confirmationGetter.latch.countDown();
        } else {
            System.out.println("got bad response");
        }
    }
}
