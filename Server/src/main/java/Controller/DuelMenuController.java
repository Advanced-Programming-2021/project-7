package Controller;

import Model.Player;

public class DuelMenuController {
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
        return "everything ok";
    }
}
