package Controller;

import Model.FileHandler;
import Model.Player;
import Model.Sound;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangeNicknameController {
    private Stage stage;
    private Scene scene;
    private AnchorPane root;

    @FXML
    private TextField nicknameField;
    @FXML
    private Label commandLabel;

    public void change(ActionEvent event) throws IOException {
        Sound.getSoundByName("button").playSoundOnce();
        String nickname = nicknameField.getText();
        if (nickname.equals("")) {
            commandLabel.setText("You have not entered your new nickname!");
            return;
        }
        Player.getActivePlayer().setNickName(nickname);
        FileHandler.updatePlayers();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Change Nickname");
        alert.setHeaderText("Nickname changed successfully!");
        alert.showAndWait();
        back(event);
    }

    public void back(ActionEvent event) throws IOException {
        Sound.getSoundByName("button").playSoundOnce();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/profile_menu_view.fxml"));
        root = loader.load();
        ProfileMenuController profileMenuController = loader.getController();
        profileMenuController.show(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
