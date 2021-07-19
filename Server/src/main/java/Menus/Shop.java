package Menus;

import Controller.ItemListener;
import Model.Cards.Card;
import Model.CommonTools;
import Model.FileHandler;
import Model.Player;
import Model.ShopInfo;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.regex.Matcher;

public class Shop {

    private static String username;
    public Label Money;
    public GridPane gridPane;
    public ScrollPane scroll;
    public CardView card;
    public ImageView selectedImage;
    public Label Amount;
    @FXML
    public Button buyButton;

    public void run(String username) throws Exception {
        Shop.username = username;
    }

    public void increaseMoney(String command, String playerName) {
        Matcher matcher = CommonTools.getMatcher(command, "^increase --money (\\d+)$");
        matcher.find();
        int amountOfMoney = Integer.parseInt(matcher.group(1));
        Player.getPlayerByUsername(playerName).increaseMoney(amountOfMoney);
    }

    public static String buyCard(String username, String command) throws IOException {
        String card = command.substring(9);
        if (Card.getCardByName(card) == null) {
            return "there is no card with this name";
        } else {
            int money = Player.getPlayerByUsername(username).getMoney();
            if (money < Card.getPriceByUsername(card)) {
                return "not enough money";
            } else if (ShopInfo.getStock(card) == 0) {
                return "shop out of stock";
            }  else if (ShopInfo.cardBanned(card)) {
                return "card is banned";
            } else {
                Card addingCard = Card.getCardByName(card);
                Player player = Player.getPlayerByUsername(username);
                player.addCard(addingCard);
                player.decreaseMoney(Card.getPriceByUsername(card));

                ShopInfo.decrease(card);

                FileHandler.updatePlayers();
                FileHandler.updateShop();
                return "Card bought!";
            }
        }
    }

    public static void showAll() {
        Card.showCards();
    }
}
