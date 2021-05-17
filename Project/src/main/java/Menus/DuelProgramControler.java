package Menus;

import Model.Cards.*;
import Model.CommonTools;
import Model.Deck;
import Model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

enum Phase {
    draw,
    standby,
    main1,
    battle,
    main2,
    end
}

class DuelProgramControler {
    private ArrayList<GameDeck> gameDecks = new ArrayList<>(2);
    private int turn = 0; //0 : firstPlayer, 1 : secondPlayer
    private int isSummoned = 0; //0 : is not summoned before, 1 : is summoned before
    private Card selectedCard = null;
    private int selectedCardIndex = -1; // -1 means Empty
    private int selectedMonsterCardIndex = -1; // -1 means empty
    private String selectedDeck = null; // hand, monster, spell, field,
                                        // opponentMonster, opponentSpell, opponentField
    private Phase phase = Phase.draw;

    public void run(String firstPlayer, String secondPlayer, int round) {
        for (int i = 1; i <= round; i++) {
            // methods to be set after each round
            if (isGameOver(i)) break;
            setGameDecks(firstPlayer, secondPlayer);
            while (true) {
                if (isRoundOver()) break;
                String command = CommonTools.scan.nextLine();
                showGameDeck(turn);
                if (command.matches("^show graveyard$")) showGraveyard(turn);
                else if (command.matches("^surrender$")) surrender(turn);
                else if (command.matches("^select .*$")) selectCard(command);
                else if (command.matches("^select -d$")) deselect();
                else if (command.matches("^summon$")) summonMonster();
                else if (command.matches("^activate effect$")) activateSpellErrorCheck();
                else if (command.matches("^set$")) set();
                else if (command.matches("^card show --selected$")) cardShow();
                else if (command.matches("^increase --LP (\\d+)$")) increasePlayerLPCheat(command);
                else if (command.matches("^duel set-winner \\S+$")) setWinnerCheat(command);
                else if (command.matches("^set --position (attack|defence)$")) setPositionMonster(command);
                else if (command.matches("flip-summon")) flipSummon();
                else System.out.println("invalid command");
            }
        }
        gameOver(round);
    }

    private boolean isRoundOver() {
        if (gameDecks.get(0).getPlayerLP() <= 0) {
            roundOver(0);
            return true;
        } else if (gameDecks.get(1).getPlayerLP() <= 0) {
            roundOver(1);
            return true;
        } else return false;
    }

    private boolean isGameOver(int round) {
        if (round < 3) return false;
        if (gameDecks.get(0).getWinRounds() > 1) return true;
        if (gameDecks.get(1).getWinRounds() > 1) return true;
        return false;
    }

    private void setGameDecks(String firstPlayer, String secondPlayer) {
        String firstNick = Player.getPlayerByUsername(firstPlayer).getNickname();
        String secondNick = Player.getPlayerByUsername(secondPlayer).getNickname();
        Deck activeDeck1 = Player.getActiveDeckByUsername(firstPlayer);
        Deck activeDeck2 = Player.getActiveDeckByUsername(secondPlayer);
        GameDeck gameDeckFirst = new GameDeck(firstNick, firstPlayer,
                Deck.getMainDeckByDeck(activeDeck1), Deck.getSideDeckByDeck(activeDeck1));
        GameDeck gameDeckSecond = new GameDeck(secondNick, secondPlayer,
                Deck.getMainDeckByDeck(activeDeck2), Deck.getSideDeckByDeck(activeDeck2));
        gameDecks.add(gameDeckFirst);
        gameDecks.add(gameDeckSecond);
    }

    private void showGameDeck(int turn) {
        GameDeck myDeck = gameDecks.get(turn);
        GameDeck enemyDeck = gameDecks.get(changeTurn(turn));
        showEnemyDeck(enemyDeck);
        System.out.println("--------------------------");
        showMyDeck(myDeck);
    }

