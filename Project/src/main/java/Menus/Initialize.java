package Menus;

import Model.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


public class Initialize {

    public void run() throws IOException {
        makeFolders();
        initializePlayers();
        // TODO: 2021-05-03  initializeCards();
        new LoginMenu().run();
    }

    public void makeFolders(){
        File theDir = new File("DataBase");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
        theDir = new File("DataBase//Players");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
        theDir = new File("DataBase//Cards");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }

    public void initializePlayers() throws IOException {
        try {
            File file = new File("DataBase//Players//players.txt");
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        Path path = Path.of("DataBase//Players//players.txt");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String playersData = Files.readString(path);
        Type arraylistOfPlayer = new TypeToken<ArrayList<Player>>(){}.getType();
        Player.setPlayers(gson.fromJson(playersData, arraylistOfPlayer));
    }
}
