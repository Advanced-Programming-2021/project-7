package Model;

public class SpellTrap extends Card {
    private String spellType;		
    private String icon;	

    
    public SpellTrap(String name, String description, String type, String cardNumber, String spellType, String icon) 		
    {
        super(name, description, type, cardNumber);
        this.spellType = spellType;
        this.icon = icon;
    }		
    
    public void setSpellType(String spellType) 		
    {
        this.spellType = spellType;
    }		
    
    public void setIcon(String icon) 		
    {
        this.icon = icon;
    }		
    
    public String getSpellType() 		
    {
        return spellType;
    }		
    
    public String getIcon() 		
    {
        return icon;
    }		
    
    public String getName() 		
    {
        return name;
    }		
    
    public String getDescription() 		
    {
        return description;
    }	
    
    public String getCardNumber() 		
    {
        return cardNumber;
    }		
}
