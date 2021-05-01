package Model;

import java.util.ArrayList;
import java.util.HashMap;

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
    
    public void addCardToMain(Card card) 		
    {
        mainDeck.put(card, value);
    }		
    
    public void addCardToSide(Card card) 		
    {
        
    }		
    
    public void removeCardFromMain(Card card) 		
    {
        
    }		
    
    public void removeCardFromSide(Card card) 		
    {
        
    }		
    
    public boolean isDeckValid() 		
    {
        
    }		
    
    public boolean isMainFull() 		
    {
        
    }		
    
    public boolean isSideFull() 		
    {
        
    }		
    
    public boolean isThereThreeCards(Card card) 		
    {
        
    }		
    
    public String toString() 		
    {
        
    }		
    
    public String getDeckName() 		
    {
        
    }		
}
