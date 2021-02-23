package io.ioxcorp.ioxbox.frame;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ConsoleScrapEdition extends Application {

    Button btn = new Button("Reload");
    TextArea output = new TextArea("ecks dee");


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Scrap time");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10 ,10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        gridPane.setConstraints(output, 0, 0);



        btn.setOnAction(event -> {
            System.out.println("yesh");
            setUserAgentStylesheet(STYLESHEET_MODENA);
            System.out.println("yesh 2 electric boogalo");

        });


        StackPane layout = new StackPane();
        layout.getChildren().add(btn);



        Scene scene = new Scene(layout, 500, 300);
        scene.getStylesheets().add("scrap.css");

        primaryStage.setScene(scene);
        primaryStage.show();

    }


}
