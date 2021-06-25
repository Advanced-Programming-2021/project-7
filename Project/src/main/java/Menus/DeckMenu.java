package Menus;

import Model.*;
import Model.Cards.Card;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DeckMenu {
    private Player loggedInUser;


    public void run(String username) throws IOException {
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^deck create [^ ]+$")) createDeck(username, command);
            else if (command.matches("^deck delete [^ ]+$")) deleteDeck(username, command);
            else if (command.matches("^deck set-activate [^ ]+$")) setActiveDeck(username, command);
            else if (command.matches("^deck add-card (?:(?:--card|--deck|--side)( (.+))* ?){2,3}$"))
                addCardToDeck(username, command);
            else if (command.matches("^deck rm-card (?:(?:--card|--deck|--side)( (.+))* ?){2,3}$"))
                removeCardFromDeck(username, command);
            else if (command.matches("^deck show --all")) showAllDecks(username);
            else if (command.matches("^deck show (?:(?:--deck-name|--side)( ([^ ]+))* ?){1,2}$"))
                showDeck(username, command);
            else if (command.matches("^deck show --cards$")) showCards(username);
            else if (command.matches("^menu enter (profile|duel|deck|shop|scoreboard)$"))
                System.out.println("menu navigation is not possible");
            else if (command.matches("^menu show-current$")) System.out.println("deck");
            else if (command.matches("^menu exit$")) {
                return;
            } else System.out.println("invalid command!");
        }
    }

    private void createDeck(String username, String command) throws IOException {
        String pattern = "^deck create ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String deckName = m.group(1);
        if (Player.getPlayerByUsername(username).getDeckByName(deckName) != null) {
            System.out.printf("deck with name %s already exists\n", deckName);
            return;
        }
        System.out.println("deck created successfully!");
        new Deck(deckName, username);
        FileHandler.updatePlayers();
    }

    private void deleteDeck(String username, String command) throws IOException {
        String pattern = "^deck delete ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String deckName = m.group(1);
        if (Player.getPlayerByUsername(username).getDeckByName(deckName) == null) {
            System.out.printf("deck with name %s does not exist\n", deckName);
            return;
        }
        System.out.println("deck delete successfully!");
        Player.getPlayerByUsername(username).removeDeck(deckName);
        if(Player.getActiveDeckByUsername(username) != null){
            if (Player.getActiveDeckByUsername(username).getDeckName().equals(deckName)){
                Player.getPlayerByUsername(username).removeActiveDeck();
            }
        }
        FileHandler.updatePlayers();
    }

    public void setActiveDeck(String username, String command) throws IOException {
        String pattern = "^deck set-activate ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String deckName = m.group(1);
        if (Player.getPlayerByUsername(username).getDeckByName(deckName) == null) {
            System.out.printf("deck with name %s does not exist\n", deckName);
            return;
        }
        System.out.println("deck activated successfully");
        Player.getPlayerByUsername(username).setActiveDeck(deckName);
        FileHandler.updatePlayers();
    }

    private void addCardToDeck(String username, String command) throws IOException {
        String pattern = "^deck add-card --card ([^-]+) --deck ([^-]+)( --side)?$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String cardName = m.group(1);
        String deckName = m.group(2);
        String side = CommonTools.takeNameOutOfCommand(command, "--side");
        if(cardName == null || deckName == null || side != null){
            System.out.println("invalid command");
            return;
        }
        if (!addCardValidity(cardName, deckName, username)) return;
        Player player = Player.getPlayerByUsername(username);
        Deck deck = player.getDeckByName(deckName);
        if (!command.contains("--side")) {
            if (deck.isMainDeckFull()) {
                System.out.println("main deck is full");
                return;
            }
            if (deck.isThereThreeCards(Card.getCardByName(cardName))) {
                System.out.printf("there are already three cards with name %s in deck %s\n", cardName, deckName);
                return;
            }
            deck.addCardToMainDeck(Card.getCardByName(cardName));
        } else {
            if (deck.isSideDeckFull()) {
                System.out.println("side deck is full");
                return;
            }
            if (deck.isThereThreeCards(Card.getCardByName(cardName))) {
                System.out.printf("there are already three cards with name %s in deck %s\n", cardName, deckName);
                return;
            }
            deck.addCardToSideDeck(Card.getCardByName(cardName));
        }
        player.removeCard(Card.getCardByName(cardName));
        System.out.println("card added to deck successfully");
        FileHandler.updatePlayers();
    }

    private void removeCardFromDeck(String username, String command) throws IOException {
        String pattern = "^deck rm-card --card ([^-]+) --deck ([^-]+)( --side)?$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String cardName = m.group(1);
        String deckName = m.group(2);
        if(cardName == null || deckName == null){
            System.out.println("invalid command");
            return;
        }
        boolean side = command.contains("--side");
        if (!removeCardValidity(cardName, deckName, username, side)) return;
        Player player = Player.getPlayerByUsername(username);
        Deck deck = player.getDeckByName(deckName);
        Card card = Card.getCardByName(cardName);
        if (!command.contains("--side")) {
            deck.removeCardFromMainDeck(card, username);
        } else {
            deck.removeCardFromSideDeck(card, username);
        }
        System.out.println("card removed form deck successfully");
        FileHandler.updatePlayers();
    }

    private boolean addCardValidity(String cardName, String deckName, String username) {
        if (cardName == null | deckName == null) {
            System.out.println("invalid command");
            return false;
        }
        Player player = Player.getPlayerByUsername(username);
        if (!player.doesCardExist(cardName)) {
            System.out.printf("card with name %s does not exist\n", cardName);
            return false;
        }
        if (player.getDeckByName(deckName) == null) {
            System.out.printf("deck with name %s does not exist\n", deckName);
            return false;
        }
        return true;
    }

    private boolean removeCardValidity(String cardName, String deckName, String username, boolean side) {
        Player player = Player.getPlayerByUsername(username);
        if (cardName == null | deckName == null) {
            System.out.println("invalid command");
            return false;
        }
        if (player.getDeckByName(deckName) == null) {
            System.out.printf("deck with name %s does not exist\n", deckName);
            return false;
        }
        Deck deck = player.getDeckByName(deckName);
        if (!side) {
            for (Map.Entry <Card, Integer> e : Deck.getMainDeckByDeck(deck).entrySet()) {
                if (e.getKey().getName().equals(cardName)) {
                    if (e.getValue() != 0) {
                        return true;
                    }
                }
            }
            System.out.printf("card with name %s does not exist in main deck\n", cardName);
            return false;
        } else {
            for (Map.Entry <Card, Integer> e : Deck.getSideDeckByDeck(deck).entrySet()) {
                if (e.getKey().getName().equals(cardName)) {
                    if (e.getValue() != 0) {
                        return true;
                    }
                }
            }
            System.out.printf("card with name %s does not exist in side deck\n", cardName);
            return false;
        }
    }

    private void showAllDecks(String username) {
        Player.getPlayerByUsername(username).showDecks();
    }

    private void showDeck(String username, String command) {
        String deckName = CommonTools.takeNameOutOfCommand(command, "--deck-name");
        String afterSide = CommonTools.takeNameOutOfCommand(command, "--side");
        if(deckName == null || afterSide != null){
            System.out.println("invalid command");
            return;
        }

        if (Player.getPlayerByUsername(username).getDeckByName(deckName) == null){
            System.out.println("there is no such a deck");
            return;
        }
        System.out.printf("Deck: %s\n", deckName);
        if (command.contains(" --side")) {
            System.out.println("Side deck");
            Objects.requireNonNull(Player.getPlayerByUsername(username)).getDeckByName(deckName).showSideDeck();
        } else {
            System.out.println("Main deck");
            Objects.requireNonNull(Player.getPlayerByUsername(username)).getDeckByName(deckName).showMainDeck();
        }
    }

    private void showCards(String username) {
        Player player = Objects.requireNonNull(Player.getPlayerByUsername(username));
        ArrayList<Card> allCards = new ArrayList<>();
        HashMap<Card, Integer> notInDeckCards = player.getCards();
        for (Map.Entry<Card, Integer> e : notInDeckCards.entrySet()) {
            for (int k = 0; k < e.getValue(); k++) {
                allCards.add(e.getKey());
            }
        }
        ArrayList<Deck> decks = player.getDecks();
        for (Deck deck : decks) {
            HashMap<Card, Integer> mainDeck = deck.getMainDeck();
            HashMap<Card, Integer> sideDeck = deck.getSideDeck();
            for (Map.Entry<Card, Integer> e : mainDeck.entrySet()) {
                for (int k = 0; k < e.getValue(); k++) {
                    allCards.add(e.getKey());
                }
            }
            for (Map.Entry<Card, Integer> e : sideDeck.entrySet()) {
                for (int k = 0; k < e.getValue(); k++) {
                    allCards.add(e.getKey());
                    System.out.printf("%s:%s\n", e.getKey().getName(), e.getKey().getDescription());
                }
            }
        }
        Collections.sort(allCards);
        for(Card card : allCards) {
            System.out.printf("%s:%s\n", card.getName(), card.getDescription());
        }
    }
}