    private void showEnemyDeck(GameDeck enemyDeck) {
        System.out.println(enemyDeck.getPlayerNickName() + " : " + enemyDeck.getPlayerLP());
        System.out.printf("\t");
        for (int i = 0; i < enemyDeck.getInHandCards().size(); i++) {
            System.out.printf("c\t");
        }
        System.out.printf("\n");
        System.out.println(enemyDeck.getDeck().size());

        System.out.printf("\t");
        for (int i = 4; i >= 0; i--) {
            System.out.printf("%s\t", enemyDeck.getSpellZones().get(i).getStatus());
        }
        System.out.printf("\n");

        System.out.printf("\t");
        for (int i = 4; i >= 0; i--) {
            System.out.printf("%s\t", enemyDeck.getMonsterZones().get(i).getStatus());
        }
        System.out.printf("\n");

        System.out.println(enemyDeck.getGraveyardCards().size() + "\t\t\t\t\t\t" + enemyDeck.getFieldZoneAsString());
    }

    private void showMyDeck(GameDeck myDeck) {
        System.out.println(myDeck.getFieldZoneAsString() + "\t\t\t\t\t\t" + myDeck.getGraveyardCards().size());

        System.out.printf("\t");
        for (int i = 0; i < 5; i++) {
            System.out.printf("%s\t", myDeck.getMonsterZones().get(i).getStatus());
        }
        System.out.printf("\n");

        System.out.printf("\t");
        for (int i = 0; i < 5; i++) {
            System.out.printf("%s\t", myDeck.getSpellZones().get(i).getStatus());
        }
        System.out.printf("\n");

        for (int i = 0; i < myDeck.getInHandCards().size(); i++) {
            System.out.printf("c\t");
        }
        System.out.println(myDeck.getPlayerNickName() + " : " + myDeck.getPlayerLP());
    }

    private void selectCard(String command) {
        String address = command.substring(7);
        if (!isAddressValid(address)) {
            System.out.println("invalid selection");
            return;
        }
        if (address.matches("^(?:(?:--monster|--spell|--field|--hand|--opponent)( (\\d+))* ?){3}$"))
            selectOpponentDeck(address);
        else selectMyDeck(address);
    }

    private void deselect(){
        if(selectedCard == null){
            System.out.println("no card is selected yet");
            return;
        }
        selectedCard = null;
        selectedCardIndex = -1;
        selectedDeck = null;
        System.out.println("card deselected");
    }

    private void selectMyDeck(String address) {
        if (address.matches("^--monster (\\d+)$")) {
            int position = Integer.parseInt(CommonTools.takeNameOutOfCommand(address, "--monster"));
            selectMonster(position);
        } else if (address.matches("^--spell (\\d+)$")) {
            int position = Integer.parseInt(CommonTools.takeNameOutOfCommand(address, "--spell"));
            selectSpell(position);
        } else if (address.matches("^--hand (\\d+)$")) {
            int position = Integer.parseInt(CommonTools.takeNameOutOfCommand(address, "--hand"));
            selectHand(position);
        } else if (address.matches("^--field$"))
            selectField();
    }

    private void selectOpponentDeck(String address) {
        if (address.matches("^(?:(?:--monster|--opponent)( (\\d+))* ?){3}$")) {
            int position = Integer.parseInt(CommonTools.takeNameOutOfCommand(address, "--monster"));
            selectOpponentMonster(position);
        } else if (address.matches("^(?:(?:--spell|--opponent)( (\\d+))* ?){3}$")) {
            int position = Integer.parseInt(CommonTools.takeNameOutOfCommand(address, "--spell"));
            selectOpponentSpell(position);
        } else if (address.matches("^(?:(?:--field|--opponent)( (\\d+))* ?){2}$"))
            selectOpponentField();
    }

