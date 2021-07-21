package Menus;

import Controller.LoginController;
import Model.Player;
import Model.Sound;
import View.MainProgramView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ChatRoom extends Application {
    public Stage stage;
    private Scene scene;
    private AnchorPane root;
    private String username = Player.getActivePlayer().getUsername();
    private ScrollPane scrollPane;

    public void run() {
        this.stage = MainProgramView.stage;
        this.stage.setResizable(true);
        try {
            start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        LoginController.dataOutputStream.writeUTF("number of online");
        LoginController.dataOutputStream.flush();
        String numberOfOnline = LoginController.dataInputStream.readUTF();
        Text titleText = new Text("number of Online: " + numberOfOnline);
        titleText.setFont(Font.font(40));
        Button back = DeckMenu.getButton();
        back.setText("back");
        VBox title = new VBox(titleText);
        title.setAlignment(Pos.CENTER);
        Background background = getBackground();
        LoginController.initializeNetwork();
        MainProgramView.stage = stage;
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(title);
        HBox hbox = new HBox(back);
        borderPane.setLeft(hbox);
        Button register = DeckMenu.getButton();
        Button refresh = DeckMenu.getButton();
        refresh.setText("refresh");
        register.setText("Register");
        refresh.setFont(Font.font(30));
        register.setMinWidth(200);
        refresh.setMinWidth(200);
        TextField chatBox = DeckMenu.getTextField();
        chatBox.setPromptText("Enter Your Message");
        chatBox.setMinWidth(300);
        Text chats = new Text();
        chats.setFont(Font.font(30));
        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        scrollPane.setContent(vBox);
        scrollPane.setMaxWidth(600);
        scrollPane.setMinWidth(500);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: rgba(86,132,205,0.5);");
        scrollPane.setVvalue(1);
        this.scrollPane = scrollPane;
        VBox chatVbox = new VBox(scrollPane);
        chatVbox.setAlignment(Pos.CENTER);
        LoginController.dataOutputStream.writeUTF("get chats");
        LoginController.dataOutputStream.flush();
        String chatContent = LoginController.dataInputStream.readUTF();
        String[] messages = chatContent.split("\n");
        for (int i = 1; i < messages.length; i++){
            Button mess = getMessageButton(i);
            mess.setText(messages[i]);
            vBox.getChildren().add(mess);
            mess.setMinWidth(600);
        }
        chats.setText(chatContent);
        vBox.setSpacing(5);
        borderPane.setBackground(background);
        register.setOnAction(actionEvent -> {
            try {
                Sound.getSoundByName("button").playSoundOnce();
                LoginController.dataOutputStream.writeUTF("chat#" + username + ": " + chatBox.getText());
                LoginController.dataOutputStream.flush();
                LoginController.dataInputStream.readUTF();
                chatBox.clear();
                scrollPane.setContent(refresh());
                scrollPane.setVvalue(1.5);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        refresh.setOnAction(actionEvent -> {
            try {
                Sound.getSoundByName("button").playSoundOnce();
                LoginController.dataOutputStream.writeUTF("get chats");
                LoginController.dataOutputStream.flush();
                String result = LoginController.dataInputStream.readUTF();
                String[] messages1 = result.split("\n");
                VBox vBox1 = new VBox();
                for (int i = 1; i < messages1.length; i++){
                    Button mess = getMessageButton(i);
                    mess.setText(messages1[i]);
                    vBox1.getChildren().add(mess);
                    mess.setMinWidth(600);
                }
                vBox1.setSpacing(5);
                scrollPane.setContent(vBox1);
                scrollPane.setVvalue(1);
                chatBox.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        back.setOnAction(actionEvent -> {
            Sound.getSoundByName("button").playSoundOnce();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/duel_menu_view.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            scene = new Scene(root);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        });
        VBox regVbox = new VBox(chatBox, register, refresh);
        regVbox.setAlignment(Pos.CENTER);
        borderPane.setBottom(regVbox);
        borderPane.setCenter(chatVbox);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    private Background getBackground() {
        javafx.scene.image.Image image = new Image("\\images\\chatBack.png");
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(1, 1, true, true, false, false));
        Background background = new Background(backgroundImage);
        return background;
    }

    private Button getMessageButton(int i) throws FileNotFoundException {
        Button button = DeckMenu.getButton();
        button.setFont(Font.font(20));
        button.setOnAction(actionEvent -> {
            String[] buttons = {"delete", "edit", "pin", "profile"};
            int returnValue = JOptionPane.showOptionDialog(null, "message options", "message options",
                    JOptionPane.OK_OPTION, 1, null, buttons, buttons[0]);
            try{
                if (returnValue == 3){
                    LoginController.dataOutputStream.writeUTF("profileChat#" + i);
                    LoginController.dataOutputStream.flush();
                    String chatContent = LoginController.dataInputStream.readUTF();
                } else if (returnValue == 2){
                    LoginController.dataOutputStream.writeUTF("pinChat#" + i);
                    LoginController.dataOutputStream.flush();
                    String chatContent = LoginController.dataInputStream.readUTF();
                } else if (returnValue == 1) {
                    String newMessage = JOptionPane.showInputDialog("new message");
                    if (newMessage != null) {
                        LoginController.dataOutputStream.writeUTF("editChat#" + i + "#" + username + ": " + newMessage);
                        LoginController.dataOutputStream.flush();
                        String chatContent = LoginController.dataInputStream.readUTF();
                    }
                } else if (returnValue == 0) {
                    LoginController.dataOutputStream.writeUTF("deleteChat#" + i);
                    LoginController.dataOutputStream.flush();
                    String chatContent = LoginController.dataInputStream.readUTF();
                }
                scrollPane.setContent(refresh());
            } catch (IOException e){
                e.printStackTrace();
            }
        });
        return button;
    }

    private VBox refresh() throws IOException {
        LoginController.dataOutputStream.writeUTF("get chats");
        LoginController.dataOutputStream.flush();
        String result = LoginController.dataInputStream.readUTF();
        String[] messages1 = result.split("\n");
        VBox vBox1 = new VBox();
        for (int i = 1; i < messages1.length; i++){
            Button mess = getMessageButton(i);
            mess.setText(messages1[i]);
            vBox1.getChildren().add(mess);
            mess.setMinWidth(600);
        }
        vBox1.setSpacing(5);
        return vBox1;
    }
}
