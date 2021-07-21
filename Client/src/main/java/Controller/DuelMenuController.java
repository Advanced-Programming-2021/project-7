package Controller;

import Menus.ChatRoom;
import Menus.DuelProgramController;
import Model.Player;
import Model.Sound;
import View.MainProgramView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class DuelMenuController implements Initializable {
    public static String firstPlayer = Player.getActivePlayer().getUsername();
    public static String secondPlayer;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public static Socket socket;
    public static DataInputStream dataInputStream;
    public static DataOutputStream dataOutputStream;

    public void newRound(ActionEvent event) throws Exception {
        Sound.getSoundByName("button").playSoundOnce();
        secondPlayer = JOptionPane.showInputDialog("Enter your opponent's username:");
        if (secondPlayer == null) return;
        String result = "";
        try {
            dataOutputStream.writeUTF("Lobby#newRound#" + firstPlayer + "#" + secondPlayer);
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (Exception e) {

        }
        if (result.equals("everything ok")) System.out.println("salam");
        else JOptionPane.showMessageDialog(null, result);
//        DuelProgramController.round = 1;
//        RockPaperScissors rockPaperScissors = new RockPaperScissors();
//        rockPaperScissors.run(firstPlayer, secondPlayer);
    }

    public void newMatch(ActionEvent event) throws Exception {
        Sound.getSoundByName("button").playSoundOnce();
        secondPlayer = JOptionPane.showInputDialog("Enter your opponent's username:");
        if (secondPlayer == null) return;
        String result = "";
        try {
            dataOutputStream.writeUTF("Lobby#newMatch#" + firstPlayer + "#" + secondPlayer);
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (Exception e) {

        }
        if (result.equals("everything ok")) System.out.println("salam");
        else JOptionPane.showMessageDialog(null, result);
//        DuelProgramController.round = 3;
//        RockPaperScissors rockPaperScissors = new RockPaperScissors();
//        rockPaperScissors.run(firstPlayer, secondPlayer);
    }

    public void startChatroom(ActionEvent event) {
        Sound.getSoundByName("button").playSoundOnce();
        ChatRoom chatRoom = new ChatRoom();
        try {
            chatRoom.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void battleWithAi(ActionEvent event) throws IOException {
        Sound.getSoundByName("button").playSoundOnce();
        String player1 = Player.getActivePlayer().getUsername();
        if (Player.getActiveDeckByUsername(player1) == null) {
            JOptionPane.showMessageDialog(null, player1 + " has no active deck");
            return;
        }
        if (!Player.getActiveDeckByUsername(player1).isDeckValid()) {
            JOptionPane.showMessageDialog(null, player1 + "'s deck is invalid");
            return;
        }
        DuelProgramController.firstPlayer = player1;
        DuelProgramController.secondPlayer = "ai";
        root = FXMLLoader.load(getClass().getResource("/FXML/game_board.fxml"));
        stage = MainProgramView.stage;
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.setWidth(1020);
        stage.setHeight(820);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public void back(ActionEvent event) throws IOException {
        Sound.getSoundByName("button").playSoundOnce();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
}
