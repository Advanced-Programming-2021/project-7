package Model.Cards;

public class MonsterZone {
    private Card currentMonster = null;
    private String status = "E";
    private boolean hasAttackedThisRound = false;
    public boolean hasBeenChanged = false;

    public MonsterZone(){

    }

    public boolean isEmpty() {
        if (status.equals("E"))
            return true;
        return false;
    }
    
    public void attack(){
        hasAttackedThisRound = true;
    }
    
    public void resetAttack(){
        hasAttackedThisRound = false;
    }

    public boolean getHasAttackedThisRound() {
        return hasAttackedThisRound;
    }

    public String getStatus() {
        return status;
    }

    public Card getCurrentMonster() {
        return currentMonster;
    }

    public void setCardAttack(Card card) {
        currentMonster = card;
        status = "OO";
    }

    public void setCardDefense(Card card) {
        currentMonster = card;
        status = "DO";
    }

    public void setCardHidden(Card card){
        currentMonster = card;
        status = "DH";
    }

    public Card removeCard(){
        hasBeenChanged = false;
        hasAttackedThisRound = false;
        Card card = currentMonster;
        currentMonster = null;
        status = "E";
        return card;
    }
}
