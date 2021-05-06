package Menus;

import Model.Card;
import Model.Deck;
import Model.Player;

import java.util.ArrayList;

class DuelProgramControler {
    private ArrayList<GameDeck> gameDecks = new ArrayList<>(2);
    private int turn;
    private ArrayList<Card> mainDeck;


    public void run(String firstPlayer, String secondPlayer, int round) {
        Deck activeDeck1 = Player.getActiveDeckByUsername(firstPlayer);
        Deck activeDeck2 = Player.getActiveDeckByUsername(secondPlayer);
        GameDeck gameDeckFirst = new GameDeck(firstPlayer, Deck.getMainDeckByDeck(activeDeck1), Deck.getSideDeckByDeck(activeDeck1));
        GameDeck gameDeckSecond = new GameDeck(secondPlayer, Deck.getMainDeckByDeck(activeDeck2), Deck.getSideDeckByDeck(activeDeck2));
        while (true) {

            showGameDeck(turn);
        }
    }

    private void setGameDecks(String firstPlayer, String secondPlayer) {
        String firstNick = Player.getPlayerByUsername(firstPlayer).getNickname();
        String secondNick = Player.getPlayerByUsername(secondPlayer).getNickname();
        Deck activeDeck1 = Player.getActiveDeckByUsername(firstPlayer);
        Deck activeDeck2 = Player.getActiveDeckByUsername(secondPlayer);
        GameDeck gameDeckFirst = new GameDeck(firstNick, Deck.getMainDeckByDeck(activeDeck1), Deck.getSideDeckByDeck(activeDeck1));
        GameDeck gameDeckSecond = new GameDeck(secondNick, Deck.getMainDeckByDeck(activeDeck2), Deck.getSideDeckByDeck(activeDeck2));
        gameDecks.add(gameDeckFirst);
        gameDecks.add(gameDeckSecond);
    }

    private void showGameDeck(int turn) {
        GameDeck myDeck = gameDecks.get(turn);
        GameDeck enemyDeck = gameDecks.get(changeTurn(turn));
        showEnemyDeck(enemyDeck);
        System.out.println("--------------------------");
        showMyDeck(myDeck);
    }

    private void showEnemyDeck(GameDeck enemyDeck) {
        System.out.println(enemyDeck.getPlayerNickName() + " : " + enemyDeck.getPlayerLP());
        System.out.printf("\t");
        for (int i = 0; i < enemyDeck.getInHandCards().size(); i++) {
            System.out.printf("c\t");
        }
        System.out.printf("\n");
        System.out.println(enemyDeck.getDeck().size());
        // TODO: 2021-05-06 spellZone
        System.out.printf("\t");
        for (int i = 4; i >= 0; i--) {
            System.out.printf("%s\t", enemyDeck.getMonsterZones().get(i).getPositioning());
        }
        System.out.printf("\n");
        System.out.println(enemyDeck.getGraveyardCards().size() + "\t\t\t\t\t\t" + enemyDeck.getFieldZone());
    }

    private void showMyDeck(GameDeck myDeck) {
        System.out.println(myDeck.getFieldZone() + "\t\t\t\t\t\t" + myDeck.getGraveyardCards().size());
        System.out.printf("\t");
        for (int i = 0; i < 5; i++) {
            System.out.printf("%s\t", myDeck.getMonsterZones().get(i).getPositioning());
        }
        System.out.printf("\n");
        // TODO: 2021-05-06 SpellZone
        for (int i = 0; i < myDeck.getInHandCards().size(); i++) {
            System.out.printf("c\t");
        }
        System.out.println(myDeck.getPlayerNickName() + " : " + myDeck.getPlayerLP());
    }

    private void whichCommand(String input, GameDeck playerDeck, GameDeck enemyDeck) {

    }

    private void setPhase(String newPhase, GameDeck playerDeck) {

    }

    private void addToHand(String cardName, GameDeck playerDeck) {

    }

    private void standByPhase(GameDeck playerDeck) {

    }

    private void mainPhase1(GameDeck playerDeck) {

    }

    private void endPhase(GameDeck playerDeck) {

    }

    private void summon(GameDeck playerDeck) {

    }

    //    private String checkSummonCard(Card card, GameDeck playerDeck)
//    {
//
//    }
//
//    private String checkAttributeCard(Card card, GameDeck playerDeck)
//    {
//
//    }
//
//    private String checkSetCard(Card card, GameDeck playerDeck)
//    {
//
//    }
//
//    private void flipSummon(GameDeck playerDeck)
//    {
//
//    }
//
//    private void attackCard(GameDeck playerDeck, GameDeck enemyDeck)
//    {
//
//    }
//
//    private void attackDirect(GameDeck playerDeck, GameDeck enemyDeck)
//    {
//
//    }
//
//    private void activateEffect(GameDeck playerDeck)
//    {
//
//    }
//
//    private void set(GameDeck playerDeck)
//    {
//
//    }
//
//    private void showGraveyard(GameDeck playerDeck, Scanner scanner)
//    {
//
//    }
//
//    private void cardShow(GameDeck playerDeck, GameDeck enemyDeck)
//    {
//
//    }
//
//    private void surrender()
//    {
//
//    }
//
//    private void increaseLP(GameDeck playerDeck)
//    {
//
//    }
//
//    private void duelSetWinner(GameDeck playerDeck)
//    {
//
//    }
//
    private int changeTurn(int turn) {
        if (turn == 1)
            return 0;
        return 1;
    }
}
