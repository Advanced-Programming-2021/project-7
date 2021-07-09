package Controller;

import Model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class DuelMenuController {
    private String firstPlayer = Player.getActivePlayer().getUsername();
    private String secondPlayer;

    private Stage stage;
    private Scene scene;
    private AnchorPane root;

    public void newRound(ActionEvent event) {
        getSecondPlayerName();

    }

    public void newMatch(ActionEvent event) {
        getSecondPlayerName();
    }

    public void getSecondPlayerName() {
        secondPlayer = JOptionPane.showInputDialog("Enter your opponent's nickname:");
        isDuelValid(firstPlayer, secondPlayer);
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
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
