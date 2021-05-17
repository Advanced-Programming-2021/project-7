package Menus;

import Model.Cards.Card;

import java.util.ArrayList;

public class MonsterPowersController {
    private ArrayList <GameDeck> gameDecks = new ArrayList<>();
    private int turn;
    private int selectedCardIndex;
    private Card selectedCard;
    private Card attackerCard;

    public MonsterPowersController(ArrayList <GameDeck> gameDecks) {
        this.gameDecks = gameDecks;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public void setAttackerCard(Card attackerCard) {
        this.attackerCard = attackerCard;
    }

    public void setSelectedCardIndex(int selectedCardIndex) {
        this.selectedCard = selectedCard;
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
//        if (cardName.equals("Crab Turtle"))
//        else if (cardName.equals("Skull Guardian"))
    }

    public void monstersWithSpecialSummonPower(Card card) {
        String cardName = card.getName();
    }

    public void yomiShipPower() {
        GameDeck myDeck = gameDecks.get(turn);
        Card card = myDeck.getMonsterZones().get(selectedCardIndex).removeCard();
        myDeck.getGraveyardCards().add(card);
        System.out.println("Your monster card is destroyed by enemy monster effect");
    }
}