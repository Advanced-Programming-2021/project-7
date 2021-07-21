package Controller;

import Menus.DuelProgramController;
import Model.Player;

import java.util.HashMap;
import java.util.Map;

public class DuelMenuController {
    public static HashMap<String, String> newRoundRequest = new HashMap<>();
    public static HashMap<String, String> newMatchRequest = new HashMap<>();
    public static HashMap<String, String> newRoundAccepted = new HashMap<>();
    public static HashMap<String, String> newRoundRejected = new HashMap<>();
    public static HashMap<String, String> newMatchAccepted = new HashMap<>();
    public static HashMap<String, String> newMatchRejected = new HashMap<>();

    public static synchronized String isDuelValid(String matchType, String player1, String player2) {
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

    public static synchronized String cancelRequest(String player) {
        newMatchRequest.remove(player);
        newRoundRequest.remove(player);
        return "Challenge canceled successfully";
    }

    public static synchronized String waitRequest(String player) {
        for (Map.Entry e : newMatchAccepted.entrySet()) {
            if (e.getValue().equals(player)) {
                newMatchAccepted.remove(e.getKey());
                return "your challenge accepted";
            }
        }
        for (Map.Entry e : newMatchRejected.entrySet()) {
            if (e.getValue().equals(player)) {
                newMatchRejected.remove(e.getKey());
                return "your challenge rejected";
            }
        }
        for (Map.Entry e : newRoundAccepted.entrySet()) {
            if (e.getValue().equals(player)) {
                newRoundAccepted.remove(e.getKey());
                return "your challenge accepted";
            }
        }
        for (Map.Entry e : newRoundRejected.entrySet()) {
            if (e.getValue().equals(player)) {
                newRoundRejected.remove(e.getKey());
                return "your challenge rejected";
            }
        }
        return "waiting";
    }

    public static synchronized String refreshRequest(String player) {
        StringBuilder string = new StringBuilder();
        for (Map.Entry e : newRoundRequest.entrySet()) {
            if (e.getValue().equals(player)) {
                String player1 = (String) e.getKey();
                string.append(player1).append(" has challenged you to a new ");
                string.append("Round.\nWould you like to accept");
                return string.toString();
            }
        }
        for (Map.Entry e : newMatchRequest.entrySet()) {
            if (e.getValue().equals(player)) {
                String player1 = (String) e.getKey();
                string.append(player1).append(" has challenged you to a new ");
                string.append("Match.\nWould you like to accept");
                return string.toString();
            }
        }
        return "nothing new";
    }

    public static synchronized String reject(String player) {
        for (Map.Entry e : newRoundRequest.entrySet()) {
            if (e.getValue().equals(player)) {
                String player1 = (String) e.getKey();
                newRoundRejected.put(player, player1);
                newRoundRequest.remove(player1);
                return "Challenge rejected";
            }
        }
        for (Map.Entry e : newMatchRequest.entrySet()) {
            if (e.getValue().equals(player)) {
                String player1 = (String) e.getKey();
                newMatchRejected.put(player, player1);
                newMatchRequest.remove(player1);
                return "Challenge rejected";
            }
        }
        return "fail";
    }

    public static synchronized String accept(String player) {
        for (Map.Entry e : newRoundRequest.entrySet()) {
            if (e.getValue().equals(player)) {
                String player1 = (String) e.getKey();
                newRoundAccepted.put(player, player1);
                ServerController.duelProgramController = new DuelProgramController();
                ServerController.duelProgramController.run(player, player1, 1);
                return "Challenge accepted";
            }
        }
        for (Map.Entry e : newMatchRequest.entrySet()) {
            if (e.getValue().equals(player)) {
                String player1 = (String) e.getKey();
                newMatchAccepted.put(player, player1);
                ServerController.duelProgramController = new DuelProgramController();
                ServerController.duelProgramController.run(player, player1, 3);
                return "Challenge accepted";
            }
        }
        return "fail";
    }
}
