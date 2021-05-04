package Menus;

import Model.Card;
import Model.Deck;
import Model.Player;

import java.util.ArrayList;

class DuelProgramControler
{
    private ArrayList<GameDeck> gameDecks;
    private int turn;		
    private ArrayList<Card> mainDeck;

    
    public void run(String firstPlayer, String secondPlayer, int round)
    {
        Deck activeDeck1 = Player.getActiveDeckByUsername(firstPlayer);
        Deck activeDeck2 = Player.getActiveDeckByUsername(secondPlayer);
        GameDeck gameDeckFirst = new GameDeck(firstPlayer, Deck.getMainDeckByDeck(activeDeck1), Deck.getSideDeckByDeck(activeDeck1));
        GameDeck gameDeckSecond = new GameDeck(secondPlayer, Deck.getMainDeckByDeck(activeDeck2), Deck.getSideDeckByDeck(activeDeck2));
        while(true){

            showGameDeck(turn);
        }
    }

    private void showGameDeck(int turn){

    }
    
    private void whichCommand(String input, GameDeck playerDeck, GameDeck enemyDeck) 		
    {
        
    }		
    
    private void setPhase(String newPhase, GameDeck playerDeck) 		
    {
        
    }		
    
    private void addToHand(String cardName, GameDeck playerDeck) 		
    {
        
    }		
    
    private void stanbyPhase(GameDeck playerDeck) 		
    {
        
    }		
    
    private void mainPhase1(GameDeck playerDeck) 		
    {
        
    }		
    
    private void endPhase(GameDeck playerDeck) 		
    {
        
    }		
    
    private void sommon(GameDeck playerDeck) 		
    {
        
    }		
    
//    private String checkSommonCard(Card card, GameDeck playerDeck)
//    {
//
//    }
//
//    private String checkAtributeCard(Card card, GameDeck playerDeck)
//    {
//
//    }
//
//    private String checkSetCard(Card card, GameDeck playerDeck)
//    {
//
//    }
//
//    private void flipSommon(GameDeck playerDeck)
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
//    private int changeTurn(int turn)
//    {
//
//    }
}
