package Menus;

import Model.Cards.Card;
import Model.Cards.Monster;
import Model.CommonTools;

import java.util.ArrayList;
import java.util.Locale;
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
        else if (cardName.equals("The Calculator")) calculatorPower(card);
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
        duelProgramController.showGraveyard((turn + 1) % 2);
        System.out.println("please select one of enemy graveyard cards: ");
        while (true) {
            int cardNumber = CommonTools.scan.nextInt();
            CommonTools.scan.nextLine();
            if (cardNumber < 1 || cardNumber > enemyDeck.getGraveyardCards().size() + 1) {
                System.out.println("there is no card with that number");
                continue;
            }
            Monster cardToBeScan = (Monster) enemyDeck.getGraveyardCards().get(cardNumber - 1);
            if (!(cardToBeScan.getCardType().equals("Monster"))) {
                System.out.println("selected card is not a monster card");
                continue;
            }
            Monster monster = (Monster) card;
            monster.setAttackPoint(cardToBeScan.getAttackPoint());
            monster.setDefensePoint(cardToBeScan.getDefensePoint());
        }
    }

    public void exploderDragonPower() {
        GameDeck myDeck = gameDecks.get(turn);
        Card card = myDeck.getMonsterZones().get(selectedCardIndex).removeCard();
        myDeck.getGraveyardCards().add(card);
        System.out.println("Your monster card is destroyed by enemy monster effect");
        isEnemyTakeDamage = false;
    }

    public void gateGuardianPower() {
        System.out.println("Do you want special summon selected monster?");
        String command = CommonTools.scan.nextLine().trim().toLowerCase(Locale.ROOT);
        if (command.equals("no")) return;
        ArrayList<Card> monsterZoneCards = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Card monsterCard = gameDecks.get(turn).getMonsterZones().get(i).getCurrentMonster();
            if (monsterCard != null) monsterZoneCards.add(monsterCard);
        }
        if (monsterZoneCards.size() < 4) {
            System.out.println("there is no way you could special summon a monster");
            return;
        }
        while (true) {
            System.out.println("enter positions of tribute monster in monster zone:");
            int numberOfTribute = 0;
            ArrayList<Integer> positionOfTributeMonsters = new ArrayList<>();
            while (true) {
                int monsterZonePosition = 0;
                monsterZonePosition = CommonTools.scan.nextInt();
                CommonTools.scan.nextLine();
                if (monsterZonePosition < 1 || monsterZonePosition > 5) {
                    System.out.println("there no monsters on this address");
                    continue;
                }
                Monster tributeMonster = (Monster) gameDecks.get(turn).getMonsterZones().get(monsterZonePosition).getCurrentMonster();
                if (tributeMonster == null) {
                    System.out.println("there no monsters on this address");
                    continue;
                }
                positionOfTributeMonsters.add(monsterZonePosition);
                numberOfTribute += tributeMonster.getLevel();
                if (numberOfTribute == 3) break;
            }
            System.out.println("summoned successfully");
            isSummoned = 1;
            for (int monsterZonePosition : positionOfTributeMonsters) {
                gameDecks.get(turn).tributeCardFromMonsterZone(monsterZonePosition);
            }
            gameDecks.get(turn).getInHandCards().remove(selectedCardIndex - 1);
            duelProgramController.setEnteredMonsterCardIndex(
                    gameDecks.get(turn).summonCardToMonsterZone(selectedCard.getName()));
            duelProgramController.deselect();
            break;
        }
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
                    duelProgramController.deselect();
                } else {
                    System.out.println("no card found in the given position");
                }
            } else {
                System.out.println("you should special summon right now");
            }
        }
    }

    public void calculatorPower(Card card) {
        ArrayList<Monster> monsterZoneCards = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            if (!(gameDecks.get(turn).getMonsterZones().get(i).getStatus().equals("OO"))) continue;
            Monster monsterCard = (Monster) gameDecks.get(turn).getMonsterZones().get(i).getCurrentMonster();
            if (monsterCard != null) monsterZoneCards.add(monsterCard);
        }
        int sumOflevel = 0;
        for (Monster monster : monsterZoneCards) {
            sumOflevel += monster.getLevel();
        }
        Monster monster = (Monster) card;
        monster.setAttackPoint(sumOflevel * 300);
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