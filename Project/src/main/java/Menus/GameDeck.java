package Menus;

import Model.Cards.Card;
import Model.Cards.SpellZone;
import Model.Cards.MonsterZone;
import Model.CommonTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



class GameDeck {
    private HashMap<Integer, MonsterZone> monsterZones = new HashMap<>();
    private HashMap<Integer, SpellZone> spellZones = new HashMap<>();
    private ArrayList<Card> inHandCards = new ArrayList<>();
    private ArrayList<Card> deck = new ArrayList<>();
    private Card fieldZone;
    private ArrayList<Card> graveyardCards = new ArrayList<>();
    private ArrayList<Integer> playerLPsAfterRound = new ArrayList<>();

    private ArrayList<Card> mainDeck = new ArrayList<>();
    private ArrayList<Card> sideDeck = new ArrayList<>();
    
    private String playerNickName;
    private String playerUserName;
    private int playerLP;
    private int winRounds;

    public GameDeck(String playerNickName, String playerUserName, HashMap<Card, Integer> mainDeck, HashMap<Card, Integer> sideDeck) {
        this.playerNickName = playerNickName;
        this.playerUserName = playerUserName;
        this.playerLP = 8000;
        this.winRounds = 0;
        for (Map.Entry<Card, Integer> cardEntry : mainDeck.entrySet()) {
            for (Integer i = 0; i < cardEntry.getValue(); i++) {
                // TODO clone
            }
        }
        for (Map.Entry<Card, Integer> cardEntry : sideDeck.entrySet()) {
            for (Integer i = 0; i < cardEntry.getValue(); i++) {
                // TODO clone
            }
        }
    }

    public String getPlayerNickName() {
        return playerNickName;
    }

    public int getPlayerLP() {
        return playerLP;
    }

    public ArrayList<Card> getInHandCards() {
        return inHandCards;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public HashMap<Integer, MonsterZone> getMonsterZones() {
        return monsterZones;
    }

    public HashMap<Integer, SpellZone> getSpellZones() {
        return spellZones;
    }

    public ArrayList<Card> getGraveyardCards() {
        return graveyardCards;
    }

    public String getFieldZoneAsString() {
        if (fieldZone == null)
            return "E";
        return "O";
    }

    public void takeDamage(int damage){
        playerLP -= damage;
    }

    public void heal(int amount){
        playerLP += amount;
    }

    public void drawCard(String cardName) {

    }

    public void summonCard(String cardName) {

    }

    public void activateCard(String cardName) {

    }

    public void destroyCard(String cardName) {

    }

    public void decreaseCardHP(String cardName) {

    }

    public void decreaseLP(int value) {
        this.playerLP -= value;
    }

    public void increaseLP(int value) {
        this.playerLP += value;
    }

    public void setPlayerLP(int value) {
        this.playerLP = value;
    }

    public void increaseWinRounds() {
        this.winRounds++;
    }

    public int getWinRounds() {
        return this.winRounds;
    }

    public void addPlayerLPAfterRound() {
        playerLPsAfterRound.add(playerLP);
    }

    public int getMaxPlayerLPAfterRounds() {
        return Collections.max(playerLPsAfterRound);
    }

    public void editBoardCell(int position) {

    }

    public void showBoard() {

    }

    public void selectMonster(int position) {
        if(monsterZones.get(position) == null) {
            System.out.println("no card found in the given position");
            return;
        }
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^select -d$")) deselect();
            else if (command.matches("^summon$")) System.out.println("you can’t summon this card");
            else System.out.println("invalid command");
        }
    }

    public void selectOpponentMonster(int position) {

    }

    public void selectSpell(int position) {
        if(spellZones.get(position) == null) {
            System.out.println("no card found in the given position");
            return;
        }
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^select -d$")) deselect();
            else if (command.matches("^summon$")) System.out.println("you can’t summon this card");
            else System.out.println("invalid command");
        }
    }

    public void selectOpponentSpell(int position){

    }

    public void selectField() {
        if(fieldZone == null) {
            System.out.println("no card found in the given position");
            return;
        }
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^select -d$")) deselect();
            else if (command.matches("^summon$")) System.out.println("you can’t summon this card");
            else System.out.println("invalid command");
        }
    }

    public void selectOpponentField(){

    }

    public void deselect() {
        System.out.println("card deselected");
        return;
    }

    public void setPosition(Card card, String position) {

    }

    public void setMat() {

    }

    public String getPlayerUserName() {
        return this.playerUserName;
    }


//    public String getBoardCellByPosition(int position) {
//
//    }
//
//    public String getPlayerName() {
//
//    }
//
//    public ArrayList<Card> getActiveCards() {
//
//    }
//
//    public ArrayList<Card> getInHandCards() {
//
//    }
//
//    public ArrayList<Card> getDestroyedCards() {
//
//    }
//
//    public Card getSelectedCard() {
//
//    }
//
//    public int getNumberOfDeckCards() {
//
//    }
//
//    public int getNumberOfGraveCards() {
//
//    }
//
//    public int getNumberOfFieldCards() {
//
//    }
//
//    public String getMonsterState(int position) {
//
//    }
//
//    public String getSpellState(int position) {
//
//    }
//
//    public int getNumberOfHandCards() {
//
//    }
}
