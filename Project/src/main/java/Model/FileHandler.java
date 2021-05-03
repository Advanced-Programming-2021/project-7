package Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler {

    public static void updatePlayers() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String toWrite = gson.toJson(Player.getPlayers());

        Path path = Path.of("DataBase//Players//players.txt");
        Files.writeString(path, toWrite);
    }
}
