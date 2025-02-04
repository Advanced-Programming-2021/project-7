package Menus;


import Model.Cards.*;
import Model.CommonTools;
import Model.Deck;
import Model.Player;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

enum Phase {
    draw,
    standby,
    main1,
    battle,
    main2,
    end;
    private static Phase[] values = values();

    public Phase next() {
        return values[(this.ordinal() + 1) % values.length];
    }
}


public class DuelProgramController {
    public static String firstPlayer;
    public static String secondPlayer;
    public static int round;
    private ArrayList<GameDeck> gameDecks = new ArrayList<>(2);
    private MonsterPowersController monsterPowersController = new MonsterPowersController(gameDecks, this);
    public int turn = 0; //0 : firstPlayer, 1 : secondPlayer
    private int isSummoned = 0; //0 : is not summoned before, 1 : is summoned before
    private Card selectedCard = null;
    private int selectedCardIndex = -1; // -1 means Empty
    private int enteredMonsterCardIndex = -1;
    private int changedPositionMonsterIndex = -1;
    private String selectedDeck = null; // hand, monster, spell, field,
    // opponentMonster, opponentSpell, opponentField
    public Phase phase = Phase.draw;
    private int timeSealTrap = 0;
    private int isCardDrawn = 0;
    private int isGameStart = 2;
    private boolean messengerChecked = false;
    private int isAI = 0;
    private AI ai = new AI();
    private boolean controlPressed = false;
    private boolean shiftPressed = false;
    private boolean cPressed = false;

    public void setField() {
        if (isRoundOver()) roundOver(turn);
        if (isGameOver()) gameOver(round);
        if (phase == Phase.standby && !messengerChecked) {
            keepMessengerOfPeace();
            messengerChecked = true;
        }
        if (phase == Phase.draw && isCardDrawn == 0 && isGameStart == 0 && timeSealTrap == 0) drawCard();
    }

    public void checkForSetOrSummon() {
        if (phase != Phase.battle && selectedCard != null) {
            if (selectedCard.getType().equals("Monster")) {
                String[] buttons = {"Set", "Summon"};
                int returnValue = JOptionPane.showOptionDialog(null, "Summon or Set Monster", "Summon or Set Monster",
                        JOptionPane.OK_OPTION, 1, null, buttons, buttons[0]);
                if (returnValue == 0)
                    JOptionPane.showMessageDialog(null, set());
                else if (returnValue == 1) summonMonster();
            } else if ((selectedCard.getType().equals("Spell") || selectedCard.getType().equals("Trap"))) {
                JOptionPane.showMessageDialog(null, set());
            }
            setField();
        }
    }

    public void run(String firstPlayer, String secondPlayer, int round) {
        DuelProgramController.round = round;
        DuelProgramController.firstPlayer = firstPlayer;
        DuelProgramController.secondPlayer = secondPlayer;
        setGameDecks(firstPlayer, secondPlayer);
        for (int j = 0; j < 5; j++) {
            gameDecks.get(turn).drawCard();
            gameDecks.get(changeTurn(turn)).drawCard();
        }

    }

    public boolean isRoundOver() {
        if (gameDecks.get(0).getPlayerLP() <= 0) {
            roundOver(0);
            return true;
        } else if (gameDecks.get(1).getPlayerLP() <= 0) {
            roundOver(1);
            return true;
        } else return false;
    }

    public boolean isGameOver() {
        if (round == 3) {
            if (gameDecks.get(0).getWinRounds() > 1) return true;
            if (gameDecks.get(1).getWinRounds() > 1) return true;
        } else {
            if (gameDecks.get(0).getWinRounds() == 1) return true;
            if (gameDecks.get(1).getWinRounds() == 1) return true;
        }
        return false;
    }

    private void setGameDecks(String firstPlayer, String secondPlayer) {
        String firstNick = Player.getPlayerByUsername(firstPlayer).getNickname();
        String secondNick = Player.getPlayerByUsername(secondPlayer).getNickname();
        Deck activeDeck1 = Player.getActiveDeckByUsername(firstPlayer);
        Deck activeDeck2 = Player.getActiveDeckByUsername(secondPlayer);
        GameDeck gameDeckFirst = new GameDeck(firstNick, firstPlayer,
                Deck.getMainDeckByDeck(activeDeck1));
        GameDeck gameDeckSecond = new GameDeck(secondNick, secondPlayer,
                Deck.getMainDeckByDeck(activeDeck2));
        gameDecks.add(gameDeckFirst);
        gameDecks.add(gameDeckSecond);
    }

    private void showGameDeck(int turn) {

        GameDeck myDeck = gameDecks.get(turn);
        GameDeck enemyDeck = gameDecks.get(changeTurn(turn));
        System.out.println("**************************");
        showEnemyDeck(enemyDeck);
        System.out.println("--------------------------");
        showMyDeck(myDeck);
        System.out.println("**************************");
    }

    private void showEnemyDeck(GameDeck enemyDeck) {
        System.out.println(enemyDeck.getPlayerNickName() + " : " + enemyDeck.getPlayerLP());
        System.out.printf("\t");
        for (int i = 0; i < enemyDeck.getInHandCards().size(); i++) {
            System.out.printf("c\t");
        }
        System.out.printf("\n");
        System.out.println(enemyDeck.getDeck().size());

        System.out.printf("\t");
        for (int i = 5; i >= 1; i--) {
            System.out.printf("%s\t", enemyDeck.getSpellZones().get(i).getStatus());
        }
        System.out.printf("\n");

        System.out.printf("\t");
        for (int i = 5; i >= 1; i--) {
            System.out.printf("%s\t", enemyDeck.getMonsterZones().get(i).getStatus());
        }
        System.out.printf("\n");

        System.out.println(enemyDeck.getGraveyardCards().size() + "\t\t\t\t\t\t" + enemyDeck.getFieldZoneStatus());
    }

    private void showMyDeck(GameDeck myDeck) {
        System.out.println(myDeck.getFieldZoneStatus() + "\t\t\t\t\t\t" + myDeck.getGraveyardCards().size());

        System.out.printf("\t");
        for (int i = 1; i <= 5; i++) {
            System.out.printf("%s\t", myDeck.getMonsterZones().get(i).getStatus());
        }
        System.out.printf("\n");

        System.out.printf("\t");
        for (int i = 1; i <= 5; i++) {
            System.out.printf("%s\t", myDeck.getSpellZones().get(i).getStatus());
        }
        System.out.printf("\n");
        System.out.println("\t\t\t\t\t\t" + myDeck.getDeck().size());
        for (int i = 0; i < myDeck.getInHandCards().size(); i++) {
            System.out.printf("c\t");
        }
        System.out.println();
        System.out.println(myDeck.getPlayerNickName() + " : " + myDeck.getPlayerLP());
    }

    private void selectCard(String command) {
        String address = command.substring(7);
        if (!isAddressValid(address)) {
            System.out.println("invalid selection");
            return;
        }
        if (address.matches("^(?:(?:--monster|--spell|--field|--hand|--opponent)( (\\d+))* ?){3}$"))
            selectOpponentDeck(address);
        else selectMyDeck(address);
    }

    public void deselect() {
        if (selectedCard == null) {
            System.out.println("no card is selected yet");
            return;
        }
        selectedCard = null;
        selectedCardIndex = -1;
        selectedDeck = null;
        System.out.println("card deselected");
    }

    private void selectMyDeck(String address) {
        if (address.matches("^--monster (\\d+)$")) {
            int position = Integer.parseInt(CommonTools.takeNameOutOfCommand(address, "--monster"));
            selectMonster(position);
        } else if (address.matches("^--spell (\\d+)$")) {
            int position = Integer.parseInt(CommonTools.takeNameOutOfCommand(address, "--spell"));
            selectSpell(position);
        } else if (address.matches("^--hand (\\d+)$")) {
            int position = Integer.parseInt(CommonTools.takeNameOutOfCommand(address, "--hand"));
            selectHand(position);
        } else if (address.matches("^--field$"))
            selectField();
    }

    private void selectOpponentDeck(String address) {
        if (address.matches("^(?:(?:--monster|--opponent)( (\\d+))* ?){3}$")) {
            int position = Integer.parseInt(CommonTools.takeNameOutOfCommand(address, "--monster"));
            selectOpponentMonster(position);
        } else if (address.matches("^(?:(?:--spell|--opponent)( (\\d+))* ?){3}$")) {
            int position = Integer.parseInt(CommonTools.takeNameOutOfCommand(address, "--spell"));
            selectOpponentSpell(position);
        } else if (address.matches("^(?:(?:--field|--opponent)( (\\d+))* ?){2}$"))
            selectOpponentField();
    }

