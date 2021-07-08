package Controller;

import Menus.RockPaperScissors;
import Model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class DuelMenuController {
    public static String firstPlayer = Player.getActivePlayer().getUsername();
    public static String secondPlayer;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void newRound(ActionEvent event) throws Exception {
        if (!getSecondPlayerName()) return;
        RockPaperScissors rockPaperScissors = new RockPaperScissors();
        rockPaperScissors.run(firstPlayer, secondPlayer);
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/game_board.fxml"));
//        root = loader.load();
//        makeStage(event);

    }

    public void newMatch(ActionEvent event) throws Exception {
        if (!getSecondPlayerName()) return;
        RockPaperScissors rockPaperScissors = new RockPaperScissors();
        rockPaperScissors.run(firstPlayer, secondPlayer);
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/game_board.fxml"));
//        root = loader.load();
//        makeStage(event);
    }

    public boolean getSecondPlayerName() {
        secondPlayer = JOptionPane.showInputDialog("Enter your opponent's nickname:");
        return isDuelValid(firstPlayer, secondPlayer);
    }

    private boolean isDuelValid(String player1, String player2){
        if (Player.getPlayerByUsername(player2) == null) {
            JOptionPane.showMessageDialog(null, "there is no player with this username");
            return false;
        }
        if (Player.getActiveDeckByUsername(player1) == null) {
            JOptionPane.showMessageDialog(null, player1 + " has no active deck");
            return false;
        }
        if (Player.getActiveDeckByUsername(player2) == null) {
            JOptionPane.showMessageDialog(null, player2 + " has no active deck");
            return false;
        }
        if (!Player.getActiveDeckByUsername(player1).isDeckValid()) {
            JOptionPane.showMessageDialog(null, player1 + "'s deck is invalid");
            return false;
        }
        if (!Player.getActiveDeckByUsername(player2).isDeckValid()) {
            JOptionPane.showMessageDialog(null, player2 + "'s deck is invalid");
            return false;
        }
        return true;
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/main_menu_view.fxml"));
        root = loader.load();
        makeStage(event);
    }

    public void makeStage(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
