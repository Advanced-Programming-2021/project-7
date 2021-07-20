package Controller;

import Model.Player;

public class MainMenuController {
    public static String logout(String token) {
        Player.removePlayerInActivePlayers(token);
        return "Logout was successful!";
    }
}
