package Model.Card;

import Model.Card.Card;

public class Spell extends Card {
    private String spellType;
    private String icon;


    public Spell(String name, String type, String icon, String description, String trapType, int price) {
        super(name, description, type, price);
        this.spellType = spellType;
        this.icon = icon;
    }

    public void setSpellType(String spellType) {
        this.spellType = spellType;
    }

    public void setSpellIcon(String icon) {
        this.icon = icon;
    }

    public String getSpellType() {
        return spellType;
    }

    public String getSpellIcon() {
        return icon;
    }

    public String getSpellName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCardNumber() {
        return cardNumber;
    }
}