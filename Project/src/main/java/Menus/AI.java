package Menus;

import Model.Cards.Monster;
import Model.Deck;

public class AI {
    private GameDeck AIDeck;
    private GameDeck playerDeck;
    private Phase phase;
    private static int isSpellSelected = 0;
    private static int isMonsterEntered = 0;
    private static int isMonsterSelectedForAttack = 0;
    private static int indexOfSelectedMonster = 1;

    public void updateAI(GameDeck AIDeck, GameDeck playerDeck, Phase phase) {
        this.AIDeck = AIDeck;
        this.playerDeck = playerDeck;
        this.phase = phase;
    }

    public String decision() {
        String decision = null;
        if (phase == Phase.draw) decision = drawPhase();
        else if (phase == Phase.standby) decision = standbyPhase();
        else if (phase == Phase.main1) decision = main1Phase();
        else if (phase == Phase.battle) decision = battlePhase();
        else if (phase == Phase.main2) decision = main2Phase();
        else if (phase == Phase.end) decision = endPhase();
        return decision;
    }

    private String drawPhase() {
        return "next phase";
    }

    private String standbyPhase() {
        return "next phase";
    }

    private String main1Phase() {
        if (isSpellSelected == 0 && !AIDeck.isSpellZoneFull()) {
            for (int i = 0; i < AIDeck.getInHandCards().size(); i++) {
                if (AIDeck.getInHandCards().get(i).getType().equals("Spell") ||
                        AIDeck.getInHandCards().get(i).getType().equals("Trap")) {
                    isSpellSelected = 1;
                    int j = i + 1;
                    return "select --hand " + j;
                }
            }
        } else if (isSpellSelected == 1) {
            isSpellSelected = 0;
            return "set";
        }
        if (isMonsterEntered == 0 && !AIDeck.isMonsterZoneFull()) {
            for (int i = 0; i < AIDeck.getInHandCards().size(); i++) {
                if (AIDeck.getInHandCards().get(i).getType().equals("Monster")) {
                    Monster selectedMonster = (Monster) AIDeck.getInHandCards().get(i);
                    if ((selectedMonster.getLevel() == 7 || selectedMonster.getLevel() == 8) && numberOfMonsters(AIDeck) >= 2) {
                        isMonsterEntered = 1;
                        int j = i + 1;
                        return "select --hand " + j;
                    } else if ((selectedMonster.getLevel() == 5 || selectedMonster.getLevel() == 6) && numberOfMonsters(AIDeck) >= 1) {
                        isMonsterEntered = 1;
                        int j = i + 1;
                        return "select --hand " + j;
                    } else if (selectedMonster.getLevel() <= 4) {
                        isMonsterEntered = 1;
                        int j = i + 1;
                        return "select --hand " + j;
                    }
                }
            }
        } else if (isMonsterEntered == 1) {
            isMonsterEntered = 2;
            return "summon";
        }
        return "next phase";
    }

    private String battlePhase() {
        if (playerDeck.isMonsterZoneEmpty() && !AIDeck.isMonsterZoneEmpty()) {
            if (isMonsterSelectedForAttack == 0) {
                for (int i = indexOfSelectedMonster; i <= 5; i++) {
                    if (AIDeck.getMonsterZones().get(i).getCurrentMonster() != null) {
                        isMonsterSelectedForAttack = 1;
                        int index = indexOfSelectedMonster;
                        indexOfSelectedMonster = i + 1;
                        return "select --monster " + index;
                    }
                }
            } else if (isMonsterSelectedForAttack == 1) {
                isMonsterSelectedForAttack = 0;
                return "attack direct";
            }
        } else if (!playerDeck.isMonsterZoneEmpty()) {
            if (isMonsterSelectedForAttack == 0) {
                for (int i = indexOfSelectedMonster; i <= 5; i++) {
                    if (AIDeck.getMonsterZones().get(i).getCurrentMonster() != null) {
                        isMonsterSelectedForAttack = 1;
                        int index = indexOfSelectedMonster;
                        indexOfSelectedMonster = i + 1;
                        return "select --monster " + index;
                    }
                }
            } else if (isMonsterSelectedForAttack == 1) {
                isMonsterSelectedForAttack = 0;
                while (true) {
                    int randomIndex = (int) Math.floor(Math.random() * 5 + 1);
                    if (playerDeck.getMonsterZones().get(randomIndex).getCurrentMonster() != null) {
                        return "attack " + randomIndex;
                    }
                }
            }
        }
        return "next phase";
    }

    private String main2Phase() {
        return "next phase";
    }

    private String endPhase() {
        isMonsterEntered = 0;
        isSpellSelected = 0;
        isMonsterSelectedForAttack = 0;
        indexOfSelectedMonster = 1;
        return "next phase";
    }

    private int numberOfMonsters(GameDeck gameDeck) {
        int number = 0;
        for (int i = 1; i <= 5; i++) {
            if (gameDeck.getMonsterZones().get(i).getCurrentMonster() != null) {
                number = number + 1;
            }
        }
        return number;
    }
}