    private boolean isAddressValid(String address) {
        if (!address.matches("^(?:(?:--monster|--spell|--field|--hand|--opponent)( (\\d+))* ?){1,3}$"))
            return false;
        String monster = CommonTools.takeNameOutOfCommand(address, "--monster");
        String spell = CommonTools.takeNameOutOfCommand(address, "--spell");
        String field = CommonTools.takeNameOutOfCommand(address, "--field");
        String hand = CommonTools.takeNameOutOfCommand(address, "--hand");
        String opponent = CommonTools.takeNameOutOfCommand(address, "--opponent");
        if ((monster == null && spell == null && field != null && hand == null) || opponent != null) return false;
        if (address.contains("--monster")) {
            if (monster == null) return false;
            if (Integer.parseInt(monster) > 5 || Integer.parseInt(monster) < 1) return false;
        }
        if (address.contains("--spell")) {
            if (spell == null) return false;
            if (Integer.parseInt(spell) > 5 || Integer.parseInt(spell) < 1) return false;
        }
        if (address.contains("--hand")) {
            if (address.contains("--opponent")) return false;
            if (hand == null) return false;
            if (Integer.parseInt(hand) > gameDecks.get(turn).getInHandCards().size() || Integer.parseInt(hand) < 1)
                return false;
        }
        return true;
    }

    public String selectMonster(int position) {
        if (gameDecks.get(turn).getMonsterZones().get(position).isEmpty()) {
            System.out.println("no card found in the given position");
            return "no card found in the given position";
        }
        selectedCard = gameDecks.get(turn).getMonsterZones().get(position).getCurrentMonster();
        selectedCardIndex = position;
        selectedDeck = "monster";
        System.out.println("card selected");
        return "card selected";
    }

    public String selectSpell(int position) {
        if (gameDecks.get(turn).getSpellZones().get(position).isEmpty()) {
            System.out.println("no card found in the given position");
            return "no card found in the given position";
        }
        selectedCard = gameDecks.get(turn).getSpellZones().get(position).getCurrentCard();
        selectedCardIndex = position;
        selectedDeck = "spell";
        System.out.println("card selected");
        return "card selected";
    }

    private void selectField() {
        if (gameDecks.get(turn).isSpellZoneEmpty()) {
            System.out.println("no card found in the given position");
            return;
        }
        selectedCard = gameDecks.get(turn).getFieldZone();
        selectedCardIndex = -1;
        selectedDeck = "field";
        System.out.println("card selected");
    }

    public String selectHand(int position) {
        ArrayList<Card> inHandCards = gameDecks.get(turn).getInHandCards();
        selectedCard = inHandCards.get(position - 1);
        selectedCardIndex = position;
        selectedDeck = "hand";
        System.out.println("card selected");
        return "card selected";
//        monsterPowersController.setSelectedCardIndex(selectedCardIndex);
//        monsterPowersController.setSelectedCard(selectedCard);
//        monsterPowersController.setTurn(turn);
//        monsterPowersController.setPhase(phase);
//        monsterPowersController.monstersWithSpecialSummonPower(selectedCard);
    }

    private void selectOpponentMonster(int position) {
        if (gameDecks.get(changeTurn(turn)).getMonsterZones().get(position).isEmpty()) {
            System.out.println("no card found in the given position");
            return;
        }
        selectedCard = gameDecks.get(changeTurn(turn)).getMonsterZones().get(position).getCurrentMonster();
        selectedCardIndex = position;
        selectedDeck = "opponentMonster";
    }

    private void selectOpponentSpell(int position) {
        if (gameDecks.get(changeTurn(turn)).getSpellZones().get(position).isEmpty()) {
            System.out.println("no card found in the given position");
            return;
        }
        selectedCard = gameDecks.get(changeTurn(turn)).getSpellZones().get(position).getCurrentCard();
        selectedCardIndex = position;
        selectedDeck = "opponentSpell";
    }

    private void selectOpponentField() {
        if (gameDecks.get(changeTurn(turn)).isSpellZoneEmpty()) {
            System.out.println("no card found in the given position");
            return;
        }
        selectedCard = gameDecks.get(changeTurn(turn)).getFieldZone();
        selectedCardIndex = -1;
        selectedDeck = "opponentField";
    }

    public String summonMonster() {
        int position = selectedCardIndex;
        ArrayList<Card> inHandCards = gameDecks.get(turn).getInHandCards();
        if (selectedCard == null) {
            System.out.println("no card is selected yet");
            return "no card is selected yet";
        }
        if (!selectedDeck.equals("hand") || !inHandCards.get(position - 1).getType().equals("Monster")) { //TODO check is Type correct
            System.out.println("you can’t summon this card");
            return "you can’t summon this card";
        }
        if (!isSummonAndSetValid(position)) return "summon is not valid";
        Monster selectedMonster = (Monster) selectedCard;
        if (selectedMonster.getLevel() <= 4) {
            System.out.println("summoned successfully");
            monsterPowersController.setTurn(turn);
            monsterPowersController.monsterPowersWhenSummon(selectedCard);
            activateOrDeactivateFieldCardForAll(-1);
            isSummoned = 1;
            enteredMonsterCardIndex = gameDecks.get(turn).summonCardToMonsterZone(selectedCard.getName());
            gameDecks.get(turn).getInHandCards().remove(position - 1);
            activateOrDeactivateFieldCardForAll(1);
            deselect();
        } else if (selectedMonster.getLevel() == 5 || selectedMonster.getLevel() == 6) summonWithOneTribute(position);
        else if (selectedMonster.getLevel() == 7 || selectedMonster.getLevel() == 8) summonWithTwoTribute(position);
        return "summoned successfully";
    }

    private void summonWithOneTribute(int position) {
        HashMap<Integer, MonsterZone> monsterZones = gameDecks.get(turn).getMonsterZones();
        int numberOfEmptyMonsterZones = 0;
        for (int i = 1; i <= 5; i++) {
            if (monsterZones.get(i).getCurrentMonster() == null)
                numberOfEmptyMonsterZones = numberOfEmptyMonsterZones + 1;
        }
        if (numberOfEmptyMonsterZones == 5) {
            System.out.println("there are not enough cards for tribute");
            return;
        }
        System.out.println("enter position of tribute monster in monster zone:");
        int monsterZonePosition = 0;
        if (isAI == 1) {
            monsterZonePosition = firstTributeIndex();
        } else {
            monsterZonePosition = CommonTools.scan.nextInt();
            CommonTools.scan.nextLine();
        }
        if (monsterZonePosition < 1 || monsterZonePosition > 5) {
            System.out.println("there no monsters on this address");
            return;
        }
        if (monsterZones.get(monsterZonePosition).getCurrentMonster() == null) {
            System.out.println("there no monsters on this address");
            return;
        }
        System.out.println("summoned successfully");
        isSummoned = 1;
        gameDecks.get(turn).tributeCardFromMonsterZone(monsterZonePosition);
        gameDecks.get(turn).getInHandCards().remove(position - 1);
        enteredMonsterCardIndex = gameDecks.get(turn).summonCardToMonsterZone(selectedCard.getName());
        deselect();
    }

    private int firstTributeIndex() {
        for (int i = 1; i <= 5; i++) {
            if (gameDecks.get(turn).getMonsterZones().get(i).getCurrentMonster() != null) {
                return i;
            }
        }
        return 1;
    }

    private int secondTributeIndex() {
        int state = 0;
        for (int i = 1; i <= 5; i++) {
            if (gameDecks.get(turn).getMonsterZones().get(i).getCurrentMonster() != null) {
                state = state + 1;
            }
            if (state == 2) {
                return i;
            }
        }
        return 2;
    }

    private void summonWithTwoTribute(int position) {
        HashMap<Integer, MonsterZone> monsterZones = gameDecks.get(turn).getMonsterZones();
        int numberOfEmptyMonsterZones = 0;
        for (int i = 1; i <= 5; i++) {
            if (monsterZones.get(i).getCurrentMonster() == null)
                numberOfEmptyMonsterZones = numberOfEmptyMonsterZones + 1;
        }
        if (numberOfEmptyMonsterZones == 5 || numberOfEmptyMonsterZones == 4) {
            System.out.println("there are not enough cards for tribute");
            return;
        }
        System.out.println("enter positions of tribute monster in monster zone:");
        int firstMonster = 0;
        int secondMonster = 0;
        if (isAI == 1) {
            firstMonster = firstTributeIndex();
            secondMonster = secondTributeIndex();

        } else {
            firstMonster = CommonTools.scan.nextInt();
            CommonTools.scan.nextLine();
            secondMonster = CommonTools.scan.nextInt();
            CommonTools.scan.nextLine();
        }
        if (firstMonster < 1 || firstMonster > 5 || secondMonster < 1 || secondMonster > 5) {
            System.out.println("there are no monsters on one of this addresses");
            return;
        }
        if (monsterZones.get(firstMonster).getCurrentMonster() == null ||
                monsterZones.get(firstMonster).getCurrentMonster() == null) {
            System.out.println("there are no monsters on one of this addresses");
            return;
        }
        System.out.println("summoned successfully");
        isSummoned = 1;
        gameDecks.get(turn).tributeCardFromMonsterZone(firstMonster);
        gameDecks.get(turn).tributeCardFromMonsterZone(secondMonster);
        gameDecks.get(turn).getInHandCards().remove(position - 1);
        enteredMonsterCardIndex = gameDecks.get(turn).summonCardToMonsterZone(selectedCard.getName());
        deselect();
    }

