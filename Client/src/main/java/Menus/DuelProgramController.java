package Menus;

import Controller.CheatMenuController;
import Model.Cards.*;
import Model.CommonTools;
import Model.Player;
import Model.Sound;
import View.MainProgramView;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.com.google.gson.reflect.TypeToken;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import static Controller.DuelMenuController.dataInputStream;
import static Controller.DuelMenuController.dataOutputStream;

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
    public static int round;
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
    private int timeSealTrap = 0;
    private int isCardDrawn = 0;
    private int isGameStart = 2;
    private boolean messengerChecked = false;
    private int isAI = 0;
    private AI ai = new AI();
    private boolean controlPressed = false;
    private boolean shiftPressed = false;
    private boolean cPressed = false;

    @FXML
    public BorderPane myBorderPane;
    public GridPane enemyGrid;
    public GridPane myGrid;
    public ImageView selectedCardShow;
    public ImageView myGrave;
    public ImageView enemyGrave;
    public HBox inHandCards;
    public HBox enemyHand;
    public Button nextPhaseButton;
    public Circle attackSign;
    public Label enemyName;
    public Label myName;
    public Rectangle enemyProfile;
    public Rectangle myProfile;
    public ImageView myField;
    public ImageView enemyFiled;
    public AnchorPane field;

    private Stage stage;
    private Scene scene;
    private AnchorPane root;

    @FXML
    public void initialize() {
        attackSign = new Circle(20);
        attackSign.setFill(new ImagePattern(new Image("/Images/Attack.png")));
        inHandCards.setSpacing(20);
        enemyHand.setSpacing(20);
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
        refresh();
//        setField();
        makeCheatMenu();
    }

    public void makeCheatMenu() {
        myBorderPane.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case CONTROL:
                    controlPressed = true;
                    break;
                case SHIFT:
                    shiftPressed = true;
                    break;
                case C:
                    cPressed = true;
                    break;
                default:
                    controlPressed = false;
                    shiftPressed = false;
                    cPressed = false;
                    break;
            }
            if (controlPressed && shiftPressed && cPressed) {
                controlPressed = false;
                shiftPressed = false;
                cPressed = false;
                CheatMenuController.setDuelProgramController(this);
                try {
                    new CheatMenu().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setField() {
        if (!gameDecks.get(turn).getPlayerUserName().equals(Player.getActivePlayer().getUsername())){
            turn = changeTurn(turn); // TODO: 2021-07-21 ask shayan
        }
        if (isRoundOver()) roundOver(turn);
        if (isGameOver(round)) gameOver(round);
        if (phase == Phase.standby && !messengerChecked) {
            keepMessengerOfPeace();
            messengerChecked = true;
        }
        if (!gameDecks.get(turn).isFieldZoneEmpty()) {
            myField.setImage(cardView(gameDecks.get(turn).getFieldZone().getName()));
            String name = gameDecks.get(turn).getFieldZone().getName();
            BackgroundFill backgroundFill;
            if (name.equals("Yami")) {
                backgroundFill = new BackgroundFill(new ImagePattern(new Image("/Images/Field/fie_yami.bmp")), null, null);
            } else if (name.equals("Forest"))
                backgroundFill = new BackgroundFill(new ImagePattern(new Image("/Images/Field/fie_sougen.bmp")), null, null);
            else if (name.equals("Closed Forest"))
                backgroundFill = new BackgroundFill(new ImagePattern(new Image("/Images/Field/fie_gaia.bmp")), null, null);
            else
                backgroundFill = new BackgroundFill(new ImagePattern(new Image("/Images/Field/fie_umi.bmp")), null, null);
            Background background = new Background(backgroundFill);
            field.setBackground(background);
        }
        if (!gameDecks.get(changeTurn(turn)).isFieldZoneEmpty()) {
            enemyFiled.setImage(cardView(gameDecks.get(changeTurn(turn)).getFieldZone().getName()));
        }
        if (phase == Phase.draw && isCardDrawn == 0 && isGameStart == 0 && timeSealTrap == 0) drawCard();
        nextPhaseButton.setText("next phase. current phase : " + phase);
        enemyGrid.getChildren().clear();
        myGrid.getChildren().clear();
        enemyName.setText(gameDecks.get(changeTurn(turn)).getPlayerNickName() + " : " + gameDecks.get(changeTurn(turn)).getPlayerLP());
        myName.setText(gameDecks.get((turn)).getPlayerNickName() + " : " + gameDecks.get((turn)).getPlayerLP());
        int enemyProfileNumber = Player.getPlayerByUsername(gameDecks.get(changeTurn(turn)).getPlayerUserName()).getProfile();
        int myProfileNumber = Player.getPlayerByUsername(gameDecks.get(turn).getPlayerUserName()).getProfile();
        enemyProfile.setFill(new ImagePattern(new Image(getClass().getResource("/Images/Profiles/profile" + enemyProfileNumber + ".png").toExternalForm())));
        myProfile.setFill(new ImagePattern(new Image(getClass().getResource("/Images/Profiles/profile" + myProfileNumber + ".png").toExternalForm())));
        for (int i = 0; i < 5; i++) {
            for (int i1 = 0; i1 < 2; i1++) {
                Rectangle rectangle = new Rectangle(60, 90);
                rectangle.setFill(Color.TRANSPARENT); // TODO optional
                Rectangle rectangle1 = new Rectangle(60, 90);
                rectangle1.setFill(Color.TRANSPARENT); // TODO optional
                rectangle.setCursor(Cursor.HAND);
                rectangle1.setCursor(Cursor.HAND);
                if (!gameDecks.get(turn).getMonsterZones().get(i + 1).isEmpty() && i1 == 0) {
                    Image image = cardView(gameDecks.get(turn).getMonsterZones().get(i + 1).getCurrentMonster().getName());
                    rectangle.setFill(new ImagePattern(image));
                    if (!gameDecks.get(turn).getMonsterZones().get(i + 1).getStatus().equals("OO"))
                        rectangle.setRotate(90);
                } else if (!gameDecks.get(turn).getSpellZones().get(i + 1).isEmpty() && i1 == 1) {
                    Image image = cardView(gameDecks.get(turn).getSpellZones().get(i + 1).getCurrentCard().getName());
                    rectangle.setFill(new ImagePattern(image));
                }
                if (!gameDecks.get(changeTurn(turn)).getMonsterZones().get(6 - i - 1).isEmpty() && i1 == 1) {
                    Image image = cardView(gameDecks.get(changeTurn(turn)).getMonsterZones().get(6 - i - 1).getCurrentMonster().getName());
                    rectangle1.setFill(new ImagePattern(image));
                    if (gameDecks.get(changeTurn(turn)).getMonsterZones().get(6 - i - 1).getStatus().equals("DH")) {
                        image = cardView("Unknown");
                        rectangle1.setFill(new ImagePattern(image));
                    }
                    if (!gameDecks.get(changeTurn(turn)).getMonsterZones().get(6 - i - 1).getStatus().equals("OO"))
                        rectangle1.setRotate(90);
                } else if (!gameDecks.get(changeTurn(turn)).getSpellZones().get(6 - i - 1).isEmpty() && i1 == 0) {
                    Image image = cardView("Unknown");
                    rectangle1.setFill(new ImagePattern(image));
                }
                int finalI1 = i1;
                int finalI = i;

                rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (phase != Phase.battle && finalI1 == 0) {
                            String[] buttons = {"attack", "defense", "flip"};
                            int returnValue = JOptionPane.showOptionDialog(null, "Change Position", "Change Position",
                                    JOptionPane.OK_OPTION, 1, null, buttons, buttons[0]);
                            if (returnValue == 0) {
                                JOptionPane.showMessageDialog(null, setPositionMonster("set --position attack"));
                            } else if (returnValue == 1) {
                                JOptionPane.showMessageDialog(null, setPositionMonster("set --position defense"));
                            } else if (returnValue == 2) {
                                selectMonster(finalI + 1);
                                JOptionPane.showMessageDialog(null, flipSummon());
                            }
                        } else if (phase != Phase.battle && finalI1 == 1) {
                            JOptionPane.showMessageDialog(null, selectSpell(finalI + 1));
                            if (gameDecks.get(turn).getSpellZones().get(finalI).getCurrentCard().getType().equals("Spell")) {
                                String message = activateSpellErrorCheck();
                                if (!message.equals(""))
                                    JOptionPane.showMessageDialog(null, message);
                            } else {
                                activateTrap();
                            }
                        } else if (phase == Phase.battle) {
                            if (finalI1 == 0) {
                                String message = selectMonster(finalI + 1);
                                JOptionPane.showMessageDialog(null, message);
                                if (message.equals("card selected")) {
                                    enemyGrid.getChildren().remove(attackSign);
                                    enemyGrid.add(attackSign, finalI, finalI1);
                                }
                            }
                        }
                    }
                });
                rectangle.setOnMouseMoved(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (rectangle.getFill() instanceof ImagePattern) {
                            Image image = ((ImagePattern) rectangle.getFill()).getImage();
                            selectedCardShow.setImage(image);
                        }
                    }
                });
                rectangle1.setOnMouseMoved(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (rectangle1.getFill() instanceof ImagePattern) {
                            Image image = ((ImagePattern) rectangle1.getFill()).getImage();
                            selectedCardShow.setImage(image);
                        }
                    }
                });
                rectangle1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (phase == Phase.battle) {
                            if (finalI1 == 1)
                                JOptionPane.showMessageDialog(null, attackCard("attack " + (5 - finalI)));
                            setField();
                        }
                    }
                });
                enemyGrid.add(rectangle, i, i1);
                myGrid.add(rectangle1, i, i1);
            }
        }

        inHandCards.getChildren().clear();
        for (int i = 0; i < gameDecks.get(turn).getInHandCards().size(); i++) {
            Rectangle rectangle = new Rectangle(60, 90);
            rectangle.setCursor(Cursor.HAND);
            rectangle.setFill(new ImagePattern(cardView(gameDecks.get(turn).getInHandCards().get(i).getName())));
            inHandCards.getChildren().add(rectangle);
            int finalI = i;
            rectangle.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (rectangle.getFill() instanceof ImagePattern) {
                        Image image = ((ImagePattern) rectangle.getFill()).getImage();
                        selectedCardShow.setImage(image);
                    }
                }
            });
            final double[] oldX = new double[1];
            final double[] oldY = new double[1];

            rectangle.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (phase == Phase.battle) {
                        JOptionPane.showMessageDialog(null, "can't do that in this this phase");
                        return;
                    }
                    rectangle.setMouseTransparent(true);
                    selectHand(finalI + 1);
                    System.out.println(selectedCard);
                    oldX[0] = mouseEvent.getSceneX();
                    oldY[0] = mouseEvent.getSceneY();
                }
            });

            rectangle.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (phase == Phase.battle)
                        return;

                    Node node = (Node) mouseEvent.getSource();
                    node.setTranslateX(mouseEvent.getSceneX() - oldX[0]);
                    node.setTranslateY(mouseEvent.getSceneY() - oldY[0]);
                }
            });

            rectangle.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (phase == Phase.battle)
                        return;

                    Bounds bounds = enemyGrid.sceneToLocal(rectangle.localToScene(rectangle.getBoundsInLocal()));
                    System.out.println(enemyGrid.sceneToLocal(rectangle.localToScene(rectangle.getBoundsInLocal())));
                    if (bounds.getMinX() > -20 &&
                            bounds.getMinX() < 450 &&
                            bounds.getMinY() > -20 &&
                            bounds.getMinY() < 150)
                        checkForSetOrSummon();

                    rectangle.setTranslateX(0);
                    rectangle.setTranslateY(0);
                    setField();
                }
            });
        }

        enemyHand.getChildren().clear();
        for (int i = 0; i < gameDecks.get(changeTurn(turn)).getInHandCards().size(); i++) {
            Rectangle rectangle = new Rectangle(60, 90);
            rectangle.setFill(new ImagePattern(new Image(getClass().getResource("/Images/Cards/Unknown.jpg").toExternalForm())));
            enemyHand.getChildren().add(rectangle);
        }
