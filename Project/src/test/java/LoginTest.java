import Menus.Initialize;
import Menus.LoginMenu;
import Model.CommonTools;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


public class LoginTest {
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeAll
    private static void initCards() throws IOException {
        Initialize initialize = new Initialize();
        initialize.importMonsterCardDate();
        initialize.importSpellTrapCardData();
    }

    @BeforeEach
    private void init() {
        System.setOut(new PrintStream(outContent));
    }


    @Test
    public void myATest() throws IOException {
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


    @Test
    public void myBTest() throws IOException {
        Path pathInput = Path.of("DataBase//TestsInput//shopTest.txt");
        Path pathOutput = Path.of("DataBase//TestsOutput//shopTest.txt");
        String command = Files.readString(pathInput);
        String expected = Files.readString(pathOutput);
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream(command.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);
        CommonTools.scan = new Scanner(System.in);
        new LoginMenu().run();
        Assertions.assertEquals(expected.replace("\r", ""),
                outContent.toString().replace("\r", ""));
        System.setIn(sysInBackup);
    }

    @Test
    public void myCTest() throws IOException {
        Path pathInput = Path.of("DataBase//TestsInput//profileTest.txt");
        Path pathOutput = Path.of("DataBase//TestsOutput//profileTest.txt");
        String command = Files.readString(pathInput);
        String expected = Files.readString(pathOutput);
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream(command.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);
        CommonTools.scan = new Scanner(System.in);
        new LoginMenu().run();
        Assertions.assertEquals(expected.replace("\r", ""),
                outContent.toString().replace("\r", ""));
        System.setIn(sysInBackup);
    }

}
