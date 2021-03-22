package io.ioxcorp.ioxbox.listeners.confirmation;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.frame.logging.LogType;
import io.ioxcorp.ioxbox.helpers.EmbedHelper;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * a class used to get confirmation from a user in discord
 * @author ioxom
 */
public final class ConfirmationGetter extends ListenerAdapter {

    /**
     * note: if any bugs result from using a {@link ThreadPoolExecutor} we can switch back to new threads every time
     */
    public static final ThreadPoolExecutor EXECUTOR = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    private final CountDownLatch latch;
    private final long id;
    private boolean response;
    private int attempts;
    private MessageChannel channel;

    /**
     * creates a {@link ConfirmationGetter} for the specified user with the specified lock
     * @param latch a {@link CountDownLatch} that will keep the thread locked until we have confirmation
     * @param id the id of the user we want confirmation from
     */
    public ConfirmationGetter(final CountDownLatch latch, final long id, final MessageChannel channel) {
        this.id = id;
        this.latch = latch;
        this.attempts = 0;
        this.channel = channel;
        CONFIRMATION_GETTERS.put(id, this);
    }

    public static final Map<Long, ConfirmationGetter> CONFIRMATION_GETTERS = new HashMap<>();

    /**
     * convenience method to check if we're getting confirmation from a user
     * @param id the id of the user we want to check if we're getting confirmation from
     * @return whether or not we're getting confirmation from the user
     */
    public static boolean gettingConfirmationFrom(final long id) {
        return CONFIRMATION_GETTERS.containsKey(id);
    }

    /**
     * gets confirmation from a user<br>
     * warning: blocks the {@link Thread} it's running on until confirmation is given - proceed with caution
     * @param id the id of the user we want confirmation from
     * @return a {@link Pair} containing the response as the right and the channel it was sent in as the left
     */
    public static Pair<MessageChannel, Boolean> crab(final long id, final MessageChannel initialChannel) {
        //safeguard: if we're already getting confirmation from someone we can't do multiple instances at the same time
        //ideally this has already been checked for
        if (gettingConfirmationFrom(id)) {
            return null;
        }

        final EmbedHelper helper = new EmbedHelper(new CustomUser(id));

        //create a ConfirmationGetter to handle it
        final CountDownLatch crabDownLatch = new CountDownLatch(1);
        final ConfirmationGetter confirmationGetter = new ConfirmationGetter(crabDownLatch, id, initialChannel);

        try {
            //ensure that we've gotten our confirmation before returning a response
            crabDownLatch.await();
            boolean response;

            //if there were more than five messages from the user we assume they won't answer
            if (confirmationGetter.attempts >= 5) {
                confirmationGetter.channel.sendMessage(helper.errorEmbed("no proper response received, assuming no")).queue();
                response = false;
            //otherwise they've answered and we take that
            } else {
                response = confirmationGetter.response;
            }

            return Pair.of(confirmationGetter.channel, response);
        } catch (InterruptedException ie) {
            try {
                confirmationGetter.channel.sendMessage(helper.errorEmbed("`an InterruptedException occurred while waiting for response: " + ie + "`" + "\naborting and assuming no")).queue();
                return Pair.of(confirmationGetter.channel, false);
            } finally {
                Thread.currentThread().interrupt();
            }
        } finally {
            //ensure that we remove references of the id from our HashMaps so we can check from this user again
            confirmationGetter.clean();
        }
    }

    /**
     * method to remove references of a user from {@link ConfirmationGetter}'s static hash maps<br>
     * this normally runs after getting confirmation from that user<br>
     * this isn't necessary after the cleanup but yes
     */
    public void clean() {
        CONFIRMATION_GETTERS.remove(id);
    }

    public CountDownLatch getLatch() {
        return this.latch;
    }

    public long getId() {
        return this.id;
    }

    public void setResponse(final boolean response) {
        this.response = response;
    }

    public void setChannel(final MessageChannel channel) {
        this.channel = channel;
    }

    public int getAttempts() {
        return this.attempts;
    }

    public void addAttempt() {
        this.attempts++;
    }

    public MessageChannel getChannel() {
        return this.channel;
    }

    /**
     * clears all confirmation getters, informing users that they're no longer active
     */
    public static void shutdown() {
        Main.FRAME.log(LogType.MAIN, "shutting down confirmation system");
        for (final ConfirmationGetter confirmationGetter : ConfirmationGetter.CONFIRMATION_GETTERS.values()) {
            confirmationGetter.getChannel().sendMessage(new EmbedHelper(new CustomUser(confirmationGetter.getId())).errorEmbed("confirmation getter closed due to JDA shutdown. ask again once this bot is back online!")).queue();
            ConfirmationGetter.CONFIRMATION_GETTERS.remove(confirmationGetter.getId());
        }
    }
}
