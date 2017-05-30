package sportsbot.model.stats;

import sportsbot.model.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by devondapuzzo on 5/28/17.
 */
public class PlayerGameStatsBaseball extends PlayerGameStatsAbstract{
    public PlayerGameStatsBaseball(Player player) {
        super(player);
    }

    @Override
    public String getDefensiveString() {
        HashMap<String, PlayerStat> defensiveStats = this.getCategorizedStats().get("Fielding");
        ArrayList<String> scoringItems = new ArrayList<>();

        StringBuilder outputBuilder = new StringBuilder();

        outputBuilder
                .append(" He played ")
                .append(defensiveStats.get(Constants.INNINGS_PLAYED).getValue())
                .append(" innings")
                .append(getConditionalPhrase(defensiveStats, Constants.ERRORS, "error", " and got "))
                .append(".");

        scoringItems.add(getConditionalPhrase(defensiveStats, Constants.FIELDER_PUT_OUTS, "put out"));
        scoringItems.add(getConditionalPhrase(defensiveStats, Constants.FIELDER_TAG_OUTS, "tag out"));
        scoringItems.add(getConditionalPhrase(defensiveStats, Constants.DOUBLE_PLAYS, "double play"));

        outputBuilder.append(processListOfPhrases(scoringItems, " He made no outs.", " He made"));

        return outputBuilder.toString();

    }

    @Override
    public String getOffensiveString(){
        HashMap<String, PlayerStat> battingStats = this.getCategorizedStats().get("Batting");

        String hittingString = getHittingString(battingStats);
        String outString = getOutString(battingStats);
        String scoringString = getScoringString(battingStats);
        String specialString = getSpecialString(battingStats);

        return getPlayer().getFirstName() + " " + getPlayer().getLastName() + " "  + hittingString + " "+ scoringString + " "+ outString + " "+ specialString;
    }

    private String getSpecialString(HashMap<String, PlayerStat> battingStats) {
        String specialString = "";
        if(battingStats.get(Constants.HOME_RUNS).getValue() > 0){
            Integer hr = battingStats.get(Constants.HOME_RUNS).getValue();
            if(hr == 1){
                specialString += " He scored a home run.";

            }else{
                specialString += "He scored " + hr + "home runs.";

            }
        }

        if(battingStats.get(Constants.STOLEN_BASES).getValue() > 0){
            Integer bases = battingStats.get(Constants.STOLEN_BASES).getValue();
            if(bases == 1){
                specialString += " He stole a base.";

            }else{
                specialString += "He stole " + bases + "bases.";

            }
        }

        if(battingStats.get(Constants.BATTER_HIT_BY_PITCH).getValue() > 0){
            specialString += " He was hit by a pitch.";
        }

        return specialString;
    }

    public String getScoringString(HashMap<String, PlayerStat> battingStats) {

        ArrayList<String> scoringItems = new ArrayList<>();
        scoringItems.add(getConditionalPhrase(battingStats, Constants.RBI, "RBI"));
        scoringItems.add(getConditionalPhrase(battingStats, Constants.RUNS, "run"));

        return processListOfPhrases(scoringItems, "He never scored.", "He got");

    }

    public String getHittingString(HashMap<String, PlayerStat> battingStats) {
        return "was at bat " + battingStats.get(Constants.AT_BATS).getValue() + " times and got " + battingStats.get(Constants.HITS).getValue()+" hits.";
    }

    public String getOutString(HashMap<String, PlayerStat> battingStats) {
        int numOuts = 0;

        ArrayList<String> outItems = new ArrayList<>();

        outItems.add(getConditionalPhrase(battingStats, Constants.BATTER_STRIKEOUT, "force out"));
        outItems.add(getConditionalPhrase(battingStats, Constants.BATTER_GROUND_OUTS, "ground out"));
        outItems.add(getConditionalPhrase(battingStats, Constants.BATTER_PUT_OUTS, "put out"));
        outItems.add(getConditionalPhrase(battingStats, Constants.BATTER_TAG_OUTS, "tag out"));
        outItems.add(getConditionalPhrase(battingStats, Constants.BATTER_SACRIFICE_BUNTS, "sacrifice bunt"));
        outItems.add(getConditionalPhrase(battingStats, Constants.BATTER_FORCE_OUTS, "force out"));
        outItems.add(getConditionalPhrase(battingStats, Constants.BATTER_SAC_FLIES, "sacrifice fly"));

        return processListOfPhrases(outItems, "He never got out!", "He got");
    }

    private String processListOfPhrases(ArrayList<String> phrases, String zeroPhrase, String actionPhrase){

        for(int i =  phrases.size() -1; i >= 0; i--){
            if(phrases.get(i).equals("")){
                phrases.remove(phrases.get(i));
            }
        }


        if(phrases.size() == 0){
            return zeroPhrase;
        }else if(phrases.size() == 1){
            return " " + actionPhrase + phrases.get(0);
        }else{
            StringBuilder outString = new StringBuilder().append(actionPhrase);
            for(int i = 0; i < phrases.size() -1; i ++){
                outString.append(phrases.get(i));
                outString.append(",");
            }
            outString.append(" and");
            outString.append(phrases.get(phrases.size() -1));
            outString.append(".");
            return outString.toString();
        }
    }


    private String getConditionalPhrase(HashMap<String, PlayerStat> battingStats, String key, String title, String prefix){
        String cond = getConditionalPhrase(battingStats,  key,  title);
        if(cond.length() > 0){
            return prefix + cond;
        }else{
            return "";
        }
    }
    private String getConditionalPhrase(HashMap<String, PlayerStat> battingStats, String key, String title){
        StringBuilder outString = new StringBuilder();

        if(battingStats.get(key).getValue() > 0){
            Integer value = battingStats.get(key).getValue();
            outString.append(" ");
            outString.append(value);
            outString.append(" ");
            outString.append(title);
            if(value > 1){
                outString.append("s");
            }
        }

        return outString.toString();
    }



    private class Constants{
        //OFFENSIVE
        public static final String AT_BATS = "AB";
        public static final String HITS = "AB";

        public static final String ON_BASE_PERCENTAGE = "OBP";

        public static final String HOME_RUNS = "HR";
        public static final String RBI = "RBI";


        public static final String EARNED_RUNS = "ER";
        public static final String BATTER_HIT_BY_PITCH = "HBP";

        public static final String BATTER_WALKS = "BB";
        public static final String BATTER_INTENTIONAL_WALKS = "IBB";

        public static final String STOLEN_BASES = "SB";
        public static final String RUNS = "R";

        public static final String BATTER_STRIKEOUT = "SO";
        public static final String BATTER_GROUND_OUTS = "GO";
        public static final String BATTER_PUT_OUTS = "BPO";
        public static final String BATTER_TAG_OUTS = "BTO";
        public static final String BATTER_SACRIFICE_BUNTS = "SAC";
        public static final String BATTER_FORCE_OUTS = "BFO";
        public static final String BATTER_SAC_FLIES = "SF";

        //DEFENSIVE
        public static final String FIELDER_TAG_OUTS = "FTO";
        public static final String FIELDER_PUT_OUTS = "FPO";

        public static final String ERRORS = "E";
        public static final String INNINGS_PLAYED = "INN";
        public static final String DOUBLE_PLAYS = "FDP";

    }

}
