import Controller.ServerController;
import Menus.Initialize;

import java.io.IOException;

public class Main{
    public static void main(String[] args) throws IOException {
        new Initialize().run();
        ServerController.getInstance().runServer();
    }
}
