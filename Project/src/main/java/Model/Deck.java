package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Deck
{
    private static ArrayList<Deck> decks = new ArrayList<>();
    private HashMap<Card, Integer> mainDeck;
    private HashMap<Card, Integer> sideDeck;
    private boolean deckIsValid;
    private String deckName;
    private String playerName;


    public Deck(String deckName, String playerName) {
        this.deckName = deckName;
        this.playerName = playerName;
        this.deckIsValid = false;
        decks.add(this);
        Player.getPlayerByUsername(playerName).addDeck(this);
    }

    public static Deck getDeckByNames(String deckName, String playerName) {
        for (int i = 0; i < decks.size(); i++) {
            Deck deck = decks.get(i);
            if (!deck.deckName.equals(deckName)) continue;
            if (!deck.playerName.equals(playerName)) continue;
            return deck;
        }
        return null;
    }

    public static boolean doesDeckExist(String deckName){
        for (int i = 0; i < decks.size(); i++) {
            Deck deck = decks.get(i);
            if(deck.getDeckName().equals(deckName)){
                return true;
            }
        }
        return false;
    }

    public static HashMap<Card, Integer>getMainDeckByDeck(Deck deck){
        return deck.mainDeck;
    }

    public static HashMap<Card, Integer>getSideDeckByDeck(Deck deck){
        return deck.sideDeck;
    }

    public void addCardToMainDeck(Card card) {
        for (Map.Entry <Card, Integer> e : mainDeck.entrySet()) {
            if (e.getKey().equals(card)) {
                int number = e.getValue() + 1;
                mainDeck.put(card, number);
                break;
            }
        }
        mainDeck.put(card, 1);
    }

    public void addCardToSideDeck(Card card) {
        for (Map.Entry <Card, Integer> e : sideDeck.entrySet()) {
            if (e.getKey().equals(card)) {
                int number = e.getValue() + 1;
                sideDeck.put(card, number);
                break;
            }
        }
        sideDeck.put(card, 1);
    }

    public void removeCardFromMainDeck(Card card) {
        for (Map.Entry <Card, Integer> e : mainDeck.entrySet()) {
            if (e.getKey().equals(card)) {
                int number = e.getValue() - 1;
                if (number > 0) mainDeck.put(card, number);
                else mainDeck.remove(card);
                break;
            }
        }
    }

    public void removeCardFromSideDeck(Card card) {
        for (Map.Entry <Card, Integer> e : sideDeck.entrySet()) {
            if (e.getKey().equals(card)) {
                int number = e.getValue() - 1;
                if (number > 0) sideDeck.put(card, number);
                else sideDeck.remove(card);
                break;
            }
        }
    }

    public boolean isDeckValid() {
        int numberOfCards = 0;
        for (Map.Entry <Card, Integer> e : mainDeck.entrySet()) {
            numberOfCards += e.getValue();
        }
        this.deckIsValid = numberOfCards >= 40;
        return deckIsValid;
    }

    public boolean isMainDeckFull() {
        int numberOfCards = 0;
        for (Map.Entry <Card, Integer> e : mainDeck.entrySet()) {
            numberOfCards += e.getValue();
        }
        return numberOfCards >= 60;
    }

    public boolean isSideDeckFull() {
        int numberOfCards = 0;
        for (Map.Entry <Card, Integer> e : sideDeck.entrySet()) {
            numberOfCards += e.getValue();
        }
        return numberOfCards >= 15;
    }

    public boolean isThereThreeCards(Card card) {
        for (Map.Entry <Card, Integer> e : mainDeck.entrySet()) {
            if (e.getKey().equals(card)) {
                if (e.getValue() >= 3) return true;
                else return false;
            }
        }
        return false;
    }

    public String getDeckName() {
        return this.deckName;
    }
}
