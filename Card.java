//package com.example.main;

import java.util.ArrayList;
import java.util.Collections;

public class Card {
    private String name;
    private String description;
    private int price;
    public static ArrayList<Card> cards = new ArrayList<>();

    public Card(){

    }

    public Card(String name, String description, int price){
        this.name = name;
        this.description = description;
        this.price = price;
        cards.add(this);
    }

    public void showCards(){
        ArrayList<String> namesAndDescriptions = new ArrayList<>();
        for(int i = 0; i < cards.size(); i++){
            String line = cards.get(i).name;
            line = line + ":";
            line = line + cards.get(i).description;
            namesAndDescriptions.add(line);
        }
        Collections.sort(namesAndDescriptions);
        for(int i = 0; i < namesAndDescriptions.size(); i++){
            System.out.println(namesAndDescriptions.get(i));
        }
    }

    public boolean isCardExist(String cardName){
        for(int i = 0; i < cards.size(); i++){
            if(cards.get(i).name.equals(cardName)){
                return true;
            }
        }
        return false;
    }

    public int getPrice(String name){
        for(int i = 0; i < cards.size(); i++){
            if(cards.get(i).name.equals(name)){
                return cards.get(i).price;
            }
        }
        return 0;
    }
}
