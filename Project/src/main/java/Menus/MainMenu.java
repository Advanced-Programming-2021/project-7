package Menus;

import Model.CommonTools;

import java.io.IOException;

class MainMenu {
    public void run(String username) throws IOException  {
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^menu enter scoreboard$"))
                Scoreboard.getInstance().run();
            else if (command.matches("^user logout$") || command.matches("^menu exit$")) {
                System.out.println("user logged out successfully!");
                return;
            }
            else if (command.matches("^menu enter profile$"))
                profile(username);
            else if (command.matches("^menu enter duel$"))
                duel(username);
            else if (command.matches("^menu enter deck$"))
                deck(username);
            else if (command.matches("^menu enter shop$"))
                shop(username);
            else if (command.matches("^menu enter import-export$"))
                importExport();
            else System.out.println("invalid command");
        }
    }

    public static void profile(String username) throws IOException {
        Profile profile = new Profile();
        profile.run(username);
    }

    public static void duel(String username) {
        PlayMenu playMenu = new PlayMenu();
        playMenu.run(username);
    }

    public static void deck(String username) throws IOException {
        DeckMenu deckMenu = new DeckMenu();
        deckMenu.run(username);
    }

    public static void shop(String username) throws IOException {
        Shop shop = new Shop();
        shop.run(username);
    }

    public static void importExport() {
        ImportExportMenu importExportMenu = new ImportExportMenu();
        importExportMenu.run();
    }
}
