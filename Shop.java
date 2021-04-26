//package com.example.main;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shop {

    public void run(String username, Scanner scan){
        while(true){
            String command = scan.nextLine();
            if(command.matches("^shop buy [^ ]+$")) buyCard(username, command);
            else if(command.matches("^shop show --all$")) showAll();
            else if(command.matches("^menu enter profile$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu enter duel$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu enter deck$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu enter shop$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu enter scoreboard$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu show-current$")) System.out.println("shop");
            else if(command.matches("^menu exit$")){
                MainMenu mainMenu = new MainMenu();
                mainMenu.run(username, scan);
            }
            else System.out.println("invalid command!");
        }
    }

    public static void buyCard(String username, String command){
        String pattern = "^shop buy ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String cardName = m.group(1);
        Card card = new Card();
        Player player = new Player();
        if(!card.isCardExist(cardName)){
            System.out.println("there is no card with this name");
        }
        else{
            if(card.getPrice(cardName) > player.getMoney(username)){
                System.out.println("not enough money");
            }
            else{
                //add to deck after purchase
            }
        }
    }

    public static void showAll(){
        Card card = new Card();
        card.showCards();
    }
}
