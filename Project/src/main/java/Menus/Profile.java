package Menus;

import Model.CommonTools;
import Model.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Profile {
    public void run(String username) {
        while (true) {
            String command = CommonTools.scan.nextLine();
            if (command.matches("^profile change --nickname [^ ]+$")) changeNickname(username, command);
            else if (command.matches("^^profile change ((?:--(current|new) (\\S+) ?)|--password ?){3}"))
                changePassword(username, command);
            else if (command.matches("^menu enter (profile|duel|deck|shop|scoreboard)$"))
                System.out.println("menu navigation is not possible");
            else if (command.matches("^menu show-current$")) System.out.println("profile");
            else if (command.matches("^menu exit$")) {
                return;
            } else System.out.println("invalid command!");
        }
    }

    public void changeNickname(String username, String command) {
        String pattern = "^profile change --nickname ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String newNickname = m.group(1);
        if (Player.getPlayerByNick(newNickname) != null) {
            System.out.printf("user with nickname %s already exists\n", newNickname);
        } else {
            Player.getPlayerByUsername(username).setNickName(newNickname);
            System.out.println("nickname changed successfully!");
        }
    }

    private void changePassword(String username, String command) {
        String oldPass = CommonTools.takeNameOutOfCommand(command, "--current");
        String newPass = CommonTools.takeNameOutOfCommand(command, "--new");
        if (oldPass == null || newPass == null) {
            System.out.println("invalid command");
        }
        if (!Player.isPasswordCorrect(username, oldPass)) {
            System.out.println("current password is invalid");
        } else {
            if (newPass.equals(oldPass)) {
                System.out.println("please enter a new password");
            } else {
                Player.getPlayerByUsername(username).setPassword(newPass);
                System.out.println("password changed successfully!");
            }
        }
    }
}
