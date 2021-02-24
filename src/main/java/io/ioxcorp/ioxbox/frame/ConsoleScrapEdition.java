package io.ioxcorp.ioxbox.frame;

import io.ioxcorp.ioxbox.Main;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.InputStream;

public class ConsoleScrapEdition extends Application {

    Button btn = new Button();
    TextField input = new TextField();
    Rectangle btnGraphic = new Rectangle();



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Scrap time");

        input.setPromptText("give me your demands daddy UwU");
        input.setPrefWidth(2000);

        InputStream stream = Main.class.getClassLoader().getResourceAsStream("images/gronk.png");
        assert stream != null;
        Image image = new Image(stream);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        btn.setGraphic(imageView);


        btn.setScaleX(0.7);
        btn.setScaleY(0.7);


        GridPane gridPane = new GridPane();
        //gridPane.setPadding(new Insets(10, 10, 10 ,10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        GridPane.setConstraints(btn, 5, 5);
        btn.setTranslateX(23);
        btn.setTranslateY(-40);

        GridPane.setConstraints(input, 0, 30);
        gridPane.getChildren().add(btn);
//        gridPane.getChildren().add(rect);
        gridPane.getChildren().add(input);


        btn.setStyle
                (
                        "-fx-font-size: 24px;"

                                + "-fx-background-color: grey;"
                                + "-fx-border-style: solid inside;"
                                + "-fx-border-width: 6;"
                                + "-fx-border-color: grey;"


                );





        btn.setOnAction(event -> {
            System.out.println("yesh");
            setUserAgentStylesheet(STYLESHEET_MODENA);
            System.out.println("yesh 2 electric boogalo");

        });






        Scene scene = new Scene(gridPane, 500, 300);
        scene.getStylesheets().add("scrap.css");

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }


}
