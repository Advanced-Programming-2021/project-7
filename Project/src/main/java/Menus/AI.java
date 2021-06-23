package Menus;

import Model.Cards.Monster;

public class AI {
    private GameDeck AIDeck;
    private GameDeck playerDeck;
    private Phase phase;
    private static int isSpellSelected = 0;
    private static int isMonsterEntered = 0;

    public void updateAI(GameDeck AIDeck, GameDeck playerDeck, Phase phase){
        this.AIDeck = AIDeck;
        this.playerDeck = playerDeck;
        this.phase = phase;
    }

    public String decision(){
        String decision = null;
        if (phase == Phase.draw) decision = drawPhase();
        else if (phase == Phase.standby) decision = standbyPhase();
        else if (phase == Phase.main1) decision = main1Phase();
        else if (phase == Phase.battle) decision = battlePhase();
        else if (phase == Phase.main2) decision = main2Phase();
        else if (phase == Phase.end) decision = endPhase();
        return decision;
    }

    private String drawPhase(){
        return "next phase";
    }

    private String standbyPhase(){
        return "next phase";
    }

    private String main1Phase(){
        if (isSpellSelected == 0 && !AIDeck.isSpellZoneFull()){
            for (int i = 0; i < AIDeck.getInHandCards().size(); i++){
                if (AIDeck.getInHandCards().get(i).getType().equals("Spell") ||
                        AIDeck.getInHandCards().get(i).getType().equals("Trap")) {
                    isSpellSelected = 1;
                    int j = i + 1;
                    return "select --hand " + j;
                }
            }
        } else if (isSpellSelected == 1){
            isSpellSelected = 0;
            return "set";
        }
        if (isMonsterEntered == 0 && !AIDeck.isMonsterZoneFull()){
            for (int i = 0; i < AIDeck.getInHandCards().size(); i++){
                if (AIDeck.getInHandCards().get(i).getType().equals("Monster")) {
                    isMonsterEntered = 1;
                    int j = i + 1;
                    return "select --hand " + j;
                }
            }
        } else if (isMonsterEntered == 1){
            isMonsterEntered = 2;
            return "set";
        }
        return "next phase";
    }

    private String battlePhase(){
        return "next phase";
    }

    private String main2Phase(){
        return "next phase";
    }

    private String endPhase(){
        isMonsterEntered = 0;
        isSpellSelected = 0;
        return "next phase";
    }
}
