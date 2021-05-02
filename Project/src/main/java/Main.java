import Menus.LoginMenu;

import java.io.File;

public class Main {
    public static void main(String[] args) {
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
        new LoginMenu().run();
    }
}
