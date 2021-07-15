package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameBoardView extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/game_board.fxml"));
        Scene scene = new Scene(root,1020,800);
        stage.setScene(scene);
        stage.show();
    }

    public void run() {
        launch();
    }

}
