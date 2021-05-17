package Menus;

import Model.Cards.Card;

import java.util.ArrayList;

public class MonsterPowersController {
    private ArrayList <GameDeck> gameDecks = new ArrayList<>();

    public MonsterPowersController(ArrayList <GameDeck> gameDecks) {
        this.gameDecks = gameDecks;
    }

    public void monsterPowersWhenSummon(Card card) {
        String cardName = card.getName();

    }

    public void monsterPowersWhenDestroyed(Card card) {
        String cardName = card.getName();
        if (cardName.equals("Yomi Ship")) yomiShipPower();
    }

    public void monsterPowersWhenGetAttacked(Card card) {
        String cardName = card.getName();
    }

    public void monstersWithRitualPower(Card card) {
        String cardName = card.getName();
    }

    public void monstersWithSpecialSummonPower(Card card) {
        String cardName = card.getName();
    }

    public void yomiShipPower() {

    }

}