    private boolean isAddressValid(String address) {
        if (!address.matches("^(?:(?:--monster|--spell|--field|--hand|--opponent)( (\\d+))* ?){1,3}$"))
            return false;
        String monster = CommonTools.takeNameOutOfCommand(address, "--monster");
        String spell = CommonTools.takeNameOutOfCommand(address, "--spell");
        String field = CommonTools.takeNameOutOfCommand(address, "--field");
        String hand = CommonTools.takeNameOutOfCommand(address, "--hand");
        String opponent = CommonTools.takeNameOutOfCommand(address, "--opponent");
        if ((monster == null && spell == null && field != null && hand == null) || opponent != null) return false;
        if (monster != null) {
            if (Integer.parseInt(monster) > 5 || Integer.parseInt(monster) < 1) return false;
        }
        if (spell != null) {
            if (Integer.parseInt(spell) > 5 || Integer.parseInt(spell) < 1) return false;
        }
        if (hand != null) {
            if (address.contains("--opponent")) return false;
            if (Integer.parseInt(hand) > gameDecks.get(turn).getInHandCards().size() || Integer.parseInt(hand) < 1)
                return false;
        }
        return true;
    }

    private void selectMonster(int position) {
        selectedCardIndex = position;
        selectedDeck = "monster";
    }

    private void selectSpell(int position) {
        selectedCardIndex = position;
        selectedDeck = "spell";
    }

    private void selectField() {
        selectedCardIndex = -1;
        selectedDeck = "field";
    }

    private void selectHand(int position) {
        ArrayList<Card> inHandCards = gameDecks.get(turn).getInHandCards();
        if (inHandCards.get(position - 1) == null) {
            System.out.println("no card found in the given position");
            return;
        }
        selectedCard = inHandCards.get(position);
        selectedCardIndex = position;
        selectedDeck = "hand";
    }

    private void selectOpponentMonster(int position) {
        selectedCardIndex = position;
        selectedDeck = "opponentMonster";
    }

    private void selectOpponentSpell(int position) {
        selectedCardIndex = position;
        selectedDeck = "opponentSpell";
    }

    private void selectOpponentField() {
        selectedCardIndex = -1;
        selectedDeck = "opponentField";
    }

    private void summonMonster() {
        int position = selectedCardIndex;
        ArrayList<Card> inHandCards = gameDecks.get(turn).getInHandCards();
        if (selectedCard == null){
            System.out.println("no card is selected yet");
            return;
        }
        if (!selectedDeck.equals("hand") || !inHandCards.get(position - 1).getType().equals("Monster")){ //TODO check is Type correct
            System.out.println("you can’t summon this card");
            return;
        }
        if (!isSummonAndSetValid(position)) return;
        Monster selectedMonster = (Monster) selectedCard;
        if (selectedMonster.getLevel() <= 4) {
            System.out.println("summoned successfully");
            isSummoned = 1;
            gameDecks.get(turn).summonCardToMonsterZone(selectedCard.getName());
            gameDecks.get(turn).getInHandCards().remove(position - 1);
        } else if (selectedMonster.getLevel() == 5 || selectedMonster.getLevel() == 6) summonWithOneTribute(position);
        else if (selectedMonster.getLevel() == 7 || selectedMonster.getLevel() == 8) summonWithTwoTribute(position);
    }

    private void summonWithOneTribute(int position) {
        HashMap<Integer, MonsterZone> monsterZones = gameDecks.get(turn).getMonsterZones();
        int numberOfEmptyMonsterZones = 0;
        for (int i = 1; i <= 5; i++) {
            if (monsterZones.get(i).getCurrentMonster() == null)
                numberOfEmptyMonsterZones = numberOfEmptyMonsterZones + 1;
        }
        if (numberOfEmptyMonsterZones > 0) {
            System.out.println("there are not enough cards for tribute");
            return;
        }
        System.out.println("enter position of tribute monster in monster zone:");
        int monsterZonePosition = CommonTools.scan.nextInt();
        CommonTools.scan.nextLine();
        if (monsterZonePosition < 1 || monsterZonePosition > 5) {
            System.out.println("there no monsters on this address");
            return;
        }
        if (monsterZones.get(monsterZonePosition).getCurrentMonster() == null) {
            System.out.println("there no monsters on this address");
            return;
        }
        System.out.println("summoned successfully");
        isSummoned = 1;
        gameDecks.get(turn).tributeCardFromMonsterZone(monsterZonePosition);
        gameDecks.get(turn).summonCardToMonsterZone(selectedCard.getName());
        gameDecks.get(turn).getInHandCards().remove(position - 1);
    }

