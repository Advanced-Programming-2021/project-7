package Controller;


import Model.FileHandler;
import Model.Player;

import java.io.IOException;

public class RegisterProfileController {
    public static synchronized String select(int counter, String createdPlayerName) {
        Player createdPlayer = Player.getPlayerByUsername(createdPlayerName);
        createdPlayer.setProfile(counter);
        try {
            FileHandler.updatePlayers();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "User created successfully!";
    }
}
