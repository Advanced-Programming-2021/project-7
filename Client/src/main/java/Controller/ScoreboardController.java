package Controller;

import Model.CommonTools;
import Model.Sound;
import View.MainProgramView;
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
import java.util.Arrays;

public class ScoreboardController {
    private int yPosition = 230;
    private int counter = 1;
//    private long time = System.currentTimeMillis();

    private Stage stage;
    private Scene scene;
    private AnchorPane root;

    public void makeLabels() {
        String result = "";
        try {
            CommonTools.dataOutputStream.writeUTF("ScoreboardController#players#");
            CommonTools.dataOutputStream.flush();
            result = CommonTools.dataInputStream.readUTF();
        } catch (Exception e) {

        }
        result = result.replace("[", "").replace("]", "");
        String[] arrays = result.split("#");
        ArrayList<String> playerNicknames = new ArrayList<String>(Arrays.asList(arrays[ 0 ].split(", ")));
        ArrayList<String> activePlayerNicknames = new ArrayList<String>(Arrays.asList(arrays[ 1 ].split(", ")));
        ArrayList<String> playerScores = new ArrayList<String>(Arrays.asList(arrays[ 2 ].split(", ")));
        makeScoreLabels(playerNicknames, activePlayerNicknames, playerScores);
    }

    public void makeScoreLabels(ArrayList<String> playerNicknames, ArrayList<String> activePlayerNicknames, ArrayList<String> playerScores) {
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
            label.getStyleClass().add("score-lab");
            if (i % 4 == 3) yPosition += 50;
            if (i < playerNicknames.size()) {
                if (isPlayerActive(playerNicknames.get(i), activePlayerNicknames)) label.setTextFill(Color.valueOf("#dddddd"));
                label.setText(counter + ". " + playerNicknames.get(i) + "     Score: " + playerScores.get(i));
            }
            counter++;
            root.getChildren().add(label);
        }
        yPosition = 230;
        counter = 1;
    }

    public boolean isPlayerActive(String nickname, ArrayList<String> activePlayerNicknames) {
        for (String activeNickname : activePlayerNicknames) {
            if (nickname.equals(activeNickname)) return true;
        }
        return false;
    }

    public void show(AnchorPane root) {
        this.root = root;
        makeLabels();
    }

    public void refresh(ActionEvent event) throws IOException {
        Sound.getSoundByName("button").playSoundOnce();
        refreshBoard();
    }

    public void refreshBoard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/score_board_view.fxml"));
        root = loader.load();
        show(root);
        stage = MainProgramView.stage;
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void back(ActionEvent event) throws IOException {
        Sound.getSoundByName("button").playSoundOnce();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/main_menu_view.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

//    public void makeRefreshThread() {
//        new Thread(() -> {
//            while (true) {
//                if (time + 2000 < System.currentTimeMillis()) {
//                    time = System.currentTimeMillis();
//                    try {
//                        refreshBoard();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("woww");
//                }
//            }
//        }).start();
//    }
}