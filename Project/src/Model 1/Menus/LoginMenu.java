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
            if (command.matches("^user create --username [^ ]+ --nickname [^ ]+ --password [^ ]+$"))
                createPlayer1(command);
            else if (command.matches("^user create --username [^ ]+ --password [^ ]+ --nickname [^ ]+$"))
                createPlayer2(command);
            else if (command.matches("^user create --nickname [^ ]+ --username [^ ]+ --password [^ ]+$"))
                createPlayer3(command);
            else if (command.matches("^user create --password [^ ]+ --username [^ ]+ --nickname [^ ]+$"))
                createPlayer4(command);
            else if (command.matches("^user create --nickname [^ ]+ --password [^ ]+ --username [^ ]+$"))
                createPlayer5(command);
            else if (command.matches("^user create --password [^ ]+ --nickname [^ ]+ --username [^ ]+$"))
                createPlayer6(command);
            else if (command.matches("^user login --username [^ ]+ --password [^ ]+$")) loginPlayer1(command, scan);
            else if (command.matches("^user login --password [^ ]+ --username [^ ]+$")) loginPlayer2(command, scan);
            else if (command.matches("^menu exit$")) System.exit(0);
            else if (command.matches("^menu enter profile$")) System.out.println("please login first");
            else if (command.matches("^menu enter duel$")) System.out.println("please login first");
            else if (command.matches("^menu enter deck$")) System.out.println("please login first");
            else if (command.matches("^menu enter shop$")) System.out.println("please login first");
            else if (command.matches("^menu enter sscoreboard$")) System.out.println("please login first");
            else if (command.matches("^menu show-current$")) System.out.println("Login");
            else System.out.println("invalid command");
        }
    }

    public static void createPlayer1(String command) {
        String pattern = "^user create --username ([^ ]+) --nickname ([^ ]+) --password ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String username = m.group(1);
        String nickName = m.group(2);
        String password = m.group(3);
        if (Player.getPlayerByUsername(username) != null) {
            System.out.printf("user with username %s already exists\n", username);
        } else {
            if (Player.getPlayerByNick(nickName) != null) {
                System.out.printf("user with nickname %s already exists\n", nickName);
            } else {
                Player newPlayer = new Player(username, password, nickName);
                System.out.println("user created successfully!");
            }
        }
    }

    public static void createPlayer2(String command) {
        String pattern = "^user create --username ([^ ]+) --password ([^ ]+) --nickname ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String username = m.group(1);
        String nickName = m.group(3);
        String password = m.group(2);
        if (Player.getPlayerByUsername(username) != null) {
            System.out.printf("user with username %s already exists\n", username);
        } else {
            if (Player.getPlayerByNick(nickName) != null) {
                System.out.printf("user with nickname %s already exists\n", nickName);
            } else {
                Player newPlayer = new Player(username, password, nickName);
                System.out.println("user created successfully!");
            }
        }
    }

    public static void createPlayer3(String command) {
        String pattern = "^user create --nickname ([^ ]+) --username ([^ ]+) --password ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String username = m.group(2);
        String nickName = m.group(1);
        String password = m.group(3);
        if (Player.getPlayerByUsername(username) != null) {
            System.out.printf("user with username %s already exists\n", username);
        } else {
            if (Player.getPlayerByNick(nickName) != null) {
                System.out.printf("user with nickname %s already exists\n", nickName);
            } else {
                Player newPlayer = new Player(username, password, nickName);
                System.out.println("user created successfully!");
            }
        }
    }

    public static void createPlayer4(String command) {
        String pattern = "^user create --password ([^ ]+) --username ([^ ]+) --nickname ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String username = m.group(2);
        String nickName = m.group(3);
        String password = m.group(1);
        if (Player.getPlayerByUsername(username) != null) {
            System.out.printf("user with username %s already exists\n", username);
        } else {
            if (Player.getPlayerByNick(nickName) != null) {
                System.out.printf("user with nickname %s already exists\n", nickName);
            } else {
                Player newPlayer = new Player(username, password, nickName);
                System.out.println("user created successfully!");
            }
        }
    }

    public static void createPlayer5(String command) {
        String pattern = "^user create --nickname ([^ ]+) --password ([^ ]+) --username ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String username = m.group(3);
        String nickName = m.group(1);
        String password = m.group(2);
        if (Player.getPlayerByUsername(username) != null) {
            System.out.printf("user with username %s already exists\n", username);
        } else {
            if (Player.getPlayerByNick(nickName) != null) {
                System.out.printf("user with nickname %s already exists\n", nickName);
            } else {
                Player newPlayer = new Player(username, password, nickName);
                System.out.println("user created successfully!");
            }
        }
    }

    public static void createPlayer6(String command) {
        String pattern = "^user create --password ([^ ]+) --nickname ([^ ]+) --username ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String username = m.group(3);
        String nickName = m.group(2);
        String password = m.group(1);
        if (Player.getPlayerByUsername(username) != null) {
            System.out.printf("user with username %s already exists\n", username);
        } else {
            if (Player.getPlayerByNick(nickName) != null) {
                System.out.printf("user with nickname %s already exists\n", nickName);
            } else {
                Player newPlayer = new Player(username, password, nickName);
                System.out.println("user created successfully!");
            }
        }
    }

    public static void loginPlayer1(String command, Scanner scan) {
        String pattern = "^user login --username ([^ ]+) --password ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String username = m.group(1);
        String password = m.group(2);
        if (Player.getPlayerByUsername(username) == null || !Player.isPasswordCorrect(username, password)) {
            System.out.println("Username and password  didn’t match!");
        } else {
            System.out.println("user logged in successfully!");
            MainMenu mainMenu = new MainMenu();
            mainMenu.run(username, scan);
        }
    }

    public static void loginPlayer2(String command, Scanner scan) {
        String pattern = "^user login --password ([^ ]+) --username ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String username = m.group(2);
        String password = m.group(1);
        if (Player.getPlayerByUsername(username) == null || !Player.isPasswordCorrect(username, password)) {
            System.out.println("Username and password  didn’t match!");
        } else {
            System.out.println("user logged in successfully!");
            MainMenu mainMenu = new MainMenu();
            mainMenu.run(username, scan);
        }
    }
}