    public String set() {
        int position = selectedCardIndex;
        ArrayList<Card> inHandCards = gameDecks.get(turn).getInHandCards();
        if (selectedCard == null) {
            System.out.println("no card is selected");
            return "no card is selected";
        }
        if (!selectedDeck.equals("hand")) {
            System.out.println("you can’t set this card");
            return "you can’t set this card";
        }
        if (!inHandCards.get(position - 1).getType().equals("Monster") &&
                !inHandCards.get(position - 1).getType().equals("Spell") &&
                !inHandCards.get(position - 1).getType().equals("Trap")) { //TODO check is Type correct
            System.out.println("you can’t set this card");
            return "you can’t set this card";
        }
        if (inHandCards.get(position - 1).getType().equals("Monster"))
            return setMonster(position);
        else if (inHandCards.get(position - 1).getType().equals("Spell"))
            return setSpell();
        else if (inHandCards.get(position - 1).getType().equals("Trap"))
            return setTrap();
        return "Error";
    }

    private String setMonster(int position) {
        if (!isSummonAndSetValid(position)) return "set is not valid";
        System.out.println("set successfully");
        activateOrDeactivateFieldCardForAll(-1);
        enteredMonsterCardIndex = gameDecks.get(turn).setCardToMonsterZone(selectedCard.getName());
        gameDecks.get(turn).getInHandCards().remove(position - 1);
        isSummoned = 1;
        activateOrDeactivateFieldCardForAll(1);
        deselect();
        return "set successfully";
    }

    private boolean isSummonAndSetValid(int position) {
        HashMap<Integer, MonsterZone> monsterZones = gameDecks.get(turn).getMonsterZones();
        if (phase != Phase.main1 && phase != Phase.main2) {
            System.out.println("action not allowed in this phase");
            return false;
        }
        if (gameDecks.get(turn).isMonsterZoneFull()) {
            System.out.println("monster card zone is full");
            return false;
        }
        if (isSummoned == 1) {
            System.out.println("you already summoned/set on this turn");
            return false;
        }
        return true;
    }

    public String setPositionMonster(String command) {
        HashMap<Integer, MonsterZone> monsterZones = gameDecks.get(turn).getMonsterZones();
        if (!isSetPositionValid()) return "change is invalid";
        if (command.matches("set --position attack")) {
            if (!monsterZones.get(selectedCardIndex).getStatus().equals("DO")) {
                System.out.println("this card is already in the wanted position");
                return "this card is already in the wanted position";
            }
            if (enteredMonsterCardIndex == selectedCardIndex || changedPositionMonsterIndex == selectedCardIndex) {
                System.out.println("you already changed this card position in this turn");
                return "you already changed this card position in this turn";
            }
            Card card = monsterZones.get(selectedCardIndex).getCurrentMonster();
            gameDecks.get(turn).getMonsterZones().get(selectedCardIndex).setCardAttack(card);
        } else {
            if (!monsterZones.get(selectedCardIndex).getStatus().equals("OO")) {
                System.out.println("this card is already in the wanted position");
                return "this card is already in the wanted position";
            }
            if (changedPositionMonsterIndex == selectedCardIndex) {
                System.out.println("you already changed this card position in this turn");
                return "you already changed this card position in this turn";
            }
            Card card = monsterZones.get(selectedCardIndex).getCurrentMonster();
            gameDecks.get(turn).getMonsterZones().get(selectedCardIndex).setCardDefense(card);
        }
        System.out.println("monster card position changed successfully");
        changedPositionMonsterIndex = selectedCardIndex;
        return "monster card position changed successfully";
    }

    private boolean isSetPositionValid() {
        if (selectedCard == null) {
            System.out.println("no card is selected");
            return false;
        }
        if (!selectedDeck.equals("monster")) {
            System.out.println("you can’t change this card position");
            return false;
        }
        if (phase != Phase.main1 && phase != Phase.main2) {
            System.out.println("action not allowed in this phase");
            return false;
        }
        return true;
    }

    public String flipSummon() {
        HashMap<Integer, MonsterZone> monsterZones = gameDecks.get(turn).getMonsterZones();
        if (!isFlipSummonValid()) return "flip is invalid";
        if (changedPositionMonsterIndex == selectedCardIndex ||
                enteredMonsterCardIndex == selectedCardIndex ||
                !monsterZones.get(selectedCardIndex).getStatus().equals("DH")) {
            System.out.println("you can’t flip summon this card");
            return "you can’t flip summon this card";
        }
        Card card = monsterZones.get(selectedCardIndex).getCurrentMonster();
        gameDecks.get(turn).getMonsterZones().get(selectedCardIndex).setCardAttack(card);
        System.out.println("flip summoned successfully");
        changedPositionMonsterIndex = selectedCardIndex;
        monsterPowersController.setTurn(turn);
        monsterPowersController.monsterPowersWhenFlipsummon(selectedCard);
        return "flip summoned successfully";
    }

    private boolean isFlipSummonValid() {
        if (selectedCard == null) {
            System.out.println("no card is selected");
            return false;
        }
        if (!selectedDeck.equals("monster")) {
            System.out.println("you can’t change this card position");
            return false;
        }
        if (phase != Phase.main1 && phase != Phase.main2) {
            System.out.println("action not allowed in this phase");
            return false;
        }
        return true;
    }

    public String attackCard(String command) {
        int selectDefender;
        Matcher matcher = CommonTools.getMatcher(command, "(\\d+)");
        matcher.find();
        selectDefender = Integer.parseInt(matcher.group(1));
        GameDeck myDeck = gameDecks.get(turn);
        GameDeck enemyDeck = gameDecks.get(changeTurn(turn));
        if (selectedCard == null) {
            System.out.println("no card is selected yet");
            return "no card is selected yet";
        } else if (!(selectedCard instanceof Monster)) {
            System.out.println("you can’t attack with this card");
            return "you can’t attack with this card";
        } else if (phase != Phase.battle) {
            System.out.println("you can’t do this action in this phase");
            return "you can’t do this action in this phase";

        } else if (!myDeck.getMonsterZones().get(selectedCardIndex).getStatus().equals("OO")) {
            System.out.println("This card is not in attacking position");
            return "This card is not in attacking position";

        } else if (myDeck.getMonsterZones().get(selectedCardIndex).getHasAttackedThisRound()) {
            System.out.println("this card already attacked");
            return "this card already attacked";
        } else if (messengerOfPeaceBlocks(((Monster) selectedCard).getAttackPoint())) {
            System.out.println("An Spell Card Stops this monster from attacking");
            return "An Spell Card Stops this monster from attacking";
        } else if (enemyDeck.getMonsterZones().get(selectDefender).isEmpty()) {
            System.out.println("there is no card to attack here");
            return "there is no card to attack here";
        } else {
            if (!checkTrap()) {
                gameDecks.get(turn).getMonsterZones().get(selectedCardIndex).attack();
                if (enemyDeck.getMonsterZones().get(selectDefender).getStatus().equals("OO")) {
                    return attackOO(selectDefender, myDeck, enemyDeck);
                } else if (enemyDeck.getMonsterZones().get(selectDefender).getStatus().equals("DO")) {
                    return attackDO(selectDefender, myDeck, enemyDeck);
                } else if (enemyDeck.getMonsterZones().get(selectDefender).getStatus().equals("DH")) {
                    return attackDH(selectDefender, myDeck, enemyDeck);
                }
                return "Error";
            }
            return "Trap Intervened";
        }
    }

    public boolean checkTrap() {
        int trapTurn = changeTurn(turn);
        boolean doesMirrorForceExist = gameDecks.get(trapTurn).doesTrapExist("Mirror Force");
        boolean doesNegateAttackExist = gameDecks.get(trapTurn).doesTrapExist("Negate Attack");
        if (!doesMirrorForceExist && !doesNegateAttackExist) return false;
        String username = gameDecks.get(trapTurn).getPlayerUserName();
        System.out.printf("now it will be %s's turn\n", username);
        showGameDeck(trapTurn);
        System.out.println("do you want to activate your spell and trap? yes/no");
        while (true) {
            String confirmation;
            if (isAI == 1) {
                confirmation = "no";
            } else {
                confirmation = CommonTools.scan.nextLine();
            }
            if (confirmation.equals("no")) return false;
            else if (confirmation.equals("yes")) break;
            else System.out.println("invalid command");
        }

        if (doesMirrorForceExist && !doesNegateAttackExist) activateTrapMirrorForce();
        else if (!doesMirrorForceExist && doesNegateAttackExist) activateTrapNegateAttack();
        else {
            System.out.println("do you want to activate 'Mirror Force' or 'Negate Attack' ?");
            while (true) {
                String trap = CommonTools.scan.nextLine();
                if (trap.equals("Mirror Force")) {
                    activateTrapMirrorForce();
                    break;
                } else if (trap.equals("Negate Attack")) {
                    activateTrapNegateAttack();
                    break;
                } else System.out.println("invalid command");
            }
        }
        return true;
    }

