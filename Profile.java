//package com.example.main;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Profile {

    public void run(String username, Scanner scan){
        while(true){
            String command = scan.nextLine();
            if(command.matches("^profile change --nickname [^ ]+$")) changeNickname(username, command);
            else if(command.matches("^profile change --password --current [^ ]+ --new [^ ]+$")) changePassword1(username, command);
            else if(command.matches("^profile change --password --new [^ ]+ --current [^ ]+$")) changePassword2(username, command);
            else if(command.matches("^profile change --currnet [^ ]+ --password --new [^ ]+$")) changePassword3(username, command);
            else if(command.matches("^profile change --new [^ ]+ --password --current [^ ]+$")) changePassword4(username, command);
            else if(command.matches("^profile change --current [^ ]+ --new [^ ]+ --password$")) changePassword5(username, command);
            else if(command.matches("^profile change --new [^ ]+ --current [^ ]+ --password$")) changePassword6(username, command);
            else if(command.matches("^menu enter profile$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu enter duel$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu enter deck$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu enter shop$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu enter scoreboard$")) System.out.println("menu navigation is not possible");
            else if(command.matches("^menu show-current$")) System.out.println("profile");
            else if(command.matches("^menu exit$")){
                MainMenu mainMenu = new MainMenu();
                mainMenu.run(username, scan);
            }
            else System.out.println("invalid command!");
        }
    }

    public static void changeNickname(String username, String command){
        Player samplePlayer = new Player();
        String pattern = "^profile change --nickname ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String newNickname = m.group(1);
        if(samplePlayer.isNickNameExist(newNickname)){
            System.out.printf("user with nickname %s already exists\n", newNickname);
        }
        else{
            samplePlayer.setNickName(username, command);
            System.out.println("nickname changed successfully!");
        }
    }

    public static void changePassword1(String username, String command){
        Player samplePlayer = new Player();
        String pattern = "^profile change --password --current ([^ ]+) --new ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String currentPassword = m.group(1);
        String newPassword = m.group(2);
        if(!samplePlayer.isPasswordCorrect(username, currentPassword)){
            System.out.println("current password is invalid");
        }
        else{
            if(newPassword.equals(currentPassword)){
                System.out.println("please enter a new password");
            }
            else{
                samplePlayer.setPassword(username, newPassword);
                System.out.println("password changed successfully!");
            }
        }
    }

    public static void changePassword2(String username, String command){
        Player samplePlayer = new Player();
        String pattern = "^profile change --password --new ([^ ]+) --current ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String currentPassword = m.group(2);
        String newPassword = m.group(1);
        if(!samplePlayer.isPasswordCorrect(username, currentPassword)){
            System.out.println("current password is invalid");
        }
        else{
            if(newPassword.equals(currentPassword)){
                System.out.println("please enter a new password");
            }
            else{
                samplePlayer.setPassword(username, newPassword);
                System.out.println("password changed successfully!");
            }
        }
    }

    public static void changePassword3(String username, String command){
        Player samplePlayer = new Player();
        String pattern = "^profile change --currnet ([^ ]+) --password --new ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String currentPassword = m.group(1);
        String newPassword = m.group(2);
        if(!samplePlayer.isPasswordCorrect(username, currentPassword)){
            System.out.println("current password is invalid");
        }
        else{
            if(newPassword.equals(currentPassword)){
                System.out.println("please enter a new password");
            }
            else{
                samplePlayer.setPassword(username, newPassword);
                System.out.println("password changed successfully!");
            }
        }
    }

    public static void changePassword4(String username, String command){
        Player samplePlayer = new Player();
        String pattern = "^profile change --new ([^ ]+) --password --current ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String currentPassword = m.group(2);
        String newPassword = m.group(1);
        if(!samplePlayer.isPasswordCorrect(username, currentPassword)){
            System.out.println("current password is invalid");
        }
        else{
            if(newPassword.equals(currentPassword)){
                System.out.println("please enter a new password");
            }
            else{
                samplePlayer.setPassword(username, newPassword);
                System.out.println("password changed successfully!");
            }
        }
    }

    public static void changePassword5(String username, String command){
        Player samplePlayer = new Player();
        String pattern = "^profile change --current ([^ ]+) --new ([^ ]+) --password$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String currentPassword = m.group(1);
        String newPassword = m.group(2);
        if(!samplePlayer.isPasswordCorrect(username, currentPassword)){
            System.out.println("current password is invalid");
        }
        else{
            if(newPassword.equals(currentPassword)){
                System.out.println("please enter a new password");
            }
            else{
                samplePlayer.setPassword(username, newPassword);
                System.out.println("password changed successfully!");
            }
        }
    }

    public static void changePassword6(String username, String command){
        Player samplePlayer = new Player();
        String pattern = "^profile change --new ([^ ]+) --current ([^ ]+) --password$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String currentPassword = m.group(2);
        String newPassword = m.group(1);
        if(!samplePlayer.isPasswordCorrect(username, currentPassword)){
            System.out.println("current password is invalid");
        }
        else{
            if(newPassword.equals(currentPassword)){
                System.out.println("please enter a new password");
            }
            else{
                samplePlayer.setPassword(username, newPassword);
                System.out.println("password changed successfully!");
            }
        }
    }
}
