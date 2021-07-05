package Controller;

import Model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label commandLabel;

    private Stage stage;
    private Scene scene;
    private AnchorPane root;

    public void signupAction(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if (username.equals("")) {
            commandLabel.setText("You have not entered your username!");
            return;
        }
        if (password.equals("")) {
            commandLabel.setText("You have not entered your password!");
            return;
        }
        if (Player.getPlayerByUsername(username) != null) {
            commandLabel.setText("A user exists with this username!");
            return;
        }
        Player player = new Player(username, password, "h");
        commandLabel.setText("Account successfully created!");
    }

    public void loginAction(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if (username.equals("")) {
            commandLabel.setText("You have not entered your username!");
            return;
        }
        if (password.equals("")) {
            commandLabel.setText("You have not entered your password!");
            return;
        }
        Player player = Player.getPlayerByUsername(username);
        if (player == null) {
            commandLabel.setText("No user exists with this username!");
            return;
        }
        if (!password.equals(player.getPassword())) {
            commandLabel.setText("Password is wrong!");
            return;
        }
//        Player.setActiveUser(player);
        commandLabel.setText("Login was successful!");
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