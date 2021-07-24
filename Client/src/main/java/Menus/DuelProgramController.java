package Menus;

import Controller.CheatMenuController;
import Model.Cards.Card;
import Model.Cards.Monster;
import Model.Cards.SpellZone;
import Model.CommonTools;
import Model.Player;
import View.MainProgramView;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.com.google.gson.reflect.TypeToken;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
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
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
    public Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2),this::refresh));

    private void refresh(ActionEvent actionEvent) {
        refresh();
    }

    public static int round;
    private ArrayList<GameDeck> gameDecks = new ArrayList<>(2);
    private int turn = 0; //0 : firstPlayer, 1 : secondPlayer
    private String turnName;
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
    private boolean isCardDrawnd = false;

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
        if (!gameDecks.get(turn).getPlayerUserName().equals(Player.getActivePlayer().getUsername())) {
            turn = changeTurn(turn); // TODO: 2021-07-21 ask shayan
        }
        if (isRoundOver()) roundOver(turn);
        if (isGameOver()) gameOver(round);
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
        if (phase == Phase.end) isCardDrawnd = false;
        if (phase == Phase.draw && isCardDrawn == 0 && isGameStart == 0 && timeSealTrap == 0 && !isCardDrawnd) {
            drawCard();
            isCardDrawnd = true;
        }
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
                            if (gameDecks.get(turn).getSpellZones().get(finalI + 1).getCurrentCard().getType().equals("Spell")) {
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
    }

    public void checkForSetOrSummon() {
        if (!turnName.equals(Player.getActivePlayer().getUsername())){
            JOptionPane.showMessageDialog(null, "it's not your turn");
            refresh();
            return;
        }
        String result = null;
        try {
            dataOutputStream.writeUTF("duel summonOrSet");
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refresh();
    }

    private boolean isRoundOver() {
        String result = null;
        try {
            dataOutputStream.writeUTF("duel isRoundOver");
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.parseBoolean(result);
    }

    private boolean isGameOver() {
        String result = null;
        try {
            dataOutputStream.writeUTF("duel isGameOver");
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.parseBoolean(result);

    }

    private String selectMonster(int position) {
        if (!turnName.equals(Player.getActivePlayer().getUsername())){
            refresh();
            return "it's not your turn";
        }
        String result = null;
        try {
            dataOutputStream.writeUTF("duel selectMonster " + position);
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    private String selectSpell(int position) {
        if (!turnName.equals(Player.getActivePlayer().getUsername())){
            refresh();
            return "it's not your turn";
        }
        String result = null;
        try {
            dataOutputStream.writeUTF("duel selectSpell " + position);
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    private String selectHand(int position) {
        if (!turnName.equals(Player.getActivePlayer().getUsername())){
            refresh();
            return "it's not your turn";
        }
        String result = null;
        try {
            dataOutputStream.writeUTF("duel selectHand " + position);
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    private String setPositionMonster(String command) {
        if (!turnName.equals(Player.getActivePlayer().getUsername())){
            refresh();
            return "it's not your turn";
        }
        String result = null;
        try {
            dataOutputStream.writeUTF("duel " + command);
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refresh();
        return result;

    }

    private String flipSummon() {
        if (!turnName.equals(Player.getActivePlayer().getUsername())){
            refresh();
            return "it's not your turn";
        }
        String result = null;
        try {
            dataOutputStream.writeUTF("duel flip");
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refresh();
        return result;
    }


    private String attackCard(String command) {
        if (!turnName.equals(Player.getActivePlayer().getUsername())){
            refresh();
            return "it's not your turn";
        }
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
        refresh();
        return results[0];
    }

    public void directAttack() {
        if (!turnName.equals(Player.getActivePlayer().getUsername())){
            JOptionPane.showMessageDialog(null, "it's not your turn");
            refresh();
            return;
        }
        String result = null;
        try {
            dataOutputStream.writeUTF("duel attack direct");
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
            JOptionPane.showMessageDialog(null, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        refresh();
    }

    private String activateSpellErrorCheck() {
        if (!turnName.equals(Player.getActivePlayer().getUsername())){
            refresh();
            return "it's not your turn";
        }
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
        if (!turnName.equals(Player.getActivePlayer().getUsername())){
            JOptionPane.showMessageDialog(null, "it's not your turn");
            refresh();
            return;
        }
        try {
            dataOutputStream.writeUTF("duel activateTrap");
            dataOutputStream.flush();
            dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refresh();
    }

    private void getTurn(){
        String result = null;
        try {
            dataOutputStream.writeUTF("get turn");
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        turnName = result;
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

    private void roundOver(int turn) {// 0 : firstPlayer losses , 1 : secondPlayer losses
        try {
            dataOutputStream.writeUTF("duel roundOver " + turn);
            dataOutputStream.flush();
            dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gameOver(int round) {
        String result = "";
        try {
            dataOutputStream.writeUTF("duel gameOver " + round);
            dataOutputStream.flush();
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, result);
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

    private void drawCard() {
        try {
            dataOutputStream.writeUTF("duel draw card");
            dataOutputStream.flush();
            dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refresh();
    }

    public void inHandCardCheat() {
        ArrayList<Card> deck = gameDecks.get(turn).getDeck();
        if (deck.size() == 0) return;
        isCardDrawn = 1;
        gameDecks.get(turn).drawCard();
        setField();
    }

    private void changePhase() {
        if (!turnName.equals(Player.getActivePlayer().getUsername())){
            JOptionPane.showMessageDialog(null, "it's not your turn");
            refresh();
            return;
        }
        try {
            dataOutputStream.writeUTF("duel changePhase");
            dataOutputStream.flush();
            dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refresh();
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

    public void refresh() {
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
        String result = null;
        try {
            dataOutputStream.writeUTF("refresh phase");
            dataOutputStream.flush();
            result = dataInputStream.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Type phaseType = new TypeToken<Phase>() {
        }.getType();
        phase = yaGson.fromJson(result, phaseType);
        getTurn();
        if (!turnName.equals(Player.getActivePlayer().getUsername())) {
            timeline.setCycleCount(Timeline.INDEFINITE);
            if (!isGameOver()) timeline.play();
        } else {
            timeline.stop();
        }
        setField();
    }

    public void setGameDecks(ArrayList<GameDeck> gameDecks) {
        this.gameDecks = gameDecks;
    }
}
