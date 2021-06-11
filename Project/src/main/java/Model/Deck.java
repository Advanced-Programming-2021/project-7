package Model;

import Model.Cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Deck implements Comparable<Deck> {
    private static ArrayList<Deck> decks = new ArrayList<>();
    private HashMap<Card, Integer> mainDeck = new HashMap<>();
    private HashMap<Card, Integer> sideDeck = new HashMap<>();
    private boolean deckIsValid;
    private String deckName;
    private String playerName;


    public Deck(String deckName, String playerName) {
        this.deckName = deckName;
        this.playerName = playerName;
        this.deckIsValid = false;
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
                mainDeck.remove(card);
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
                sideDeck.remove(card);
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
        if (mainDeck == null){
            return false;
        }
        for (Map.Entry <Card, Integer> e : mainDeck.entrySet()) {
            numberOfCards += e.getValue();
        }
        this.deckIsValid = numberOfCards >= 40 && numberOfCards <= 60;
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
        int numberOfCards = 0;
        for (Map.Entry <Card, Integer> e : mainDeck.entrySet()) {
            if (e.getKey().equals(card)) {
                numberOfCards += e.getValue();
            }
        }
        for (Map.Entry <Card, Integer> e : sideDeck.entrySet()) {
            if (e.getKey().equals(card)) {
                numberOfCards += e.getValue();
            }
        }
        if (numberOfCards >= 3)
            return true;
        return false;
    }

    public String getDeckName() {
        return this.deckName;
    }

    public int getMainNumberOfCards(){
        int numberOfCards = 0;
        if (mainDeck == null){
            return 0;
        }
        for (Map.Entry <Card, Integer> e : mainDeck.entrySet()) {
            numberOfCards += e.getValue();
        }
        return numberOfCards;
    }

    public int getSideNumberOfCards(){
        int numberOfCards = 0;
        if (sideDeck == null){
            return 0;
        }
        for (Map.Entry <Card, Integer> e : sideDeck.entrySet()) {
            numberOfCards += e.getValue();
        }
        return numberOfCards;
    }

    public int compareTo(Deck deck) {
        return this.deckName.compareTo(deck.deckName);
    }

    public void showMainDeck(){
        ArrayList<Card> monsters = new ArrayList<>();
        ArrayList<Card> spellsAndTraps = new ArrayList<>();
        for (Map.Entry <Card, Integer> e : mainDeck.entrySet()) {
            if(e.getKey().getType().equals("Monster")) {
                for (int i = 0; i < e.getValue(); i++) {
                    monsters.add(e.getKey());
                }
            }
            else if(e.getKey().getType().equals("Spell") || e.getKey().getType().equals("Trap")) {
                for (int i = 0; i < e.getValue(); i++) {
                    spellsAndTraps.add(e.getKey());
                }
            }
        }
        Collections.sort(monsters);
        Collections.sort(spellsAndTraps);
        System.out.println("Monsters:");
        for (int i = 0; i < monsters.size(); i++){
            System.out.printf("%s: %s\n", monsters.get(i).getName(), monsters.get(i).getDescription());
        }
        System.out.println("Spell and Traps:");
        for (int i = 0; i < spellsAndTraps.size(); i++){
            System.out.printf("%s: %s\n", spellsAndTraps.get(i).getName(), spellsAndTraps.get(i).getDescription());
        }
    }

    public void showSideDeck(){
        ArrayList<Card> monsters = new ArrayList<>();
        ArrayList<Card> spellsAndTraps = new ArrayList<>();
        for (Map.Entry <Card, Integer> e : sideDeck.entrySet()) {
            if(e.getKey().getType().equals("Monster")) {
                for (int i = 0; i < e.getValue(); i++) {
                    monsters.add(e.getKey());
                }
            }
            else if(e.getKey().getType().equals("Spell") || e.getKey().getType().equals("Trap")) {
                for (int i = 0; i < e.getValue(); i++) {
                    spellsAndTraps.add(e.getKey());
                }
            }
        }
        Collections.sort(monsters);
        Collections.sort(spellsAndTraps);
        System.out.println("Monsters:");
        for (int i = 0; i < monsters.size(); i++){
            System.out.printf("%s: %s\n", monsters.get(i).getName(), monsters.get(i).getDescription());
        }
        System.out.println("Spell and Traps:");
        for (int i = 0; i < spellsAndTraps.size(); i++){
            System.out.printf("%s: %s\n", spellsAndTraps.get(i).getName(), spellsAndTraps.get(i).getDescription());
        }
    }
}
