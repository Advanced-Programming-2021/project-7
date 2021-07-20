package Controller;

import Model.Player;
import Model.Sound;
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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

    public static Socket socket;
    public static DataInputStream dataInputStream;
    public static DataOutputStream dataOutputStream;

    public static void initializeNetwork() {
        try {
            socket = new Socket("localhost", 7755);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Network");
            alert.setHeaderText("You have not connected to any server!");
            alert.showAndWait();
        }
    }

    public void loginAction(ActionEvent event) throws IOException {
        Sound.getSoundByName("button").playSoundOnce();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String result = "";
        try {
            dataOutputStream.writeUTF("LoginController#login#" + username + "#" + password);
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
            System.out.println(result);
        } catch (Exception e) {

        }
        commandLabel.setText(result);
        if (!result.equals("Login was successful!")) return;
        Player.setActivePlayer(Player.getPlayerByUsername(username));
        loadMainMenu(event);
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

    public void loadMainMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/main_menu_view.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
