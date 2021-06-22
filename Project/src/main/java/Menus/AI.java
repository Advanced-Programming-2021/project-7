package Menus;

public class AI {
    private GameDeck AIDeck;
    private GameDeck playerDeck;
    private Phase phase;

    public String decision(GameDeck AIDeck, GameDeck playerDeck, Phase phase){
        String decision = null;
        if (phase == Phase.draw){
            decision = drawPhase();
        } else if (phase == Phase.standby){
            decision = standbyPhase();
        } else if (phase == Phase.main1){

        } else if (phase == Phase.battle){

        } else if (phase == Phase.main2){

        } else if (phase == Phase.end){

        }
        return decision;
    }

    private String drawPhase(){
        return "next phase";
    }

    private String standbyPhase(){
        return "next phase";
    }

    private String main1Phase(){
        for (int i = 0; i <= AIDeck.getInHandCards().size(); i++){
            if (AIDeck.getInHandCards().get(i).getType().equals("Spell")){
                return "select --hand " + i + 1;
            }
        }
        return "next phase";
    }
}
