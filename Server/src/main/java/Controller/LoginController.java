package Controller;

import Model.Player;

import java.util.UUID;

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
        String token = UUID.randomUUID().toString();
        Player.setActivePlayer(player);
        Player.setPlayerInActivePlayers(token, player);
        return "Login was successful!#" + token;
    }
}