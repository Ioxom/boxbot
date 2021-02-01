package ca.ioxom.boxbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class boxReader {
    public static void main() {
        try {
            File boxContentFile = new File("C:\\Users\\darby\\Documents\\GitHub\\boxbot\\src\\main\\java\\ca\\ioxom\\boxbot\\Files\\boxmoment.txt");
            Scanner myReader = new Scanner(boxContentFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            boxReader.main();
            myReader.close();
        }catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try {
            File boxContentFile = new File("C:\\Users\\darby\\Documents\\GitHub\\boxbot\\src\\main\\java\\ca\\ioxom\\boxbot\\Files\\boxmoment.txt");
            Scanner myReader = new Scanner(boxContentFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        }catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }

}
