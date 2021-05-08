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
        for (int i = 1; i <= round; i++) {
            // methods to be set after each round
            if (isGameOver(i)) break;
            setGameDecks(firstPlayer, secondPlayer);
            while (true) {
                if (isRoundOver()) break;
                String command = CommonTools.scan.nextLine();
                showGameDeck(turn);
                if (command.matches("^show graveyard$")) showGraveyard(turn);
                else if (command.matches("^surrender$")) surrender(turn);
                else if (command.matches("^select .*$")) selectCard(command);
                else if (command.matches("^select -d$")) System.out.println("no card is selected yet");
                else System.out.println("invalid command");
            }
        }
        gameOver(round);
    }

    private boolean isRoundOver() {
        if (gameDecks.get(0).getPlayerLP() <= 0) {
            roundOver(0);
            return true;
        } else if (gameDecks.get(1).getPlayerLP() <= 0) {
            roundOver(1);
            return  true;
        } else return  false;
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
                Deck.getMainDeckByDeck(activeDeck1), Deck.getSideDeckByDeck(activeDeck1));
        GameDeck gameDeckSecond = new GameDeck(secondNick, secondPlayer,
                Deck.getMainDeckByDeck(activeDeck2), Deck.getSideDeckByDeck(activeDeck2));
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
        int selectedSide = turn;
        if (address.matches("^(?:(?:--monster|--spell|--field|--hand|--opponent)( (\\d+))* ?){3}$"))
            selectedSide = changeTurn(turn);
        if (command.matches("^(?:(?:--monster|--opponent)( (\\d+))* ?){2,3}$")) {
            int position = Integer.parseInt(CommonTools.takeNameOutOfCommand(address, "--monster"));
            gameDecks.get(selectedSide).selectMonster(position);
        } else if (command.matches("^(?:(?:--spell|--opponent)( (\\d+))* ?){2,3}$")) {
            int position = Integer.parseInt(CommonTools.takeNameOutOfCommand(address, "--spell"));
            gameDecks.get(selectedSide).selectSpell(position);
        } else if (command.matches("^(?:(?:--hand|--opponent)( (\\d+))* ?){2,3}$")) {
            int position = Integer.parseInt(CommonTools.takeNameOutOfCommand(address, "--hand"));
            gameDecks.get(selectedSide).selectHand(position);
        } else if (command.matches("^(?:(?:--field|--opponent)( (\\d+))* ?){1,2}$"))
            gameDecks.get(selectedSide).selectField();
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
        gameDecks.get(turn).setPlayerLP(0);
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
        int firstScore = 0;
        int secondScore = 0;
        if (round == 1) {
            if (gameDecks.get(0).getWinRounds() == 1) {
                firstScore = 1000 + gameDecks.get(0).getMaxPlayerLPAfterRounds();
                secondScore = 100;
                winnerUsername = gameDecks.get(0).getPlayerUserName();
            } else {
                firstScore = 100;
                secondScore = 1000 + gameDecks.get(0).getMaxPlayerLPAfterRounds();
                winnerUsername = gameDecks.get(1).getPlayerUserName();
            }
        } else if (round == 3) {
            if (gameDecks.get(0).getWinRounds() == 2) {
                firstScore = 3000 + (3 * gameDecks.get(0).getMaxPlayerLPAfterRounds());
                secondScore = 300;
                winnerUsername = gameDecks.get(0).getPlayerUserName();
            } else {
                firstScore = 300;
                secondScore = 3000 + (3 * gameDecks.get(0).getMaxPlayerLPAfterRounds());
                winnerUsername = gameDecks.get(1).getPlayerUserName();
            }
        }
        System.out.println(winnerUsername + " won the game and the score is: "
                + firstScore + "-" + secondScore);
    }

    private int changeTurn(int turn) {
        if (turn == 1)
            return 0;
        return 1;
    }
}
