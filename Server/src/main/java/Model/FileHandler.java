package Model;


import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler {

    public static void updatePlayers() throws IOException {
        YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
        String toWrite = yaGson.toJson(Player.getPlayers());

        Path path = Path.of("DataBase//Players//players.txt");
        Files.writeString(path, toWrite);
    }

    public static void updateShop() throws IOException {
        YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
        String toWrite = yaGson.toJson(ShopInfo.bannedCards);

        Path path = Path.of("DataBase//Cards//shopBanned.txt");
        Files.writeString(path, toWrite);

        toWrite = yaGson.toJson(ShopInfo.availableStock);
        path = Path.of("DataBase//Cards//shop.txt");
        Files.writeString(path, toWrite);
    }

}
