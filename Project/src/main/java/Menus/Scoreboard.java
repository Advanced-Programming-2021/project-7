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
        while (true) {
            String command = CommonTools.scan.nextLine();
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
        int lastScore = -1;
        int position = 0;
        for (int i = 0; i < players.size(); i++) {
            int score = players.get(i).getScore();
            if (score != lastScore) {
                position = position + 1;
            }
            System.out.println(position + "- " + players.get(i).toString());
            lastScore = score;
        }
    }
}
