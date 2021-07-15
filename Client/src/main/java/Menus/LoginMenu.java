package Menus;

import Model.CommonTools;
import Model.FileHandler;
import Model.Player;
import View.MainProgramView;

import java.io.IOException;

public class LoginMenu {

    public void run() throws Exception {
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^user create (?:(?:--username|--nickname|--password) ([^ ]+) ?){3}$"))
                createPlayer(command);
            else if (command.matches("^user login (?:(--username|--password) (\\S+) ?){2}$"))
                loginPlayer(command);
            else if (command.matches("^menu exit$")) {
                System.out.println("GoodBye");
                return;
            } else if (command.matches("^menu enter (profile|duel|shop|scoreboard|deck)$"))
                System.out.println("please login first");
            else if (command.matches("^menu show-current$")) System.out.println("Login");
            else System.out.println("invalid command");
        }
    }

    public void createPlayer(String command) throws IOException {
        String username = CommonTools.takeNameOutOfCommand(command, "--username");
        String nickname = CommonTools.takeNameOutOfCommand(command, "--nickname");
        String password = CommonTools.takeNameOutOfCommand(command, "--password");
        if (username == null || password == null || nickname == null) {
            System.out.println("invalid command");
            return;
        }
        if (Player.getPlayerByUsername(username) != null) {
            System.out.printf("user with username %s already exists\n", username);
        } else {
            if (Player.getPlayerByNick(nickname) != null) {
                System.out.printf("user with nickname %s already exists\n", nickname);
            } else {
                new Player(username, password, nickname);
                FileHandler.updatePlayers();
                System.out.println("user created successfully!");
            }
        }
    }

    public void loginPlayer(String command) throws Exception {
        String username = CommonTools.takeNameOutOfCommand(command, "--username");
        String password = CommonTools.takeNameOutOfCommand(command, "--password");
        if (username == null || password == null) {
            System.out.println("invalid command");
            return;
        }
        if (Player.getPlayerByUsername(username) == null || !Player.isPasswordCorrect(username, password)) {
            System.out.println("Username and password didn't match!");
        } else {
            System.out.println("user logged in successfully!");
            MainMenu mainMenu = new MainMenu();
            mainMenu.run(username);
        }
    }
}
