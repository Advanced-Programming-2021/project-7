package Model.Cards;

import java.util.ArrayList;
import java.util.Collections;

public class Card implements Comparable<Card> {
    protected static ArrayList<Card> cards = new ArrayList<>();
    protected String name;
    protected String description;
    protected String type;
    protected String cardNumber;
    protected int price;

    public Card(String name, String description, String type, int price) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
        cards.add(this);
    }

    public static Card getCardByName(String name) {
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (card.name.equals(name)) return card;
        }
        return null;
    }

    public void setCardDescription(Object StringDecsription) {
        if (StringDecsription == null) return;
        if (!(StringDecsription instanceof String)) return;
        String description = (String) StringDecsription;
        this.description = description;
    }

    public String getType(){
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static void showCards() {
        ArrayList<String> namesAndDescriptions = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            String line = cards.get(i).name;
            line = line + ":";
            line = line + cards.get(i).price;
            namesAndDescriptions.add(line);
        }
        Collections.sort(namesAndDescriptions);
        for (int i = 0; i < namesAndDescriptions.size(); i++) {
            System.out.println(namesAndDescriptions.get(i));
        }
    }

    public boolean isCardExist(String cardName) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).name.equals(cardName)) {
                return true;
            }
        }
        return false;
    }

    public static int getPriceByUsername(String name) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).name.equals(name)) {
                return cards.get(i).price;
            }
        }
        return 0;
    }

    public String toString() {
        return this.name + ":" + this.description;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int compareTo(Card card) {
        return this.name.compareTo(card.name);
    }
}
