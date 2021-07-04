import Menus.Initialize;
import Menus.LoginMenu;

import java.io.IOException;

public class Main{
    public static void main(String[] args) throws IOException {
        new Initialize().run();
        new LoginMenu().run();
    }
}
