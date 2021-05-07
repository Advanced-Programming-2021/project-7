package Menus;

import Model.Cards.*;
import Model.CommonTools;
import Model.Deck;
import Model.Player;

import java.util.ArrayList;

class DuelProgramControler {
    private ArrayList<GameDeck> gameDecks = new ArrayList<>(2);
    private int turn = 0; //0 : firstPlayer, 1 : secondPlayer
    private ArrayList<Card> mainDeck;

    public void run(String firstPlayer, String secondPlayer, int round) {
        setGameDecks(firstPlayer, secondPlayer);
        String command = CommonTools.scan.nextLine();
        while (true) {
            showGameDeck(turn);
            if (command.matches("^show graveyard$")) showGraveyard(turn);
            else if (command.matches("^surrender$")) surrender(turn);
            else if (command.matches("^select .*$")) selectCard(command);
            else System.out.println("invalid command");
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

        System.out.printf("\t");
        for (int i = 4; i >= 0; i--) {
            System.out.printf("%s\t", enemyDeck.getSpellZones().get(i).getStatus());
        }
        System.out.printf("\n");

        System.out.printf("\t");
        for (int i = 4; i >= 0; i--) {
            System.out.printf("%s\t", enemyDeck.getMonsterZones().get(i).getStatus());
        }
        System.out.printf("\n");

        System.out.println(enemyDeck.getGraveyardCards().size() + "\t\t\t\t\t\t" + enemyDeck.getFieldZoneAsString());
    }

    private void showMyDeck(GameDeck myDeck) {
        System.out.println(myDeck.getFieldZoneAsString() + "\t\t\t\t\t\t" + myDeck.getGraveyardCards().size());

        System.out.printf("\t");
        for (int i = 0; i < 5; i++) {
            System.out.printf("%s\t", myDeck.getMonsterZones().get(i).getStatus());
        }
        System.out.printf("\n");

        System.out.printf("\t");
        for (int i = 0; i < 5; i++) {
            System.out.printf("%s\t", myDeck.getSpellZones().get(i).getStatus());
        }
        System.out.printf("\n");

        for (int i = 0; i < myDeck.getInHandCards().size(); i++) {
            System.out.printf("c\t");
        }
        System.out.println(myDeck.getPlayerNickName() + " : " + myDeck.getPlayerLP());
    }

    private void selectCard(String command) {
        String address = command.substring(7);
        if (!isAddressValid(address)) {
            System.out.println("invalid selection");
            return;
        }
        System.out.println("card selected");
    }

    private boolean isAddressValid(String address) {
        if (!address.matches("^(?:(?:--monster|--spell|--field|--hand|--opponent)( (\\d+))* ?){2,3}$"))
            return false;
        String monster = CommonTools.takeNameOutOfCommand(address, "--monster");
        String spell = CommonTools.takeNameOutOfCommand(address, "--spell");
        String field = CommonTools.takeNameOutOfCommand(address, "--field");
        String hand = CommonTools.takeNameOutOfCommand(address, "--hand");
        String opponent = CommonTools.takeNameOutOfCommand(address, "--opponent");
        if ((monster == null && spell == null && field != null && hand == null) || opponent != null) return false;
        if (monster != null) {
            if (Integer.parseInt(monster) > 5 || Integer.parseInt(monster) < 1) return false;
        }
        if (spell != null) {
            if (Integer.parseInt(spell) > 5 || Integer.parseInt(spell) < 1) return false;
        }
        if (hand != null) {
            if (address.contains("--opponent")) return false;
            if (Integer.parseInt(hand) > gameDecks.get(turn).getInHandCards().size() || Integer.parseInt(hand) < 1)
                return false;

        }
        return true;
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
    private void showGraveyard(int turn) {
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

    //
//    private void cardShow(GameDeck playerDeck, GameDeck enemyDeck)
//    {
//
//    }
//
    private void surrender(int turn) {

    }

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
