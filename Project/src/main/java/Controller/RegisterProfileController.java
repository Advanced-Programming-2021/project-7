package Controller;

import Model.Player;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;


public class RegisterProfileController {
    private HashMap<Rectangle, Integer> profiles = new HashMap<>();
    private int yPosition = 80;
    private int xPosition = 580;
    private int counter = 1;
    private Player createdPlayer;
    private Rectangle mainProfile = new Rectangle();

    private Stage stage;
    private Scene scene;
    private AnchorPane root;

    @FXML
    private Label commandLabel;

    {
        mainProfile.setWidth(350);
        mainProfile.setHeight(350);
        mainProfile.setY(200);
        mainProfile.setX(100);
        mainProfile.setVisible(false);
        mainProfile.setStroke(Color.BLACK);
        mainProfile.setStrokeWidth(2);
        makeMainProfileEffect(mainProfile);
    }

    public void makeProfiles() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Image image = new Image(getClass().
                        getResource("/Images/Profiles/profile" + counter + ".png").toExternalForm());
                Rectangle rectangle = new Rectangle();
                rectangle.setHeight(150);
                rectangle.setWidth(150);
                makeEffect(rectangle);
                makeOnMouseEvent(rectangle);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(1.5);
                rectangle.setY(yPosition);
                rectangle.setX(xPosition);
                rectangle.setCursor(Cursor.HAND);
                rectangle.setFill(new ImagePattern(image));
                root.getChildren().add(rectangle);
                profiles.put(rectangle, counter);
                xPosition += 200;
                counter++;
            }
            xPosition = 580;
            yPosition += 200;
        }
    }

    public void makeEffect(Rectangle rectangle) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.YELLOW);
        dropShadow.setSpread(1);
        dropShadow.setRadius(1);
        dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
        rectangle.setEffect(dropShadow);
    }

    public void makeOnMouseEnteredEffect(Rectangle rectangle) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GRAY);
        dropShadow.setSpread(1);
        dropShadow.setRadius(1);
        dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
        rectangle.setEffect(dropShadow);
    }

    public void makeMainProfileEffect(Rectangle rectangle) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.WHITE);
        dropShadow.setSpread(1);
        dropShadow.setRadius(1);
        dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
        rectangle.setEffect(dropShadow);
    }

    public void makeOnMouseEvent(Rectangle rectangle) {
        rectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                makeOnMouseEnteredEffect(rectangle);
            }
        });
        rectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                makeEffect(rectangle);
            }
        });
        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mainProfile.setVisible(true);
                TranslateTransition transition = new TranslateTransition();
                transition.setNode(rectangle);
                transition.setDuration(Duration.millis(70));
                transition.setCycleCount(2);
                transition.setByY(7);
                transition.setAutoReverse(true);
                transition.play();
                counter = profiles.get(rectangle);
                mainProfile.setFill(new ImagePattern(new Image(
                        getClass().getResource("/Images/Profiles/profile" + counter + ".png").toExternalForm())));
            }
        });
    }


    public void show(AnchorPane root, Player player) {
        this.root = root;
        this.createdPlayer = player;
        root.getChildren().add(mainProfile);
        makeProfiles();
    }

    public void select(ActionEvent event) throws IOException {
        if (mainProfile.isVisible() == false) {
            commandLabel.setText("You have not selected any picture!");
            return;
        }
        commandLabel.setText("User created successfully!");
        createdPlayer.setProfile(counter);
        alertUserCreated();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/main_program_view.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void alertUserCreated() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Register");
        alert.setHeaderText("User created successfully!");
        alert.showAndWait();
    }
}