    public String attackOO(int selectDefender, GameDeck myDeck, GameDeck enemyDeck) {
        Monster selectedMonster = (Monster) selectedCard;
        int attackerDamage = selectedMonster.getAttackPoint();
        int defenderDamage = ((Monster) enemyDeck.getMonsterZones()
                .get(selectDefender).getCurrentMonster()).getAttackPoint();
        int damage = attackerDamage - defenderDamage;
        System.out.println(attackerDamage + " " + defenderDamage);
        monsterPowersController.setSelectedCardIndex(selectedCardIndex);
        monsterPowersController.setAttackerCard(selectedCard);
        monsterPowersController.setTurn(turn);
        if (damage > 0) {
            monsterPowersController.monsterPowersWhenDestroyed(enemyDeck.getMonsterZones()
                    .get(selectDefender).getCurrentMonster());
            if (monsterPowersController.getIsEnemyTakeDamage()) enemyDeck.takeDamage(damage);
            moveToGraveyard(changeTurn(turn), "MonsterZone", selectDefender);
            return "your opponent’s monster is destroyed and your opponent receives " + damage + " battle damage#opponent " + damage + " 0";
        } else if (damage == 0) {
            monsterPowersController.monsterPowersWhenDestroyed(enemyDeck.getMonsterZones()
                    .get(selectDefender).getCurrentMonster());
            moveToGraveyard(turn, "MonsterZone", selectedCardIndex);
            moveToGraveyard(changeTurn(turn), "MonsterZone", selectDefender);
            return "both you and your opponent monster cards are destroyed and no one receives damage#both" + selectedCardIndex + " 0 0";
        } else {
            monsterPowersController.monsterPowersWhenDestroyed(enemyDeck.getMonsterZones()
                    .get(selectDefender).getCurrentMonster());
            Card card = myDeck.getMonsterZones().get(selectedCardIndex).removeCard();
            moveToGraveyard(turn, "MonsterZone", selectedCardIndex);
            myDeck.takeDamage(-1 * damage);
            return "Your monster card is destroyed and you received " + damage + " damage#your" + selectedCardIndex + " 0 " + damage;
        }
    }

    public String attackDO(int selectDefender, GameDeck myDeck, GameDeck enemyDeck) {
        Monster selectedMonster = (Monster) selectedCard;
        int attackerDamage = selectedMonster.getAttackPoint();
        int defenderDamage = ((Monster) enemyDeck.getMonsterZones().get(selectDefender)
                .getCurrentMonster()).getDefensePoint();
        System.out.println(attackerDamage + " " + defenderDamage);
        int damage = attackerDamage - defenderDamage;
        if (damage > 0) {
            monsterPowersController.setSelectedCardIndex(selectedCardIndex);
            monsterPowersController.setAttackerCard(selectedCard);
            monsterPowersController.setTurn(turn);
            monsterPowersController.monsterPowersWhenDestroyed(enemyDeck.getMonsterZones()
                    .get(selectDefender).getCurrentMonster());
            moveToGraveyard(changeTurn(turn), "MonsterZone", selectDefender);
            System.out.println("the defense position monster is destroyed");
            return "the defense position monster is destroyed#opponent 0 0";
        } else if (attackerDamage == defenderDamage) {
            System.out.println("no card is destroyed");
            return "no card is destroyed#none 0 0";
        } else {
            myDeck.takeDamage(-1 * damage);
            System.out.printf("no card is destroyed and you received %d battle damage\n", damage);
            return "no card is destroyed and you received " + damage + " battle damage#none 0 " + damage;
        }
    }

    public String attackDH(int selectDefender, GameDeck myDeck, GameDeck enemyDeck) {
        Monster selectedMonster = (Monster) selectedCard;
        String enemyCardName = enemyDeck.getMonsterZones()
                .get(selectDefender).getCurrentMonster().getName();
        int attackerDamage = selectedMonster.getAttackPoint();
        int defenderDamage = ((Monster) enemyDeck.getMonsterZones().get(selectDefender)
                .getCurrentMonster()).getDefensePoint();
        enemyDeck.getMonsterZones().get(selectDefender).setAttacked();
        enemyDeck.getMonsterZones().get(selectDefender);
        System.out.println(attackerDamage + " " + defenderDamage);
        int damage = attackerDamage - defenderDamage;
        if (damage > 0) {
            monsterPowersController.setSelectedCardIndex(selectedCardIndex);
            monsterPowersController.setAttackerCard(selectedCard);
            monsterPowersController.setTurn(turn);
            monsterPowersController.monsterPowersWhenDestroyed(enemyDeck.getMonsterZones()
                    .get(selectDefender).getCurrentMonster());
            moveToGraveyard(changeTurn(turn), "MonsterZone", selectDefender);
            return "the defense position monster (" + enemyCardName + ") is destroyed#opponent 0 0";
        } else if (damage == 0) {
            return "enemy card was " + enemyCardName + ", no card is destroyed#none 0 0";
        } else {
            myDeck.takeDamage(-1 * damage);
            return "enemy card was " + enemyCardName + ", no card is destroyed and you received " + damage + " battle damage#none 0 " + damage;
        }
    }

    public String directAttack() {
        GameDeck myDeck = gameDecks.get(turn);
        GameDeck enemyDeck = gameDecks.get(changeTurn(turn));
        if (selectedCard == null) {
            return "no card is selected yet";
        } else if (!(selectedCard instanceof Monster)) {
            return "you can’t attack with this card";
        } else if (phase != Phase.battle) {
            return "you can’t do this action in this phase";
        } else if (!myDeck.getMonsterZones().get(selectedCardIndex).getStatus().equals("OO")) {
            return "This card is not in attacking position";
        } else if (messengerOfPeaceBlocks(((Monster) selectedCard).getAttackPoint())) {
            return "An Spell Card Stops this monster from attacking";
        } else if (myDeck.getMonsterZones().get(selectedCardIndex).getHasAttackedThisRound()) {
            return "this card already attacked";
        } else {
            gameDecks.get(turn).getMonsterZones().get(selectedCardIndex).attack();
            Monster selectedMonster = (Monster) selectedCard;
            int attackerDamage = selectedMonster.getAttackPoint();
            enemyDeck.takeDamage(attackerDamage);
            setField();
            return "your opponent receives " + attackerDamage + " battle damage";
        }
    }

    public String activateSpellErrorCheck() {
        if (selectedCard == null) {
            System.out.println("no card is selected yet");
            return "no card is selected yet";
        } else if (!(selectedCard instanceof Spell)) {
            System.out.println("activate effect is only for spell cards.");
            return "activate effect is only for spell cards.";
        } else if (!(phase == Phase.main1 || phase == Phase.main2 ||
                ((Spell) selectedCard).getSpellIcon().equals("Quick-play"))) {
            System.out.println("you can’t activate an effect on this turn");
            return "you can’t activate an effect on this turn";
        } else if ((selectedDeck.equals("spell")
                && gameDecks.get(turn).getSpellZones().get(selectedCardIndex).getStatus().equals("O"))
                || (selectedDeck.equals("field") && gameDecks.get(turn).getFieldZoneStatus().equals("O"))) {
            System.out.println("you have already activated this card");
            return "you have already activated this card";
        } else if (gameDecks.get(turn).isSpellZoneFull() && ((CommonTools.getMatcher
                (((Spell) selectedCard).getSpellIcon(), "Continuous|Field").matches()))) {
            System.out.println("spell card zone is full");
            return "spell card zone is full";
        }
        spellAbsorptionCheck();
        Spell spell = (Spell) selectedCard;
        if (selectedDeck.equals("hand")) {
            if (CommonTools.getMatcher(spell.getSpellIcon(), "Continuous").matches()) {
                moveToGraveyard(turn, "inHand", selectedCardIndex - 1);
                int freeIndex = gameDecks.get(turn).spellZoneFirstFreeSpace();
                SpellZone spellZone = gameDecks.get(turn).getSpellZones().get(freeIndex);
                spellZone.setSpell(selectedCard);
                spellZone.setVisible();
                System.out.println("Spell activated");
                return "Spell activated";
            } else if (CommonTools.getMatcher(spell.getSpellIcon(), "Field").matches()) {
                moveToGraveyard(turn, "inHand", selectedCardIndex - 1);
                activateOrDeactivateFieldCardForAll(-1);
                if (!gameDecks.get(turn).isFieldZoneEmpty())
                    moveToGraveyard(turn, "field", 0);
                gameDecks.get(turn).setFieldZone(selectedCard);
                gameDecks.get(turn).setFieldZoneStatus("O");
                activateOrDeactivateFieldCardForAll(1);
                System.out.println("Field activated");
                return "Field activated";
            } else {
                moveToGraveyard(turn, "inHand", selectedCardIndex - 1);
            }
        } else if (selectedDeck.equals("spell")) {
            if (CommonTools.getMatcher(spell.getSpellIcon(), "Continuous").matches()) {
                SpellZone spellZone = gameDecks.get(turn).getSpellZones().get(selectedCardIndex);
                spellZone.setVisible();
                System.out.println("Spell activated");
                return "Spell activated";
            } else {
                moveToGraveyard(turn, "SpellZone", selectedCardIndex);
            }
        } else if (selectedDeck.equals("field")) {
            gameDecks.get(turn).setFieldZoneStatus("O");
            activateOrDeactivateFieldCardForAll(1);
            return "Field activated";
        }
        System.out.println("Spell activated");
        return checkSpellCard();
    }

