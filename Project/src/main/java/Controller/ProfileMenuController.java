package Controller;

import Model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileMenuController {
    private Stage stage;
    private Scene scene;
    private AnchorPane root;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label nicknameLabel;

    public void makeLabels() {
        usernameLabel.setText("Username: " + Player.getActivePlayer().getUsername());
        nicknameLabel.setText("Nickname: " + Player.getActivePlayer().getNickname());
    }

    public void show(AnchorPane root) {
        this.root = root;
        makeLabels();
    }

    public void changePassword(ActionEvent event) {

    }

    public void changeNickname(ActionEvent event) {

    }

    public void changeProfile(ActionEvent event) {

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
