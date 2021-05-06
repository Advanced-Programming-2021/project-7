package Menus;

import Model.Card;
import Model.MonsterZone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class GameDeck {
    private ArrayList<MonsterZone> monsterZones = new ArrayList<>(5);
    private ArrayList<Card> spellZones = new ArrayList<>(5);
    private ArrayList<Card> inHandCards = new ArrayList<>();
    private ArrayList<Card> deck = new ArrayList<>();
    private Card fieldZone;
    private ArrayList<Card> graveyardCards = new ArrayList<>();

    private ArrayList<Card> mainDeck = new ArrayList<>();
    private ArrayList<Card> sideDeck = new ArrayList<>();
    
    private String playerNickName;
    private int playerLP;

    public GameDeck(String playerNickName, HashMap<Card, Integer> mainDeck, HashMap<Card, Integer> sideDeck) {
        this.playerNickName = playerNickName;
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

    public ArrayList<MonsterZone> getMonsterZones() {
        return monsterZones;
    }

    public ArrayList<Card> getGraveyardCards() {
        return graveyardCards;
    }

    public String getFieldZone() {
        if (fieldZone == null)
            return "E";
        return "O";
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

    }

    public void increaseLP(int value) {

    }

    public void editBoardCell(int position) {

    }

    public void showBoard() {

    }

    public void selectMonster(int position) {

    }

    public void selectOpponentMonster(int position) {

    }

    public void selectSpell(int position) {

    }

    public void selectField() {

    }

    public void selectHand(int position) {

    }

//    public boolean checkSelectValidity(String field, int position) {
//
//    }

    public void deselect() {

    }

    public void setPosition(Card card, String position) {

    }

    public void setMat() {

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
