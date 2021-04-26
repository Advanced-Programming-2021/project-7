package Model;

import java.util.ArrayList;

public class Card {
    protected static ArrayList<Card> cards = new ArrayList<>();		
    protected String name;		
    protected String description;		
    protected String type;		
    protected String cardNumber;	
    protected int price;

    
    public Card(String name, String description, String type, String cardNumber, int price) 		
    {  
        this.name = name;
        this.description = description;
        this.type = type;
        this.cardNumber = cardNumber;
        this.price = price;
        cards.add(this);
        
    }		
    
    public Card getCardByName(String name) 		
    {
        
    }		
    
    public void serCardDescription(String StringDecsription)
    {
        
    }		
    
    public String getName() 		
    {
        
    }		
    
    public String getDescription() 		
    {
        
    }
    
    public void showCards(){
        ArrayList<String> namesAndDescriptions = new ArrayList<>();
        for(int i = 0; i < cards.size(); i++){
            String line = cards.get(i).name;
            line = line + ":";
            line = line + cards.get(i).description;
            namesAndDescriptions.add(line);
        }
        Collections.sort(namesAndDescriptions);
        for(int i = 0; i < namesAndDescriptions.size(); i++){
            System.out.println(namesAndDescriptions.get(i));
        }
    }
    
    public boolean isCardExist(String cardName){
        for(int i = 0; i < cards.size(); i++){
            if(cards.get(i).name.equals(cardName)){
                return true;
            }
        }
        return false;
    }

    public int getPriceByUsername(String name){
        for(int i = 0; i < cards.size(); i++){
            if(cards.get(i).name.equals(name)){
                return cards.get(i).price;
            }
        }
        return 0;
    }
}
