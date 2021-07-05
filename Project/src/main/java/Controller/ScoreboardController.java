package Controller;

import Model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ScoreboardController {
    private int yPosition = 230;
    private int counter = 1;

    private Stage stage;
    private Scene scene;
    private AnchorPane root;

    public void makeLabels() {
        Player.sortPlayers();
        ArrayList<Player> players = Player.getPlayers();
        for (int i = 0; i < 20; i++) {
            Label label = new Label();
            label.setPrefWidth(250);
            label.setPrefHeight(35);
            label.setTextFill(Color.valueOf("#ddd78a"));
            label.setLayoutY(yPosition);
            if (i % 4 == 0) label.setLayoutX(15);
            if (i % 4 == 1) label.setLayoutX(315);
            if (i % 4 == 2) label.setLayoutX(615);
            if (i % 4 == 3) label.setLayoutX(915);
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font("Gabriola", 26));
            label.getStylesheets().add(getClass().getResource("/CSS/Main.css").toExternalForm());
            label.getStyleClass().add("text-field");
            if (i % 4 == 3) yPosition += 50;
            if (i < players.size()) {
                Player player = players.get(i);
                label.setText(counter + ". " + player.getNickname() + "     Score: " + player.getScore());
            }
            counter++;
            root.getChildren().add(label);
        }
    }

    public void show(AnchorPane root) {
        this.root = root;
        makeLabels();
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/main_menu_view.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
