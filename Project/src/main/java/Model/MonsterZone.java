package Model;

public class MonsterZone {
    private Card currentMonster = null;
    private String positioning = "E";

    public boolean isEmpty() {
        if (positioning.equals("E"))
            return true;
        return false;
    }

    public String getPositioning() {
        return positioning;
    }

    public Card getCurrentMonster() {
        return currentMonster;
    }

    public void setCardAttack(Card card) {
        currentMonster = card;
        positioning = "OO";
    }

    public void setCardDefense(Card card) {
        currentMonster = card;
        positioning = "DO";
    }

    public Card removeCard(){
        Card card = currentMonster;
        currentMonster = null;
        positioning = "E";
        return card;
    }
}
