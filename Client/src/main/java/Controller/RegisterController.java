package Controller;

import Model.Player;
import Model.Sound;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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

    private static Socket socket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    public static void initializeNetwork() {
        try {
            socket = new Socket("localhost", 7755);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerAction(ActionEvent event) throws IOException {
        Sound.getSoundByName("button").playSoundOnce();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String nickname = nicknameTextField.getText();
        String result = "wow";
        try {
            dataOutputStream.writeUTF("RegisterController#register#" + username + "#" + nickname + "#" + password);
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
            System.out.println(result);
        } catch (Exception e) {

        }
        commandLabel.setText(result);
        if (!result.equals("User created successfully!")) return;
        Player player = new Player(username, password, nickname);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/register_profile_view.fxml"));
        root = loader.load();
        RegisterProfileController registerProfileController = loader.getController();
        registerProfileController.show(root, player);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void back(ActionEvent event) throws IOException {
        Sound.getSoundByName("button").playSoundOnce();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/main_program_view.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}