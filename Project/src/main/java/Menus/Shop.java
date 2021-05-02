package Menus;

import Model.Card;
import Model.CommonTools;
import Model.Player;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Shop {
    public void run(String username) {
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^shop buy [^ ]+$")) buyCard(username, command);
            else if (command.matches("^shop show --all$")) showAll();
            else if (command.matches("^menu enter (profile|duel|deck|shop|scoreboard)$")) System.out.println("menu navigation is not possible");
            else if (command.matches("^menu show-current$")) System.out.println("shop");
            else if (command.matches("^menu exit$")) {
                return;
            } else System.out.println("invalid command!");
        }
    }

    public static void buyCard(String username, String command) {
        // TODO: 2021-04-30  
    }

    public static void showAll() {
        // TODO: 2021-04-30  
    }
}