    private void summonWithTwoTribute(int position) {
        HashMap<Integer, MonsterZone> monsterZones = gameDecks.get(turn).getMonsterZones();
        int numberOfEmptyMonsterZones = 0;
        for (int i = 1; i <= 5; i++) {
            if (monsterZones.get(i).getCurrentMonster() == null)
                numberOfEmptyMonsterZones = numberOfEmptyMonsterZones + 1;
        }
        if (numberOfEmptyMonsterZones > 1) {
            System.out.println("there are not enough cards for tribute");
            return;
        }
        System.out.println("enter position of tribute monster in monster zone:");
        int firstMonster = CommonTools.scan.nextInt();
        CommonTools.scan.nextLine();
        int secondMonster = CommonTools.scan.nextInt();
        CommonTools.scan.nextLine();
        if (firstMonster < 1 || firstMonster > 5 || secondMonster < 1 || secondMonster > 5) {
            System.out.println("there are no monsters on one of this addresses");
            return;
        }
        if (monsterZones.get(firstMonster).getCurrentMonster() == null ||
                monsterZones.get(firstMonster).getCurrentMonster() == null) {
            System.out.println("there are no monsters on one of this addresses");
            return;
        }
        System.out.println("summoned successfully");
        isSummoned = 1;
        gameDecks.get(turn).tributeCardFromMonsterZone(firstMonster);
        gameDecks.get(turn).tributeCardFromMonsterZone(secondMonster);
        gameDecks.get(turn).summonCardToMonsterZone(selectedCard.getName());
        gameDecks.get(turn).getInHandCards().remove(position - 1);
    }

    private void set(){
        int position = selectedCardIndex;
        ArrayList<Card> inHandCards = gameDecks.get(turn).getInHandCards();
        if (selectedCard == null){
            System.out.println("no card is selected");
            return;
        }
        if (!selectedDeck.equals("hand")){
            System.out.println("you can’t set this card");
            return;
        }
        if (!inHandCards.get(position - 1).getType().equals("Monster") &&
                !inHandCards.get(position - 1).getType().equals("Spell")){ //TODO check is Type correct
            System.out.println("you can’t set this card");
            return;
        }
        if (inHandCards.get(position - 1).getType().equals("Monster")) setMonster(position);
        else if (inHandCards.get(position - 1).getType().equals("Spell")) setSpell();
    }

    private void setMonster(int position) {
        if (!isSummonAndSetValid(position)) return;
        System.out.println("set successfully");
        gameDecks.get(turn).setCardToMonsterZone(selectedCard.getName());
        gameDecks.get(turn).getInHandCards().remove(position - 1);
    }

    private boolean isSummonAndSetValid(int position) {
        HashMap<Integer, MonsterZone> monsterZones = gameDecks.get(turn).getMonsterZones();
        if (phase != Phase.main1 && phase != Phase.main2) {
            System.out.println("action not allowed in this phase");
            return false;
        }
        if (!monsterZones.containsValue(null)) {
            System.out.println("monster card zone is full");
            return false;
        }
        if (isSummoned == 1) {
            System.out.println("you already summoned/set on this turn");
            return false;
        }
        return true;
    }

