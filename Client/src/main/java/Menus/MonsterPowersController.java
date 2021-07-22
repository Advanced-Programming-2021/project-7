package Menus;

import Model.Cards.Card;
import Model.Cards.Monster;
import Model.CommonTools;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;

public class MonsterPowersController {
    private ArrayList <GameDeck> gameDecks = new ArrayList<>();
    private DuelProgramController duelProgramController;
    private Phase phase;
    private int turn;
    private int selectedCardIndex;
    private int isSummoned = 0;
    private Card selectedCard;
    private Card attackerCard;
    private boolean isEnemyTakeDamage = true;

    public MonsterPowersController(ArrayList <GameDeck> gameDecks, DuelProgramController duelProgramController) {
        this.gameDecks = gameDecks;
        this.duelProgramController = duelProgramController;
    }
}