    public void activateTrap() {
        if (!selectedDeck.equals("spell")) {
            System.out.println("invalid deck or card");
            return;
        }
        if (!selectedCard.getName().equals("Mind Crush") &&
                !selectedCard.getName().equals("Torrential Tribute") &&
                !selectedCard.getName().equals("Time Seal")) {
            System.out.println("invalid card");
            return;
        }
        if (selectedCard.getName().equals("Mind Crush")) activateTrapMindCrush();
        else if (selectedCard.getName().equals("Torrential Tribute")) activateTrapTorrentialTribute();
        else if (selectedCard.getName().equals("Time Seal")) activateTrapTimeSeal();
    }

    private String setSpell() {
        GameDeck myDeck = gameDecks.get(turn);
        if (selectedCard == null) {
            return "no card selected";
        } else if (!(phase == Phase.main1 || phase == Phase.main2
                || ((Spell) selectedCard).getSpellIcon().equals("Quick-play"))) {
            System.out.println("you can’t do this action in this phase");
            return "you can’t do this action in this phase";
        } else if (myDeck.isSpellZoneFull()) {
            System.out.println("spell card zone is full");
            return "spell card zone is full";
        } else {
            System.out.println("set successfully");
            Spell spell = (Spell) selectedCard;
            if (spell.getSpellIcon().equals("Field")) {
                if (!(myDeck.isFieldZoneEmpty())) {
                    moveToGraveyard(turn, "field", 0);
                }
                myDeck.setFieldZone(selectedCard);
                myDeck.setFieldZoneStatus("H");
            } else {
                gameDecks.get(turn).setSpellToSpellZone(selectedCard.getName());
            }
            gameDecks.get(turn).getInHandCards().remove(selectedCardIndex - 1);
            deselect();
            return "set successfully";
        }
    }

    private String setTrap() {
        GameDeck myDeck = gameDecks.get(turn);
        if (selectedCard == null) {
            return "no card selected";
        } else if (!(phase == Phase.main1 || phase == Phase.main2
                || ((Trap) selectedCard).getTrapIcon().equals("Quick-play"))) {
            System.out.println("you can’t do this action in this phase");
            return "you can’t do this action in this phase";
        } else if (myDeck.isSpellZoneFull()) {
            System.out.println("spell card zone is full");
            return "spell card zone is full";
        } else {
            System.out.println("set successfully");
            Trap trap = (Trap) selectedCard;
            System.out.println(trap.getTrapIcon());
            if (trap.getTrapIcon().equals("Field")) {
                if (!(myDeck.isFieldZoneEmpty())) {
                    moveToGraveyard(turn, "field", 0);
                }
                myDeck.setFieldZone(selectedCard);
                myDeck.setFieldZoneStatus("H");
                activateOrDeactivateFieldCardForAll(1);
            } else {
                gameDecks.get(turn).setTrapToSpellZone(selectedCard.getName());
            }
            gameDecks.get(turn).getInHandCards().remove(selectedCardIndex - 1);
            deselect();
            return "set successfully";
        }
    }

    public void showGraveyard(int turn) {
        GameDeck gameDeck;
        if (turn == 0)
            gameDeck = gameDecks.get(0);
        else
            gameDeck = gameDecks.get(1);
        ArrayList<Card> graveyardCards = gameDeck.getGraveyardCards();
        if (graveyardCards.size() == 0)
            System.out.println("graveyard empty");
        else {
            for (int i = 0; i < graveyardCards.size(); i++) {
                Card card = graveyardCards.get(i);
                System.out.println(++i + ". " + card);
            }
        }
        while (true) {
            String command = CommonTools.scan.nextLine().trim();
            if (command.equals("back")) return;
            System.out.println("invalid command");
        }
    }

    private void cardShow() {
        HashMap<Integer, MonsterZone> monsterZones = gameDecks.get(changeTurn(turn)).getMonsterZones();
        if (selectedCard == null) {
            System.out.println("no card is selected yet");
        } else if (monsterZones.get(selectedCardIndex).getStatus().equals("DH")) {
            System.out.println("card is not visible");
        } else {
            System.out.println(selectedCard.getName() + ":" + selectedCard.getType() + ":" + selectedCard.getDescription());
        }
    }

    private String spellActivateChangeOfHeart() {
        int opponentTurn = changeTurn(turn);
        if (gameDecks.get(opponentTurn).isMonsterZoneEmpty()) {
            System.out.println("opponent's monster zone is empty");
            return "opponent's monster zone is empty";
        }
        if (gameDecks.get(turn).isMonsterZoneFull()) {
            System.out.println("your monster zone is full");
            return "your monster zone is full";
        }
        System.out.println("enter position of a monster from opponent's monster zone");
        int position;
        while (true) {
            position = CommonTools.scan.nextInt();
            if (position < 1 || position > 5) {
                System.out.println("enter a number between 1 and 5");
                JOptionPane.showMessageDialog(null, "enter a number between 1 and 5");
            } else if (gameDecks.get(opponentTurn).getMonsterZones().get(position).isEmpty()) {
                System.out.println("entered position is empty");
                JOptionPane.showMessageDialog(null, "entered position is empty");
            } else {
                Card card = gameDecks.get(opponentTurn).getMonsterZones().get(position).getCurrentMonster();
                int index = gameDecks.get(turn).summonCardToMonsterZone(card.getName());
                gameDecks.get(opponentTurn).getMonsterZones().get(position).removeCard();
                gameDecks.get(turn).getMonsterZones().get(index).hasBeenChanged = true;
                System.out.println("opponent's monster added to your monster zone");
                return "opponent's monster added to your monster zone";
            }
        }
    }

    private void surrender(int turn) {
        gameDecks.get(turn).setPlayerLP(0);
        gameDecks.get(changeTurn(turn)).increaseWinRounds();
    }

    public void increasePlayerLPCheat(String command) {
        Matcher matcher = CommonTools.getMatcher(command, "^increase --LP (\\d+)$");
        matcher.find();
        int amountOfLP = Integer.parseInt(matcher.group(1));
        GameDeck myDeck = gameDecks.get(turn);
        myDeck.increaseLP(amountOfLP);
        setField();
    }

    public void setWinnerCheat(String command) {
        Matcher matcher = CommonTools.getMatcher(command, "^duel set-winner (\\S+)$");
        matcher.find();
        String playerNickname = matcher.group(1);
        if (gameDecks.get(0).getPlayerNickName().equals(playerNickname)) {
            surrender(1);
        } else if (gameDecks.get(1).getPlayerNickName().equals(playerNickname)) {
            surrender(0);
        } else System.out.println("There is no player with this nickname!");
        setField();
    }

    public void roundOver(int turn) { // 0 : firstPlayer losses , 1 : secondPlayer losses
        GameDeck firstDeck = gameDecks.get(0);
        GameDeck secondDeck = gameDecks.get(1);
        firstDeck.addPlayerLPAfterRound();
        secondDeck.addPlayerLPAfterRound();
        firstDeck.setPlayerLP(8000);
        secondDeck.setPlayerLP(8000);
        if (turn == 0) secondDeck.increaseWinRounds();
        else firstDeck.increaseWinRounds();
    }

