package Menus;

import Model.Cards.Monster;

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
                    int index = indexOfLestPowerfulMonster();
                    if (playerDeck.getMonsterZones().get(index).getCurrentMonster() != null) {
                        return "attack " + index;
                    }
                }
            }
        }
        isSpellSelected = 0;
        return "next phase";
    }

    private String main2Phase() {
        if (isSpellSelected == 0) {
            for (int i = 1; i <= 5; i++) {
                if (AIDeck.getSpellZones().get(i).getCurrentCard() != null) {
                    if (AIDeck.getSpellZones().get(i).getStatus().equals("H")) {
                        String cardName = AIDeck.getSpellZones().get(i).getCurrentCard().getName();
                        if (cardName.equals("Terraforming") || cardName.equals("Pot of Greed") || cardName.equals("Raigeki") ||
                                cardName.equals("Harpie’s Feather Duster") || cardName.equals("Dark Hole")) {
                            isSpellSelected = 1;
                            return "select --spell " + i;
                        }
                    }
                }
            }
        } else if (isSpellSelected == 1) {
            isSpellSelected = 2;
            return "activate effect";
        }
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

    private int indexOfLestPowerfulMonster() {
        int index = 1;
        int power = 100000;
        for (int i = 1; i <= 5; i++) {
            if (playerDeck.getMonsterZones().get(i).getCurrentMonster() != null) {
                Monster monster = (Monster) playerDeck.getMonsterZones().get(i).getCurrentMonster();
                if (playerDeck.getMonsterZones().get(i).getStatus().equals("OO")) {
                    if (monster.getAttackPoint() <= power) {
                        power = monster.getAttackPoint();
                        index = i;
                    }
                } else {
                    if (monster.getDefensePoint() <= power) {
                        power = monster.getAttackPoint();
                        index = i;
                    }
                }
            }
        }
        return index;
    }
}
