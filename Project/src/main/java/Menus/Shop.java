package Menus;

import Model.Cards.Card;
import Model.CommonTools;
import Model.FileHandler;
import Model.Player;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Shop {
    public void run(String username) throws IOException {
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^shop buy .+$")) buyCard(username, command);
            else if (command.matches("^shop show --all$")) showAll();
            else if (command.matches("^menu enter (profile|duel|deck|shop|scoreboard)$"))
                System.out.println("menu navigation is not possible");
            else if (command.matches("^menu show-current$")) System.out.println("shop");
            else if (command.matches("^menu exit$")) {
                return;
            } else System.out.println("invalid command!");
        }
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
}
