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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

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

        int column = 0;
        int row = 0;

        for (int i = 0; i < graveYard.size(); i++) {
            if (column == 2){
                row++;
                column = 0;
            }

            Rectangle rectangle = new Rectangle(240, 360);
            ImageView imageView = new ImageView();
            rectangle.setFill(new ImagePattern(Objects.requireNonNull(CardView.getCardViewByName(graveYard.get(i).getName())).imageView.getImage()));
            gridPane.add(rectangle, column++, row);

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
