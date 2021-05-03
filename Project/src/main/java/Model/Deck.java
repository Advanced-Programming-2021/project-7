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

    
    public Deck(String deckName, String playerName) 
    {
        this.deckName = deckName;
        this.playerName = playerName;
        decks.add(this);
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
    
    public void addCardToMainDeck(Card card) 		
    {
        for (Map.Entry <Card, Integer> e : mainDeck.entrySet()) {
            if (e.getKey().equals(card)) {
                int number = e.getValue() + 1;
                mainDeck.put(card, number);
                break;
            }
        }
        mainDeck.put(card, 1);
    }		
    
    public void addCardToSideDeck(Card card) 		
    {
        for (Map.Entry <Card, Integer> e : sideDeck.entrySet()) {
            if (e.getKey().equals(card)) {
                int number = e.getValue() + 1;
                sideDeck.put(card, number);
                break;
            }
        }
        sideDeck.put(card, 1);
    }		
    
    public void removeCardFromMainDeck(Card card) 		
    {
        for (Map.Entry <Card, Integer> e : mainDeck.entrySet()) {
            if (e.getKey().equals(card)) {
                int number = e.getValue() - 1;
                if (number > 0) mainDeck.put(card, number);
                else mainDeck.remove(card);
                break;
            }
        }
    }		
    
    public void removeCardFromSideDeck(Card card) 		
    {
        for (Map.Entry <Card, Integer> e : sideDeck.entrySet()) {
            if (e.getKey().equals(card)) {
                int number = e.getValue() - 1;
                if (number > 0) sideDeck.put(card, number);
                else sideDeck.remove(card);
                break;
            }
        }
    }		
    
//    public boolean isDeckValid()
//    {
//
//    }
//
//    public boolean isMainDeckFull()
//    {
//
//    }
//
//    public boolean isSideDeckFull()
//    {
//
//    }
//
//    public boolean isThereThreeCards(Card card)
//    {
//
//    }
    
    public String getDeckName() 		
    {
        return this.deckName;
    }		
}
