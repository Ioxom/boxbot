package io.ioxcorp.ioxbox.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

//TODO: 0.3.0: implement confirmation dialogue that doesn't break the main thread
public class ConfirmationDialogListener extends ListenerAdapter {
    boolean awaitingConfirmation = false;
    public  void onMessageRecieved(@NotNull MessageReceivedEvent event) {

    }
    //plan:
    //make this class use its own dedicated thread so it can properly sleep while awaiting confirmation
    //use a private boolean object that saves whether we're awaiting confirmation
    //also save the user id so we know who to accept messages from
    //use a MessageReceivedEvent that only triggers anything if said boolean variable is true
    //if we get "yes" or "true" from the user being asked to confirm tell the place asking "true"
    //if we get "no" or "false" from the user being asked to confirm tell the place asking "false"
    //we're going to need some sort of await() method that makes the thread sleep until a condition is true
}
