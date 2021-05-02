import Menus.Initialize;
import Menus.LoginMenu;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
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
        new Initialize().importMonsterCardDate();
        new LoginMenu().run();
    }
}