    private void setPositionMonster(String command){
        HashMap<Integer, MonsterZone> monsterZones = gameDecks.get(turn).getMonsterZones();
        if (!isSetPositionValid()) return;
        if (command.matches("set --position attack")){
            if (!monsterZones.get(selectedCardIndex).getStatus().equals("DO")){
                System.out.println("this card is already in the wanted position");
                return;
            }
            if (selectedMonsterCardIndex != -1){
                System.out.println("you already changed this card position in this turn");
                return;
            }
            Card card = monsterZones.get(selectedCardIndex).getCurrentMonster();
            gameDecks.get(turn).getMonsterZones().get(selectedCardIndex).setCardAttack(card);
        }
        else {
            if (!monsterZones.get(selectedCardIndex).getStatus().equals("OO")){
                System.out.println("this card is already in the wanted position");
                return;
            }
            if (selectedMonsterCardIndex != -1){
                System.out.println("you already changed this card position in this turn");
                return;
            }
            Card card = monsterZones.get(selectedCardIndex).getCurrentMonster();
            gameDecks.get(turn).getMonsterZones().get(selectedCardIndex).setCardDefense(card);
        }
        System.out.println("monster card position changed successfully");
        selectedMonsterCardIndex = selectedCardIndex;
    }

    private boolean isSetPositionValid(){
        if (selectedCard == null){
            System.out.println("no card is selected");
            return false;
        }
        if (!selectedDeck.equals("monster")){
            System.out.println("you can’t change this card position");
            return false;
        }
        if (phase != Phase.main1 && phase != Phase.main2) {
            System.out.println("action not allowed in this phase");
            return false;
        }
        return true;
    }
    
    private void flipSummon(){
        HashMap<Integer, MonsterZone> monsterZones = gameDecks.get(turn).getMonsterZones();
        if (!isFlipSummonValid()) return;
        if (isSummoned != 0 || !monsterZones.get(selectedCardIndex).getStatus().equals("DH")){
            System.out.println("you can’t flip summon this card");
            return;
        }
        Card card = monsterZones.get(selectedCardIndex).getCurrentMonster();
        gameDecks.get(turn).getMonsterZones().get(selectedCardIndex).setCardAttack(card);
        System.out.println("flip summoned successfully");
    }

    private boolean isFlipSummonValid(){
        if (selectedCard == null){
            System.out.println("no card is selected");
            return false;
        }
        if (!selectedDeck.equals("monster")){
            System.out.println("you can’t change this card position");
            return false;
        }
        if (phase != Phase.main1 && phase != Phase.main2) {
            System.out.println("action not allowed in this phase");
            return false;
        }
        return true;
    }

    private void whichCommand(String input, GameDeck playerDeck, GameDeck enemyDeck) {

    }

    private void setPhase(String newPhase, GameDeck playerDeck) {

    }

    private void addToHand(String cardName, GameDeck playerDeck) {

    }

    private void standByPhase(GameDeck playerDeck) {

    }

    private void mainPhase1(GameDeck playerDeck) {

    }

    private void endPhase(GameDeck playerDeck) {

    }

    private void summon(GameDeck playerDeck) {

    }

    //    private String checkSummonCard(Card card, GameDeck playerDeck)
//    {
//
//    }
//
//    private String checkAttributeCard(Card card, GameDeck playerDeck)
//    {
//
//    }
//
//    private String checkSetCard(Card card, GameDeck playerDeck)
//    {
//
//    }
//
//    private void flipSummon(GameDeck playerDeck)
//    {
//
//    }
//
    private void attackCard(String command) {
        int selectDefender;
        Matcher matcher = CommonTools.getMatcher(command, "(\\d)");
        matcher.find();
        selectDefender = Integer.parseInt(matcher.group(1));
        GameDeck myDeck = gameDecks.get(turn);
        GameDeck enemyDeck = gameDecks.get(changeTurn(turn));
        if (selectedCard == null) {
            System.out.println("no card is selected yet");
            return;
        } else if (!(selectedCard instanceof Monster)) {
            System.out.println("you can’t attack with this card");
            return;
        } else if (phase != Phase.battle) {
            System.out.println("you can’t do this action in this phase");
            return;
        } else if (myDeck.getMonsterZones().get(selectedCardIndex).getHasAttackedThisRound()) {
            System.out.println("this card already attacked");
            return;
        } else if (enemyDeck.getMonsterZones().get(selectDefender).isEmpty()) {
            System.out.println("there is no card to attack here");
            return;
        } else if (enemyDeck.getMonsterZones().get(selectDefender).getStatus().equals("OO")) {
            attackOO(selectDefender, myDeck, enemyDeck);
        } else if (enemyDeck.getMonsterZones().get(selectDefender).getStatus().equals("DO")) {
            attackDO(selectDefender, myDeck, enemyDeck);
        } else if (enemyDeck.getMonsterZones().get(selectDefender).getStatus().equals("DH")) {
            attackDH(selectDefender, myDeck, enemyDeck);
        }
    }

