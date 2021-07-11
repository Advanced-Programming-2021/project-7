package Controller;

import Menus.DuelProgramController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CheatMenuController {
    private Stage stage;
    private Scene scene;
    private AnchorPane root;
    private static DuelProgramController duelProgramController;

    @FXML
    private TextField commandField;
    @FXML
    private Label commandLabel;

    public void submit(ActionEvent event) {
        String command = commandField.getText();
        if (command.matches("^increase --LP (\\d+)$")) duelProgramController.increasePlayerLPCheat(command);
        else if (command.matches("^duel set-winner \\S+$")) duelProgramController.setWinnerCheat(command);
        else if (command.matches("^select --hand --force$")) duelProgramController.inHandCardCheat();
        else {
            commandLabel.setText("Cheat code is invalid!");
            return;
        }
        commandLabel.setText("Cheat activated!");
    }

    public static void setDuelProgramController(DuelProgramController duel) {
        duelProgramController = duel;
    }
}
