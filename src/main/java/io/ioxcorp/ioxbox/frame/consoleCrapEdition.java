package io.ioxcorp.ioxbox.frame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class consoleCrapEdition extends Application {
    public static void main(String[] args) {
        launch();
    }

    TextField commandTextField = new TextField();

    @Override
    public void start(Stage primaryStage) {
        BorderPane pane = new BorderPane();
        pane.setPrefSize(300, 300);
        pane.setBottom(commandTextField);
        Scene scene = new Scene(pane);

        primaryStage.setScene(scene);
        primaryStage.setTitle(System.getProperty("javafx.version"));
        primaryStage.show();

        //I'm gnot a gnoblin
        //I'm gnot a gnome
        //I'm a g ronk
        //and you've been
        //gronked!!

        //share this will all your friends to gronk them
    }
}
