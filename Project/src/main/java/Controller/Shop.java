package Controller;

import Model.Cards.Card;
import Model.CommonTools;
import Model.FileHandler;
import Model.Player;
import Model.Sound;
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

public class Shop extends Application{

    private static String username;
    public Label Money;
    public GridPane gridPane;
    public ScrollPane scroll;
    public CardView card;
    public ImageView selectedImage;
    public Label Amount;
    @FXML
    public Button buyButton;

    @FXML
    public void initialize() {
        buyButton.setDisable(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Money.setText("Money: " + Player.getPlayerByUsername(username).getMoney());
        int column = 0;
        int row = 0;
        for (int i = 0; i < CardView.cardViews.size(); i++) {
            if (column == 3){
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

            gridPane.add(pane,column++,row);
        }

    }

    private void setCard(CardView cardView) {
        card = cardView;
        selectedImage.setImage(cardView.imageView.getImage());
        Amount.setText("You have: " + String.valueOf(Player.getPlayerByUsername(username).getNumberOfCards(cardView.name)));
        if (Card.getCardByName(cardView.name).getPrice() <= Player.getPlayerByUsername(username).getMoney())
            buyButton.setDisable(false);
    }

    public void run(String username) throws Exception {
        Shop.username = username;
        start(MainProgramView.stage);
        System.out.println("Welcome to Shop");
    }

    public void increaseMoney(String command, String playerName) {
        Matcher matcher = CommonTools.getMatcher(command, "^increase --money (\\d+)$");
        matcher.find();
        int amountOfMoney = Integer.parseInt(matcher.group(1));
        Player.getPlayerByUsername(playerName).increaseMoney(amountOfMoney);
    }

    public static void buyCard(String username, String command) throws IOException {
        String card = command.substring(9);
        if (Card.getCardByName(card) == null) {
            System.out.println("there is no card with this name");
        } else {
            int money = Player.getPlayerByUsername(username).getMoney();
            if (money < Card.getPriceByUsername(card)) {
                System.out.println("not enough money");
            } else {
                System.out.println("Card bought!");
                Card addingCard = Card.getCardByName(card);
                Player player = Player.getPlayerByUsername(username);
                player.addCard(addingCard);
                player.decreaseMoney(Card.getPriceByUsername(card));
                FileHandler.updatePlayers();
            }
        }
    }

    public static void showAll() {
        Card.showCards();
    }

    @Override
    public void start(Stage stage) throws Exception { // TODO: 2021-07-08 price of cards
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
        Sound.getSoundByName("button").playSoundOnce();
        String command = "shop buy " + card.name;
        try {
            buyCard(username, command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Amount.setText("You have: " + String.valueOf(Player.getPlayerByUsername(username).getNumberOfCards(card.name)));
        Money.setText("Money: " + Player.getPlayerByUsername(username).getMoney());
        if (Card.getCardByName(card.name).getPrice() > Player.getPlayerByUsername(username).getMoney())
            buyButton.setDisable(true);
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Sound.getSoundByName("button").playSoundOnce();
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/main_menu_view.fxml"));
        Stage stage = MainProgramView.stage;
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
