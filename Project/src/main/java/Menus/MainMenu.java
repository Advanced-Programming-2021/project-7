package Menus;

import Model.CommonTools;
import Model.Deck;
import Model.Player;

class MainMenu {
    public void run(String username) {
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^menu enter scoreboard$"))
                Scoreboard.getInstance().run();
            else if (command.matches("^logout$") || command.matches("^menu exit$"))
                return;
            else if (command.matches("^menu enter profile$"))
                profile(username);
            else if (command.matches("^menu enter duel$"))
                duel(username);
            else if (command.matches("^menu enter deck$"))
                deck(username);
            else if (command.matches("^menu enter shop$"))
                shop(username);
            else System.out.println("invalid command");
        }
    }

    public static void profile(String username) {
        Profile profile = new Profile();
        profile.run(username);
    }

    public static void duel(String username) {

    }

    public static void deck(String username) {

    }

    public static void shop(String username) {
        Shop shop = new Shop();
        shop.run(username);
    }
}