    public void attackOO(int selectDefender, GameDeck myDeck, GameDeck enemyDeck) {

        Monster selectedMonster = (Monster) selectedCard;
        // TODO: 2021-05-10 Do Effect
        int attackerDamage = selectedMonster.getAttackPoint();
        int defenderDamage = ((Monster) enemyDeck.getMonsterZones()
                .get(selectDefender).getCurrentMonster()).getAttackPoint();
        int damage = attackerDamage - defenderDamage;
        if (damage > 0) {
            Card card = enemyDeck.getMonsterZones().get(selectDefender).removeCard();
            enemyDeck.getGraveyardCards().add(card);
            enemyDeck.takeDamage(damage);
            // TODO: 2021-05-08 check if dead
            System.out.printf("your opponent’s monster is destroyed and your opponent receives"
                    + " %d battle damage\n", damage);
        } else if (damage == 0) {
            Card card = myDeck.getMonsterZones().get(selectedCardIndex).removeCard();
            myDeck.getGraveyardCards().add(card);
            card = enemyDeck.getMonsterZones().get(selectDefender).removeCard();
            enemyDeck.getGraveyardCards().add(card);
            System.out.println("both you and your opponent monster cards are destroyed and no" +
                    "one receives damage\n");
        } else {
            Card card = myDeck.getMonsterZones().get(selectedCardIndex).removeCard();
            myDeck.getGraveyardCards().add(card);
            myDeck.takeDamage(damage);
            // TODO: 2021-05-08 check if dead
            System.out.printf("Your monster card is destroyed and you received %d battle" +
                    "damage", damage);
        }
    }

    public void attackDO(int selectDefender, GameDeck myDeck, GameDeck enemyDeck) {
        Monster selectedMonster = (Monster) selectedCard;
        // TODO: 2021-05-10 Do Effect
        int attackerDamage = selectedMonster.getAttackPoint();
        int defenderDamage = ((Monster) enemyDeck.getMonsterZones().get(selectDefender)
                .getCurrentMonster()).getDefensePoint();
        int damage = attackerDamage - defenderDamage;
        if (damage > 0) {
            Card card = enemyDeck.getMonsterZones().get(selectDefender).removeCard();
            enemyDeck.getGraveyardCards().add(card);
            System.out.println("the defense position monster is destroyed");
        } else if (attackerDamage == defenderDamage) {
            System.out.println("no card is destroyed");
        } else {
            myDeck.takeDamage(damage);
            // TODO: 2021-05-08 check if dead
            System.out.printf("no card is destroyed and you received %d battle damage\n", damage);
        }
    }

    public void attackDH(int selectDefender, GameDeck myDeck, GameDeck enemyDeck) {
        Monster selectedMonster = (Monster) selectedCard;
        String enemyCardName = enemyDeck.getMonsterZones()
                .get(selectDefender).getCurrentMonster().getName();
        // TODO: 2021-05-10 Do Effect
        int attackerDamage = selectedMonster.getAttackPoint();
        int defenderDamage = ((Monster) enemyDeck.getMonsterZones().get(selectDefender)
                .getCurrentMonster()).getDefensePoint();
        int damage = attackerDamage - defenderDamage;
        if (damage > 0) {
            Card card = enemyDeck.getMonsterZones().get(selectDefender).removeCard();
            enemyDeck.getGraveyardCards().add(card);
            System.out.printf("the defense position monster (%s) is destroyed\n", enemyCardName);
        } else if (damage == 0) {
            System.out.printf("enemy card was %s no card is destroyed\n", enemyCardName);
        } else {
            myDeck.takeDamage(defenderDamage - attackerDamage);
            // TODO: 2021-05-08 check if dead
            System.out.printf("enemy card was %s no card is destroyed and you received %d battle damage\n"
                    , enemyCardName, damage);
        }
    }

