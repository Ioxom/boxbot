package ca.ioxom.boxbot;

import java.io.FileWriter;
import java.io.IOException;

public class boxWriter {
    public static void main(String[] args) {
        try {

            FileWriter myWriter = new FileWriter("C:\\Users\\darby\\Documents\\GitHub\\boxbot\\src\\main\\java\\ca\\ioxom\\boxbot\\Files\\boxmoment.txt");
            myWriter.write("Lots of fun");
            myWriter.close();
            System.out.println("did a yes thank you very much");


        }catch (IOException e) {
            System.out.println("Error\n\ndone happened again");
            e.printStackTrace();
        }
    }
}
