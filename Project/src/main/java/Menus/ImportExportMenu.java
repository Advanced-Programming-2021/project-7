package Menus;

import Model.Cards.Card;
import Model.CommonTools;
import com.gilecode.yagson.com.google.gson.Gson;
import com.gilecode.yagson.com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class ImportExportMenu {
    public void run() {
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^import card .+$")) importCard(command);
            else if (command.matches("^export card .+$")) exportCard(command);
            else if (command.matches("^menu enter (profile|duel|deck|shop|scoreboard)$"))
                System.out.println("menu navigation is not possible");
            else if (command.matches("^menu show-current$")) System.out.println("import-export");
            else if (command.matches("^menu exit$")) {
                return;
            } else System.out.println("invalid command!");
        }
    }

    public void importCard(String command) {
        Matcher matcher = CommonTools.getMatcher(command, "^import card ([^-]+)$");
        matcher.find();
        String cardName = matcher.group(1);
        ArrayList<Card> cards = new ArrayList<>();
        try {
            String json = new String(Files.readAllBytes(Paths.get("DataBase\\Cards\\ExportedCards.json")));
            cards = new Gson().fromJson(json,
                    new TypeToken<List<Card>>(){}.getType()
            );
        } catch (Exception e) {
            System.out.println("there is a problem in reading the file");
        }
        boolean isCardFound = false;
        for (Card card : cards) {
            if (card.getName().equals(cardName)) {
                isCardFound = true;
                break;
            }
        }
        if (isCardFound) {
            System.out.println("card " + cardName + " is available");
        } else {
            System.out.println("card " + cardName + " is not available");
        }
    }

    public void exportCard(String command) {
        Matcher matcher = CommonTools.getMatcher(command, "^export card ([^-]+)$");
        matcher.find();
        Card card = Card.getCardByName(matcher.group(1));
        if (card == null) {
            System.out.println("there is no card with this name");
            return;
        }
        ArrayList<Card> cards = new ArrayList<>();
        try {
            String json = new String(Files.readAllBytes(Paths.get("DataBase\\Cards\\ExportedCards.json")));
            cards = new Gson().fromJson(json,
                    new TypeToken<List<Card>>(){}.getType()
            );
        } catch (Exception e) {
            System.out.println("there is a problem in reading the file");
        }
        cards.add(card);
        try {
            FileWriter myWriter = new FileWriter("DataBase\\Cards\\ExportedCards.json");
            myWriter.write(new Gson().toJson(cards));
            myWriter.close();
            System.out.println("card exported successfully");
        } catch (Exception e) {
            System.out.println("there is a problem in writing the file");
        }
    }
}