    public void directAttack() {
        GameDeck myDeck = gameDecks.get(turn);
        GameDeck enemyDeck = gameDecks.get(changeTurn(turn));
        if (selectedCard == null) {
            System.out.println("no card is selected yet");
        } else if (!(selectedCard instanceof Monster)) {
            System.out.println("you can’t attack with this card");
        } else if (phase != Phase.battle) {
            System.out.println("you can’t do this action in this phase");
        } else if (myDeck.getMonsterZones().get(selectedCardIndex).getHasAttackedThisRound()) {
            System.out.println("this card already attacked");
        } else {
            Monster selectedMonster = (Monster) selectedCard;
            // TODO: 2021-05-10 Do Effect
            int attackerDamage = selectedMonster.getAttackPoint();
            enemyDeck.takeDamage(attackerDamage);
            // TODO: 2021-05-08 check if dead
            System.out.println("you opponent receives " + attackerDamage + " battle damage");
        }
    }

    private void activateSpellErrorCheck() {
        if (selectedCard == null) {
            System.out.println("no card is selected yet");
            return;
        } else if (!(selectedCard instanceof Spell)) {
            System.out.println("activate effect is only for spell cards.");
            return;
        } else if (!(phase == Phase.main1 || phase == Phase.main2)) {   // TODO: 2021-05-10 check if fast
            System.out.println("you can’t activate an effect on this turn");
            return;
        } //else if () TODO check if activated
        else if (gameDecks.get(turn).isSpellZoneFull()) {
            System.out.println("spell card zone is full");
            return;
        } //else if () TODO check if ready to be played
        System.out.println("spell activated");
        // TODO: 2021-05-10 set on board -> use spellZoneFirstFreeSpace(); or set on fieldZone
        // TODO: 2021-05-10 Use spell
    }

    private void setSpell() {
        GameDeck myDeck = gameDecks.get(turn);
        if (selectedCard == null) {
            System.out.println("no card is selected yet");
        } //else if () TODO check if is in hand
        else if (!(phase == Phase.main1 || phase == Phase.main2)) {   // TODO: 2021-05-10 check if fast
            System.out.println("you can’t do this action in this phase");
        } else if (myDeck.isSpellZoneFull()) {
            System.out.println("spell card zone is full");
        } else {
            System.out.println("set successfully");
            int freeIndex = myDeck.spellZoneFirstFreeSpace();
            SpellZone spellZone = myDeck.getSpellZones().get(freeIndex);
            spellZone.setSpell(selectedCard);
            spellZone.setHidden();
        }
    }

    private void showGraveyard(int turn) {
        GameDeck gameDeck;
        if (turn == 0)
            gameDeck = gameDecks.get(0);
        else
            gameDeck = gameDecks.get(1);
        ArrayList<Card> graveyardCards = gameDeck.getGraveyardCards();
        if (graveyardCards.size() == 0)
            System.out.println("graveyard empty");
        else {
            for (int i = 0; i < graveyardCards.size(); i++) {
                Card card = graveyardCards.get(i);
                System.out.println(++i + ". " + card);
            }
        }
        while (true) {
            String command = CommonTools.scan.nextLine().trim();
            if (command.equals("back")) return;
            System.out.println("invalid command");
        }
    }

    private void cardShow() {
        HashMap<Integer, MonsterZone> monsterZones = gameDecks.get(changeTurn(turn)).getMonsterZones();
        if (selectedCard == null) {
            System.out.println("no card is selected yet");
        } else if (monsterZones.get(selectedCardIndex).getStatus().equals("DH")) {
            System.out.println("card is not visible");
        } else {
            System.out.println(selectedCard);
        }
    }

