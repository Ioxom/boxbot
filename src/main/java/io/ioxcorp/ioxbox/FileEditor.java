package io.ioxcorp.ioxbox;

import io.ioxcorp.ioxbox.frame.LogType;
import static io.ioxcorp.ioxbox.Main.*;
import static io.ioxcorp.ioxbox.frame.Frame.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


import javax.swing.*;
import java.io.*;
import java.util.Scanner;

public class FileEditor {
    public static FileWriter myWriter;

    static {
        try {
            myWriter = new FileWriter("C:\\Users\\darby\\Documents\\GitHub\\boxbot\\src\\main\\java\\io\\ioxcorp\\ioxbox\\data\\files\\loggers.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main() throws IOException {

            FileWriter myWriter = new FileWriter("C:\\Users\\darby\\Documents\\GitHub\\boxbot\\src\\main\\java\\io\\ioxcorp\\ioxbox\\data\\files\\loggers.txt", true);
            myWriter.write("e");
            myWriter.close();
            frame.log(LogType.WRTR, "Succesfully wrote command to file");




    }
}

