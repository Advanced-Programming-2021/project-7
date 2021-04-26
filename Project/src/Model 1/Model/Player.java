package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Player implements Comparable<Player> {
    private static ArrayList<Player> players;
    private String username;
    private String password;
    private String nickname;
    private int score;
    private HashMap<Card, Integer> cards;
    private ArrayList<Deck> decks;
    private int money;
    private Deck activeDeck = null;


    public Player(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        score = 0;
        money = 0;
    }

    public static Player getPlayerByUsername(String username) {
        Iterator<Player> playerIterator = players.iterator();
        while (playerIterator.hasNext()) {
            Player player = playerIterator.next();
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    public static boolean isPasswordCorrect(String username, String password) {
        if (getPlayerByUsername(username).password.equals(password)) {
            return true;
        }
        return false;
    }

    public static ArrayList<Player> getPlayers(){
        return players;
    }

    public void addCard(Card card) {
        if (cards.containsKey(card)) {
            int numberOfCards = cards.get(card);
            numberOfCards++;
            cards.put(card, numberOfCards);
        } else {
            cards.put(card, 1);
        }
    }

    public void addDeck(Deck deck) {
        decks.add(deck);
    }

    public void removeDeck(String deckName) {
        decks.removeIf(deck -> deck.getDeckName().equals(deckName));
    }

    public void increaseMoney(int money) {
        this.money += money;
    }

    public void decreaseMoney(int money) {
        this.money -= money;
    }

    public void setNickName(String nickname) {
        this.nickname = nickname;
    }

    public int getScore() {
        return score;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public Deck getDeckByName(String deckName) {
        Iterator<Deck> deckIterator = decks.iterator();
        while (deckIterator.hasNext()) {
            Deck deck = deckIterator.next();
            if (deck.getDeckName().equals(deckName)) {
                return deck;
            }
        }
        return null;
    }

    public int getMoney() {
        return money;
    }

    public int compareTo(Player player) {
        if (this.score > player.score) {
            return 1;
        } else if (this.score < player.score){
            return -1;
        }
        return this.username.compareTo(player.username);
    }

    public String toString(){
        return username + ": " + score;
    }
}
