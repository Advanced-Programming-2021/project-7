import Menus.Initialize;
import Model.CommonTools;
import View.MainProgramView;

import java.io.IOException;

public class Main{
    public static void main(String[] args) throws IOException {
        new Initialize().run();
        CommonTools.initializeNetwork();
        MainProgramView.run();
    }
}
