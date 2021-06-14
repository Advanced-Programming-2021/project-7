package Menus;

import Model.CommonTools;
import Model.Player;

class PlayMenu {
    private Player loggedInUser;


    public void run(String username) {
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^duel --new (?:(?:--second-player|--rounds) ([^ ]+) ?){2}$"))
                newDuel(username, command);
            else if (command.matches("^menu enter (profile|duel|deck|shop|scoreboard)$"))
                System.out.println("menu navigation is not possible");
            else if (command.matches("^menu show-current$")) System.out.println("duel");
            else if (command.matches("^menu exit$")) {
                return;
            } else System.out.println("invalid command!");
        }
    }

    private void newDuel(String username, String command) {
        String player1 = username;
        String player2 = CommonTools.takeNameOutOfCommand(command, "--second-player");
        String rounds = CommonTools.takeNameOutOfCommand(command, "--rounds");
        if (player2 == null || rounds == null || !rounds.matches("\\d+")) {
            System.out.println("invalid command");
            return;
        }
        if (Player.getPlayerByUsername(player2) == null) {
            System.out.println("there is no player with this username");
            return;
        }
        if(!isDuelValid(player1, player2, rounds)) return;
        int round = Integer.parseInt(rounds);
        DuelProgramController duelProgramController = new DuelProgramController();
        duelProgramController.run(player1, player2, round);
    }

    private boolean isDuelValid(String player1, String player2, String rounds){
        if (Player.getPlayerByUsername(player2) == null) {
            System.out.println("there is no player with this username");
            return false;
        }
        if (Player.getActiveDeckByUsername(player1) == null) {
            System.out.printf("%s has no active deck\n", player1);
            return false;
        }
        if (Player.getActiveDeckByUsername(player2) == null) {
            System.out.printf("%s has no active deck\n", player2);
            return false;
        }
        if (!Player.getActiveDeckByUsername(player1).isDeckValid()) {
            System.out.printf("%s's deck is invalid\n", player1);
            return false;
        }
        if (!Player.getActiveDeckByUsername(player2).isDeckValid()) {
            System.out.printf("%s's deck is invalid\n", player2);
            return false;
        }
        int round = Integer.parseInt(rounds);
        if (round != 1 && round != 3) {
            System.out.println("number of rounds is not supported");
            return false;
        }
        return true;
    }
}
