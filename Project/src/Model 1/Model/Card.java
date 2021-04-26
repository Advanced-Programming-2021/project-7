package Model;

import java.util.ArrayList;

class Card
{
    private ArrayList<Card> cards;
    private String name;		
    private String description;		
    private String type;		
    private String cardNumber;		

    
    public Card(){

    }

    public Card(String name, String description, int price){
        this.name = name;
        this.description = description;
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
