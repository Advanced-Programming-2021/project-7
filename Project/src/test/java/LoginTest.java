import Menus.LoginMenu;
import Model.CommonTools;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


public class LoginTest {
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private LoginMenu loginMenu = new LoginMenu();


    @BeforeEach
    private void init() throws IOException {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void allOfLoginMenuTest() throws IOException {
        Path pathInput = Path.of("DataBase//TestsInput//allOfLoginTest.txt");
        Path pathOutput = Path.of("DataBase//TestsOutput//allOfLoginTest.txt");
        String command = Files.readString(pathInput);
        String expected = Files.readString(pathOutput);
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream(command.getBytes());
        System.setIn(in);
        CommonTools.scan = new Scanner(System.in);
        new LoginMenu().run();
        Assertions.assertEquals(expected.replace("\r", ""),
                outContent.toString().replace("\r", ""));
        System.setIn(sysInBackup);

    }

}
