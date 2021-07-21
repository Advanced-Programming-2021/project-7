package Controller;

import Model.Sound;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class WaitMenuController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public static Socket socket = DuelMenuController.socket;
    public static DataInputStream dataInputStream = DuelMenuController.dataInputStream;
    public static DataOutputStream dataOutputStream = DuelMenuController.dataOutputStream;

    public void cancel(ActionEvent event) {
        Sound.getSoundByName("button").playSoundOnce();
    }
}
