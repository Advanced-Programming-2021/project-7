package Menus;

import Model.CommonTools;

public class ImportExportMenu {
    public void run() {
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^import card$")) importCard();
            else if (command.matches("^export card$")) exportCard();
            else if (command.matches("^menu enter (profile|duel|deck|shop|scoreboard)$"))
                System.out.println("menu navigation is not possible");
            else if (command.matches("^menu show-current$")) System.out.println("import-export");
            else if (command.matches("^menu exit$")) {
                return;
            } else System.out.println("invalid command!");
        }
    }

    public void importCard() {

    }

    public void exportCard() {

    }
}
