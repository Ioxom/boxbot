package io.ioxcorp.ioxbox.frame;

import io.ioxcorp.ioxbox.Main;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;

public class ConsoleCrapEdition extends Application {
    public static void main(String[] args) {
        launch();
    }

    TextField commandTextField = new TextField();
    Button reloadButton = new Button();
    Color backgroundColor = Color.rgb(0, 0, 51);

    @Override
    public void start(Stage primaryStage) {


        BorderPane pane = new BorderPane();
        pane.setPrefSize(1920, 1017);


        reloadButton.setScaleX(3);
        reloadButton.setScaleY(3);
        commandTextField.setText("give me your demands here UwU");


        pane.setBottom(commandTextField);
        pane.setRight(reloadButton);

        InputStream stream = Main.class.getClassLoader().getResourceAsStream("images/gronk.png");
        assert stream != null;
        Image image = new Image(stream);
        //Creating the image view
        ImageView imageView = new ImageView();
        //Setting image to the image view
        imageView.setImage(image);
        reloadButton.setGraphic(imageView);

        Group group = new Group(pane, imageView);

        Scene scene = new Scene(group, 300, 300);
        scene.getStylesheets().add("console.css");
        pane.setStyle("console.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle(System.getProperty("javafx.version"));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
