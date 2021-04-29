package Menus;

import Model.Player;

import java.util.Scanner;

class MainMenu
{
    public void run(String username, Scanner scan){
        while(true){
            String command = scan.nextLine();
            if(command.matches("^menu enter scoreboard$")) scoreBoard(username, scan);
            else if(command.matches("^logout$") || command.matches("^menu exit$")) logout(scan);
            else if(command.matches("^menu enter profile$")) profile(username, scan);
            else if(command.matches("^menu enter duel$")) duel(username, scan);
            else if(command.matches("^menu enter deck$")) deck(username, scan);
            else if(command.matches("^menu enter shop$")) shop(username, scan);
            else System.out.println("invalid command");
        }
    }

    public static void scoreBoard(String username, Scanner scan){
        while(true){
            String command = scan.nextLine();
            if(command.matches("^scoreboard show$")){
                Player samplePlayer = new Player();
                samplePlayer.showScoreBoard();
            }
            else if(command.matches("^menu enter profile$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu enter duel$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu enter deck$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu enter shop$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu enter scoreboard$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu show-current$")) System.out.println("scoreboard");
            else if(command.matches("^menu exit$")){
                MainMenu mainMenu = new MainMenu();
                mainMenu.run(username, scan);
            }
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
