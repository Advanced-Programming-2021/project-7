package Controller;

import Model.Player;

import java.util.HashMap;

public class DuelMenuController {
    public static HashMap<String, String> newRoundRequest = new HashMap<>();
    public static HashMap<String, String> newMatchRequest = new HashMap<>();
    public static HashMap<String, String> newRoundAccepted = new HashMap<>();
    public static HashMap<String, String> newMatchAccepted = new HashMap<>();

    public static String isDuelValid(String matchType, String player1, String player2) {
        if (Player.getPlayerByUsername(player2) == null) {
            return "there is no player with this username";
        }
        if (!Player.isPlayerOnline(player2)) {
            return player2 + " is not online right now";
        }
        if (Player.getActiveDeckByUsername(player1) == null) {
            return player1 + " has no active deck";
        }
        if (Player.getActiveDeckByUsername(player2) == null) {
            return player2 + " has no active deck";
        }
        if (!Player.getActiveDeckByUsername(player1).isDeckValid()) {
            return player1 + "'s deck is invalid";
        }
        if (!Player.getActiveDeckByUsername(player2).isDeckValid()) {
            return player2 + "'s deck is invalid";
        }
        if (matchType.equals("newRound")) {
            newRoundRequest.put(player1, player2);
        } else  {
            newMatchRequest.put(player1, player2);
        }
        return "everything ok";
    }
}
