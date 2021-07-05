package Controller;

import Model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField nicknameTextField;
    @FXML
    private Label commandLabel;

    private Stage stage;
    private Scene scene;
    private AnchorPane root;

    public void registerAction(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String nickname = nicknameTextField.getText();
        if (username.equals("")) {
            commandLabel.setText("You have not entered your username!");
            return;
        }
        if (password.equals("")) {
            commandLabel.setText("You have not entered your password!");
            return;
        }
        if (nickname.equals("")) {
            commandLabel.setText("You have not entered your nickname!");
            return;
        }
        if (Player.getPlayerByUsername(username) != null) {
            commandLabel.setText("A user exists with this username!");
            return;
        }
        Player player = new Player(username, password, nickname);
        commandLabel.setText("User created successfully!");
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/main_program_view.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}