package Controller;

import Model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardController {
    public static synchronized String scoreBoardPlayers() {
        Player.sortPlayers();
        ArrayList<Player> players = Player.getPlayers();
        HashMap<String, Player> activePlayers = Player.getActivePlayers();
        ArrayList<String> playerNicknames = new ArrayList<>();
        ArrayList<String> activePlayerNicknames = new ArrayList<>();
        ArrayList<Integer> playerScores = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (i > players.size() - 1) break;
            playerNicknames.add(players.get(i).getNickname());
            playerScores.add(players.get(i).getScore());
        }
        for (Map.Entry e : activePlayers.entrySet()) {
            Player player = (Player) e.getValue();
            activePlayerNicknames.add(player.getNickname());
        }
        System.out.println(playerNicknames.toString() + "#" + activePlayerNicknames.toString() + "#" + playerScores.toString());
        return playerNicknames.toString() + "#" + activePlayerNicknames.toString() + "#" + playerScores.toString();
    }
}
