package Menus;

import Model.Sound;
import View.MainProgramView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class RockPaperScissors extends Application {
    private Scene scene;
    private Parent root;

    private String firstPlayer;
    private String secondPlayer;
    private static Stage stage;
    private String firstPlayerChoice;
    private String secondPlayerChoice;
    private ArrayList<String> players = new ArrayList<>();
    private int turn = 0;

    public void run(String firstPlayer, String secondPlayer) throws Exception {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        players.add(firstPlayer);
        players.add(secondPlayer);
        this.stage = MainProgramView.stage;
        start(stage);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setMaximized(true);
        Text title = new Text("\n It's " + players.get(turn) + "'s Turn");
        title.setFont(new Font(75));
        VBox textBox = new VBox(title);
        title.setFill(Color.web("#CB2501"));
        textBox.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        Background background = getBackground();
        borderPane.setBackground(background);
        borderPane.setPrefHeight(800);
        borderPane.setPrefWidth(1600);
        Button rock = getButton();
        Button paper = getButton();
        Button scissors = getButton();
        rock.setText("Rock");
        paper.setText("Paper");
        scissors.setText("Scissors");
        rock.setOnAction(actionEvent -> {
            Sound.getSoundByName("button").playSoundOnce();
            if (turn == 0){
                this.firstPlayerChoice = "rock";
                turn = changeTurn(turn);
            } else {
                this.secondPlayerChoice = "rock";
                turn = changeTurn(turn);
                try {
                    process();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            title.setText("\n It's " + players.get(turn) + "'s turn");
        });
        paper.setOnAction(actionEvent -> {
            Sound.getSoundByName("button").playSoundOnce();
            if (turn == 0){
                this.firstPlayerChoice = "paper";
                turn = changeTurn(turn);
            } else {
                this.secondPlayerChoice = "paper";
                turn = changeTurn(turn);
                try {
                    process();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            title.setText("\n It's " + players.get(turn) + "'s Turn");
        });
        scissors.setOnAction(actionEvent -> {
            Sound.getSoundByName("button").playSoundOnce();
            if (turn == 0){
                this.firstPlayerChoice = "scissors";
                turn = changeTurn(turn);
            } else {
                this.secondPlayerChoice = "scissors";
                turn = changeTurn(turn);
                try {
                    process();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            title.setText("\n It's " + players.get(turn) + "'s Turn");
        });
        VBox vBox = new VBox(rock, paper, scissors);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        borderPane.setCenter(vBox);
        borderPane.setTop(textBox);
        Scene scene = new Scene(borderPane);
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();
    }

    private Background getBackground() {
        Image image = new Image("\\images\\rock.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(1600);
        imageView.setFitHeight(800);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,new BackgroundSize(1, 1, true, true, false, false));
        Background background = new Background(backgroundImage);
        return background;
    }

    private int changeTurn(int turn) {
        if (turn == 0) return 1;
        else return 0;
    }

    private void process() throws Exception {
        stage.setMaximized(true);
        Button button = getButton();
        button.setText("Ok");
        Text title = new Text();
        title.setFont(new Font(50));
        VBox textBox = new VBox(title, button);
        textBox.setSpacing(40);
        title.setFill(Color.web("#CB2501"));
        textBox.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        Background background = getBackground();
        borderPane.setBackground(background);
        if (firstPlayerChoice.equals("paper") && secondPlayerChoice.equals("rock") ||
                (firstPlayerChoice.equals("scissors") && secondPlayerChoice.equals("paper")) ||
                (firstPlayerChoice.equals("rock") && secondPlayerChoice.equals("scissors"))){
            title.setText("\n " + firstPlayer + " will start first");
            borderPane.setCenter(textBox);
            Scene scene = new Scene(borderPane, 1600, 800);
            stage.sizeToScene();
            stage.setScene(scene);
            stage.show();
            button.setOnAction(actionEvent -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/game_board.fxml"));
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DuelProgramController.firstPlayer = firstPlayer;
                DuelProgramController.secondPlayer = secondPlayer;
                makeStage();
            });
        } else if (secondPlayerChoice.equals("paper") && firstPlayerChoice.equals("rock") ||
                (secondPlayerChoice.equals("scissors") && firstPlayerChoice.equals("paper")) ||
                (secondPlayerChoice.equals("rock") && firstPlayerChoice.equals("scissors"))){
            title.setText("\n " + secondPlayer + " will start first");
            borderPane.setCenter(textBox);
            Scene scene = new Scene(borderPane, 1600, 800);
            stage.sizeToScene();
            stage.setScene(scene);
            stage.show();
            button.setOnAction(actionEvent -> {
                try {
                    root = FXMLLoader.load(getClass().getResource("/FXML/game_board.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DuelProgramController.firstPlayer = secondPlayer;
                DuelProgramController.secondPlayer = firstPlayer;
                makeStage();
            });
        } else {
            new RockPaperScissors().run(firstPlayer, secondPlayer);
        }
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
        button.setMinWidth(250);
        return button;
    }

    public void makeStage() {
        stage = MainProgramView.stage;
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.setWidth(1020);
        stage.setHeight(820);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}
