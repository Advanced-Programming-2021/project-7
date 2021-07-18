package Controller;

import Model.Player;

public class RegisterController {

    public static synchronized String register(String username, String nickname, String password) {
        if (username.equals("")) {
            return "You have not entered your username!";
        }
        if (password.equals("")) {
            return "You have not entered your password!";
        }
        if (nickname.equals("")) {
            return "You have not entered your nickname!";
        }
        if (Player.getPlayerByUsername(username) != null) {
            return "A user exists with this username!";
        }
        Player player = new Player(username, password, nickname);
        return "User created successfully!";
    }
}