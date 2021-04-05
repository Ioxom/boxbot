package io.ioxcorp.ioxbox.listeners.confirmation;

import net.dv8tion.jda.api.entities.MessageChannel;

public class Response {
    private final MessageChannel channel;
    private final boolean answer;
    private final boolean gotProperResponse;

    public Response(MessageChannel inputChannel, boolean inputAnswer, boolean properResponse) {
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
