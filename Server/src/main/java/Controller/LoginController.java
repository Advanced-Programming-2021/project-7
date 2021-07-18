package Controller;

import Model.Player;

public class LoginController {
    public static synchronized String login(String username, String password) {
        if (username.equals("")) {
            return "You have not entered your username!";
        }
        if (password.equals("")) {
            return "You have not entered your password!";
        }
        Player player = Player.getPlayerByUsername(username);
        if (player == null) {
            return "No user exists with this username!";
        }
        if (!password.equals(player.getPassword())) {
            return "Password is wrong!";
        }
        Player.setActivePlayer(player);
        return "Login was successful!";
    }
}