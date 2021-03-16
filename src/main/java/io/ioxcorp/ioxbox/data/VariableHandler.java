package io.ioxcorp.ioxbox.data;

import io.ioxcorp.ioxbox.data.json.JacksonYeehawHelper;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.Map;

/**
 * funni system for making variables and adding to them, implemented for fun
 * @author ioxom
 */
public final class VariableHandler {
    private final Map<String, Integer> variables;

    public VariableHandler() {
        variables = JacksonYeehawHelper.readVariables();
    }

    @Override
    public String toString() {
        StringBuilder yes = new StringBuilder("data:");
        for (String key : variables.keySet()) {
            yes.append("\n").append(key).append(": ").append(variables.get(key));
        }

        return yes.toString();
    }

    public void handle(final String message, final MessageChannel channel) {
        String key = message.split("\\+\\+")[0];
        if (variables.containsKey(key)) {
            variables.put(key, variables.get(key) + 1);
        } else {
            variables.put(key, 1);
        }

        JacksonYeehawHelper.saveVariables();

        channel.sendMessage(key + " == " + variables.get(key)).queue();
    }

    public Map<String, Integer> getVariables() {
        return variables;
    }
}
