package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainProgramView extends Application {
    public static void run() { launch(); };

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/FXML/main_program_view.fxml"));
        Scene scene = new Scene(root);
        setStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    public void setStage(Stage stage) {
        Image icon = new Image(getClass().getResource("/Images/logo.png").toExternalForm());
        stage.getIcons().add(icon);
        stage.setTitle("Yu-Gi-Oh!");
        stage.setWidth(1200);
        stage.setHeight(700);
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
            stage.close();
        }
    }
}