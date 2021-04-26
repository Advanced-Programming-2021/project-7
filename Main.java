//package com.example.main;

import java.util.Scanner;

public class Main {
    public static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.run(scan);
    }
}
