//package com.example.main;

import java.util.ArrayList;
import java.util.Collections;

public class Player {
    private String username;
    private String password;
    private String nickName;
    private int score;
    private int money;
    public static ArrayList<Player> players = new ArrayList<>();

    public Player(){

    }

    public Player(String username, String password, String nickName){
        this.username = username;
        this.password = password;
        this.nickName = nickName;
        this.score = 0;
        this.money = 0;
        players.add(this);
    }

    public boolean isUsernameExist(String username){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).username.equals(username)){
                return true;
            }
        }
        return false;
    }

    public boolean isNickNameExist(String nickName){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).nickName.equals(nickName)){
                return true;
            }
        }
        return false;
    }

    public boolean isPasswordCorrect(String username, String password){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).username.equals(username)){
                if(players.get(i).password.equals(password)){
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public void showScoreBoard(){
        ArrayList<String> sorted = new ArrayList<>();
        for(int i = 0; i < players.size(); i++){
            String line = String.valueOf(players.get(i).score);
            line = line + " ";
            line = line + players.get(i).nickName;
            sorted.add(line);
        }
        Collections.sort(sorted);
        int previousScore = -1;
        int rank = 0;
        for(int i = 0; i < players.size(); i++){
            String[] token = sorted.get(i).split(" ");
            if(!(Integer.parseInt(token[0]) ==previousScore)){
                rank = rank + 1;
                previousScore = Integer.parseInt(token[0]);
            }
            System.out.printf("%d- %s: %s\n", rank, token[1], token[0]);
        }
    }

    public void setNickName(String username, String newNickname){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).username.equals(username)){
                players.get(i).nickName = nickName;
            }
        }
    }

    public void setPassword(String username, String newPassword){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).username.equals(username)){
                players.get(i).password = newPassword;
            }
        }
    }

    public int getMoney(String username){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).username.equals(username)){
                return players.get(i).money;
            }
        }
        return 0;
    }
}
