package io.ioxcorp.ioxbox.listeners;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ConfirmationGetter extends ListenerAdapter implements Runnable {
    // CountDownLatch latch = new CountDownLatch(1);
    // ConfirmationGetter g = new ConfirmationGetter(latch, 6L);
    // new Thread(g).start();
    //
    // should be used like this

    private final CountDownLatch latch;
    private final long id;
    private boolean response;
    private int attempts;
    private MessageChannel channel;
    private boolean timedOut;

    public ConfirmationGetter(CountDownLatch latch, long id) {
        this.id = id;
        this.latch = latch;
        this.attempts = 0;
    }

    public static ArrayList<Boolean> booleans;
    private static int pos;

    public static boolean crab(long id) {
        if (booleans == null) {
            pos = 0;
            booleans = new ArrayList<>();
        } else {
            pos = booleans.size();
        }
        booleans.add(pos, false);

        CountDownLatch crabDownLatch = new CountDownLatch(1);
        ConfirmationGetter confirmationGetter = new ConfirmationGetter(crabDownLatch, id);
        new Thread(confirmationGetter).start();

        return booleans.get(pos);
    }

    @Override
    public void run() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        if (this.timedOut) {
            this.channel.sendMessage("no proper response received, assuming no").queue();
            booleans.add(pos, false);
        } else {
            booleans.add(pos, response);
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (this.attempts > 5) {
            this.response = false;
            this.channel = event.getChannel();
            this.latch.countDown();
        }

        if (!(event.getAuthor().getIdLong() == id)) return;

        if (event.getMessage().getContentRaw().equals("yes") || event.getMessage().getContentRaw().equals("true")) {
            this.response = true;
            this.channel = event.getChannel();
            this.latch.countDown();
        } else if (event.getMessage().getContentRaw().equals("false") || event.getMessage().getContentRaw().equals("no")) {
            this.attempts ++;
        }
    }


}
