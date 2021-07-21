package Controller;

import Menus.DuelProgramController;
import Menus.Shop;
import Model.CommonTools;
import Model.Player;
import Model.ShopInfo;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;

public class ServerController {
    private static ServerController instance;
    private static String chats = "";
    public static DuelProgramController duelProgramController;

    private ServerController() {}

    public static ServerController getInstance() {
        if (instance == null)
            return new ServerController();
        return instance;
    }

    public void runServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(7755);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                        process(inputStream, outputStream);
                        inputStream.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        while (true) {
            String command;
            try {
                command = inputStream.readUTF();
            } catch (Exception e) {
                break;
            }
            String output = processCommands(command);
            System.out.println(output);
            outputStream.writeUTF(output);
            outputStream.flush();
        }
    }

    private String processCommands(String command) {
        System.out.println(command);
        if (command.matches("^RegisterController#register.+")) {
            String[] inputs = command.split("#");
            if (inputs.length < 5) return RegisterController.register("", "", "");
            else return RegisterController.register(inputs[ 2 ], inputs[ 3 ], inputs[ 4 ]);
        } else if (command.matches("^RegisterProfileController#selcet#.+")) {
            String[] inputs = command.split("#");
            return RegisterProfileController.select(Integer.parseInt(inputs[ 2 ]), inputs[ 3 ]);
        } else if (command.matches("^LoginController#login#.+")) {
            String[] inputs = command.split("#");
            if (inputs.length < 4) return LoginController.login("", "");
            else return LoginController.login(inputs[2], inputs[3]);
        } else if (command.matches("^MainMenuController#logout#.+")) {
            String[] inputs = command.split("#");
            return MainMenuController.logout(inputs[2]);
        } else if (command.matches("^ScoreboardController#players#$")) {
            return ScoreboardController.scoreBoardPlayers();
        }  else if (command.matches("^Lobby#[a-zA-Z]+#.+")) {
            String[] inputs = command.split("#");
            return DuelMenuController.isDuelValid(inputs[1], inputs[2], inputs[3]);
        } else if (command.matches("^WaitMenu#cancel#.+")) {
            String[] inputs = command.split("#");
            return DuelMenuController.cancelRequest(inputs[2]);
        } else if (command.matches("^WaitMenu#wait#.+")) {
            String[] inputs = command.split("#");
            return DuelMenuController.waitRequest(inputs[ 2 ]);
        } else if (command.matches("^WaitMenu#refresh#.+")) {
            String[] inputs = command.split("#");
            return DuelMenuController.refreshRequest(inputs[ 2 ]);
        } else if (command.matches("^WaitMenu#reject#.+")) {
            String[] inputs = command.split("#");
            return DuelMenuController.reject(inputs[ 2 ]);
        } else if (command.matches("^WaitMenu#accept#.+")) {
            String[] inputs = command.split("#");
            return DuelMenuController.accept(inputs[ 2 ]);
        } else if (command.startsWith("Player"))
            return playerCommandProcess(command);
        else if (command.startsWith("shop"))
            return shopCommandProcess(command);
        else if (command.startsWith("chat"))
            return chatCommandProcess(command);
        else if (command.startsWith("get chats"))
            return getChats(command);
        else if (command.startsWith("duel"))
            return duelProcess(command);
        else if (command.equals("refresh"))
            return refreshDuel(command);
        else if (command.equals("number of online"))
            return numberOfOnline();
        else if (command.startsWith("deleteChat"))
            return deleteChat(command);
        else if (command.startsWith("editChat"))
            return editChat(command);
        else if (command.startsWith("pinChat"))
            return pinChat(command);
        else if (command.startsWith("profileChat"))
            return profileChat(command);
        return "";
    }

    private String refreshDuel(String command) {
        YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
        String toWrite = yaGson.toJson(duelProgramController.getGameDecks());
        System.out.println(duelProgramController);
        System.out.println(duelProgramController.getGameDecks());
        return toWrite;
    }

    private String duelProcess(String command) {
        Matcher matcher;
        if ((matcher = CommonTools.getMatcher(command, "duel attack (\\d+)")).matches())
            return attack(matcher);
        else if ((CommonTools.getMatcher(command, "duel attack direct")).matches())
            return duelProgramController.directAttack();
        else if ((CommonTools.getMatcher(command, "duel activate effect")).matches())
            return duelProgramController.activateSpellErrorCheck();
        else if ((CommonTools.getMatcher(command, "duel summon")).matches())
            return duelProgramController.summonMonster();
        else if ((CommonTools.getMatcher(command, "duel set")).matches())
            return duelProgramController.set();
        else if ((CommonTools.getMatcher(command, "duel summonOrSet")).matches())
            duelProgramController.checkForSetOrSummon();
        else if ((CommonTools.getMatcher(command, "duel flip")).matches())
            return duelProgramController.flipSummon();
        else if ((CommonTools.getMatcher(command, "duel deselect")).matches())
            duelProgramController.deselect();
        else if ((CommonTools.getMatcher(command, "duel draw card")).matches())
            duelProgramController.drawCard();
        else if ((CommonTools.getMatcher(command, "duel activateTrap")).matches())
            duelProgramController.activateTrap();
        else if ((matcher = CommonTools.getMatcher(command, "duel selectMonster (\\d+)")).matches())
            return selectMonster(matcher);
        else if ((matcher = CommonTools.getMatcher(command, "duel selectSpell (\\d+)")).matches())
            return selectSpell(matcher);
        else if ((matcher = CommonTools.getMatcher(command, "duel selectHand (\\d+)")).matches())
            return selectHand(matcher);
        else if ((matcher = CommonTools.getMatcher(command, "duel set --position (attack|defense)")).matches())
            return setPosition(matcher);
        else if ((CommonTools.getMatcher(command, "duel isRoundOver")).matches())
            return String.valueOf(duelProgramController.isRoundOver());
        else if ((CommonTools.getMatcher(command, "duel isGame")).matches())
            return String.valueOf(duelProgramController.isGameOver());
        else if ((matcher = CommonTools.getMatcher(command, "duel roundOver (\\d+)")).matches())
            return roundOver(matcher);
        else if ((matcher = CommonTools.getMatcher(command, "duel gameOver (\\d+)")).matches())
            return gameOver(matcher);
        else if ((CommonTools.getMatcher(command, "duel changePhase")).matches())
            duelProgramController.changePhase();
        return "";
    }

    private String gameOver(Matcher matcher) {
        int turn = Integer.parseInt(matcher.group(1));
        duelProgramController.gameOver(turn);
        return "";
    }

    private String roundOver(Matcher matcher) {
        int turn = Integer.parseInt(matcher.group(1));
        duelProgramController.roundOver(turn);
        return "";
    }

    private String setPosition(Matcher matcher) {
        String command = "set --position " + matcher.group(1);
        return duelProgramController.setPositionMonster(command);
    }

    private String selectHand(Matcher matcher) {
        int index = Integer.parseInt(matcher.group(1));
        return duelProgramController.selectHand(index);
    }

    private String selectSpell(Matcher matcher) {
        int index = Integer.parseInt(matcher.group(1));
        return duelProgramController.selectSpell(index);
    }

    private String selectMonster(Matcher matcher) {
        int index = Integer.parseInt(matcher.group(1));
        return duelProgramController.selectMonster(index);
    }

    private String attack(Matcher matcher) {
        String num = matcher.group(1);
        return duelProgramController.attackCard("attack " + num);
    }

    private String shopCommandProcess(String command) {
        Matcher matcher;
        if ((matcher = CommonTools.getMatcher(command, "shop (\\S+) buy (.+)")).matches())
            return buyCard(matcher);
        else if ((matcher = CommonTools.getMatcher(command, "shop ban (.+)")).matches())
            return banCard(matcher);
        else if ((matcher = CommonTools.getMatcher(command, "shop unban (.+)")).matches())
            return unbanCard(matcher);
        else if ((matcher = CommonTools.getMatcher(command, "shop stock (.+)")).matches())
            return getStock(matcher);
        else if ((matcher = CommonTools.getMatcher(command, "shop isBanned (.+)")).matches())
            return isBanned(matcher);
        else if ((matcher = CommonTools.getMatcher(command, "shop increase (.+)")).matches())
            return increase(matcher);
        else if ((matcher = CommonTools.getMatcher(command, "shop decrease (.+)")).matches())
            return decrease(matcher);
        else return "";
    }

    private String decrease(Matcher matcher) {
        String cardName = matcher.group(1);
        return ShopInfo.decrease(cardName);
    }

    private String increase(Matcher matcher) {
        String cardName = matcher.group(1);
        return ShopInfo.increase(cardName);
    }

    private String isBanned(Matcher matcher) {
        String cardName = matcher.group(1);
        return String.valueOf(ShopInfo.isBanned(cardName));
    }

    private String getStock(Matcher matcher) {
        String cardName = matcher.group(1);
        return String.valueOf(ShopInfo.getStock(cardName));
    }

    private String unbanCard(Matcher matcher) {
        String cardName = matcher.group(1);
        ShopInfo.unbanCard(cardName);
        return "Success";
    }

    private String banCard(Matcher matcher) {
        String cardName = matcher.group(1);
        ShopInfo.banCard(cardName);
        return "Success";
    }

    private String buyCard(Matcher matcher) {
        String player = matcher.group(1);
        String cardName = matcher.group(2);
        String command = "shop buy " + cardName;
        try {
            return Shop.buyCard(player, command);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String playerCommandProcess(String command) {
        Matcher matcher;
        if ((matcher = CommonTools.getMatcher(command, "Player (\\S+) Money")).matches())
            return sendMoney(matcher);
        else if ((matcher = CommonTools.getMatcher(command, "Player (\\S+) Card (.+)")).matches())
            return sendCardNum(matcher);
        else return "";
    }

    private String chatCommandProcess(String command){
        chats = chats + "\n" + command.substring(5);
        System.out.println(command.substring(5));
        return "message added successfully";
    }

    private String getChats(String command){
        return chats;
    }

    private String sendCardNum(Matcher matcher) {
        String player = matcher.group(1);
        String cardName = matcher.group(2);
        if (Player.getPlayerByUsername(player) != null)
            return String.valueOf(Player.getPlayerByUsername(player).getNumberOfCards(cardName));
        else return "";
    }

    private String sendMoney(Matcher matcher) {
        String player = matcher.group(1);
        if (Player.getPlayerByUsername(player) != null)
            return String.valueOf(Player.getPlayerByUsername(player).getMoney());
        else return "";
    }

    private String numberOfOnline(){
        return "";
    }

    private String deleteChat(String command){
        return "";
    }

    private String editChat(String command){
        return "";
    }

    private String pinChat(String command){
        return "";
    }

    private String profileChat(String command){
        return "";
    }
}
