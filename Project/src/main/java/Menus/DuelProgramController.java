package Menus;

import Model.Cards.*;
import Model.CommonTools;
import Model.Deck;
import Model.Player;
import View.CardView;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
    private ArrayList<GameDeck> gameDecks = new ArrayList<>(2);
    private MonsterPowersController monsterPowersController = new MonsterPowersController(gameDecks, this);
    private int turn = 0; //0 : firstPlayer, 1 : secondPlayer
    private int isSummoned = 0; //0 : is not summoned before, 1 : is summoned before
    private Card selectedCard = null;
    private int selectedCardIndex = -1; // -1 means Empty
    private int enteredMonsterCardIndex = -1;
    private int changedPositionMonsterIndex = -1;
    private String selectedDeck = null; // hand, monster, spell, field,
    // opponentMonster, opponentSpell, opponentField
    private Phase phase = Phase.draw;
    private int round = 1;
    private int timeSealTrap = 0;
    private int isCardDrawn = 0;
    private int isGameStart = 2;
    private boolean messengerChecked = false;
    private int isAI = 0;

    @FXML
    public GridPane enemyGrid;
    public GridPane myGrid;
    public ImageView selectedCardShow;
    public ImageView myGrave;
    public ImageView enemyGrave;
    public HBox inHandCards;
    public HBox enemyHand;
    public Button nextPhaseButton;
    public Circle attackSign;

    @FXML
    public void initialize(){
        attackSign = new Circle(20);
        attackSign.setFill(new ImagePattern(new Image("/Images/Attack.png")));
        inHandCards.setSpacing(20);
        enemyHand.setSpacing(20);
        setGameDecks("Mohsen", "Mohsen");
        for (int j = 0; j < 5; j++) {
            gameDecks.get(turn).drawCard();
            gameDecks.get(changeTurn(turn)).drawCard();
        }
        nextPhaseButton.setOnAction(actionEvent -> {
            changePhase();
            enemyGrid.getChildren().remove(attackSign);
            setField();
        });
        selectedCardShow.setImage(new Image(getClass().getResource("/Images/Cards/Unknown.jpg").toExternalForm()));
        myGrave.setImage(new Image(getClass().getResource("/Images/Cards/Unknown.jpg").toExternalForm()));
        enemyGrave.setImage(new Image(getClass().getResource("/Images/Cards/Unknown.jpg").toExternalForm()));
        enemyGrid.setHgap(43);
        enemyGrid.setVgap(35);
        myGrid.setHgap(44);
        myGrid.setVgap(30);
        setField();
    }

    public void setField(){
        if (phase == Phase.draw && isCardDrawn == 0 && isGameStart == 0 && timeSealTrap == 0) drawCard();
        nextPhaseButton.setText( "next phase. current phase : " + phase);
        for (int i = 0; i < 5; i++) {
            for (int i1 = 0; i1 < 2; i1++) {
                Rectangle rectangle = new Rectangle(60,90);
                Rectangle rectangle1 = new Rectangle(60,90);
                if (!gameDecks.get(turn).getMonsterZones().get(i + 1).isEmpty() && i1 == 0){
                    Image image = cardView(gameDecks.get(turn).getMonsterZones().get(i + 1).getCurrentMonster().getName());
                    rectangle.setFill(new ImagePattern(image));
                } else if (!gameDecks.get(turn).getSpellZones().get(i + 1).isEmpty() && i1 == 1){
                    Image image = cardView(gameDecks.get(turn).getSpellZones().get(i + 1).getCurrentCard().getName());
                    rectangle.setFill(new ImagePattern(image));
                }
                if (!gameDecks.get(changeTurn(turn)).getMonsterZones().get(6 - i - 1).isEmpty() && i1 == 1){
                    Image image = cardView(gameDecks.get(changeTurn(turn)).getMonsterZones().get(6 - i - 1).getCurrentMonster().getName());
                    rectangle1.setFill(new ImagePattern(image));
                    if (!gameDecks.get(changeTurn(turn)).getMonsterZones().get(6 - i - 1).getStatus().equals("OO")){
                        image = cardView("Unknown");
                        rectangle1.setFill(new ImagePattern(image));
                    }
                } else if (!gameDecks.get(changeTurn(turn)).getSpellZones().get(6 - i - 1).isEmpty() && i1 == 0){
                    Image image = cardView("Unknown");
                    rectangle1.setFill(new ImagePattern(image));
                }
                int finalI1 = i1;
                int finalI = i;
                rectangle.setOnMouseClicked(EventHandler->{
                    if (phase != Phase.battle) {
                        if (finalI1 == 0) JOptionPane.showMessageDialog(null,summonMonster());
                        else if (selectedCard.getType().equals("Spell") || selectedCard.getType().equals("Trap")) {
                            set();
                        }
                        setField();
                    } else if (phase == Phase.battle){
                        if (finalI1 == 0){
                            String message = selectMonster(finalI + 1);
                            JOptionPane.showMessageDialog(null, message);
                            if (message.equals("card selected")) {
                                enemyGrid.getChildren().remove(attackSign);
                                enemyGrid.add(attackSign, finalI, finalI1);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,selectSpell(finalI + 1));
                        }
                    }
                });
                rectangle.setOnMouseMoved(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (rectangle.getFill() instanceof ImagePattern){
                            Image image = ((ImagePattern) rectangle.getFill()).getImage();
                            selectedCardShow.setImage(image);
                        }
                    }
                });
                rectangle1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (phase == Phase.battle){
                            if (finalI1 == 1)
                            JOptionPane.showMessageDialog(null, attackCard("attack " + (5 - finalI)));
                        }
                        setField();
                    }
                });
                enemyGrid.add(rectangle,i, i1);
                myGrid.add(rectangle1,i, i1);
            }
        }

        inHandCards.getChildren().clear();
        for (int i = 0; i < gameDecks.get(turn).getInHandCards().size(); i++){
            Rectangle rectangle = new Rectangle(60,90);
            rectangle.setFill(new ImagePattern(cardView(gameDecks.get(turn).getInHandCards().get(i).getName())));
            inHandCards.getChildren().add(rectangle);
            int finalI = i;
            rectangle.setOnMouseClicked(actionEvent -> {
                selectHand(finalI +1);
                System.out.println(selectedCard.getName());
            });
            rectangle.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (rectangle.getFill() instanceof ImagePattern){
                        Image image = ((ImagePattern) rectangle.getFill()).getImage();
                        selectedCardShow.setImage(image);
                    }
                }
            });
        }

        enemyHand.getChildren().clear();
        for (int i = 0; i < gameDecks.get(changeTurn(turn)).getInHandCards().size(); i++){
            Rectangle rectangle = new Rectangle(60,90);
            rectangle.setFill(new ImagePattern(new Image(getClass().getResource("/Images/Cards/Unknown.jpg").toExternalForm())));
            enemyHand.getChildren().add(rectangle);
        }

    }

    public void run(String firstPlayer, String secondPlayer, int round) {
        for (int i = 1; i <= round; i++) {
            setGameDecks(firstPlayer, secondPlayer);
            // methods to be set after each round
            for (int j = 0; j < 5; j++) {
                gameDecks.get(turn).drawCard();
                gameDecks.get(changeTurn(turn)).drawCard();
            }
            if (isGameOver(i)) break;
            AI ai = new AI();
            while (true) {
                if (phase == Phase.draw && isCardDrawn == 0 && isGameStart == 0 && timeSealTrap == 0) drawCard();
                if (isRoundOver()) break;
                System.out.println("phase: " + phase);
                showGameDeck(turn);
                if (phase == Phase.standby && !messengerChecked) {
                    keepMessengerOfPeace();
                    messengerChecked = true;
                }
                String command = null;
                if (secondPlayer.equals("ai") && turn == 1) {
                    ai.updateAI(gameDecks.get(1), gameDecks.get(0), phase);
                    command = ai.decision();
                    isAI = 1;
                } else {
                    command = CommonTools.scan.nextLine();
                }
                if (command.matches("^show graveyard$")) showGraveyard(turn);
                else if (command.matches("^surrender$")) surrender(turn);
                else if (command.matches("^select --hand --force$")) inHandCardCheat();
                else if (command.matches("^select -d$")) deselect();
                else if (command.matches("^show card$")) showCard();
                else if (command.matches("^select .*$")) selectCard(command);
                else if (command.matches("^summon$")) summonMonster();
                else if (command.matches("^activate effect$")) activateSpellErrorCheck();
                else if (command.matches("^activate trap$")) activateTrap();
                else if (command.matches("^attack (\\d+)")) attackCard(command);
                else if (command.matches("^attack direct")) directAttack();
                else if (command.matches("^set$")) set();
                else if (command.matches("^card show --selected$")) cardShow();
                else if (command.matches("^increase --LP (\\d+)$")) increasePlayerLPCheat(command);
                else if (command.matches("^duel set-winner \\S+$")) setWinnerCheat(command);
                else if (command.matches("^set --position (attack|defence)$")) setPositionMonster(command);
                else if (command.matches("^flip-summon$")) flipSummon();
                else if (command.matches("^next phase$")) changePhase();
                else System.out.println("invalid command");
            }
        }
        gameOver(round);
    }

    private void showCard() {
        if (selectedCard == null) {
            System.out.println("No card is selected");
            return;
        } else {
            System.out.println(selectedCard.toString());
            if (selectedCard instanceof Monster) {
                Monster monster = (Monster) selectedCard;
                System.out.println(monster.getAttackPoint() + " : " + monster.getDefensePoint());
            }
        }
    }

    private boolean isRoundOver() {
        if (gameDecks.get(0).getPlayerLP() <= 0) {
            roundOver(0);
            return true;
        } else if (gameDecks.get(1).getPlayerLP() <= 0) {
            roundOver(1);
            return true;
        } else return false;
    }

    private boolean isGameOver(int round) {
        if (round < 3) return false;
        if (gameDecks.get(0).getWinRounds() > 1) return true;
        if (gameDecks.get(1).getWinRounds() > 1) return true;
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

    private String selectMonster(int position) {
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

    private String selectSpell(int position) {
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

    private void selectHand(int position) {
        ArrayList<Card> inHandCards = gameDecks.get(turn).getInHandCards();
        selectedCard = inHandCards.get(position - 1);
        selectedCardIndex = position;
        selectedDeck = "hand";
        System.out.println("card selected");
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

    private String summonMonster() {
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

    private String set() {
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

    private void setPositionMonster(String command) {
        HashMap<Integer, MonsterZone> monsterZones = gameDecks.get(turn).getMonsterZones();
        if (!isSetPositionValid()) return;
        if (command.matches("set --position attack")) {
            if (!monsterZones.get(selectedCardIndex).getStatus().equals("DO")) {
                System.out.println("this card is already in the wanted position");
                return;
            }
            if (enteredMonsterCardIndex == selectedCardIndex || changedPositionMonsterIndex == selectedCardIndex) {
                System.out.println("you already changed this card position in this turn");
                return;
            }
            Card card = monsterZones.get(selectedCardIndex).getCurrentMonster();
            gameDecks.get(turn).getMonsterZones().get(selectedCardIndex).setCardAttack(card);
        } else {
            if (!monsterZones.get(selectedCardIndex).getStatus().equals("OO")) {
                System.out.println("this card is already in the wanted position");
                return;
            }
            if (changedPositionMonsterIndex == selectedCardIndex) {
                System.out.println("you already changed this card position in this turn");
                return;
            }
            Card card = monsterZones.get(selectedCardIndex).getCurrentMonster();
            gameDecks.get(turn).getMonsterZones().get(selectedCardIndex).setCardDefense(card);
        }
        System.out.println("monster card position changed successfully");
        changedPositionMonsterIndex = selectedCardIndex;
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

    private void flipSummon() {
        HashMap<Integer, MonsterZone> monsterZones = gameDecks.get(turn).getMonsterZones();
        if (!isFlipSummonValid()) return;
        if (changedPositionMonsterIndex == selectedCardIndex ||
                enteredMonsterCardIndex == selectedCardIndex ||
                !monsterZones.get(selectedCardIndex).getStatus().equals("DH")) {
            System.out.println("you can’t flip summon this card");
            return;
        }
        Card card = monsterZones.get(selectedCardIndex).getCurrentMonster();
        gameDecks.get(turn).getMonsterZones().get(selectedCardIndex).setCardAttack(card);
        System.out.println("flip summoned successfully");
        changedPositionMonsterIndex = selectedCardIndex;
        monsterPowersController.setTurn(turn);
        monsterPowersController.monsterPowersWhenFlipsummon(selectedCard);
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

    private String attackCard(String command) {
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
                enemyGrid.getChildren().remove(attackSign);
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
            System.out.printf("your opponent’s monster is destroyed and your opponent receives"
                    + " %d battle damage\n", damage);
            return "your opponent’s monster is destroyed and your opponent receives " + damage + " battle damage";
        } else if (damage == 0) {
            monsterPowersController.monsterPowersWhenDestroyed(enemyDeck.getMonsterZones()
                    .get(selectDefender).getCurrentMonster());
            moveToGraveyard(turn, "MonsterZone", selectedCardIndex);
            moveToGraveyard(changeTurn(turn), "MonsterZone", selectDefender);
            System.out.println("both you and your opponent monster cards are destroyed and no" +
                    "one receives damage\n");
            return "both you and your opponent monster cards are destroyed and no one receives damage";
        } else {
            monsterPowersController.monsterPowersWhenDestroyed(enemyDeck.getMonsterZones()
                    .get(selectDefender).getCurrentMonster());
            Card card = myDeck.getMonsterZones().get(selectedCardIndex).removeCard();
            moveToGraveyard(turn, "MonsterZone", selectedCardIndex);
            myDeck.takeDamage(-1 * damage);
            System.out.printf("Your monster card is destroyed and you received %d battle" +
                    "damage", damage);
            return "Your monster card is destroyed and you received " + damage + " damage";
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
            return "the defense position monster is destroyed";
        } else if (attackerDamage == defenderDamage) {
            System.out.println("no card is destroyed");
            return "no card is destroyed";
        } else {
            myDeck.takeDamage(-1 * damage);
            System.out.printf("no card is destroyed and you received %d battle damage\n", damage);
            return "no card is destroyed and you received %d battle damage";
        }
    }

    public String attackDH(int selectDefender, GameDeck myDeck, GameDeck enemyDeck) {
        Monster selectedMonster = (Monster) selectedCard;
        String enemyCardName = enemyDeck.getMonsterZones()
                .get(selectDefender).getCurrentMonster().getName();
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
            System.out.printf("the defense position monster (%s) is destroyed\n", enemyCardName);
            return "the defense position monster (" + enemyCardName + ") is destroyed";
        } else if (damage == 0) {
            System.out.printf("enemy card was %s no card is destroyed\n", enemyCardName);
            return "enemy card was " + enemyCardName + ", no card is destroyed";
        } else {
            myDeck.takeDamage(-1 * damage);
            System.out.printf("enemy card was %s no card is destroyed and you received %d battle damage\n"
                    , enemyCardName, damage);
            return "enemy card was " + enemyCardName + ", no card is destroyed and you received " + damage + " battle damage";
        }
    }

    public void directAttack() {
        GameDeck myDeck = gameDecks.get(turn);
        GameDeck enemyDeck = gameDecks.get(changeTurn(turn));
        if (selectedCard == null) {
            System.out.println("no card is selected yet");
        } else if (!(selectedCard instanceof Monster)) {
            System.out.println("you can’t attack with this card");
        } else if (phase != Phase.battle) {
            System.out.println("you can’t do this action in this phase");
        } else if (!myDeck.getMonsterZones().get(selectedCardIndex).getStatus().equals("OO")) {
            System.out.println("This card is not in attacking position");
        } else if (messengerOfPeaceBlocks(((Monster) selectedCard).getAttackPoint())) {
            System.out.println("An Spell Card Stops this monster from attacking");
        } else if (myDeck.getMonsterZones().get(selectedCardIndex).getHasAttackedThisRound()) {
            System.out.println("this card already attacked");
        } else {
            gameDecks.get(turn).getMonsterZones().get(selectedCardIndex).attack();
            Monster selectedMonster = (Monster) selectedCard;
            int attackerDamage = selectedMonster.getAttackPoint();
            enemyDeck.takeDamage(attackerDamage);
            System.out.println("you opponent receives " + attackerDamage + " battle damage");
        }
    }

    private void activateSpellErrorCheck() {
        if (selectedCard == null) {
            System.out.println("no card is selected yet");
            return;
        } else if (!(selectedCard instanceof Spell)) {
            System.out.println("activate effect is only for spell cards.");
            return;
        } else if (!(phase == Phase.main1 || phase == Phase.main2 ||
                ((Spell) selectedCard).getSpellIcon().equals("Quick-play"))) {
            System.out.println("you can’t activate an effect on this turn");
            return;
        } else if ((selectedDeck.equals("spell")
                && gameDecks.get(turn).getSpellZones().get(selectedCardIndex).getStatus().equals("O"))
                || (selectedDeck.equals("field") && gameDecks.get(turn).getFieldZoneStatus().equals("O"))) {
            System.out.println("you have already activated this card");
            return;
        } else if (gameDecks.get(turn).isSpellZoneFull() && ((CommonTools.getMatcher
                (((Spell) selectedCard).getSpellIcon(), "Continuous|Field").matches()))) {
            System.out.println("spell card zone is full");
            return;
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
                return;
            } else if (CommonTools.getMatcher(spell.getSpellIcon(), "Field").matches()) {
                moveToGraveyard(turn, "inHand", selectedCardIndex - 1);
                activateOrDeactivateFieldCardForAll(-1);
                if (!gameDecks.get(turn).isFieldZoneEmpty())
                    moveToGraveyard(turn, "field", 0);
                gameDecks.get(turn).setFieldZone(selectedCard);
                gameDecks.get(turn).setFieldZoneStatus("O");
                activateOrDeactivateFieldCardForAll(1);
                System.out.println("Field activated");
                return;
            } else {
                moveToGraveyard(turn, "inHand", selectedCardIndex - 1);
            }
        } else if (selectedDeck.equals("spell")) {
            if (CommonTools.getMatcher(spell.getSpellIcon(), "Continuous").matches()) {
                SpellZone spellZone = gameDecks.get(turn).getSpellZones().get(selectedCardIndex);
                spellZone.setVisible();
                System.out.println("Spell activated");
                return;
            } else {
                moveToGraveyard(turn, "SpellZone", selectedCardIndex);
            }
        } else if (selectedDeck.equals("field")) {
            gameDecks.get(turn).setFieldZoneStatus("O");
            activateOrDeactivateFieldCardForAll(1);
            return;
        }
        System.out.println("Spell activated");
        checkSpellCard();
        deselect();

    }

    private void activateTrap() { // todo complete commands
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
        moveToGraveyard(turn, "SpellZone", selectedCardIndex);
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

    private void activateSpellTerraforming() {
        System.out.println("enter number of selected field spell card:");
        while (true) {
            int index = CommonTools.scan.nextInt();
            Spell spell = (Spell) gameDecks.get(turn).getDeck().get(index - 1);
            if (index > gameDecks.get(turn).getDeck().size()) {
                System.out.println("entered index is bigger than deck's size");
            } else if (!spell.getSpellIcon().equals("Field")) {
                System.out.println("selected card is not a field spell");
            } else {
                index = index - 1;
                Card card = gameDecks.get(turn).getDeck().get(index);
                gameDecks.get(turn).getInHandCards().add(card);
                gameDecks.get(turn).getDeck().remove(index);
                System.out.println("card successfully added to your hand");
                break;
            }
        }
    }

    private void activateSpellPotOfGreed() {
        int size = gameDecks.get(turn).getDeck().size();
        if (size >= 2) {
            Card card1 = gameDecks.get(turn).getDeck().get(size - 1);
            Card card2 = gameDecks.get(turn).getDeck().get(size - 2);
            gameDecks.get(turn).getInHandCards().add(card1);
            gameDecks.get(turn).getInHandCards().add(card2);
            gameDecks.get(turn).getDeck().remove(size - 1);
            gameDecks.get(turn).getDeck().remove(size - 2);
            System.out.println("cards added to your hand successfully");
        }
        if (size == 1) {
            Card card1 = gameDecks.get(turn).getDeck().get(0);
            gameDecks.get(turn).getInHandCards().add(card1);
            gameDecks.get(turn).getDeck().remove(0);
            System.out.println("card added to your hand successfully");
        } else {
            System.out.println("there is no card in your deck");
        }
    }

    private void activateSpellRaigeki() {
        int opponentTurn = changeTurn(turn);
        for (int i = 1; i < 5; i++) {
            gameDecks.get(opponentTurn).getMonsterZones().get(i).removeCard();
            //todo move to graveyard
        }
        System.out.println("all the monsters controlled by your enemy destroyed");
    }

    private void spellActivateChangeOfHeart() {
        int opponentTurn = changeTurn(turn);
        if (gameDecks.get(opponentTurn).isMonsterZoneEmpty()) {
            System.out.println("opponent's monster zone is empty");
            return;
        }
        if (gameDecks.get(turn).isMonsterZoneFull()) {
            System.out.println("your monster zone is full");
            return;
        }
        System.out.println("enter position of a monster from opponent's monster zone");
        int position;
        while (true) {
            position = CommonTools.scan.nextInt();
            if (position < 1 || position > 5) {
                System.out.println("enter a number between 1 and 5");
            } else if (gameDecks.get(opponentTurn).getMonsterZones().get(position).isEmpty()) {
                System.out.println("entered position is empty");
            } else {
                Card card = gameDecks.get(opponentTurn).getMonsterZones().get(position).getCurrentMonster();
                int index = gameDecks.get(turn).summonCardToMonsterZone(card.getName());
                gameDecks.get(opponentTurn).getMonsterZones().get(position).removeCard();
                gameDecks.get(turn).getMonsterZones().get(index).hasBeenChanged = true;
                System.out.println("opponent's monster added to your monster zone");
                break;
            }
        }
    }

    private void spellActivateHarpiesFeatherDuster() {
        int opponentTurn = changeTurn(turn);
        for (int i = 1; i < 5; i++) {
            gameDecks.get(opponentTurn).getSpellZones().get(i).removeCard();
            //todo move to graveyard
        }
        System.out.println("all the spells and traps controlled by your enemy destroyed");
    }

    private void spellActivateDarkHole() {
        for (int i = 1; i <= 5; i++) {
            gameDecks.get(turn).getMonsterZones().get(i).removeCard();
            //todo move to graveyard
        }
        int opponentTurn = changeTurn(turn);
        for (int i = 1; i <= 5; i++) {
            gameDecks.get(opponentTurn).getMonsterZones().get(i).removeCard();
            //todo move to graveyard
        }
        System.out.println("all the monsters on the game board destroyed");
    }

    private void TwinTwisters() {
        if (gameDecks.get(turn).getInHandCards().size() == 0) {
            System.out.println("your hand is empty");
            return;
        }
        if (gameDecks.get(turn).isSpellZoneEmpty()) {
            System.out.println("opponent's spellZone is empty");
            return;
        }
        System.out.println("select a card to remove from your hand");
        int removePosition;
        while (true) {
            removePosition = CommonTools.scan.nextInt();
            if (removePosition > gameDecks.get(turn).getInHandCards().size()) {
                System.out.println("entered index more than in hand cards");
            } else {
                moveToGraveyard(turn, "inHand", removePosition - 1);
                break;
            }
        }
        int destroyedPosition1;
        int destroyedPosition2;
        int opponentTurn = changeTurn(turn);
        while (true) {
            destroyedPosition1 = CommonTools.scan.nextInt();
            if (destroyedPosition1 > 5 || destroyedPosition1 < 1) {
                System.out.println("entered index more than 5 or less than 1");
            } else if (gameDecks.get(opponentTurn).getSpellZones().get(destroyedPosition1).isEmpty()) {
                System.out.println("entered position is empty");
            } else {
                moveToGraveyard(changeTurn(turn), "SpellZone", destroyedPosition1);
                System.out.println("first card removed");
                break;
            }
        }
        while (true) {
            destroyedPosition2 = CommonTools.scan.nextInt();
            if (destroyedPosition2 > 5 || destroyedPosition2 < 1) {
                System.out.println("entered index more than 5 or less than 1");
            } else if (gameDecks.get(opponentTurn).getSpellZones().get(destroyedPosition2).isEmpty()) {
                System.out.println("entered position is empty");
            } else {
                //Todo move to graveyard
                moveToGraveyard(changeTurn(turn), "SpellZone", destroyedPosition2);
                gameDecks.get(opponentTurn).getSpellZones().get(destroyedPosition2).removeCard();
                System.out.println("second card removed");
                break;
            }
        }
    }

    private void surrender(int turn) {
        gameDecks.get(turn).setPlayerLP(0);
        gameDecks.get(changeTurn(turn)).increaseWinRounds();
    }

    private void increasePlayerLPCheat(String command) {
        Matcher matcher = CommonTools.getMatcher(command, "^increase --LP (\\d+)$");
        matcher.find();
        int amountOfLP = Integer.parseInt(matcher.group(1));
        GameDeck myDeck = gameDecks.get(turn);
        myDeck.increaseLP(amountOfLP);
    }

    private void setWinnerCheat(String command) {
        Matcher matcher = CommonTools.getMatcher(command, "^duel set-winner (\\S+)$");
        matcher.find();
        String playerNickname = matcher.group(1);
        if (gameDecks.get(0).getPlayerNickName().equals(playerNickname)) {
            surrender(1);
        } else if (gameDecks.get(1).getPlayerNickName().equals(playerNickname)) {
            surrender(0);
        } else System.out.println("There is no player with this nickname!");
    }

    private void roundOver(int turn) { // 0 : firstPlayer losses , 1 : secondPlayer losses
        GameDeck firstDeck = gameDecks.get(0);
        GameDeck secondDeck = gameDecks.get(1);
        firstDeck.addPlayerLPAfterRound();
        secondDeck.addPlayerLPAfterRound();
        firstDeck.setPlayerLP(8000);
        secondDeck.setPlayerLP(8000);
        if (turn == 0) secondDeck.increaseWinRounds();
        else firstDeck.increaseWinRounds();
    }

    private void gameOver(int round) {
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
        System.out.println(winnerUsername + " won the game and the score is: "
                + firstScore + "-" + secondScore);
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
        round++;
    }

    private void drawCard() {
        ArrayList<Card> deck = gameDecks.get(turn).getDeck();
        if (deck.size() == 0) {
            gameDecks.get(turn).setPlayerLP(0);
            return;
        }
        isCardDrawn = 1;
        gameDecks.get(turn).drawCard();
    }

    private void inHandCardCheat() {
        ArrayList<Card> deck = gameDecks.get(turn).getDeck();
        if (deck.size() == 0) return;
        isCardDrawn = 1;
        gameDecks.get(turn).drawCard();
    }

    private void changePhase() {
        if (phase == Phase.end) changeGameTurn();
        phase = phase.next();
        if (isGameStart == 2 && phase == Phase.battle) {
            phase = Phase.end;
        }
        System.out.println(phase);
    }

    private void checkSpellCard() {
        Spell spell = (Spell) selectedCard;
        if (spell.getName().equals("Terraforming")) {
            Terraforming();
        } else if (spell.getName().equals("Pot of Greed")) {
            gameDecks.get(turn).drawCard();
            gameDecks.get(turn).drawCard();
        } else if (spell.getName().equals("Raigeki")) {
            Raigeki();
            System.out.println("All enemy monsters were destroyed");
        } else if (spell.getName().equals("Harpie’s Feather Duster")) {
            HarpieFeatherDuster();
        } else if (spell.getName().equals("Dark Hole")) {
            darkHole();
        } else if (spell.getName().equals("Twin Twisters")) {
            TwinTwisters();
        } else if (spell.getName().equals("Mystical space typhoon")) {
            mysticalTyphoon();
        } else if (spell.getName().equals("Change of Heart")) {
            spellActivateChangeOfHeart();
        } else if (spell.getName().equals("Advance Ritual Art")) {
            advanceRitualArt();
        }
    }

    private void advanceRitualArt() {
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
            ritualSummon();
        } else {
            System.out.println("there is no way you could ritual summon a monster");
        }
    }

    private void ritualSummon() {
        while (true) {
            System.out.println("please select a ritual monster to summon");
            String command = CommonTools.scan.nextLine();
            if (command.matches("^cancel$")) break;
            Matcher matcher = CommonTools.getMatcher(command, "^select --hand (\\d+)$");
            matcher.find();
            int position = Integer.parseInt(matcher.group(1));
            if (position < 1 || position > 5) {
                System.out.println("invalid selection");
                continue;
            }
            ArrayList<Card> inHandCards = gameDecks.get(turn).getInHandCards();
            selectedCard = inHandCards.get(position - 1);
            selectedCardIndex = position;
            selectedDeck = "hand";
            if (!(inHandCards.get(position - 1).getName().equals("Crab Turtle")
                    || inHandCards.get(position - 1).getName().equals("Skull Guardian"))) {
                System.out.println("you should ritual summon right now");
                continue;
            }
            System.out.println("card selected");
            while (true) {
                System.out.println("enter positions of tribute monster in monster zone:");
                boolean isSummonSuccessful = false;
                int sumOfLevels = 0;
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
                    sumOfLevels += tributeMonster.getLevel();
                    if (sumOfLevels > 7) {
                        System.out.println("selected monsters levels don't match with ritual monster");
                        break;
                    } else if (sumOfLevels == 7) {
                        isSummonSuccessful = true;
                        break;
                    }
                }
                if (isSummonSuccessful) {
                    System.out.println("summoned successfully");
                    isSummoned = 1;
                    for (int monsterZonePosition : positionOfTributeMonsters) {
                        gameDecks.get(turn).tributeCardFromMonsterZone(monsterZonePosition);
                    }
                    gameDecks.get(turn).getInHandCards().remove(position - 1);
                    enteredMonsterCardIndex = gameDecks.get(turn).summonCardToMonsterZone(selectedCard.getName());
                    deselect();
                    break;
                }
            }
        }
    }

    private void Terraforming() {
        GameDeck myDeck = gameDecks.get(turn);
        for (int i = 0; i < myDeck.getDeck().size(); i++) {
            if (myDeck.getDeck().get(i).getType().equals("Spell")) {
                Spell spell = (Spell) myDeck.getDeck().get(i);
                if (spell.getSpellIcon().equals("Field")) {
                    myDeck.getInHandCards().add(myDeck.getDeck().get(i));
                    myDeck.getDeck().remove(i);
                    System.out.println("A field card was drawn");
                    return;
                }
            }
        }
        System.out.println("No field card was found");
    }

    private void Raigeki() {
        GameDeck enemyDeck = gameDecks.get(changeTurn(turn));
        for (int i = 1; i <= 5; i++) {
            if (!(enemyDeck.getMonsterZones().get(i).isEmpty())) {
                moveToGraveyard(changeTurn(turn), "MonsterZone", i);
            }
        }
    }

    private void HarpieFeatherDuster() {
        GameDeck enemyDeck = gameDecks.get(changeTurn(turn));
        for (int i = 1; i <= 5; i++) {
            if (!(enemyDeck.getSpellZones().get(i).isEmpty())) {
                moveToGraveyard(changeTurn(turn), "SpellZone", i);
            }
        }
        System.out.println("All enemy spell were destroyed");
    }

    private void darkHole() {
        Raigeki();
        turn = changeTurn(turn);
        Raigeki();
        turn = changeTurn(turn);
        System.out.println("All cards were destroyed");
    }

    private void supplySquad(int turn) {
        if (gameDecks.get(turn).supplyCheck)
            return;
        for (int i = 1; i < 6; i++) {
            SpellZone spellZone = gameDecks.get(turn).getSpellZones().get(i);
            if ((!spellZone.isEmpty()) && spellZone.getCurrentCard().getName().equals("Supply Squad")
                    && spellZone.getStatus().equals("O")) {
                System.out.println("Supply Squad for " + gameDecks.get(turn).getPlayerNickName());
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
                String answer = CommonTools.scan.nextLine();
                if (answer.equals("yes")) {
                    myDeck.decreaseLP(100);
                    System.out.println("You lost 100 LP");
                } else if (answer.equals("no")) {
                    moveToGraveyard(turn, "SpellZone", i);
                    System.out.println("You lost Messenger of Peace");
                } else {
                    System.out.println("invalid answer");
                    keepMessengerOfPeace();
                    return;
                }
            }
        }
    }

    private void twinTwisters() {
        GameDeck myDeck = gameDecks.get(turn);
        GameDeck enemyDeck = gameDecks.get(changeTurn(turn));
        System.out.println("Choose which card you want to discard");
        while (true) {
            int index = CommonTools.scan.nextInt();
            if (index < 1 || index > myDeck.getInHandCards().size() + 1) {
                System.out.println("index is not valid");
                continue;
            }
            moveToGraveyard(turn, "inHand", index - 1);
            break;
        }
        System.out.println("choose index of 2 cards you want to destroy (in 2 separate lines)");
        int[] enemyCardIndexes = new int[2];
        enemyCardIndexes[0] = CommonTools.scan.nextInt();
        enemyCardIndexes[1] = CommonTools.scan.nextInt();
        for (int i = 0; i < 2; i++) {
            if (enemyCardIndexes[i] >= 1 && enemyCardIndexes[i] <= 5) {
                if (!(enemyDeck.getSpellZones().get(enemyCardIndexes[i]).isEmpty())) {
                    moveToGraveyard(changeTurn(turn), "SpellZone", enemyCardIndexes[i]);
                }
            }
        }
        System.out.println("Chosen cards were destroyed");
    }

    private void mysticalTyphoon() {
        System.out.println("Enter the index of the enemy card you want to destroy");
        while (true) {
            int index = CommonTools.scan.nextInt();
            if (index < 1 || index > 5 || !gameDecks.get(changeTurn(turn)).getSpellZones().get(index).isEmpty()) {
                System.out.println("This square is not valid");
                continue;
            }
            System.out.println("Enemy card destroyed");
            moveToGraveyard(changeTurn(turn), "SpellZone", index);
            break;
        }
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
        String cardName = CommonTools.scan.nextLine();
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
}
