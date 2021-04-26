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
    
    public void setMonsterType(String monsterType) 		
    {
        this.monsterType = monsterType;
    }		
    
    public int getAttackPonit() 		
    {
        return attackPoint;
    }		
    
    public int getDefensePonit() 		
    {
        return defensePoint;
    }	
    
    public int getLevel() 		
    {
        return level;
    }	
    
    public String getAttribute() 		
    {
        return attribue;
    }		
    
    public String getMonsterType() 		
    {
        return monsterType;
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
