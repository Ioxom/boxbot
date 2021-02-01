package ca.ioxom.boxbot;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Listener extends ListenerAdapter {
    private boolean isCheckingForResponse;
    private User checkedUser;
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        switch (event.getMessage().getContentRaw().toLowerCase()) {
            case "-box yes" :
                event.getChannel().sendMessage("Box is here :package: ").queue();
                break;
            case "-box add" :
                String addToBox = event.getMessage().getContentRaw();
                try {
                    FileWriter myWriter = new FileWriter("C:\\Users\\darby\\Documents\\GitHub\\boxbot\\src\\main\\java\\ca\\ioxom\\boxbot\\Files\\boxmoment.txt");
                    myWriter.write("\n" + Arrays.toString(addToBox.split("-box add")));
                    myWriter.close();



                } catch (IOException e) {
                    e.printStackTrace();
                }


        }
        ObjectMapper mapper = new ObjectMapper().setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        try {
            mapper.writeValue(new File("box_data.json"), new Box(new CustomUser(event.getAuthor()), event.getAuthor()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