    private void surrender(int turn) {
        gameDecks.get(turn).setPlayerLP(0);
        gameDecks.get(changeTurn(turn)).increaseWinRounds();
    }

    private void increasePlayerLPCheat(String command) {
        Matcher matcher = CommonTools.getMatcher(command, "^increase --LP (\\d+)$");
        matcher.find();
        int amountOfLP = Integer.parseInt(matcher.group(1));
        GameDeck myDeck = gameDecks.get(turn);
        myDeck.increaseLP(amountOfLP);
    }

    private void setWinnerCheat(String command) {
        Matcher matcher = CommonTools.getMatcher(command, "^duel set-winner (\\S+)$");
        matcher.find();
        String playerNickname = matcher.group(1);
        if (gameDecks.get(0).getPlayerNickName().equals(playerNickname)) {
            surrender(1);
        } else if (gameDecks.get(1).getPlayerNickName().equals(playerNickname)) {
            surrender(0);
        } else System.out.println("There is no player with this nickname!");
    }

    private void roundOver(int turn) { // 0 : firstPlayer losses , 1 : secondPlayer losses
        GameDeck firstDeck = gameDecks.get(0);
        GameDeck secondDeck = gameDecks.get(1);
        firstDeck.addPlayerLPAfterRound();
        secondDeck.addPlayerLPAfterRound();
        firstDeck.setPlayerLP(8000);
        secondDeck.setPlayerLP(8000);
        if (turn == 0) secondDeck.increaseWinRounds();
        else firstDeck.increaseWinRounds();
    }

    private void gameOver(int round) {
        String winnerUsername = "";
        int firstMoney = 0;
        int secondMoney = 0;
        int firstScore = gameDecks.get(0).getWinRounds() * 1000;
        int secondScore = gameDecks.get(1).getWinRounds() * 1000;
        if (round == 1) {
            if (gameDecks.get(0).getWinRounds() == 1) {
                firstMoney = 1000 + gameDecks.get(0).getMaxPlayerLPAfterRounds();
                secondMoney = 100;
                winnerUsername = gameDecks.get(0).getPlayerUserName();
            } else {
                firstMoney = 100;
                secondMoney = 1000 + gameDecks.get(0).getMaxPlayerLPAfterRounds();
                winnerUsername = gameDecks.get(1).getPlayerUserName();
            }
        } else if (round == 3) {
            if (gameDecks.get(0).getWinRounds() == 2) {
                firstMoney = 3000 + (3 * gameDecks.get(0).getMaxPlayerLPAfterRounds());
                secondMoney = 300;
                winnerUsername = gameDecks.get(0).getPlayerUserName();
            } else {
                firstMoney = 300;
                secondMoney = 3000 + (3 * gameDecks.get(0).getMaxPlayerLPAfterRounds());
                winnerUsername = gameDecks.get(1).getPlayerUserName();
            }
        }
        Player firstPlayer = Player.getPlayerByUsername(gameDecks.get(0).getPlayerUserName());
        Player secondPlayer = Player.getPlayerByUsername(gameDecks.get(1).getPlayerUserName());
        firstPlayer.increaseScore(firstScore);
        secondPlayer.increaseScore(secondScore);
        firstPlayer.increaseMoney(firstMoney);
        secondPlayer.increaseMoney(secondMoney);
        System.out.println(winnerUsername + " won the game and the score is: "
                + firstScore + "-" + secondScore);
    }

    private int changeTurn(int turn) {
        if (turn == 1)
            return 0;
        return 1;
    }

    private void changeGameTurn() {
        selectedCard = null;
        isSummoned = 0;
        selectedCardIndex = -1;
        selectedMonsterCardIndex = -1;
        selectedDeck = null;
        turn = changeTurn(turn);
    }
}
