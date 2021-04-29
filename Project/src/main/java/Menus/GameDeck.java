 class GameDeck
{
    private ArrayList<Card> attackPositionCards;
    private ArrayList<Card> defencePositionCards;		
    private ArrayList<Card> inhandCards;		
    private ArrayList<Card> graveyardCards;		
    private ArrayList<Card> mainDeck;		
    private ArrayList<Card> sideDeck;		
    private ArrayList<Card> deckZone;		
    private ArrayList<Card> fieldZone;		
    private Array< Char> mat;		
    private HashMap<Ineger, ArrayList<Card>> monster;		
    private HashMap<Ineger, ArrayList<Card>> spellAndTrap;		
    private String playerName;		
    private String[] board;		
    private int playerLP;		
    private int matPosition;		

    
    public <<constructor>> GameDeck(String playerName, ArrayList<Card> mainDeck, ArrayList<Card> sideDeck) 		
    {
        
    }		
    
    public void drawCard(String cardName) 		
    {
        
    }		
    
    public void summonCard(String cardName) 		
    {
        
    }		
    
    public void activateCard(String cardName) 		
    {
        
    }		
    
    public void destroyCard(String cardName) 		
    {
        
    }		
    
    public void decreaseCardHP(String cardName) 		
    {
        
    }		
    
    public void decreaseLP(int value) 		
    {
        
    }		
    
    public void increaseLP(int value) 		
    {
        
    }		
    
    public void editBoardCell(int position) 		
    {
        
    }		
    
    public void showBoard() 		
    {
        
    }		
    
    public void selectMonster(int position) 		
    {
        
    }		
    
    public void selectOpponentMonster(int position) 		
    {
        
    }		
    
    public void selectSpell(int position) 		
    {
        
    }		
    
    public void selectField() 		
    {
        
    }		
    
    public void selectHand(int position) 		
    {
        
    }		
    
    public boolean checkSelectValidity(String field, int position) 		
    {
        
    }		
    
    public void deselct() 		
    {
        
    }		
    
    public void setPosition(Card card, String position) 		
    {
        
    }		
    
    public void setMat() 		
    {
        
    }		
    
    public String getBoardCellByPosition(int position) 		
    {
        
    }		
    
    public String getPlayerName() 		
    {
        
    }		
    
    public ArrayList<Card> getActiveCards() 		
    {
        
    }		
    
    public ArrayList<Card> getInhandCards() 		
    {
        
    }		
    
    public ArrayList<Card> getdestroyedCards() 		
    {
        
    }		
    
    public Card getSelectedCard() 		
    {
        
    }		
    
    public int getNumberOfDeckCards() 		
    {
        
    }		
    
    public int getNumberOfGraveCards() 		
    {
        
    }		
    
    public int getNumberOfFieldCards() 		
    {
        
    }		
    
    public String getMonsterState(int position) 		
    {
        
    }		
    
    public String getSpellState(int position) 		
    {
        
    }		
    
    public int getNumberOfHandCards() 		
    {
        
    }		
}
