package View;

import Model.CommonTools;
import Model.Player;
import Model.Sound;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainProgramView extends Application {
    public static void run() { launch(); }
    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        MainProgramView.stage = stage;
        AnchorPane root = FXMLLoader.load(getClass().getResource("/FXML/main_program_view.fxml"));
        Scene scene = new Scene(root);
        setStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    public void setStage(Stage stage) {
        Image icon = new Image(getClass().getResource("/Images/logo.png").toExternalForm());
        Sound sound = new Sound("/Sounds/BackMusic.mp3", "mainSound");
        Sound buttonSound = new Sound("/Sounds/buttonSound.wav", "button");
        Sound setSound = new Sound("/Sounds/setCard.mp3", "set");
        Sound attackSound = new Sound("/Sounds/attackSound.mp3", "attack");
        sound.playSound();
        stage.getIcons().add(icon);
        stage.setTitle("Yu-Gi-Oh!");
        stage.setWidth(1200);
        stage.setHeight(735);
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            event.consume();
            exitGame(stage);
        });
    }

    public void exitGame(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Game");
        alert.setHeaderText("You're about to close the game");
        alert.setContentText("Do you want to exit?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            String result = "";
            try {
                CommonTools.dataOutputStream.writeUTF("MainMenuController#logout#" + Player.getToken());
                CommonTools.dataOutputStream.flush();
                result = CommonTools.dataInputStream.readUTF();
            } catch (Exception e) {

            }
            stage.close();
        }
    }
}