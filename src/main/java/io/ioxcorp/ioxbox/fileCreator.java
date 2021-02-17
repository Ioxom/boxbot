package io.ioxcorp.ioxbox;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import io.ioxcorp.ioxbox.frame.LogType;
import static io.ioxcorp.ioxbox.Main.frame;

public class fileCreator {

    public static void main() throws IOException {
        Scanner scanner = new Scanner(System.in);

        File f = new File("testingfile4.txt");

        String path = "C:\\Users\\darby\\Documents\\GitHub\\boxbot\\src\\main\\java\\io\\ioxcorp\\ioxbox\\data\\files\\loggers.txt";


        File file = new File(path);

        if (!file.exists()) {
            file.createNewFile();
            frame.log(LogType.WRTR, "Sucessfully created the log file");
        } else if (file.exists()) {
            frame.log(LogType.INIT, "log file already existed, nothing happened");
        } else {
            frame.log(LogType.ERROR, "An error occured with the writer, nothing should have happened");
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        File f = new File("testingfile4.txt");

        String path = "C:\\Users\\darby\\Documents\\GitHub\\boxbot\\src\\main\\java\\io\\ioxcorp\\ioxbox\\data\\files\\loggers.log";


        File file = new File(path);

        if (!file.exists()) {
            file.createNewFile();
            frame.log(LogType.WRTR, "Sucessfully created the log file");
        } else if (file.exists()) {
            frame.log(LogType.WRTR, "log file already existed, nothing happened");
        } else {
            frame.log(LogType.ERROR, "An error occured with the writer, nothing should have happened");
        }










    }
}
