package Menus;

import Model.CommonTools;
import Model.Player;

class PlayMenu {
    private Player loggedInUser;


    public void run(String username) {
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^duel new (?:(?:--second-player|--rounds) ([^ ]+) ?){2}$")) newDuel(username, command);
            else if (command.matches("^menu enter (profile|duel|deck|shop|scoreboard)$")) System.out.println("menu navigation is not possible");
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
        if(player2 == null || rounds == null){
            System.out.println("invalid command");
            return;
        }
        if (Player.getPlayerByUsername(player2) == null) {
            System.out.println("there is no player with this username");
        }
        if (Player.getActiveDeckByUsername(player1) == null) {
            System.out.printf("%s has no active deck", player1);
            return;
        } else if (Player.getActiveDeckByUsername(player2) == null) {
            System.out.printf("%s has no active deck", player2);
            return;
        }

        //TODO: check deck validity

        int round = Integer.parseInt(rounds);
        if(round != 1 && round != 3){
            System.out.println("number of rounds is not supported");
            return;
        }
        Player firstPlayer = Player.getPlayerByUsername(player1);
        Player secondPlayer = Player.getPlayerByUsername(player2);
        DuelProgramControler duelProgramControler = new DuelProgramControler();
        duelProgramControler.run(firstPlayer, secondPlayer, round);
    }		
}
