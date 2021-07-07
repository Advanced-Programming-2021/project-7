package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangePasswordController {
    private Stage stage;
    private Scene scene;
    private AnchorPane root;

    public void change(ActionEvent event) {

    }

    public void back(ActionEvent event) throws IOException {
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