//        if (secondPlayer.equals("ai") && turn == 1) {
//            ai.updateAI(gameDecks.get(1), gameDecks.get(0), phase);
//            String command = ai.decision();
//            runCommand(command);
//            setField();
//        }
    }

    private void runCommand(String command) {
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
        for (int i = 1; i <= round; i++) {
//            setGameDecks(firstPlayer, secondPlayer);
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
        if (round == 3) {
            if (gameDecks.get(0).getWinRounds() > 1) return true;
            if (gameDecks.get(1).getWinRounds() > 1) return true;
        } else {
            if (gameDecks.get(0).getWinRounds() == 1) return true;
            if (gameDecks.get(1).getWinRounds() == 1) return true;
        }
        return false;
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

    private String selectHand(int position) {
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

    private String summonMonster() {
        String result = null;
        try {
            dataOutputStream.writeUTF("duel summon");
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refresh();
        return result;
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
        Sound.getSoundByName("set").playSoundOnce();
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

    private String setPositionMonster(String command) {
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

    private String flipSummon() {
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

    private String attackCard(String command) {
        int selectDefender;
        Matcher matcher = CommonTools.getMatcher(command, "(\\d+)");
        matcher.find();
        selectDefender = Integer.parseInt(matcher.group(1));
        String result = null;
        try {
            dataOutputStream.writeUTF("duel attack " + selectDefender);
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] results = result.split("#");
        String[] parts = results[1].split(" ");
        gameDecks.get(turn).takeDamage(Integer.parseInt(parts[2]));
        gameDecks.get(changeTurn(turn)).takeDamage(Integer.parseInt(parts[1]));
        if (parts[0].equals("opponent")){
            moveToGraveyard(changeTurn(turn), "MonsterZone", selectDefender);
        } else if (parts[0].startsWith("your")){
            matcher = CommonTools.getMatcher(command, "(\\d+)");
            matcher.find();
            int num = Integer.parseInt(matcher.group(1));
            moveToGraveyard(turn, "MonsterZone", num);
        } else if (parts[0].startsWith("both")){
            matcher = CommonTools.getMatcher(command, "(\\d+)");
            matcher.find();
            int num = Integer.parseInt(matcher.group(1));
            moveToGraveyard(turn, "MonsterZone", num);
            moveToGraveyard(changeTurn(turn), "MonsterZone", selectDefender);
        }
        return results[0];
    }

    public void directAttack() {
        String result = null;
        try {
            dataOutputStream.writeUTF("duel attack direct");
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
            JOptionPane.showMessageDialog(null, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result.startsWith("your")) {
            String damage = CommonTools.takeNameOutOfCommand(result, "receives");
            gameDecks.get(changeTurn(turn)).takeDamage(Integer.parseInt(damage));
            setField();
        }

    }

    private String activateSpellErrorCheck() {
        String result = null;
        try {
            dataOutputStream.writeUTF("duel activate effect");
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refresh();
        return result;
    }

    private void activateTrap() {
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
        //moveToGraveyard(turn, "SpellZone", selectedCardIndex);
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
            Sound.getSoundByName("set").playSoundOnce();
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
            Sound.getSoundByName("set").playSoundOnce();
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
        JOptionPane.showMessageDialog(null, winnerUsername + " won the game and the score is: "
                + firstScore + "-" + secondScore);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/main_menu_view.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = MainProgramView.stage;
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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

    private void drawCard() {
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

    private void changePhase() {
        if (phase == Phase.end) changeGameTurn();
        phase = phase.next();
        if (isGameStart == 2 && phase == Phase.battle) {
            phase = Phase.end;
        }
        System.out.println(phase);
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

    private void refresh() {
        String gameDecks = null;
        try {
            dataOutputStream.writeUTF("refresh");
            dataOutputStream.flush();
            gameDecks = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        YaGson yaGson = new YaGson();
        Type arraylistOfPlayer = new TypeToken<ArrayList<GameDeck>>() {
        }.getType();
        setGameDecks(yaGson.fromJson(gameDecks, arraylistOfPlayer));
        setField();
    }

    public void setGameDecks(ArrayList<GameDeck> gameDecks) {
        this.gameDecks = gameDecks;
    }
}
