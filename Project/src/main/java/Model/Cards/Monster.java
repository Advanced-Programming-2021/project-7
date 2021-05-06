package Model;

import Model.Cards.Card;

public class Monster extends Card {
    private int attackPoint;
    private int defensePoint;
    private int level;
    private String attribute;
    private String monsterType;
    private String cardType;

    public Monster(String name, int level, String attribute
            , String monsterType, String cardType, int attackPoint,
                   int defensePoint, String description, int price, String type) {
        super(name, description, type, price);
        this.attackPoint = attackPoint;
        this.defensePoint = defensePoint;
        this.level = level;
        this.attribute = attribute;
        this.monsterType = monsterType;
        this.cardType = cardType;
    }

    public void setAttackPoint(int attackPoint) {
        this.attackPoint = attackPoint;
    }

    public void setDefensePoint(int defensePoint) {
        this.defensePoint = defensePoint;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setMonsterType(String monsterType) {
        this.monsterType = monsterType;
    }

    public int getAttackPoint() {
        return attackPoint;
    }

    public int getDefensePoint() {
        return defensePoint;
    }

    public int getLevel() {
        return level;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getMonsterType() {
        return monsterType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCardNumber() {
        return cardNumber;
    }

//    public Monster Clone1(){
//        return new Monster(name, level, attribute, monsterType
//                , cardType, attackPoint, defensePoint, description, price, type);
//    }
}