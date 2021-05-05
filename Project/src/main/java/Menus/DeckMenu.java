package Menus;

import Model.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DeckMenu
{
    private Player loggedInUser;


    public void run(String username) throws IOException {
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^deck create [^ ]+$")) createDeck(username, command);
            else if (command.matches("^deck delete [^ ]+$")) deleteDeck(username, command);
            else if (command.matches("^deck set-activate [^ ]+$")) setActiveDeck(username, command);
            else if (command.matches("^deck add-card (?:(?:--card|--deck) ([^ ]+) (--side)*?){2,3}$"))
                addCardToDeck(username, command);
            else if (command.matches("^deck rm-card (?:(?:--card|--deck) ([^ ]+) (--side)*?){2,3}$"))
                removeCardFromDeck(username, command);
            else if (command.matches("^deck show --all")) showAllDecks(username);
            else if (command.matches("^menu enter (profile|duel|deck|shop|scoreboard)$"))
                System.out.println("menu navigation is not possible");
            else if (command.matches("^menu show-current$")) System.out.println("deck");
            else if (command.matches("^menu exit$")) {
                return;
            } else System.out.println("invalid command!");
        }
    }		
    
    private void createDeck(String username, String command) throws IOException {
        String pattern = "^deck create ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String deckName = m.group(1);
        if(Deck.doesDeckExist(deckName)){
            System.out.printf("deck with name %s already exists\n", deckName);
            return;
        }
        System.out.println("deck created successfully!");
        new Deck(deckName, username);
        FileHandler.updatePlayers();
    }		
    
    private void deleteDeck(String username, String command) throws IOException {
        String pattern = "^deck delete ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String deckName = m.group(1);
        if(Deck.getDeckByNames(deckName, username) == null){
            System.out.printf("deck with name %s does not exist\n", deckName);
            return;
        }
        System.out.println("deck delete successfully!");
        Player.getPlayerByUsername(username).removeDeck(deckName);
        if(Player.getActiveDeckByUsername(username).getDeckName().equals(deckName)){
            Player.getPlayerByUsername(username).removeActiveDeck();
        }
        FileHandler.updatePlayers();
    }

    public void setActiveDeck(String username, String command) throws IOException {
        String pattern = "^deck set-activate ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String deckName = m.group(1);
        if(Deck.getDeckByNames(deckName, username) == null){
            System.out.printf("deck with name %s does not exist\n", deckName);
            return;
        }
        System.out.println("deck activated successfully");
        Player.getPlayerByUsername(username).setActiveDeck(deckName);
        FileHandler.updatePlayers();
    }
    
    private void addCardToDeck(String username, String command) throws IOException {
        String cardName = CommonTools.takeNameOutOfCommand(command, "--card");
        String deckName = CommonTools.takeNameOutOfCommand(command, "--deck");
        if(!addCardValidity(cardName, deckName, username)) return;
        Deck deck = Deck.getDeckByNames(deckName, username);
        Player player = Player.getPlayerByUsername(username);
        if(!command.contains("--side")){
            if(deck.isMainDeckFull()){
                System.out.println("main deck is full");
                return;
            }
            if(deck.isThereThreeCards(Card.getCardByName(cardName))){
                System.out.printf("there are already three cards with name %s in deck %s\n", cardName, deckName);
                return;
            }
            deck.addCardToMainDeck(Card.getCardByName(cardName));
        }
        else{
            if(deck.isSideDeckFull()){
                System.out.println("side deck is full");
                return;
            }
            if(deck.isThereThreeCards(Card.getCardByName(cardName))){
                System.out.printf("there are already three cards with name %s in deck %s\n", cardName, deckName);
                return;
            }
            deck.addCardToSideDeck(Card.getCardByName(cardName));
        }
        //TODO duplicate keys in hashmap
        player.removeCard(Card.getCardByName(cardName));
        System.out.println("card added to deck successfully");
        FileHandler.updatePlayers();
    }
    
    private void removeCardFromDeck(String username, String command) throws IOException {
        String cardName = CommonTools.takeNameOutOfCommand(command, "--card");
        String deckName = CommonTools.takeNameOutOfCommand(command, "--deck");
        boolean side = command.contains("--side");
        if(!removeCardValidity(cardName, deckName, username, side)) return;
        Deck deck = Deck.getDeckByNames(deckName, username);
        Player player = Player.getPlayerByUsername(username);
        if(!command.contains("--side")){
            deck.removeCardFromMainDeck(Card.getCardByName(cardName));
        }
        else{
            deck.removeCardFromSideDeck(Card.getCardByName(cardName));
        }
        player.removeCard(Card.getCardByName(cardName));
        System.out.println("card removed form deck successfully");
        FileHandler.updatePlayers();
    }

    private boolean addCardValidity(String cardName, String deckName, String username){
        if(cardName == null | deckName == null){
            System.out.println("invalid command");
            return false;
        }
        Player player = Player.getPlayerByUsername(username);
        if(!player.doesCardExist(cardName)){
            System.out.printf("card with name %s does not exist\n", cardName);
            return false;
        }
        if(Deck.getDeckByNames(deckName, username) == null){
            System.out.printf("deck with name %s does not exist\n", deckName);
            return false;
        }
        return true;
    }

    private boolean removeCardValidity(String cardName, String deckName, String username, boolean side){
        if(cardName == null | deckName == null){
            System.out.println("invalid command");
            return false;
        }
        if(Deck.getDeckByNames(deckName, username) == null){
            System.out.printf("deck with name %s does not exist\n", deckName);
            return false;
        }
        Deck deck = Deck.getDeckByNames(deckName, username);
        if(!side){
            if(!Deck.getMainDeckByDeck(deck).containsKey(Card.getCardByName(cardName))){
                System.out.printf("card with name %s does not exist in main deck\n", cardName);
                return false;
            }
        }
        else{
            if(!Deck.getSideDeckByDeck(deck).containsKey(Card.getCardByName(cardName))){
                System.out.printf("card with name %s does not exist in side deck\n", cardName);
                return false;
            }
        }
        return true;
    }
    
    private void showAllDecks(String username)
    {
        Player.getPlayerByUsername(username).showDecks();
    }		
    
    private void showDeck(Matcher matcher) 		
    {
        
    }		
    
    private void showCards() 		
    {
        
    }		
}
