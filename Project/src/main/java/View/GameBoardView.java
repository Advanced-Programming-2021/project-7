package View;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameBoardView extends Application {
    @FXML
    public GridPane enemyGrid;
    public GridPane myGrid;
    public ImageView selectedCardShow;

    @FXML
    public void initialize(){
        selectedCardShow.setImage(new Image(getClass().getResource("/Images/Cards/Unknown.jpg").toExternalForm()));
        enemyGrid.setHgap(30);
        enemyGrid.setVgap(25);
        myGrid.setHgap(30);
        myGrid.setVgap(25);
        for (int i = 0; i < 5; i++) {
            for (int i1 = 0; i1 < 2; i1++) {
                Rectangle rectangle = new Rectangle(60,90);
                rectangle.setFill(Color.BLACK);
                Rectangle rectangle1 = new Rectangle(60,90);
                rectangle.setFill(Color.RED);
                enemyGrid.add(rectangle,i, i1);
                myGrid.add(rectangle1,i, i1);
            }
        }
//        new DuelProgramController().run(DuelMenuController.firstPlayer, DuelMenuController.secondPlayer, 1);
    }

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
