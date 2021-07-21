package Controller;

import Model.Player;
import Model.Sound;
import View.MainProgramView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
    public static boolean isPlayerInWaitMenu;
    private long time;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public static Socket socket = DuelMenuController.socket;
    public static DataInputStream dataInputStream = DuelMenuController.dataInputStream;
    public static DataOutputStream dataOutputStream = DuelMenuController.dataOutputStream;

    public void cancel(ActionEvent event) throws Exception {
        Sound.getSoundByName("button").playSoundOnce();
        fail();
    }

    public void fail() throws Exception {
        String result = "";
        dataOutputStream.writeUTF("WaitMenu#cancel#" + Player.getActivePlayer().getUsername());
        dataOutputStream.flush();
        result = dataInputStream.readUTF();
        JOptionPane.showMessageDialog(null, result);
        isPlayerInWaitMenu = false;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/duel_menu_view.fxml"));
        root = loader.load();
        stage = MainProgramView.stage;
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        time = System.currentTimeMillis();
        isPlayerInWaitMenu = true;
        new Thread(() -> {
            while (isPlayerInWaitMenu) {
                if (time + 4000 < System.currentTimeMillis()) {
                    time = System.currentTimeMillis();
                    String result = "";
                    try {
                        dataOutputStream.writeUTF("WaitMenu#wait#" + Player.getActivePlayer().getUsername());
                        dataOutputStream.flush();
                        result = dataInputStream.readUTF();
                    } catch (Exception e) {}
                    if (result.equals("your challenge accepted")) {
                        JOptionPane.showMessageDialog(null, result);
                        // rock
                        break;
                    } else if (result.equals("your challenge rejected")) {
                        JOptionPane.showMessageDialog(null, result);
                        try {
                            fail();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }).start();
    }
}
