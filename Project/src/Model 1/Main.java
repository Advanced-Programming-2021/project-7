import Menus.LoginMenu;
import java.util.Scanner;

public class Main {
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        new LoginMenu().run(scan);
    }
}
