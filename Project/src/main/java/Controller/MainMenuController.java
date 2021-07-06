package Controller;

import Menus.Shop;
import Model.Player;
import View.CardView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    private Stage stage;
    private Scene scene;
    private AnchorPane root;

    public void scoreboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/score_board_view.fxml"));
        root = loader.load();
        ScoreboardController scoreboardController = loader.getController();
        scoreboardController.show(root);
        makeStage(event);
    }

    public void shop(ActionEvent event) throws IOException {
        Shop shop = new Shop();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        shop.run(Player.getActivePlayer().getUsername(), stage);
    }

    public void logout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/main_program_view.fxml"));
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
