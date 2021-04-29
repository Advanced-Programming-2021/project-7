package Menus;

import Model.Player;

import java.util.ArrayList;
import java.util.Collections;

public class Scoreboard {
    public static Scoreboard instance;

    private Scoreboard() {

    }

    public static Scoreboard getInstance() {
        if (instance == null)
            instance = new Scoreboard();
        return instance;
    }

    public void run() {
        String command = null;
        while (true) {
            // TODO: 2021-04-25
            if (command.equals("scoreboard show"))
                showBoard();
             else if (command.equals("menu exit"))
                return;
            else if (command.matches("^menu enter (profile|duel|shop|scoreboard|deckmenu)$"))
                System.out.println("menu navigation is not possible");
             else
                System.out.println("invalid command");

        }

    }

    public void showBoard() {
        ArrayList<Player> players = Player.getPlayers();
        Collections.sort(players);
        for (int i = 0; i < players.size(); i++) {
            System.out.println(i + 1 + "- " + players.get(i).toString());
        }
    }
}
