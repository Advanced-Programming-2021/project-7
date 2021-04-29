package Menus;

import Model.Player;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenu {
    private Player loggedInUsername;
    private String password;


    public void run(Scanner scan) {
        while (true) {
            String command = scan.nextLine();
            if (command.matches("user create (?:(?:--username|--nickname|--password) ([^ ]+) ?)+"))
                createPlayer(command);
            else if (command.matches("^user login --username [^ ]+ --password [^ ]+$")) loginPlayer1(command, scan);
            else if (command.matches("^user login --password [^ ]+ --username [^ ]+$")) loginPlayer2(command, scan);
            else if (command.matches("^menu exit$")) System.exit(0);
            else if (command.matches("^menu enter (profile|duel|shop|scoreboard)$")) System.out.println("please login first");
            else if (command.matches("^menu enter sscoreboard$")) System.out.println("please login first");
            else if (command.matches("^menu show-current$")) System.out.println("Login");
            else System.out.println("invalid command");
        }
    }

    public void createPlayer(String command) {
        String username = takeNameOutOfCommand(command, "--username");
        String nickname = takeNameOutOfCommand(command, "--nickname");
        String password = takeNameOutOfCommand(command, "--password");
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
                Player newPlayer = new Player(username, password, nickname);
                System.out.println("user created successfully!");
            }
        }
    }

    public void loginPlayer1(String command, Scanner scan) {
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

    public void loginPlayer2(String command, Scanner scan) {
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

    public Matcher getMatcher(String command, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(command);
    }

    public String takeNameOutOfCommand(String command, String key) {
        Matcher matcher = getMatcher(command, key + " (\\S+)");
        if (matcher.find())
            return matcher.group(1);
        return null;
    }
}
