package Model;

import Model.Cards.Card;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class ShopInfo {
    public static ArrayList<String> bannedCards = new ArrayList<>();
    public static HashMap<String, Integer> availableStock = new HashMap<>();

    public static void setAvailableStock(HashMap<String, Integer> availableStock) {
        ShopInfo.availableStock = availableStock;
    }

    public static void setBannedCards(ArrayList<String> bannedCards) {
        ShopInfo.bannedCards = bannedCards;
    }

    public static boolean cardBanned(String card) {
        for (String bannedCard : bannedCards) {
            if (card.equals(bannedCard))
                return true;
        }
        return false;
    }

    public static int getStock(String card){
        return availableStock.get(card);
    }

    public static void banCard(String cardName) {
        if (!bannedCards.contains(cardName))
            bannedCards.add(cardName);
        try {
            FileHandler.updateShop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unbanCard(String cardName) {
        bannedCards.removeIf(card -> card.equals(cardName));
        try {
            FileHandler.updateShop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isBanned(String cardName) {
        for (String bannedCard : bannedCards) {
            if (bannedCard.equals(cardName))
                return true;
        }
        return false;
    }

    public static String increase(String name){
        int num = availableStock.get(name);
        num++;
        availableStock.put(name, num);
        try {
            FileHandler.updateShop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Success";

    }

    public static String decrease(String name){
        int num = availableStock.get(name);
        if (num == 0)
            return "Stock can't go below zero";
        num--;
        availableStock.put(name, num);
        try {
            FileHandler.updateShop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Success";
    }
}
