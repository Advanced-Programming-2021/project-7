package Controller;

import Model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangePasswordController {
    private Stage stage;
    private Scene scene;
    private AnchorPane root;

    @FXML
    private TextField newPasswordTextField;
    @FXML
    private TextField oldPasswordTextField;
    @FXML
    private Label commandLabel;

    public void change(ActionEvent event) throws IOException {
        String newPassword = newPasswordTextField.getText();
        String oldPassword = oldPasswordTextField.getText();
        if (oldPassword.equals("")) {
            commandLabel.setText("You have not entered your old password!");
            return;
        }
        if (newPassword.equals("")) {
            commandLabel.setText("You have not entered your new password!");
            return;
        }
        Player player = Player.getActivePlayer();
        if (!oldPassword.equals(player.getPassword())) {
            commandLabel.setText("Old password is wrong!");
            return;
        }
        player.setPassword(newPassword);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Change Password");
        alert.setHeaderText("Password changed successfully!");
        alert.showAndWait();
        back(event);
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
