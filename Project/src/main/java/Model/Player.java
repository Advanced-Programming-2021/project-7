package Model;

import Model.Card.Card;

import java.util.*;

public class Player implements Comparable<Player> {
    private static ArrayList<Player> players = new ArrayList<>();
    private String username;
    private String password;
    private String nickname;
    private int score;
    private int money;
    private HashMap<Card, Integer> cards = new HashMap<>();
    private ArrayList<Deck> decks = new ArrayList<>();
    private Deck activeDeck = null;


    public Player(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        score = 0;
        money = 0;
        players.add(this);
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

    public static Player getPlayerByNick(String nickname) {
        Iterator<Player> playerIterator = players.iterator();
        while (playerIterator.hasNext()) {
            Player player = playerIterator.next();
            if (player.getNickname().equals(nickname)) {
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

    public void setPassword(String password) {
        this.password = password;
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public static void setPlayers(ArrayList<Player> players1) {
        players = players1;
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

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public boolean doesCardExist(String cardName) {
        Card card = Card.getCardByName(cardName);
        for (int i = 0; i < cards.size(); i++) {
            if (cards.containsKey(card)) {
                return true;
            }
        }
        return false;
    }

    public static Deck getActiveDeckByUsername(String username) {
        Iterator<Player> playerIterator = players.iterator();
        while (playerIterator.hasNext()) {
            Player player = playerIterator.next();
            if (player.getUsername().equals(username)) {
                return player.activeDeck;
            }
        }
        return null;
    }

    public void removeActiveDeck() {
        activeDeck = null;
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

    public void setActiveDeck(String deckName) {
        for (int i = 0; i < decks.size(); i++) {
            if (decks.get(i).getDeckName().equals(deckName)) {
                activeDeck = decks.get(i);
            }
        }
    }

    public int getMoney() {
        return money;
    }

    public int compareTo(Player player) {
        if (this.score > player.score) {
            return -1;
        } else if (this.score < player.score) {
            return 1;
        }
        return this.username.compareTo(player.username);
    }

    public String toString() {
        return username + ": " + score;
    }

    public void showDecks() {
        System.out.println("Decks:");
        System.out.println("Active Deck:");
        if (activeDeck != null) {
            String validity;
            if (activeDeck.isDeckValid()) validity = "valid";
            else validity = "invalid";
            System.out.printf("%s: main deck %d, side deck %d, %s\n",
                    activeDeck.getDeckName(),
                    activeDeck.getMainNumberOfCards(),
                    activeDeck.getSideNumberOfCards(), validity);

        }
        System.out.println("Other Decks:");
        if (decks != null) Collections.sort(decks);
        for (int i = 0; i < decks.size(); i++) {
            if (!decks.get(i).getDeckName().equals(activeDeck.getDeckName())) {
                String validity;
                if (decks.get(i).isDeckValid()) validity = "valid";
                else validity = "invalid";
                System.out.printf("%s: main deck %d, side deck %d, %s\n",
                        decks.get(i).getDeckName(),
                        decks.get(i).getMainNumberOfCards(),
                        decks.get(i).getSideNumberOfCards(), validity);
            }
        }
    }

    public void showCards() {
        ArrayList<Card> cardsArray = new ArrayList<Card>();
        for (Map.Entry<Card, Integer> e : cards.entrySet()) {
            cardsArray.add(e.getKey());
        }
        Collections.sort(cardsArray);
        for (int i = 0; i < cardsArray.size(); i++) {
            System.out.printf("%s:%s\n", cardsArray.get(i).getName(), cardsArray.get(i).getDescription());
        }
    }
}
