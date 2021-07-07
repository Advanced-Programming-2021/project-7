package Controller;

import Model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
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
        Integer counter = Player.getActivePlayer().getProfile();
        if (counter == 0) counter = 1;
        Image image = new Image(getClass().
                getResource("/Images/Profiles/profile" + counter + ".png").toExternalForm());
        Rectangle mainProfile = new Rectangle();
        mainProfile.setWidth(300);
        mainProfile.setHeight(300);
        mainProfile.setY(30);
        mainProfile.setX(60);
        mainProfile.setStroke(Color.BLACK);
        mainProfile.setStrokeWidth(2);
        makeMainProfileEffect(mainProfile);
        mainProfile.setFill(new ImagePattern(image));
        root.getChildren().add(mainProfile);
    }

    public void show(AnchorPane root) {
        this.root = root;
        makeLabels();
    }

    public void changePassword(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/change_password_view.fxml"));
        root = loader.load();
        makeStage(event);
    }

    public void changeNickname(ActionEvent event) {

    }

    public void changeProfile(ActionEvent event) {

    }

    public void makeMainProfileEffect(Rectangle rectangle) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.YELLOW);
        dropShadow.setSpread(1);
        dropShadow.setRadius(1);
        dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
        rectangle.setEffect(dropShadow);
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/main_menu_view.fxml"));
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
