package Model.Card;

enum Types{
    Spell,
    Trap,
    Empty
}

public class SpellZone {
    private Card currentCard = null;
    private Types type = Types.Empty;
    private String status = "E";

    public boolean isEmpty() {
        if (status.equals("E"))
            return true;
        return false;
    }

    public String getStatus() {
        return status;
    }

    public void setTrap(Card card){
        currentCard = card;
        type = Types.Trap;
    }

    public void setSpell(Card card){
        currentCard = card;
        type = Types.Spell;
    }

    public void setHidden(){
        status = "H";
    }

    public void setVisible(){
        status = "O";
    }

    public Card removeCard(){
        Card card = currentCard;
        currentCard = null;
        type = Types.Empty;
        status = "E";
        return card;
    }
}
