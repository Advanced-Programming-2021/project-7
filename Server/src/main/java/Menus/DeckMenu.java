package Menus;

import Model.*;
import Model.Cards.Card;
import View.CardView;
import View.MainProgramView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckMenu extends Application {
    private static String logged;
    public CardView card;
    public static Stage stage;
    private Stage mainStage;
    private Scene mainScene;
    private AnchorPane mainRoot;

    public void run(String username) throws Exception {
        logged = username;
        this.stage = MainProgramView.stage;
        start(stage);
        System.out.println("Welcome to deck menu");
//        while (true) {
//            String command = CommonTools.scan.nextLine();
//            if (command.matches("^deck create [^ ]+$")) createDeck(username, command);
//            else if (command.matches("^deck delete [^ ]+$")) deleteDeck(username, command);
//            else if (command.matches("^deck set-activate [^ ]+$")) setActiveDeck(username, command);
//            else if (command.matches("^deck add-card (?:(?:--card|--deck|--side)( (.+))* ?){2,3}$"))
//                addCardToDeck(username, command);
//            else if (command.matches("^deck rm-card (?:(?:--card|--deck|--side)( (.+))* ?){2,3}$"))
//                removeCardFromDeck(username, command);
//            else if (command.matches("^deck show --all")) showAllDecks(username);
//            else if (command.matches("^deck show (?:(?:--deck-name|--side)( ([^ ]+))* ?){1,2}$"))
//                showDeck(username, command);
//            else if (command.matches("^deck show --cards$")) showCards(username);
//            else if (command.matches("^menu enter (profile|duel|deck|shop|scoreboard)$"))
//                System.out.println("menu navigation is not possible");
//            else if (command.matches("^menu show-current$")) System.out.println("deck");
//            else if (command.matches("^menu exit$")) {
//                System.out.println("MainMenu");
//                return;
//            } else System.out.println("invalid command!");
//        }
    }

    private void createDeck(String username, String command) throws IOException {
        String pattern = "^deck create ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String deckName = m.group(1);
        if (Player.getPlayerByUsername(username).getDeckByName(deckName) != null) {
            System.out.printf("deck with name %s already exists\n", deckName);
            return;
        }
        System.out.println("deck created successfully!");
        new Deck(deckName, username);
        FileHandler.updatePlayers();
    }

    private void deleteDeck(String username, String command) throws IOException {
        String pattern = "^deck delete ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String deckName = m.group(1);
        if (Player.getPlayerByUsername(username).getDeckByName(deckName) == null) {
            System.out.printf("deck with name %s does not exist\n", deckName);
            return;
        }
        System.out.println("deck delete successfully!");
        Player.getPlayerByUsername(username).removeDeck(deckName);
        if (Player.getActiveDeckByUsername(username) != null) {
            if (Player.getActiveDeckByUsername(username).getDeckName().equals(deckName)) {
                Player.getPlayerByUsername(username).removeActiveDeck();
            }
        }
        FileHandler.updatePlayers();
    }

    public void setActiveDeck(String username, String command) throws IOException {
        String pattern = "^deck set-activate ([^ ]+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String deckName = m.group(1);
        if (Player.getPlayerByUsername(username).getDeckByName(deckName) == null) {
            System.out.printf("deck with name %s does not exist\n", deckName);
            return;
        }
        System.out.println("deck activated successfully");
        Player.getPlayerByUsername(username).setActiveDeck(deckName);
        FileHandler.updatePlayers();
    }

    private void addCardToDeck(String username, String command) throws IOException {
        String pattern = "^deck add-card --card ([^-]+-?[^-]*) --deck ([^-]+)( --side)?$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String cardName = m.group(1);
        String deckName = m.group(2);
        String side = CommonTools.takeNameOutOfCommand(command, "--side");
        if (cardName == null || deckName == null || side != null) {
            System.out.println("invalid command");
            return;
        }
        if (!addCardValidity(cardName, deckName, username)) return;
        Player player = Player.getPlayerByUsername(username);
        Deck deck = player.getDeckByName(deckName);
        if (!command.contains("--side")) {
            if (deck.isMainDeckFull()) {
                System.out.println("main deck is full");
                return;
            }
            if (deck.isThereThreeCards(Card.getCardByName(cardName))) {
                System.out.printf("there are already three cards with name %s in deck %s\n", cardName, deckName);
                return;
            }
            deck.addCardToMainDeck(Card.getCardByName(cardName));
        } else {
            if (deck.isSideDeckFull()) {
                System.out.println("side deck is full");
                return;
            }
            if (deck.isThereThreeCards(Card.getCardByName(cardName))) {
                System.out.printf("there are already three cards with name %s in deck %s\n", cardName, deckName);
                return;
            }
            deck.addCardToSideDeck(Card.getCardByName(cardName));
        }
        player.removeCard(Card.getCardByName(cardName));
        System.out.println("card added to deck successfully");
        FileHandler.updatePlayers();
    }

    private void removeCardFromDeck(String username, String command) throws IOException {
        String pattern = "^deck rm-card --card ([^-]+-?[^-]*) --deck ([^-]+)( --side)?$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(command);
        m.find();
        String cardName = m.group(1);
        String deckName = m.group(2);
        if (cardName == null || deckName == null) {
            System.out.println("invalid command");
            return;
        }
        boolean side = command.contains("--side");
        if (!removeCardValidity(cardName, deckName, username, side)) return;
        Player player = Player.getPlayerByUsername(username);
        Deck deck = player.getDeckByName(deckName);
        Card card = Card.getCardByName(cardName);
        if (!command.contains("--side")) {
            deck.removeCardFromMainDeck(card, username);
        } else {
            deck.removeCardFromSideDeck(card, username);
        }
        System.out.println("card removed form deck successfully");
        FileHandler.updatePlayers();
    }

    private boolean addCardValidity(String cardName, String deckName, String username) {
        if (cardName == null | deckName == null) {
            System.out.println("invalid command");
            return false;
        }
        Player player = Player.getPlayerByUsername(username);
        if (!player.doesCardExist(cardName)) {
            System.out.printf("card with name %s does not exist\n", cardName);
            return false;
        }
        if (player.getDeckByName(deckName) == null) {
            System.out.printf("deck with name %s does not exist\n", deckName);
            return false;
        }
        return true;
    }

    private boolean removeCardValidity(String cardName, String deckName, String username, boolean side) {
        Player player = Player.getPlayerByUsername(username);
        if (cardName == null | deckName == null) {
            System.out.println("invalid command");
            return false;
        }
        if (player.getDeckByName(deckName) == null) {
            System.out.printf("deck with name %s does not exist\n", deckName);
            return false;
        }
        Deck deck = player.getDeckByName(deckName);
        if (!side) {
            for (Map.Entry<Card, Integer> e : Deck.getMainDeckByDeck(deck).entrySet()) {
                if (e.getKey().getName().equals(cardName)) {
                    if (e.getValue() != 0) {
                        return true;
                    }
                }
            }
            System.out.printf("card with name %s does not exist in main deck\n", cardName);
            return false;
        } else {
            for (Map.Entry<Card, Integer> e : Deck.getSideDeckByDeck(deck).entrySet()) {
                if (e.getKey().getName().equals(cardName)) {
                    if (e.getValue() != 0) {
                        return true;
                    }
                }
            }
            System.out.printf("card with name %s does not exist in side deck\n", cardName);
            return false;
        }
    }

    private void showAllDecks(String username) {
        Player.getPlayerByUsername(username).showDecks();
    }

    private void showDeck(String username, String command) {
        String deckName = CommonTools.takeNameOutOfCommand(command, "--deck-name");
        String afterSide = CommonTools.takeNameOutOfCommand(command, "--side");
        if (deckName == null || afterSide != null) {
            System.out.println("invalid command");
            return;
        }

        if (Player.getPlayerByUsername(username).getDeckByName(deckName) == null) {
            System.out.println("there is no such a deck");
            return;
        }
        System.out.printf("Deck: %s\n", deckName);
        if (command.contains(" --side")) {
            System.out.println("Side deck");
            Objects.requireNonNull(Player.getPlayerByUsername(username)).getDeckByName(deckName).showSideDeck();
        } else {
            System.out.println("Main deck");
            Objects.requireNonNull(Player.getPlayerByUsername(username)).getDeckByName(deckName).showMainDeck();
        }
    }

    private void showCards(String username) {
        Player player = Objects.requireNonNull(Player.getPlayerByUsername(username));
        ArrayList<Card> allCards = new ArrayList<>();
        HashMap<Card, Integer> notInDeckCards = player.getCards();
        for (Map.Entry<Card, Integer> e : notInDeckCards.entrySet()) {
            for (int k = 0; k < e.getValue(); k++) {
                allCards.add(e.getKey());
            }
        }
        System.out.println(allCards.size());
        ArrayList<Deck> decks = player.getDecks();
        for (Deck deck : decks) {
            HashMap<Card, Integer> mainDeck = deck.getMainDeck();
            HashMap<Card, Integer> sideDeck = deck.getSideDeck();
            for (Map.Entry<Card, Integer> e : mainDeck.entrySet()) {
                for (int k = 0; k < e.getValue(); k++) {
                    allCards.add(e.getKey());
                }
            }
            for (Map.Entry<Card, Integer> e : sideDeck.entrySet()) {
                for (int k = 0; k < e.getValue(); k++) {
                    allCards.add(e.getKey());
                    System.out.printf("%s:%s\n", e.getKey().getName(), e.getKey().getDescription());
                }
            }
        }
        Collections.sort(allCards);
        for (Card card : allCards) {
            System.out.printf("%s:%s\n", card.getName(), card.getDescription());
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setHeight(700);
        stage.setWidth(1400);
        stage.setX(0);
        stage.setY(0);
        BorderPane borderPane = new BorderPane();
        Background background = getBackground();
        Group group = new Group();
        Button seeCards = getButton();
        Button seeDecks = getButton();
        Button createDeck = getButton();
        seeDecks.setText("See Decks");
        seeCards.setText("See Inventory");
        createDeck.setText("Create Deck");
        seeDecks.setLayoutY(-100);
        seeCards.setLayoutY(0);
        createDeck.setLayoutY(100);
        seeCards.setMinWidth(350);
        seeDecks.setMinWidth(350);
        createDeck.setMinWidth(350);
        group.getChildren().add(seeDecks);
        group.getChildren().add(seeCards);
        group.getChildren().add(createDeck);

        Button back = getButton();
        back.setText("Back");
        back.setLayoutY(200);
        back.setMinWidth(350);
        back.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/main_menu_view.fxml"));
            try {
                mainRoot = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainStage = MainProgramView.stage;
            mainStage.setMaximized(false);
            mainScene = new Scene(mainRoot);
            mainStage.setScene(mainScene);
        });
        seeCards.setOnAction(actionEvent -> {
            try {
                seeCards(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        seeDecks.setOnAction(actionEvent -> {
            try {
                seeDecks(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        createDeck.setOnAction(actionEvent -> {
            try {
                createDeck(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        back.setLayoutY(200);
        group.getChildren().add(back);
        VBox vBox = new VBox(group);
        vBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox);
        borderPane.setBackground(background);
        borderPane.setPrefHeight(800);
        borderPane.setPrefWidth(1600);
        Scene scene = new Scene(borderPane);
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();
    }

    private void createDeck(Stage stage) throws IOException {
        FileHandler.updatePlayers();
        stage.setMaximized(true);
        BorderPane borderPane = new BorderPane();
        Background background = getBackground();
        TextField passwordTextField = getTextField();
        Button back = getButton();
        Button register = getButton();
        register.setMinWidth(300);
        back.setText("Back");
        register.setText("Register");
        back.setOnAction(actionEvent -> {
            try {
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        register.setOnAction(actionEvent -> {
            try {
                createDeck(logged, "deck create " + passwordTextField.getText());
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        VBox backV = new VBox(passwordTextField, register, back);
        backV.setSpacing(20);
        backV.setAlignment(Pos.CENTER);
        borderPane.setCenter(backV);
        borderPane.setBackground(background);
        stage.setResizable(true);
        Scene scene = new Scene(borderPane, 1600, 800);
        stage.setScene(scene);
        stage.show();
    }

    private void seeDecks(Stage stage) throws IOException {
        stage.setMaximized(true);
        FileHandler.updatePlayers();
        BorderPane borderPane = new BorderPane();
        Background background = getBackground();
        Player player = Objects.requireNonNull(Player.getPlayerByUsername(logged));
        ArrayList<Deck> decks = player.getDecks();
        int number = 0;
        Group group = new Group();
        Text title = new Text();
        for (int i = 0; i < 20 && number < decks.size(); i++) {
            for (int j = 0; j < 10 && number < decks.size(); j++) {
                Button button = getDeckButton();
                button.setLayoutX(0);
                button.setLayoutY(70 * j);
                String deckText = decks.get(number).getDeckName();
                if (Player.getActiveDeckByUsername(logged) != null &&
                        Player.getActiveDeckByUsername(logged).getDeckName().equals(deckText)) {
                    deckText = deckText + " (Active Deck)";
                }
                button.setText(deckText);
                button.setTextFill(Color.WHITE);
                button.setMinWidth(300);
                button.setMaxWidth(300);
                button.setFont(Font.font(20));
                int finalNumber1 = number;
                button.setOnAction(actionEvent -> {
                    try {
                        deckMenu(stage, decks.get(finalNumber1).getDeckName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                group.getChildren().add(button);
                number = number + 1;
            }
        }
        VBox v = new VBox(title);
        v.setAlignment(Pos.CENTER);
        title.setText("\nChoose A Deck");
        title.setFont(Font.font(65));
        title.setFill(Color.WHITE);
        borderPane.setTop(v);
        VBox vBox = new VBox(group);
        Button back = getButton();
        back.setText("Back");
        back.setOnAction(actionEvent -> {
            try {
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        VBox backV = new VBox(back);
        backV.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox);
        borderPane.setBottom(backV);
        borderPane.setBackground(background);
        Scene scene = new Scene(borderPane, 1600, 800);
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        stage.setScene(scene);
        stage.show();
    }

    private void deckMenu(Stage stage, String deckName) throws IOException {
        FileHandler.updatePlayers();
        stage.setMaximized(true);
        BorderPane borderPane = new BorderPane();
        Background background = getBackground();
        Button seeCards = getButton();
        Button setActivate = getButton();
        Button deleteDeck = getButton();
        seeCards.setMinWidth(350);
        setActivate.setMinWidth(350);
        deleteDeck.setMinWidth(350);
        seeCards.setText("See Cards");
        setActivate.setText("Set As Active");
        deleteDeck.setText("Delete Deck");
        seeCards.setOnAction(actionEvent -> {
            try {
                choseCard(stage, deckName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        setActivate.setOnAction(actionEvent -> {
            try {
                setActiveDeck(logged, "deck set-activate " + deckName);
                seeDecks(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        deleteDeck.setOnAction(actionEvent -> {
            try {
                deleteDeck(logged, "deck delete " + deckName);
                seeDecks(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        VBox vBox = new VBox(seeCards, setActivate, deleteDeck);
        vBox.setSpacing(20);
        Button back = getButton();
        back.setText("Back");
        back.setOnAction(actionEvent -> {
            try {
                seeDecks(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        VBox backV = new VBox(back);
        backV.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox);
        borderPane.setBottom(backV);
        borderPane.setBackground(background);
        Scene scene = new Scene(borderPane, 1600, 800);
        stage.setScene(scene);
        stage.show();
    }

    private void choseCard(Stage stage, String deckName) throws IOException {
        FileHandler.updatePlayers();
        stage.setMaximized(true);
        BorderPane borderPane = new BorderPane();
        Background background = getBackground();
        final String[] cardName = {""};
        final int[] number = {0};
        Player player = Player.getPlayerByUsername(logged);
        Deck deck = player.getDeckByName(deckName);
        HashMap<Card, Integer> mainDeck = deck.getMainDeck();
        ArrayList<Card> allCards = new ArrayList<>();
        for (Map.Entry<Card, Integer> e : mainDeck.entrySet()) {
            for (int k = 0; k < e.getValue(); k++) {
                allCards.add(e.getKey());
            }
        }
        Group group = new Group();
        Button delete = getButton();
        delete.setText("Delete");
        for (int i = 0; i < 15 && number[0] < allCards.size(); i++) {
            for (int j = 0; j < 6 && number[0] < allCards.size(); j++) {
                Button button = new Button();
                button.setLayoutX(220 * j + 200);
                button.setLayoutY(300 * i + 200);
                button.setText(allCards.get(number[0]).getName());
                button.setMaxWidth(0);
                button.setMinWidth(0);
                button.setMaxHeight(0);
                button.setMinHeight(0);
                button.setGraphic(cardView(allCards.get(number[0]).getName()));
                final int[] finalNumber = {number[0]};
                group.getChildren().add(button);
                button.setOnAction(actionEvent -> {
                    cardName[0] = allCards.get(finalNumber[0]).getName();
                    ImageView imageView = cardView(cardName[0]);
                    imageView.setEffect(new DropShadow(100, Color.BLACK));
                    button.setGraphic(imageView);
                    delete.setTextFill(Color.BLACK);
                });
                number[0] = number[0] + 1;
            }
        }
        Text title = new Text("Choose A Card To Remove From Your Deck");
        title.setFont(new Font(40));
        VBox textBox = new VBox(title);
        title.setFill(Color.web("#4bdae9"));
        textBox.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(group);
        scrollPane.setVmax(1400);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox vBox = new VBox(scrollPane);
        vBox.setMaxWidth(1400);
        if (cardName[0].equals("")) {
            delete.setTextFill(Color.web("#727070"));
        }
        borderPane.setCenter(vBox);
        borderPane.setTop(textBox);
        borderPane.setBackground(background);
        scrollPane.setBackground(background);
        vBox.setBackground(background);
        vBox.setAlignment(Pos.CENTER);

        delete.setMinWidth(200);
        Button back = getButton();
        back.setText("Back");
        back.setOnAction(actionEvent -> {
            try {
                deckMenu(stage, deckName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        delete.setOnAction(actionEvent -> {
            if (!cardName[0].equals("")) {
                try {
                    removeCardFromDeck(logged, "deck rm-card --card " + cardName[0] + " --deck " + deckName);
                    choseCard(stage, deckName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        VBox backV = new VBox(delete, back);
        backV.setSpacing(10);
        backV.setAlignment(Pos.CENTER);
        borderPane.setBottom(backV);
        Scene scene = new Scene(borderPane, 1600, 800);
        stage.setScene(scene);
        stage.show();
    }

    private void seeCards(Stage stage) throws IOException {
        FileHandler.updatePlayers();
        stage.setMaximized(true);
        BorderPane borderPane = new BorderPane();
        Background background = getBackground();
        Player player = Objects.requireNonNull(Player.getPlayerByUsername(logged));
        ArrayList<Card> allCards = new ArrayList<>();
        HashMap<Card, Integer> notInDeckCards = player.getCards();
        for (Map.Entry<Card, Integer> e : notInDeckCards.entrySet()) {
            for (int k = 0; k < e.getValue(); k++) {
                allCards.add(e.getKey());
            }
        }
        int number = 0;
        Group group = new Group();
        for (int i = 0; i < 15 && number < allCards.size(); i++) {
            for (int j = 0; j < 5 && number < allCards.size(); j++) {
                Button button = new Button();
                button.setLayoutX(250 * j + 200);
                button.setLayoutY(300 * i + 200);
                button.setText(allCards.get(number).getName());
                button.setMaxWidth(0);
                button.setMinWidth(0);
                button.setMaxHeight(0);
                button.setMinHeight(0);
                button.setGraphic(cardView(allCards.get(number).getName()));
                int finalNumber = number;
                group.getChildren().add(button);
                button.setOnAction(actionEvent -> {
                    try {
                        addCardMenu(stage, allCards.get(finalNumber).getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                number = number + 1;
            }
        }

        Text title = new Text("Choose A Card To Add To Your Deck");
        title.setFont(new Font(50));
        VBox textBox = new VBox(title);
        title.setFill(Color.web("#4bdae9"));
        textBox.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(group);
        scrollPane.setVmax(1400);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox vBox = new VBox(scrollPane);
        vBox.setMaxWidth(1400);
        borderPane.setCenter(vBox);
        borderPane.setTop(textBox);
        borderPane.setBackground(background);
        scrollPane.setBackground(background);
        vBox.setBackground(background);
        vBox.setAlignment(Pos.CENTER);
        Button back = getButton();
        back.setText("Back");
        back.setOnAction(actionEvent -> {
            try {
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        VBox backV = new VBox(back);
        backV.setAlignment(Pos.CENTER);
        borderPane.setBottom(backV);
        stage.setY(0);
        Scene scene = new Scene(borderPane, 1600, 800);
        stage.setScene(scene);
        stage.show();
    }

    public void addCardMenu(Stage stage, String cardName) throws IOException {
        FileHandler.updatePlayers();
        stage.setMaximized(true);
        BorderPane borderPane = new BorderPane();
        Background background = getBackground();
        Player player = Objects.requireNonNull(Player.getPlayerByUsername(logged));
        ArrayList<Deck> decks = player.getDecks();
        int number = 0;
        Group group = new Group();
        Text title = new Text();
        Text error = new Text();
        error.setFill(Color.RED);
        for (int i = 0; i < 20 && number < decks.size(); i++) {
            for (int j = 0; j < 10 && number < decks.size(); j++) {
                Button button = getDeckButton();
                button.setLayoutX(0);
                button.setLayoutY(70 * j);
                button.setText(decks.get(number).getDeckName());
                button.setTextFill(Color.WHITE);
                button.setMinWidth(200);
                button.setFont(Font.font(20));
                int finalNumber1 = number;
                button.setOnAction(actionEvent -> {
                    try {
                        error.setText("\nError: " + addCardToDeckOperator(cardName, decks.get(finalNumber1).getDeckName(), "", stage));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                group.getChildren().add(button);
                number = number + 1;
            }
        }
        VBox v = new VBox(title, error);
        v.setAlignment(Pos.CENTER);
        title.setText("\nChoose A Deck To Add This Card");
        title.setFont(Font.font(65));
        title.setFill(Color.WHITE);
        error.setFill(Color.web("#ff00e6"));
        error.setFont(Font.font(35));
        borderPane.setTop(v);
        VBox vBox = new VBox(group);
        Button back = getButton();
        back.setText("Back");
        back.setOnAction(actionEvent -> {
            try {
                seeCards(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        VBox backV = new VBox(back);
        backV.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox);
        borderPane.setBottom(backV);
        borderPane.setBackground(background);
        Scene scene = new Scene(borderPane, 1600, 800);
        stage.setScene(scene);
        stage.show();
    }

    private String addCardToDeckOperator(String cardName, String deckName, String command, Stage stage) throws IOException {
        Player player = Player.getPlayerByUsername(logged);
        Deck deck = player.getDeckByName(deckName);
        if (!command.contains("--side")) {
            if (deck.isMainDeckFull()) {
                return "main deck is full";
            }
            if (deck.isThereThreeCards(Card.getCardByName(cardName))) {
                return "there are already three cards with name " + cardName + " in deck " + deckName;
            }
            deck.addCardToMainDeck(Card.getCardByName(cardName));
        } else {
            if (deck.isSideDeckFull()) {
                return "side deck is full";
            }
            if (deck.isThereThreeCards(Card.getCardByName(cardName))) {
                return "there are already three cards with name " + cardName + " in deck " + deckName;
            }
            deck.addCardToSideDeck(Card.getCardByName(cardName));
        }
        player.removeCard(Card.getCardByName(cardName));
        System.out.println("card added to deck successfully");
        FileHandler.updatePlayers();
        seeCards(stage);
        return "";
    }

    private ImageView cardView(String name) {
        ImageView imageView = new ImageView();
        if (name.equals("\"Terratiger, the Empowered Warrior\""))
            name = "Terratiger";
        String searchName = name.replace("of", "Of").replace(" ", "").replace("-", "");
        String url = "/Images/Cards/" + searchName + ".jpg";
        Image image = new Image(getClass().getResource(url).toExternalForm());
        imageView.setImage(image);
        imageView.setFitWidth(198);
        imageView.setFitHeight(272);
        return imageView;
    }

    public static Button getButton() throws FileNotFoundException {
        Font font = Font.font("Albertus Medium", 30);
        Button button = new Button();
        button.setTextFill(Color.BLACK);
        button.setOnMouseEntered(actionEvent -> {
            button.setStyle("-fx-border-color: #ffffff; -fx-border-width: 3px; -fx-border-radius: 16px;" +
                    " -fx-background-radius: 20px; -fx-background-color: #fdc44480");
        });
        button.setOnMouseExited(actionEvent -> {
            button.setStyle("-fx-border-color: #727070; -fx-border-width: 3px; -fx-border-radius: 16px;" +
                    " -fx-background-radius: 20px; -fx-background-color: #f5f58780");
        });
        button.setStyle("-fx-border-color: #727070; -fx-border-width: 3px; -fx-border-radius: 16px;" +
                " -fx-background-radius: 20px; -fx-background-color: #f5f58780");
        button.setFont(font);
        button.setMaxWidth(150);
        button.setMaxHeight(5);
        button.setMinHeight(65);
        return button;
    }

    private Background getBackground() {
        Image image = new Image("\\images\\shopBackground.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        return background;
    }

    private Button getDeckButton() {
        Font font = Font.font("Albertus Medium", 30);
        Button button = new Button();
        button.setLayoutX(0);
        button.setTextFill(Color.WHITE);
        button.setMinWidth(200);
        button.setFont(font);
        button.setOnMouseEntered(actionEvent -> {
            button.setStyle("-fx-border-color: #ffffff; -fx-border-width: 3px; -fx-border-radius: 5px;" +
                    " -fx-background-radius: 5px; -fx-background-color: #04156e95");
        });
        button.setOnMouseExited(actionEvent -> {
            button.setStyle("-fx-border-color: #727070; -fx-border-width: 3px; -fx-border-radius: 5px;" +
                    " -fx-background-radius: 5px; -fx-background-color: #00123b95");
        });
        button.setStyle("-fx-border-color: #727070; -fx-border-width: 3px; -fx-border-radius: 0px;" +
                " -fx-background-radius: 5px; -fx-background-color: #00123b95");
        return button;
    }

    public static TextField getTextField() throws FileNotFoundException {
        TextField textField = new TextField();
        textField.setMaxWidth(300);
        textField.setPromptText("Enter Your Deck Name");
        Font font = Font.font("Albertus Medium", 20);
        textField.setFont(font);
        textField.setStyle("-fx-border-color: #727070; -fx-border-width: 3px; -fx-border-radius: 6px;" +
                " -fx-background-radius: 6px; -fx-background-color: #000000;" +
                "-fx-text-fill: #ffffff; -fx-prompt-text-fill: #5a5a5a ");
        return textField;
    }
}
