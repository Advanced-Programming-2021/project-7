package Menus;

public class AI {
    private GameDeck AIDeck;
    private GameDeck playerDeck;
    private Phase phase;
    private boolean isSelected = false;

    public void updateAI(GameDeck AIDeck, GameDeck playerDeck, Phase phase){
        this.AIDeck = AIDeck;
        this.playerDeck = playerDeck;
        this.phase = phase;
    }

    public String decision(GameDeck AIDeck, GameDeck playerDeck, Phase phase){
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
        if (!isSelected){
            if (!AIDeck.isSpellZoneFull()){
                for (int i = 0; i <= AIDeck.getInHandCards().size(); i++){
                    if (AIDeck.getInHandCards().get(i).getType().equals("Spell")){
                        isSelected = true;
                        return "select --hand " + i + 1;
                    }
                }
            }
        } else {
            isSelected = false;
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
        return "next phase";
    }
}
