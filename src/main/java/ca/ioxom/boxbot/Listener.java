package ca.ioxom.boxbot;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Listener extends ListenerAdapter {
    private boolean isCheckingForResponse;
    private User checkedUser;
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        ObjectMapper mapper = new ObjectMapper().setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        try {
            mapper.writeValue(new File("box_data.json"), new Box(new CustomUser(event.getAuthor()), event.getAuthor()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
