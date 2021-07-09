package Menus;

import Model.Cards.Card;
import View.CardView;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GraveYardController extends Application {
    public static ArrayList<Card> graveYard;
    public GridPane gridPane;
    public ScrollPane scroll;

    @FXML
    public void initialize(){
        CardView.init();
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        gridPane.setHgap(30);
        gridPane.setVgap(30);

        gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
        gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        gridPane.setMaxWidth(Region.USE_PREF_SIZE);

        gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
        gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridPane.setMaxHeight(Region.USE_PREF_SIZE);
        for (int i = 0; i < 2; i++) {
            for (int i1 = 0; i1 < 10; i1++) {
                Rectangle rectangle = new Rectangle(240, 360);
                rectangle.setFill(new ImagePattern(CardView.getCardViewByName("Trap Hole").imageView.getImage()));
                gridPane.add(rectangle, i, i1);
            }
        }

    }


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/grave_yard.fxml"));
        Scene scene = new Scene(root, 600, 600);
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.close();
            }
        });
        stage.setScene(scene);
        stage.show();
    }
}
