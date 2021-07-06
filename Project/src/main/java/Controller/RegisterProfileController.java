package Controller;

import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;


public class RegisterProfileController {
    private ArrayList<Rectangle> profiles = new ArrayList<>();
    private int yPosition = 80;
    private int xPosition = 580;
    private int counter = 1;

    private Stage stage;
    private Scene scene;
    private AnchorPane root;

    public void makeProfiles() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Image image = new Image(getClass().
                        getResource("/Images/Profiles/profile" + counter + ".png").toExternalForm());
                Rectangle rectangle = new Rectangle();
                rectangle.setHeight(150);
                rectangle.setWidth(150);
                makeEffect(rectangle);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(1.5);
                rectangle.setY(yPosition);
                rectangle.setX(xPosition);
                rectangle.setFill(new ImagePattern(image));
                root.getChildren().add(rectangle);
                profiles.add(rectangle);
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

    public void show(AnchorPane root) {
        this.root = root;
        makeProfiles();
    }
}
