package Model;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonTools {

    public static Scanner scan = new Scanner(System.in);

    public static Matcher getMatcher(String command, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(command);
    }

    public static String takeNameOutOfCommand(String command, String key) {
        Matcher matcher = getMatcher(command, key + " (\\S+)");
        if (matcher.find())
            return matcher.group(1);
        return null;
    }

}