    public String gameOver(int round) {
        String winnerUsername = "";
        int firstMoney = 0;
        int secondMoney = 0;
        int firstScore = gameDecks.get(0).getWinRounds() * 1000;
        int secondScore = gameDecks.get(1).getWinRounds() * 1000;
        if (round == 1) {
            if (gameDecks.get(0).getWinRounds() == 1) {
                firstMoney = 1000 + gameDecks.get(0).getMaxPlayerLPAfterRounds();
                secondMoney = 100;
                winnerUsername = gameDecks.get(0).getPlayerUserName();
            } else {
                firstMoney = 100;
                secondMoney = 1000 + gameDecks.get(0).getMaxPlayerLPAfterRounds();
                winnerUsername = gameDecks.get(1).getPlayerUserName();
            }
        } else if (round == 3) {
            if (gameDecks.get(0).getWinRounds() == 2) {
                firstMoney = 3000 + (3 * gameDecks.get(0).getMaxPlayerLPAfterRounds());
                secondMoney = 300;
                winnerUsername = gameDecks.get(0).getPlayerUserName();
            } else {
                firstMoney = 300;
                secondMoney = 3000 + (3 * gameDecks.get(0).getMaxPlayerLPAfterRounds());
                winnerUsername = gameDecks.get(1).getPlayerUserName();
            }
        }
        Player firstPlayer = Player.getPlayerByUsername(gameDecks.get(0).getPlayerUserName());
        Player secondPlayer = Player.getPlayerByUsername(gameDecks.get(1).getPlayerUserName());
        firstPlayer.increaseScore(firstScore);
        secondPlayer.increaseScore(secondScore);
        firstPlayer.increaseMoney(firstMoney);
        secondPlayer.increaseMoney(secondMoney);
        return winnerUsername + " won the game and the score is: "
                + firstScore + "-" + secondScore;
    }

    private int changeTurn(int turn) {
        if (turn == 1)
            return 0;
        return 1;
    }

    private void changeGameTurn() {
        for (int i = 1; i < 6; i++) {
            if ((!gameDecks.get(turn).getMonsterZones().get(i).isEmpty()) &&
                    gameDecks.get(turn).getMonsterZones().get(i).hasBeenChanged) {
                gameDecks.get(turn).getMonsterZones().get(i).hasBeenChanged = false;
                Card card = gameDecks.get(turn).getMonsterZones().get(i).removeCard();
                gameDecks.get(changeTurn(turn)).summonCardToMonsterZone(card.getName());
            }
        }
        gameDecks.get(turn).supplyCheck = false;
        messengerChecked = false;
        selectedCard = null;
        isSummoned = 0;
        selectedCardIndex = -1;
        enteredMonsterCardIndex = -1;
        changedPositionMonsterIndex = -1;
        selectedDeck = null;
        isCardDrawn = 0;
        if (timeSealTrap != 0) timeSealTrap = timeSealTrap - 1;
        if (isGameStart != 0) isGameStart = isGameStart - 1;
        turn = changeTurn(turn);
        for (int i = 1; i < 6; i++) {
            gameDecks.get(turn).getMonsterZones().get(i).resetAttack();
        }
        System.out.println("its " + gameDecks.get(turn).getPlayerNickName() + "'s turn");
    }

    public void drawCard() {
        ArrayList<Card> deck = gameDecks.get(turn).getDeck();
        if (deck.size() == 0) {
            gameDecks.get(turn).setPlayerLP(0);
            return;
        }
        isCardDrawn = 1;
        gameDecks.get(turn).drawCard();
    }

    public void inHandCardCheat() {
        ArrayList<Card> deck = gameDecks.get(turn).getDeck();
        if (deck.size() == 0) return;
        isCardDrawn = 1;
        gameDecks.get(turn).drawCard();
        setField();
    }

    public void changePhase() {
        if (phase == Phase.end) changeGameTurn();
        phase = phase.next();
        if (isGameStart == 2 && phase == Phase.battle) {
            phase = Phase.end;
        }
        System.out.println(phase);
    }

    private String checkSpellCard() {
        Spell spell = (Spell) selectedCard;
        if (spell.getName().equals("Terraforming")) {
            return Terraforming();
        } else if (spell.getName().equals("Pot of Greed")) {
            gameDecks.get(turn).drawCard();
            gameDecks.get(turn).drawCard();
        } else if (spell.getName().equals("Raigeki")) {
            Raigeki();
            System.out.println("All enemy monsters were destroyed");
            return "All enemy monsters were destroyed";
        } else if (spell.getName().equals("Harpie’s Feather Duster")) {
            return HarpieFeatherDuster();
        } else if (spell.getName().equals("Dark Hole")) {
            return darkHole();
        } else if (spell.getName().equals("Twin Twisters")) {
            return TwinTwisters();
        } else if (spell.getName().equals("Mystical space typhoon")) {
            return mysticalTyphoon();
        } else if (spell.getName().equals("Change of Heart")) {
            return spellActivateChangeOfHeart();
        } else if (spell.getName().equals("Advance Ritual Art")) {
            return advanceRitualArt();
        }
        deselect();
        return "";
    }

