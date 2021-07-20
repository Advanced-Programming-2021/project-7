package Model;

import Controller.RegisterProfileController;
import Model.Cards.Card;

import java.io.IOException;
import java.util.*;

public class Player implements Comparable<Player> {
    private static ArrayList<Player> players = new ArrayList<Player>();
    private static Player activePlayer;
    private static String token;
    private String username;
    private String password;
    private String nickname;
    private int score;
    private int money;
    private int profile;
    private HashMap<Card, Integer> cards = new HashMap<>();
    private ArrayList<Deck> decks = new ArrayList<>();
    private Deck activeDeck = null;


    public Player(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        score = 0;
        money = 100000;
        players.add(this);
    }

    public static Player getPlayerByUsername(String username) {
        if (players == null)
            players = new ArrayList<>();
        Iterator<Player> playerIterator = players.iterator();
        while (playerIterator.hasNext()) {
            Player player = playerIterator.next();
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String toBeToken) {
        token = toBeToken;
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

    public String getPassword() {
        return password;
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
        for (Map.Entry<Card, Integer> cardIntegerEntry : cards.entrySet()) {
            if (cardIntegerEntry.getKey().getName().equals(card.getName())) {
                int numberOfCards = cardIntegerEntry.getValue();
                numberOfCards++;
                cards.put(card, numberOfCards);
                return;
            }
        }
        cards.put(card, 1);
    }

    public HashMap<Card, Integer> getCards() {
        return cards;
    }

    public void removeCard(Card card) {
        if (cards == null) {
            return;
        }
        for (Map.Entry<Card, Integer> e : cards.entrySet()) {
            if (e.getKey().getName().equals(card.getName()) && e.getValue() != 0) {
                e.setValue(e.getValue() - 1);
                return;
            }
        }
    }

    public boolean doesCardExist(String cardName) {
        ArrayList<Card> cardsArray = new ArrayList<Card>();
        for (Map.Entry<Card, Integer> e : cards.entrySet()) {
            for (int i = 0; i < e.getValue(); i++) {
                cardsArray.add(e.getKey());
            }
        }
        for (int i = 0; i < cardsArray.size(); i++) {
            if (cardsArray.get(i).getName().equals(cardName)) return true;
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

    public void removeDeck(String deckName) throws IOException {
        for (Map.Entry<Card, Integer> e : Deck.getMainDeckByDeck(this.getDeckByName(deckName)).entrySet()) {
            for (int i = 0; i < e.getValue(); i++) {
                Card addingCard = Card.getCardByName(e.getKey().getName());
                Player player = Player.getPlayerByUsername(username);
                player.addCard(addingCard);
                FileHandler.updatePlayers();
            }
        }
        decks.removeIf(deck -> deck.getDeckName().equals(deckName));
    }

    public void increaseMoney(int money) {
        this.money += money;
    }

    public void decreaseMoney(int money) {
        this.money -= money;
    }

    public void increaseScore(int score) {
        this.score += score;
    }

    public void decreaseScore(int score) {
        this.score -= score;
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

    public static int getMoney() {
        int money = -1;
        try {
            RegisterProfileController.dataOutputStream.writeUTF("Player " + Player.getActivePlayer().getUsername() + " Money");
            RegisterProfileController.dataOutputStream.flush();
            String result = RegisterProfileController.dataInputStream.readUTF();
            money = Integer.parseInt(result);
        }catch (Exception e){
            e.printStackTrace();
        }
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
            if (true) {
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
            for (int i = 0; i < e.getValue(); i++) {
                cardsArray.add(e.getKey());
            }
        }
        Collections.sort(cardsArray);
        for (int i = 0; i < cardsArray.size(); i++) {
            System.out.printf("%s:%s\n", cardsArray.get(i).getName(), cardsArray.get(i).getDescription());
        }
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public static int getNumberOfCards(String name) {
        int num = -1;
        try {
            RegisterProfileController.dataOutputStream.writeUTF("Player " +
                    Player.getActivePlayer().getUsername() + " Card " + name);
            RegisterProfileController.dataOutputStream.flush();
            String result = RegisterProfileController.dataInputStream.readUTF();
            num = Integer.parseInt(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return num;
    }

    public static void sortPlayers() {
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                Player first = players.get(i);
                Player second = players.get(j);
                if (second.compare(first)) {
                    players.set(i, second);
                    players.set(j, first);
                }
            }
        }
    }

    public boolean compare(Player player) {
        if (this.score > player.score) {
            return true;
        } else if (this.score == player.score) {
            if (this.username.compareTo(player.username) > 0) return false;
            else return true;
        } else {
            return false;
        }
    }

    public static void setActivePlayer(Player player) {
        activePlayer = player;
    }

    public static Player getActivePlayer() {
        return activePlayer;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }
}
