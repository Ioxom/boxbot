package io.ioxcorp.ioxbox.listeners.confirmation;

import net.dv8tion.jda.api.entities.MessageChannel;

public final class Response {
    private final MessageChannel channel;
    private final boolean answer;
    private final boolean gotProperResponse;

    public Response(final MessageChannel inputChannel, final boolean inputAnswer, final boolean properResponse) {
        channel = inputChannel;
        answer = inputAnswer;
        gotProperResponse = properResponse;
    }

    public boolean getAnswer() {
        return answer;
    }

    public boolean gotProperResponse() {
        return gotProperResponse;
    }

    public MessageChannel getChannel() {
        return channel;
    }
}
