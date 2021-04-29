package Menus;

import Model.CommonTools;
import Model.Player;

import java.util.Scanner;

class MainMenu
{
    public void run(String username){
        while(true){
            String command = CommonTools.scan.nextLine();
            if(command.matches("^menu enter scoreboard$")) Scoreboard.getInstance().run();
            else if(command.matches("^logout$") || command.matches("^menu exit$")) logout(scan);
            else if(command.matches("^menu enter profile$")) profile(username, scan);
            else if(command.matches("^menu enter duel$")) duel(username, scan);
            else if(command.matches("^menu enter deck$")) deck(username, scan);
            else if(command.matches("^menu enter shop$")) shop(username, scan);
            else System.out.println("invalid command");
        }
    }

    public static void logout(Scanner scan){
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.run(scan);
    }

    public static void profile(String username, Scanner scan){
        Profile profile = new Profile();
        profile.run(username, scan);
    }

    public static void duel(String username, Scanner scan){

    }

    public static void deck(String username, Scanner scan){

    }

    public static void shop(String username, Scanner scan){
        Shop shop = new Shop();
        shop.run(username, scan);
    }		
}
