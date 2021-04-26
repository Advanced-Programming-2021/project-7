package Model;

 public class Monster extends Card {
    private int attackPoint;		
    private int defensePoint;		
    private int level;		
    private String attribue;		
    private String monsterType;		

    
    public Monster(String name, String description, String type, String cardNumber, int attackPoint, int defensePoint, int level, String attribute, String monsterType) 		
    {
        super(name, description, type, cardNumber);
        this.attackPoint = attackPoint;
        this.defensePoint = defensePoint;
        this.level = level;
        this.attribue = attribute;
        this.monsterType = monsterType;
    }		
    
    public void setAttackPonit(int attackpoint) 		
    {
        this.attackPoint = attackpoint;
    }	
    
    public void setDefensePoint(int defensePoint) 		
    {
        this.defensePoint = defensePoint;
    }		
    
    public void setLevel(int level) 		
    {
        this.level = level;
    }	
    
    public void setAttribute(String attribute) 		
    {
        this.attribue = attribute;
    }		
    
    public void setMonsterType(String type) 		
    {
        
    }		
    
    public int getAttackPonit() 		
    {
        
    }		
    
    public int getDefensePonit() 		
    {
        
    }		
    
    public int getLevel() 		
    {
        
    }		
    
    public String getAttribute() 		
    {
        
    }		
    
    public String getMonsterType() 		
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
