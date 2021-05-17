package Menus;

import Model.Cards.Card;
import Model.CommonTools;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

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
        if (cardName.equals("Scanner")) ScannerPower();

    }

    public void monsterPowersWhenFlipsummon(Card card) {
        String cardName = card.getName();
        if (cardName.equals("Man-Eater Bug")) manEaterBugPower();
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
//        if (cardName.equals("Gate Guardian"))
    }

    public void yomiShipPower() {
        GameDeck myDeck = gameDecks.get(turn);
        Card card = myDeck.getMonsterZones().get(selectedCardIndex).removeCard();
        myDeck.getGraveyardCards().add(card);
        System.out.println("Your monster card is destroyed by enemy monster effect");
    }

    public void manEaterBugPower() {
        GameDeck enemyDeck = gameDecks.get((turn + 1) % 2);
        System.out.println("select one of opponent's monsters to be destroyed:");
        String command;
        while (true) {
            command = CommonTools.scan.nextLine();
            if (command.matches("^select --monster (\\d)")) break;
            else System.out.println("invalid selection");
        }
        Matcher matcher = CommonTools.getMatcher(command, "(\\d)");
        matcher.find();
        int selectedMonster = Integer.parseInt(matcher.group(1));
        Card card = enemyDeck.getMonsterZones().get(selectedMonster).removeCard();
        enemyDeck.getGraveyardCards().add(card);
        System.out.println("your opponent’s monster is destroyed");
    }

    public void ScannerPower() {
        
    }
}