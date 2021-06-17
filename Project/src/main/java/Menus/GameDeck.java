package Menus;

import Model.Cards.Card;
import Model.Cards.MonsterZone;
import Model.Cards.SpellZone;

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
    private String fieldZoneStatus = "E";
    private ArrayList<Card> graveyardCards = new ArrayList<>();
    private ArrayList<Integer> playerLPsAfterRound = new ArrayList<>();

    private String playerNickName;
    private String playerUserName;
    private int playerLP;
    private int winRounds;

    public GameDeck(String playerNickName, String playerUserName, HashMap<Card, Integer> mainDeck, HashMap<Card, Integer> sideDeck) {
        this.playerNickName = playerNickName;
        this.playerUserName = playerUserName;
        this.playerLP = 8000;
        this.winRounds = 0;
        for (int i = 1; i <= 5; i++){
            MonsterZone monsterZone = new MonsterZone();
            monsterZones.put(i, monsterZone);
        }
        for (int i = 1; i <= 5; i++){
            SpellZone spellZone = new SpellZone();
            spellZones.put(i, spellZone);
        }
        for (Map.Entry<Card, Integer> cardEntry : mainDeck.entrySet()) {
            for (Integer i = 0; i < cardEntry.getValue(); i++) {
                deck.add(Card.getCardByName(cardEntry.getKey().getName()));
            }
        }
        Collections.shuffle(deck);
        for (Map.Entry<Card, Integer> cardEntry : sideDeck.entrySet()) {
            for (Integer i = 0; i < cardEntry.getValue(); i++) {
                // TODO clone
            }
        }
    }

    public void setFieldZoneStatus(String fieldZoneStatus) {
        this.fieldZoneStatus = fieldZoneStatus;
    }

    public String getFieldZoneStatus() {
        return fieldZoneStatus;
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

    public boolean isSpellZoneFull() {
        for (int i = 1; i <= 5; i++) {
            if (spellZones.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isSpellZoneEmpty() {
        for (int i = 1; i <= 5; i++) {
            if (!spellZones.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean doesTrapExist(String cardName) {
        for (int i = 1; i <= 5; i++) {
            if (spellZones.get(i).getCurrentCard().getName().equals(cardName)) {
                return true;
            }
        }
        return false;
    }

    public Card getFieldZone() {
        return fieldZone;
    }

    public void emptyFieldZone() {
        fieldZone = null;
    }

    public boolean isFieldZoneEmpty() {
        if (fieldZone == null)
            return true;
        return false;
    }

    public String getFieldZoneAsString() {
        if (fieldZone == null)
            return "E";
        return "O";
    }

    public boolean isMonsterZoneEmpty() {
        for (int i = 1; i <= monsterZones.size(); i++) {
            if (!monsterZones.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isMonsterZoneFull() {
        for (int i = 1; i <= monsterZones.size(); i++) {
            if (monsterZones.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void setFieldZone(Card fieldZone) {
        this.fieldZone = fieldZone;
    }

    public int spellZoneFirstFreeSpace() {
        for (int i = 1; i <= spellZones.size(); i++) {
            if (spellZones.get(i).isEmpty()) {
                return i;
            }
        }
        return -1;
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

    public void takeDamage(int damage) {
        playerLP -= damage;
    }

    public void heal(int amount) {
        playerLP += amount;
    }

    public void drawCard() {
        Card card = deck.get(deck.size() - 1);
        inHandCards.add(card);
        deck.remove(deck.size() - 1);
        System.out.println("new card added to the hand: " + card.getName());
    }

    public void summonCardToMonsterZone(String cardName) {
        for (int i = 1; i <= 5; i++) {
            if (monsterZones.get(i).getCurrentMonster() == null) {
                monsterZones.get(i).setCardAttack(Card.getCardByName(cardName));
                return;
            }
        }
    }

    public void tributeCardFromMonsterZone(int position) {
        graveyardCards.add(monsterZones.get(position).getCurrentMonster());
        monsterZones.get(position).removeCard();
    }

    public void setCardToMonsterZone(String cardName) {
        for (int i = 1; i <= 5; i++) {
            if (monsterZones.get(i).getCurrentMonster() == null) {
                monsterZones.get(i).setCardHidden(Card.getCardByName(cardName));
                return;
            }
        }
    }

    public void setSpellToSpellZone(String cardName){
        for (int i = 1; i <= 5; i++) {
            if (spellZones.get(i).getCurrentCard() == null) {
                spellZones.get(i).setSpell(Card.getCardByName(cardName));
                spellZones.get(i).setHidden();
                return;
            }
        }
    }

    public void setTrapToSpellZone(String cardName){
        for (int i = 1; i <= 5; i++) {
            if (spellZones.get(i).getCurrentCard() == null) {
                spellZones.get(i).setTrap(Card.getCardByName(cardName));
                spellZones.get(i).setHidden();
                return;
            }
        }
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
