package Model 1;

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
        
    }		
    
    public String getType() 		
    {
        
    }		
    
    public String getIcon() 		
    {
        
    }		
    
    public String getName() 		
    {
        
    }		
    
    public String getDescription() 		
    {
        
    }		
    
    public String getCardNumber() 		
    {
        
    }		
}
