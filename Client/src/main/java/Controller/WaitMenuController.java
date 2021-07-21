package Controller;

import Model.Player;
import Model.Sound;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class WaitMenuController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public static Socket socket = DuelMenuController.socket;
    public static DataInputStream dataInputStream = DuelMenuController.dataInputStream;
    public static DataOutputStream dataOutputStream = DuelMenuController.dataOutputStream;

    public void cancel(ActionEvent event) throws Exception {
        Sound.getSoundByName("button").playSoundOnce();
        String result = "";
        dataOutputStream.writeUTF("WaitMenu#cancel#" + Player.getActivePlayer().getUsername());
        dataOutputStream.flush();
        result = dataInputStream.readUTF();
        JOptionPane.showMessageDialog(null, result);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/duel_menu_view.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            while (true) {
                String result = "";
                try {
                    dataOutputStream.writeUTF("WaitMenu#wait#" + Player.getActivePlayer().getUsername());
                    dataOutputStream.flush();
                    result = dataInputStream.readUTF();
                } catch (Exception e) {}
                if (result.equals("your challenge accepted")) {
                    JOptionPane.showMessageDialog(null, result);
                    break;
                }
            }
        }).start();
    }
}
