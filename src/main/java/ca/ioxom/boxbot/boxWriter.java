package ca.ioxom.boxbot;

import java.io.FileWriter;
import java.io.IOException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class boxWriter {
    public static void main(String[] args) {
        try {

            FileWriter myWriter = new FileWriter("C:\\Users\\darby\\Documents\\GitHub\\boxbot\\src\\main\\java\\ca\\ioxom\\boxbot\\Files\\boxmoment.txt");
            myWriter.write("b");
            myWriter.close();
            System.out.println("did a yes thank you very much");


        }catch (IOException e) {
            System.out.println("Error\n\ndone happened again");
            e.printStackTrace();
        }
    }
}