    private String advanceRitualArt() {
        ArrayList<Card> inHandCards = gameDecks.get(turn).getInHandCards();
        int isRitualSummonPossible = 0;
        for (Card card : inHandCards) {
            if (card.getName().equals("Crab Turtle") || card.getName().equals("Skull Guardian")) {
                isRitualSummonPossible++;
                break;
            }
        }
        ArrayList<Card> monsterZoneCards = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Card monsterCard = gameDecks.get(turn).getMonsterZones().get(i).getCurrentMonster();
            if (monsterCard != null) monsterZoneCards.add(monsterCard);
        }
        for (int i = 0; i < monsterZoneCards.size(); i++) {
            Monster firstMonster = (Monster) monsterZoneCards.get(i);
            int sumOfLevels = firstMonster.getLevel();
            for (int j = i + 1; j < monsterZoneCards.size(); j++) {
                if (sumOfLevels == 7) {
                    isRitualSummonPossible++;
                    break;
                }
                Monster secondMonster = (Monster) monsterZoneCards.get(i);
                sumOfLevels += secondMonster.getLevel();
            }
        }
        if (isSummoned == 0) isRitualSummonPossible++;
        if (isRitualSummonPossible == 3) {
            return ritualSummon();
        } else {
            System.out.println("there is no way you could ritual summon a monster");
            return "there is no way you could ritual summon a monster";
        }
    }

    private String ritualSummon() {
        while (true) {
            System.out.println("please select a ritual monster to summon");
            String command = JOptionPane.showInputDialog("Enter your opponent's nickname:");
            if (command.matches("^cancel$")) break;
            Matcher matcher = CommonTools.getMatcher(command, "^select --hand (\\d+)$");
            matcher.find();
            int position = Integer.parseInt(matcher.group(1));
            if (position < 1 || position > 5) {
                System.out.println("invalid selection");
                JOptionPane.showMessageDialog(null, "invalid selection");
                continue;
            }
            ArrayList<Card> inHandCards = gameDecks.get(turn).getInHandCards();
            selectedCard = inHandCards.get(position - 1);
            selectedCardIndex = position;
            selectedDeck = "hand";
            if (!(inHandCards.get(position - 1).getName().equals("Crab Turtle")
                    || inHandCards.get(position - 1).getName().equals("Skull Guardian"))) {
                System.out.println("you should ritual summon right now");
                JOptionPane.showMessageDialog(null, "you should ritual summon right now");
                continue;
            }
            System.out.println("card selected");
            JOptionPane.showMessageDialog(null, "card selected");
            while (true) {
                System.out.println("enter positions of tribute monster in monster zone:");
                boolean isSummonSuccessful = false;
                int sumOfLevels = 0;
                ArrayList<Integer> positionOfTributeMonsters = new ArrayList<>();
                while (true) {
                    int monsterZonePosition = 0;
                    monsterZonePosition = Integer.parseInt(JOptionPane.showInputDialog("enter positions of tribute monster in monster zone:"));
                    if (monsterZonePosition < 1 || monsterZonePosition > 5) {
                        System.out.println("there no monsters on this address");
                        JOptionPane.showMessageDialog(null, "there no monsters on this address");
                        continue;
                    }
                    Monster tributeMonster = (Monster) gameDecks.get(turn).getMonsterZones().get(monsterZonePosition).getCurrentMonster();
                    if (tributeMonster == null) {
                        System.out.println("there no monsters on this address");
                        JOptionPane.showMessageDialog(null, "there no monsters on this address");
                        continue;
                    }
                    positionOfTributeMonsters.add(monsterZonePosition);
                    sumOfLevels += tributeMonster.getLevel();
                    if (sumOfLevels > 7) {
                        System.out.println("selected monsters levels don't match with ritual monster");
                        JOptionPane.showMessageDialog(null, "selected monsters levels don't match with ritual monster");
                        break;
                    } else if (sumOfLevels == 7) {
                        isSummonSuccessful = true;
                        break;
                    }
                }
                if (isSummonSuccessful) {
                    System.out.println("summoned successfully");
                    JOptionPane.showMessageDialog(null, "summoned successfully");
                    isSummoned = 1;
                    for (int monsterZonePosition : positionOfTributeMonsters) {
                        gameDecks.get(turn).tributeCardFromMonsterZone(monsterZonePosition);
                    }
                    gameDecks.get(turn).getInHandCards().remove(position - 1);
                    enteredMonsterCardIndex = gameDecks.get(turn).summonCardToMonsterZone(selectedCard.getName());
                    deselect();
                }
            }
        }
        return "summoned successfully";
    }


    private String Terraforming() {
        GameDeck myDeck = gameDecks.get(turn);
        for (int i = 0; i < myDeck.getDeck().size(); i++) {
            if (myDeck.getDeck().get(i).getType().equals("Spell")) {
                Spell spell = (Spell) myDeck.getDeck().get(i);
                if (spell.getSpellIcon().equals("Field")) {
                    myDeck.getInHandCards().add(myDeck.getDeck().get(i));
                    myDeck.getDeck().remove(i);
                    System.out.println("A field card was drawn");
                    return "A field card was drawn";
                }
            }
        }
        System.out.println("No field card was found");
        return "No field card was found";
    }

    private void Raigeki() {
        GameDeck enemyDeck = gameDecks.get(changeTurn(turn));
        for (int i = 1; i <= 5; i++) {
            if (!(enemyDeck.getMonsterZones().get(i).isEmpty())) {
                moveToGraveyard(changeTurn(turn), "MonsterZone", i);
            }
        }
    }

    private String HarpieFeatherDuster() {
        GameDeck enemyDeck = gameDecks.get(changeTurn(turn));
        for (int i = 1; i <= 5; i++) {
            if (!(enemyDeck.getSpellZones().get(i).isEmpty())) {
                moveToGraveyard(changeTurn(turn), "SpellZone", i);
            }
        }
        System.out.println("All enemy spell were destroyed");
        return "All enemy spell were destroyed";
    }

    private String darkHole() {
        Raigeki();
        turn = changeTurn(turn);
        Raigeki();
        turn = changeTurn(turn);
        System.out.println("All cards were destroyed");
        return "All cards were destroyed";
    }

    private void supplySquad(int turn) {
        if (gameDecks.get(turn).supplyCheck)
            return;
        for (int i = 1; i < 6; i++) {
            SpellZone spellZone = gameDecks.get(turn).getSpellZones().get(i);
            if ((!spellZone.isEmpty()) && spellZone.getCurrentCard().getName().equals("Supply Squad")
                    && spellZone.getStatus().equals("O")) {
                System.out.println("Supply Squad for " + gameDecks.get(turn).getPlayerNickName());
                JOptionPane.showMessageDialog(null, "Supply Squad for " + gameDecks.get(turn).getPlayerNickName());
                gameDecks.get(turn).drawCard();
            }
        }
        gameDecks.get(turn).supplyCheck = true;
    }

    private void spellAbsorptionCheck() {
        for (int i = 0; i < gameDecks.size(); i++) {
            GameDeck deck = gameDecks.get(i);
            for (int i1 = 1; i1 <= 5; i1++) {
                if (!deck.getSpellZones().get(i1).getStatus().equals("O"))
                    continue;
                if (deck.getSpellZones().get(i1).getCurrentCard().getName().equals("Spell Absorption")) {
                    deck.increaseLP(500);
                    System.out.println(deck.getPlayerNickName() + " Gained health from spell absorption +500");
                    JOptionPane.showMessageDialog(null, deck.getPlayerNickName() + " Gained health from spell absorption +500");
                }
            }
        }
    }

    private boolean messengerOfPeaceBlocks(int attackPoint) {
        if (attackPoint > 1500) {
            for (int i = 0; i < gameDecks.size(); i++) {
                GameDeck deck = gameDecks.get(i);
                for (int i1 = 1; i1 <= 5; i1++) {
                    if ((!deck.getSpellZones().get(i1).isEmpty()) &&
                            deck.getSpellZones().get(i1).getCurrentCard().getName()
                                    .equals("Messenger of peace")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void keepMessengerOfPeace() {
        GameDeck myDeck = gameDecks.get(turn);
        for (int i = 1; i <= 5; i++) {
            if ((!myDeck.getSpellZones().get(i).isEmpty()) &&
                    myDeck.getSpellZones().get(i).getCurrentCard().getName().equals("Messenger of peace")) {
                System.out.println("Do you want to keep Messenger of peace for the cost of 100 LP yes/no");
                String[] buttons = {"yes", "no"};
                int returnValue = JOptionPane.showOptionDialog(null, "Do you want to keep Messenger of peace for the cost of 100 LP"
                        , "Messenger of peace", JOptionPane.OK_OPTION, 1, null, buttons, buttons[0]);
                if (returnValue == 0) {
                    myDeck.decreaseLP(100);
                    System.out.println("You lost 100 LP");
                    JOptionPane.showMessageDialog(null, "You lost 100 LP");
                } else if (returnValue == 1) {
                    moveToGraveyard(turn, "SpellZone", i);
                    System.out.println("You lost Messenger of Peace");
                    JOptionPane.showMessageDialog(null, "You lost Messenger of Peace");
                } else {
                    System.out.println("invalid answer");
                    keepMessengerOfPeace();
                    return;
                }
            }
        }
    }

    private String mysticalTyphoon() {
        if (gameDecks.get(turn).isSpellZoneEmpty()) {
            System.out.println("opponent's spellZone is empty");
            return "opponent's spellZone is empty";
        }
        while (true) {
            String response = JOptionPane.showInputDialog("Enter the index of the enemy card you want to destroy");
            int index;
            try {
                index = Integer.parseInt(response);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "entered number is not valid");
                continue;
            }
            if (index < 1 || index > 5 || !gameDecks.get(changeTurn(turn)).getSpellZones().get(index).isEmpty()) {
                System.out.println("This square is not valid");
                continue;
            }
            System.out.println("Enemy card destroyed");
            JOptionPane.showMessageDialog(null, "Enemy card destroyed");
            moveToGraveyard(changeTurn(turn), "SpellZone", index);
            break;
        }
        return "";
    }

    private String TwinTwisters() {
        if (gameDecks.get(turn).getInHandCards().size() == 0) {
            System.out.println("your hand is empty");
            return "your hand is empty";
        }
        if (gameDecks.get(turn).isSpellZoneEmpty()) {
            System.out.println("opponent's spellZone is empty");
            return "opponent's spellZone is empty";
        }
        System.out.println("select a card to remove from your hand");
        int removePosition;
        while (true) {
            String response = JOptionPane.showInputDialog("choose a card index to discard from your hand");
            try {
                removePosition = Integer.parseInt(response);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "entered number is not valid");
                continue;
            }
            if (removePosition > gameDecks.get(turn).getInHandCards().size()) {
                System.out.println("entered index more than in hand cards");
                JOptionPane.showMessageDialog(null, "entered index more than in hand cards");
            } else {
                moveToGraveyard(turn, "inHand", removePosition - 1);
                break;
            }
        }
        int destroyedPosition1;
        int destroyedPosition2;
        int opponentTurn = changeTurn(turn);
        while (true) {
            String response = JOptionPane.showInputDialog("choose an enemy card index to destroy");
            try {
                destroyedPosition1 = Integer.parseInt(response);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "entered number is not valid");
                continue;
            }
            if (destroyedPosition1 > 5 || destroyedPosition1 < 1) {
                System.out.println("entered index more than 5 or less than 1");
            } else if (gameDecks.get(opponentTurn).getSpellZones().get(destroyedPosition1).isEmpty()) {
                System.out.println("entered position is empty");
            } else {
                moveToGraveyard(changeTurn(turn), "SpellZone", destroyedPosition1);
                System.out.println("first card removed");
                JOptionPane.showMessageDialog(null, "first card removed");
                break;
            }
        }
        while (true) {
            String response = JOptionPane.showInputDialog("choose an enemy card index to destroy");
            try {
                destroyedPosition2 = Integer.parseInt(response);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "entered number is not valid");
                continue;
            }
            if (destroyedPosition2 > 5 || destroyedPosition2 < 1) {
                System.out.println("entered index more than 5 or less than 1");
            } else {
                moveToGraveyard(changeTurn(turn), "SpellZone", destroyedPosition2);
                gameDecks.get(opponentTurn).getSpellZones().get(destroyedPosition2).removeCard();
                System.out.println("second card removed");
                JOptionPane.showMessageDialog(null, "second card removed");
                break;
            }
        }
        return "";
    }

    private void yami(Monster monster, int activeMode) {
        if (monster.getMonsterType().equals("Fiend") || monster.getMonsterType().equals("Spellcaster")) {
            monster.changeAttackPoint(activeMode * 200);
            monster.changeDefensePoint(activeMode * 200);
        } else if (monster.getMonsterType().equals("Fairy")) {
            monster.changeAttackPoint(activeMode * -200);
            monster.changeDefensePoint(activeMode * -200);
        }
    }

    private void forest(Monster monster, int activeMode) {
        if (monster.getMonsterType().equals("Insect") ||
                monster.getMonsterType().equals("Beast") ||
                monster.getMonsterType().equals("Beast-Warrior")) {
            monster.changeDefensePoint(activeMode * 200);
            monster.changeAttackPoint(activeMode * 200);
        }
    }

    private void closedForest(Monster monster, int turn, int activeMode) {
        if (monster.getMonsterType().equals("Beast")) {
            for (int i = 0; i < gameDecks.get(turn).getGraveyardCards().size(); i++) {
                monster.changeAttackPoint(activeMode * 100);
                monster.changeDefensePoint(activeMode * 100);
            }
        }
    }

    private void Umiiruka(Monster monster, int activeMode) {
        if (monster.getAttribute().equals("WATER")) {
            monster.changeAttackPoint(activeMode * 500);
            monster.changeDefensePoint(activeMode * -400);
        }
    }

    private void activateTrapMirrorForce() {
        System.out.println("trap 'Mirror Force' activated");
        for (int i = 1; i <= 5; i++) {
            if (gameDecks.get(turn).getMonsterZones().get(i).getStatus().equals("OO")) {
                moveToGraveyard(turn, "MonsterZone", i);
            }
        }
        for (int i = 1; i <= 5; i++) {
            if (gameDecks.get(changeTurn(turn)).getSpellZones().get(i).getCurrentCard().getName().equals("Mirror Force")) {
                moveToGraveyard(turn, "SpellZone", i);
                return;
            }
        }
    }

    private void activateTrapMindCrush() {
        System.out.println("trap 'Mind Crush' activated");
        System.out.println("enter name of a card:");
        String cardName = JOptionPane.showInputDialog("enter name of a card:");
        int opponentTurn = changeTurn(turn);
        int size = gameDecks.get(opponentTurn).getInHandCards().size();
        boolean doesCardExist = false;
        for (int i = 0; i < size; i++) {
            Card card = gameDecks.get(opponentTurn).getInHandCards().get(i);
            if (card.getName().equals(cardName)) {
                doesCardExist = true;
                break;
            }
        }
        if (!doesCardExist) {
            int sizeOfHand = gameDecks.get(turn).getInHandCards().size();
            int randomIndex = (int) Math.floor(Math.random() * sizeOfHand);
            moveToGraveyard(turn, "inHand", randomIndex);
        } else {
            size = gameDecks.get(opponentTurn).getInHandCards().size();
            for (int i = 0; i < size; i++) {
                Card card = gameDecks.get(opponentTurn).getInHandCards().get(i);
                if (card.getName().equals(cardName)) moveToGraveyard(opponentTurn, "inHand", i);
            }
        }
        moveToGraveyard(turn, "SpellZone", selectedCardIndex);
    }

    private void activateTrapTorrentialTribute() {
        for (int i = 1; i <= 5; i++) {
            if (gameDecks.get(turn).getMonsterZones().get(i).getStatus().isEmpty()) {
                moveToGraveyard(turn, "MonsterZone", i);
            }
        }
        int opponentTurn = changeTurn(turn);
        for (int i = 1; i <= 5; i++) {
            if (gameDecks.get(opponentTurn).getMonsterZones().get(i).getStatus().isEmpty()) {
                moveToGraveyard(opponentTurn, "MonsterZone", i);
            }
        }
        moveToGraveyard(turn, "SpellZone", selectedCardIndex);
    }

    private void activateTrapTimeSeal() {
        System.out.println("trap 'Time Seal' activated");
        timeSealTrap = 2;
        moveToGraveyard(turn, "SpellZone", selectedCardIndex);
    }

    private void activateTrapNegateAttack() {
        System.out.println("trap 'Negate Attack' activated");
        int opponentTurn = changeTurn(turn);
        phase = Phase.main2;
        for (int i = 1; i <= 5; i++) {
            if (gameDecks.get(opponentTurn).getSpellZones().get(i).getCurrentCard().getName().equals("Negate Attack")) {
                moveToGraveyard(opponentTurn, "SpellZone", i);
                break;
            }
        }
    }

    private void activateOrDeactivateFieldCardForAll(int activeMode) { // 1 for activate and -1 for deactivate
        for (int i = 0; i < gameDecks.size(); i++) {
            if (!gameDecks.get(i).getFieldZoneStatus().equals("O"))
                continue;
            String fieldCardName = gameDecks.get(i).getFieldZone().getName();
            for (int i1 = 1; i1 < 6; i1++) {
                if (fieldCardName.equals("Yami")) {
                    if (!gameDecks.get(i).getMonsterZones().get(i1).isEmpty())
                        yami((Monster) gameDecks.get(i).getMonsterZones().get(i1).getCurrentMonster(), activeMode);
                    if (!gameDecks.get(changeTurn(i)).getMonsterZones().get(i1).isEmpty())
                        yami((Monster) gameDecks.get(changeTurn(i)).getMonsterZones().get(i1).getCurrentMonster(), activeMode);

                } else if (fieldCardName.equals("Forest")) {
                    if (!gameDecks.get(i).getMonsterZones().get(i1).isEmpty())
                        forest((Monster) gameDecks.get(i).getMonsterZones().get(i1).getCurrentMonster(), activeMode);
                    if (!gameDecks.get(changeTurn(i)).getMonsterZones().get(i1).isEmpty())
                        forest((Monster) gameDecks.get(changeTurn(i)).getMonsterZones().get(i1).getCurrentMonster(), activeMode);

                } else if (fieldCardName.equals("Closed Forest")) {
                    if (!gameDecks.get(i).getMonsterZones().get(i1).isEmpty())
                        closedForest((Monster) gameDecks.get(i).getMonsterZones().get(i1).getCurrentMonster(), i, activeMode);

                } else if (fieldCardName.equals("Umiiruka")) {
                    if (!gameDecks.get(i).getMonsterZones().get(i1).isEmpty())
                        Umiiruka((Monster) gameDecks.get(i).getMonsterZones().get(i1).getCurrentMonster(), activeMode);
                    if (!gameDecks.get(changeTurn(i)).getMonsterZones().get(i1).isEmpty())
                        Umiiruka((Monster) gameDecks.get(changeTurn(i)).getMonsterZones().get(i1).getCurrentMonster(), activeMode);
                }
            }
        }
    }

    private void moveToGraveyard(int turn, String place, int index) {
        activateOrDeactivateFieldCardForAll(-1);
        if (place.equals("MonsterZone")) {
            Card card = gameDecks.get(turn).getMonsterZones().get(index).removeCard();
            gameDecks.get(turn).getGraveyardCards().add(card);
            supplySquad(turn);
        } else if (place.equals("SpellZone")) {
            Card card = gameDecks.get(turn).getSpellZones().get(index).removeCard();
            gameDecks.get(turn).getGraveyardCards().add(card);
        } else if (place.equals("field")) {
            gameDecks.get(turn).getGraveyardCards().add(gameDecks.get(turn).getFieldZone());
            gameDecks.get(turn).emptyFieldZone();
        } else if (place.equals("inHand")) {
            gameDecks.get(turn).getGraveyardCards().add(gameDecks.get(turn).getInHandCards().get(index));
            gameDecks.get(turn).getInHandCards().remove(index);
        }
        activateOrDeactivateFieldCardForAll(1);
    }

    public void setIsSummoned(int isSummoned) {
        this.isSummoned = isSummoned;
    }

    public void setSelectedMonsterCardIndex(int selectedMonsterCardIndex) {
        this.enteredMonsterCardIndex = selectedMonsterCardIndex;
    }

    public void setEnteredMonsterCardIndex(int enteredMonsterCardIndex) {
        this.enteredMonsterCardIndex = enteredMonsterCardIndex;
    }

    public void showMyGrave(MouseEvent mouseEvent) {
        GraveYardController.graveYard = gameDecks.get(turn).getGraveyardCards();
        try {
            new GraveYardController().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Image cardView(String name) {
        ImageView imageView = new ImageView();
        String searchName = name.replace("of", "Of")
                .replace(" ", "").replace("-", "").replace("\"", "");
        String url = "/Images/Cards/" + searchName + ".jpg";
        Image image = new Image(getClass().getResource(url).toExternalForm());
        imageView.setImage(image);
        return image;
    }

    public void showEnemyGrave(MouseEvent mouseEvent) {
        GraveYardController.graveYard = gameDecks.get(changeTurn(turn)).getGraveyardCards();
        try {
            new GraveYardController().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<GameDeck> getGameDecks() {
        return gameDecks;
    }

    public String getUsernameTurn() {
        return gameDecks.get(turn).getPlayerUserName();
    }
}
