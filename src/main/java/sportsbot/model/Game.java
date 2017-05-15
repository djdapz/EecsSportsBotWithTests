package sportsbot.model;

import org.springframework.beans.factory.annotation.Autowired;
import sportsbot.enums.GameStatus;
import sportsbot.enums.TemporalContext;
import sportsbot.service.RosterService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by devondapuzzo on 4/24/17.
 */


public class Game {

    private Team homeTeam;
    private Team awayTeam;
    private String date;
    private int id;
    private int homeScore;
    private int awayScore;
    private String gameTime;
    private String location;
    private int inning;

    public String getLocation() {
        return location;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    private GameStatus gameStatus;



    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }


    public void buildFromScoreboard(LinkedHashMap todaysGameMap, Roster roster) {
        HashMap<String, Team> teams = roster.getTeams();

        LinkedHashMap gamePart = (LinkedHashMap) todaysGameMap.get("game");

        LinkedHashMap homeTeamMap = (LinkedHashMap) gamePart.get("homeTeam");
        LinkedHashMap awayTeamMap = (LinkedHashMap) gamePart.get("awayTeam");

        String homeTeamAbbr = (String) homeTeamMap.get("Abbreviation");
        String awayTeamAbbr = (String) awayTeamMap.get("Abbreviation");

        this.homeTeam = teams.get(homeTeamAbbr);
        this.awayTeam = teams.get(awayTeamAbbr);

        this.id = Integer.parseInt((String) gamePart.get("ID"));
        this.location = (String) gamePart.get("location");
        this.gameTime = (String) gamePart.get("time") + " Eastern Time";

        boolean isCompleted = Boolean.parseBoolean((String) todaysGameMap.get("isCompleted"));
        boolean isInProgress = Boolean.parseBoolean((String) todaysGameMap.get("isInProgress"));

        if(isCompleted){
            this.gameStatus = GameStatus.COMPLETED;
        }else if(isInProgress){
            this.gameStatus = GameStatus.INPROGRESS;
        }else{
            this.gameStatus = GameStatus.SCHEDULED;
        }

        //TODO - consider live access
        if(this.gameStatus == GameStatus.COMPLETED ||  this.gameStatus == GameStatus.INPROGRESS){
            this.homeScore = Integer.parseInt((String) todaysGameMap.get("homeScore"));
            this.awayScore = Integer.parseInt((String) todaysGameMap.get("awayScore"));
        }

        if(this.getGameStatus() == GameStatus.INPROGRESS){
            LinkedHashMap inningSummary = (LinkedHashMap) todaysGameMap.get("inningSummary");
            ArrayList innings = (ArrayList) inningSummary.get("inning");
            inning  = innings.size();
        }else{
            inning = 0;
        }

    }
    public String getGameTime() {
        return gameTime;
    }

    public String generateGameStatusString(QuestionContext questionContext){

        Team teamOfInterestObject = questionContext.getTeam();
        TemporalContext temporalContext = questionContext.getTemporalContext();

        String finalString;
        String teamOfInterest;
        String secondTeam;
        String locationString;
        String scoreString;

        boolean toiIsHome;

        if(teamOfInterestObject.getAbbreviation().equals(getHomeTeam().getAbbreviation())){
            teamOfInterest = this.getHomeTeam().getCity().getName()+" " +this.getHomeTeam().getName();
            secondTeam = this.getAwayTeam().getCity().getName()+" " +this.getAwayTeam().getName();
            toiIsHome = true;
        }else{
            teamOfInterest = this.getAwayTeam().getCity().getName()+" " +this.getAwayTeam().getName();
            secondTeam= this.getHomeTeam().getCity().getName()+" " +this.getHomeTeam().getName();
            toiIsHome = false;
        }

        locationString = " at " + this.getLocation() + " in " + this.getHomeTeam().getCity().getName() + " at " + this.getGameTime();

        if(temporalContext == TemporalContext.TOMORROW){
            locationString += " tomorrow";
        }else if(temporalContext == TemporalContext.TODAY){
            locationString += " today";
        }else if(temporalContext == TemporalContext.YESTERDAY){
            locationString += " yesterday";
        }


        if(this.getGameStatus() == GameStatus.SCHEDULED){
            finalString =
                    "The " + teamOfInterest + " are scheduled to play the " + secondTeam + locationString + ".";
        }else {

            if(toiIsHome){
                scoreString = Integer.toString(homeScore) + "-" + Integer.toString(awayScore);
            }else{
                scoreString = Integer.toString(awayScore) + "-" + Integer.toString(homeScore);
            }

            String verbString;
            String inningString = this.getInningString();
            String timeEnding = "";

            if(this.getGameStatus() == GameStatus.INPROGRESS){
                if((toiIsHome && homeScore > awayScore) || (!toiIsHome && awayScore > homeScore)){
                    //Team of interest won
                    verbString = " are beating the ";
                }else if ((toiIsHome && homeScore < awayScore) || (!toiIsHome && awayScore < homeScore)){
                    verbString = " are losing to the ";
                }else{
                    verbString = " are tied to the ";
                }

            }else{

                if(temporalContext == TemporalContext.TODAY){
                    timeEnding = " today";
                }else if(temporalContext == TemporalContext.YESTERDAY){
                    timeEnding = " yesterday";
                }

                if((toiIsHome && homeScore > awayScore) || (!toiIsHome && awayScore > homeScore)){
                    //Team of interest won
                    verbString = " beat the ";
                }else{
                    verbString = " lost to the ";
                }
            }

            finalString = "The " + teamOfInterest + verbString + secondTeam +" "+ scoreString+ inningString+ timeEnding+".";
        }

        return finalString;
    }

    private String getInningString(){
        if(this.inning <=0){
            return "";
        }else if(this.inning == 1){
            return " in the 1st inning";
        }else if(this.inning == 2){
            return " in the 2nd inning";
        }else if(this.inning == 3){
            return " in the 3rd inning";
        }else{
            return " in the " +Integer.toString(this.inning)+ "th inning";
        }
    }

    public String toString(){
        return  awayTeam.toString() + " vs " +homeTeam.toString();
    }

}