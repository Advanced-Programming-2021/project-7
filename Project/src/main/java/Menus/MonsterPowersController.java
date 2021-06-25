package Menus;

import Model.Cards.Card;
import Model.Cards.MonsterZone;
import Model.CommonTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;

public class MonsterPowersController {
    private ArrayList <GameDeck> gameDecks = new ArrayList<>();
    private DuelProgramController duelProgramController;
    private Phase phase;
    private int turn;
    private int selectedCardIndex;
    private int isSummoned = 0;
    private Card selectedCard;
    private Card attackerCard;
    private boolean isEnemyTakeDamage = true;

    public MonsterPowersController(ArrayList <GameDeck> gameDecks, DuelProgramController duelProgramController) {
        this.gameDecks = gameDecks;
        this.duelProgramController = duelProgramController;
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

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public void setIsSummoned(int isSummoned) {
        this.isSummoned = isSummoned;
    }

    public boolean getIsEnemyTakeDamage() {
        return this.isEnemyTakeDamage;
    }

    public void monsterPowersWhenSummon(Card card) {
        String cardName = card.getName();
        if (cardName.equals("Scanner")) ScannerPower(card);

    }

    public void monsterPowersWhenFlipsummon(Card card) {
        String cardName = card.getName();
        if (cardName.equals("Man-Eater Bug")) manEaterBugPower();
    }

    public void monsterPowersWhenDestroyed(Card card) {
        String cardName = card.getName();
        isEnemyTakeDamage = true;
        if (cardName.equals("Yomi Ship")) yomiShipPower();
        else if (cardName.equals("Exploder Dragon")) exploderDragonPower();
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
        if (isSummoned == 1) return;
        if (!isSummonAndSetValid()) return;
        else if (cardName.equals("Gate Guardian")) gateGuardianPower();
        else if (cardName.equals("The Tricky")) theTrickyPower();
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

    public void ScannerPower(Card card) {
        GameDeck enemyDeck = gameDecks.get((turn + 1) % 2);
        enemyDeck.getGraveyardCards();
    }

    public void exploderDragonPower() {
        GameDeck myDeck = gameDecks.get(turn);
        Card card = myDeck.getMonsterZones().get(selectedCardIndex).removeCard();
        myDeck.getGraveyardCards().add(card);
        System.out.println("Your monster card is destroyed by enemy monster effect");
        isEnemyTakeDamage = false;
    }

    public void gateGuardianPower() {

    }

    public void theTrickyPower() {
        System.out.println("Do you want special summon selected monster?");
        String command = CommonTools.scan.nextLine().trim().toLowerCase(Locale.ROOT);
        if (command.equals("no")) return;
        if (gameDecks.get(turn).getInHandCards().size() < 2) {
            System.out.println("there is no way you could special summon a monster");
            return;
        }
        System.out.println("Please select one card from your hand to tribute");
        while (true) {
            command = CommonTools.scan.nextLine();
            if (command.matches("^select --hand (\\d)")) {
                Matcher matcher = CommonTools.getMatcher(command, "(\\d)");
                matcher.find();
                int position = Integer.parseInt(matcher.group(1));
                if (position < 1 || position > 5) {
                    System.out.println("invalid selection");
                    continue;
                }
                ArrayList<Card> inHandCards = gameDecks.get(turn).getInHandCards();
                Card selectedCardFromHand = inHandCards.get(position - 1);
                if (selectedCardFromHand != null) {
                    System.out.println("summoned successfully");
                    duelProgramController.setSelectedMonsterCardIndex(selectedCardIndex);
                    duelProgramController.setIsSummoned(1);
                    gameDecks.get(turn).summonCardToMonsterZone(selectedCard.getName());
                    gameDecks.get(turn).getInHandCards().remove(position - 1);
                    gameDecks.get(turn).getInHandCards().remove(selectedCardIndex - 1);
                } else {
                    System.out.println("no card found in the given position");
                }
            } else {
                System.out.println("you should special summon right now");
            }
        }
    }

    private boolean isSummonAndSetValid() {
        if (phase != Phase.main1 && phase != Phase.main2) {
            System.out.println("action not allowed in this phase");
            return false;
        }
        if (gameDecks.get(turn).isMonsterZoneFull()) {
            System.out.println("monster card zone is full");
            return false;
        }
        return true;
    }
}