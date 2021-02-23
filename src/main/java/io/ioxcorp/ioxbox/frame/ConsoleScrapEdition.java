package io.ioxcorp.ioxbox.frame;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ConsoleScrapEdition extends Application {

    Button btn = new Button("Reload");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Scrap time");

        btn.setOnAction(event -> System.out.println("fun"));

        StackPane layout = new StackPane();
        layout.getChildren().add(btn);

        Scene scene = new Scene(layout, 500, 300);
        primaryStage.setScene(scene);

        primaryStage.show();

    }


}
