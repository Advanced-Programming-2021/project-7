package Controller;

import Model.Cards.Card;
import Model.Player;
import View.CardView;
import View.ItemController;
import View.MainProgramView;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

import static Controller.Admin.isBanned;

public class Shop extends Application {

    private static String username;
    public Label Money;
    public GridPane gridPane;
    public ScrollPane scroll;
    public CardView card;
    public ImageView selectedImage;
    public Label Amount;
    @FXML
    public Button buyButton;
    public Label Stock;

    @FXML
    public void initialize() {
        buyButton.setDisable(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Money.setText("Money: " + Player.getMoney());
        int column = 0;
        int row = 0;
        for (int i = 0; i < CardView.cardViews.size(); i++) {
            if (column == 3) {
                column = 0;
                row++;
            }
            AnchorPane pane = null;
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                fxmlLoader.setLocation(getClass().getResource("/FXML/Item.fxml"));
                pane = fxmlLoader.load();
                ItemController itemController = fxmlLoader.getController();
                itemController.setImage(CardView.cardViews.get(i).imageView);
                itemController.setCardView(CardView.cardViews.get(i));
                itemController.setItemListener(new ItemListener() {
                    @Override
                    public void onClick(CardView cardView) {
                        setCard(itemController.getCardView());
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }


            gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
            gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gridPane.setMaxWidth(Region.USE_PREF_SIZE);

            gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
            gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            gridPane.setMaxHeight(Region.USE_PREF_SIZE);


            gridPane.add(pane, column++, row);
            if (isBanned(CardView.cardViews.get(i).name)){
                Circle circle = new Circle(50);
                circle.setFill(new ImagePattern(new Image(getClass().getResource("/Images/Lock.png").toExternalForm())));
                gridPane.add(circle, column-1, row);
                circle.toFront();
            }
        }

    }

    private void setCard(CardView cardView) {
        card = cardView;
        selectedImage.setImage(cardView.imageView.getImage());
        Stock.setText("Stock: " + getStock(card.name));
        Amount.setText("You have: " + String.valueOf(Player.getNumberOfCards(cardView.name)));
        if (Card.getCardByName(cardView.name).getPrice() <= Player.getMoney() ||
                isBanned(cardView.name))
            buyButton.setDisable(false);
    }

    public void run(String username) throws Exception {
        Shop.username = username;
        start(MainProgramView.stage);
        System.out.println("Welcome to Shop");
    }

//    public void increaseMoney(String command, String playerName) {
//        Matcher matcher = CommonTools.getMatcher(command, "^increase --money (\\d+)$");
//        matcher.find();
//        int amountOfMoney = Integer.parseInt(matcher.group(1));
//        Player.getPlayerByUsername(playerName).increaseMoney(amountOfMoney);
//    }


    public static void showAll() {
        Card.showCards();
    }

    @Override
    public void start(Stage stage) throws Exception {
        CardView.init();
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/Shop.fxml"));
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.show();
    }


    public void mouseEnter(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setCursor(Cursor.HAND);
    }

    public void mouseExited(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setCursor(Cursor.NONE);
    }

    public void buy(MouseEvent mouseEvent) {
        try {
            LoginController.dataOutputStream.writeUTF("shop " + Player.getActivePlayer().getUsername() + " buy " + card.name);
            LoginController.dataOutputStream.flush();
            String result = LoginController.dataInputStream.readUTF();
            JOptionPane.showMessageDialog(null, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Amount.setText("You have: " + String.valueOf(Player.getNumberOfCards(card.name)));
        Money.setText("Money: " + Player.getMoney());
        Stock.setText("Stock: " + getStock(card.name));
        if (Card.getCardByName(card.name).getPrice() > Player.getMoney())
            buyButton.setDisable(true);
    }

    public static String getStock(String name) {
        String num = null;
        try {
            LoginController.dataOutputStream.writeUTF("shop stock " + name);
            LoginController.dataOutputStream.flush();
            num = LoginController.dataInputStream.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/main_menu_view.fxml"));
        Stage stage = MainProgramView.stage;
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void admin(MouseEvent mouseEvent) {
        String pass = JOptionPane.showInputDialog(null, "Enter Secret Password");
        if (pass.equals("Saaz")) {
            try {
                new Admin().start(MainProgramView.stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
