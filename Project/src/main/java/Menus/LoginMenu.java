package Menus;

import Model.CommonTools;
import Model.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenu {

    public void run() {
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("user create (?:(?:--username|--nickname|--password) ([^ ]+) ?){3}"))
                createPlayer(command);
            else if (command.matches("^user login --username [^ ]+ --password [^ ]+$")) loginPlayer1(command);
            else if (command.matches("^user login --password [^ ]+ --username [^ ]+$")) loginPlayer2(command);
            else if (command.matches("^menu exit$")) System.exit(0);
            else if (command.matches("^menu enter (profile|duel|shop|scoreboard|deck)$"))
                System.out.println("please login first");
            else if (command.matches("^menu show-current$")) System.out.println("Login");
            else System.out.println("invalid command");
        }
    }

    public void createPlayer(String command) {
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
                // TODO: 2021-04-27
                new Player(username, password, nickname);
                System.out.println("user created successfully!");
            }
        }
    }

    public void loginPlayer1(String command) {
        String pattern = "^user login --username ([^ ]+) --password ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String username = m.group(1);
        String password = m.group(2);
        if (Player.getPlayerByUsername(username) == null || !Player.isPasswordCorrect(username, password)) {
            System.out.println("Username and password  didn¡¯t match!");
        } else {
            System.out.println("user logged in successfully!");
            MainMenu mainMenu = new MainMenu();
            //mainMenu.run(username, scan);
        }
    }

    public void loginPlayer2(String command) {
        String pattern = "^user login --password ([^ ]+) --username ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String username = m.group(2);
        String password = m.group(1);
        if (Player.getPlayerByUsername(username) == null || !Player.isPasswordCorrect(username, password)) {
            System.out.println("Username and password  didn¡¯t match!");
        } else {
            System.out.println("user logged in successfully!");
            MainMenu mainMenu = new MainMenu();
            //mainMenu.run(username, scan);
        }
    }


}
