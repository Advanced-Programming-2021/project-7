package Model.Cards;

public class MonsterZone {
    private Card currentMonster = null;
    private String status = "E";
    private boolean hasAttackedThisRound = false; // TODO: 2021-05-08 reset at the start of the round

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
        Card card = currentMonster;
        currentMonster = null;
        status = "E";
        return card;
    }
}
