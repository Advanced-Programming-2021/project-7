package Model.Card;

public class MonsterZone {
    private Card currentMonster = null;
    private String status = "E";

    public boolean isEmpty() {
        if (status.equals("E"))
            return true;
        return false;
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

    public Card removeCard(){
        Card card = currentMonster;
        currentMonster = null;
        status = "E";
        return card;
    }
}
