package io.ioxcorp.ioxbox.frame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class consoleCrapEdition extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    TextField commandTextField = new TextField();

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane pane = new BorderPane();
        pane.setPrefSize(300, 300);
        pane.setBottom(commandTextField);
        Scene scene = new Scene(pane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("IoxBot and smt else should go here");
        primaryStage.show();



    }
}
