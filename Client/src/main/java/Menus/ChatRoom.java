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

import java.io.IOException;

public class ChatRoom extends Application {
    public Stage stage;
    private Scene scene;
    private AnchorPane root;
    private String username = Player.getActivePlayer().getUsername();

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
        Text titleText = new Text("Chat Room");
        titleText.setFont(Font.font(50));
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
        VBox vBox = new VBox(chats);
        vBox.setAlignment(Pos.CENTER);
        scrollPane.setContent(vBox);
        scrollPane.setMaxWidth(600);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: rgba(86,132,205,0.5);");
        scrollPane.setVvalue(1);
        VBox chatVbox = new VBox(scrollPane);
        chatVbox.setAlignment(Pos.CENTER);
        LoginController.dataOutputStream.writeUTF("get chats");
        LoginController.dataOutputStream.flush();
        String chatContent = LoginController.dataInputStream.readUTF();
        chats.setText(chatContent);
        borderPane.setBackground(background);
        register.setOnAction(actionEvent -> {
            try {
                Sound.getSoundByName("button").playSoundOnce();
                LoginController.dataOutputStream.writeUTF("chat#" + username + ": " + chatBox.getText());
                LoginController.dataOutputStream.flush();
                LoginController.dataInputStream.readUTF();
                chatBox.clear();
                LoginController.dataOutputStream.writeUTF("get chats");
                LoginController.dataOutputStream.flush();
                String result = LoginController.dataInputStream.readUTF();
                chats.setText(result);
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
                chats.setText(result);
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
}
