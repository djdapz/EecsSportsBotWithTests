package sportsbot.model;

import sportsbot.enums.Sport;
import sportsbot.enums.TemporalContext;

import java.util.Random;

/**
 * Created by devondapuzzo on 4/19/17.
 */
public class QuestionContext {

    private String question;
    private String response;
    private TemporalContext temporalContext;
    private Sport sport;
    private Game game;
    private Player player;
    private Team team;
    private QuestionContext previousQuestion;
    private boolean error = false;
    private String errorMessage;
    private final Integer id;

    public QuestionContext(Integer id) {
        this.id = id;
    }

    public QuestionContext() {
        this.id = Math.abs(new Random().nextInt());
    }

    public Integer getId() {
        return id;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public TemporalContext getTemporalContext() {
        return temporalContext;
    }

    public void setTemporalContext(TemporalContext temporalContext) {
        this.temporalContext = temporalContext;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        this.sport = team.getSport();
    }

    public QuestionContext getPreviousQuestion() {
        return previousQuestion;
    }

    public void setPreviousQuestion(QuestionContext previousQuestion) {
        this.previousQuestion = previousQuestion;
    }

    public String toString(){

        String questionString;
        String responseString;
        String temporalContextString;
        String sportString;
        String gameString;
        String playerString;
        String teamString;
        String previousQuestionString;
        String errorString;
        String errorMessageString;
        String idString;

        if(this.question!=null){
            questionString = this.question;
        }else{
            questionString = "null";
        };

        if(this.response!=null){
            responseString = this.response;
        }else{
            responseString = "null";
        };

        if(this.temporalContext!=null){
            temporalContextString = this.temporalContext.toString();
        }else{
            temporalContextString = "null";
        };

        if(this.sport!=null){
            sportString = this.sport.toString();
        }else{
            sportString = "null";
        };

        if(this.game!=null){
            gameString = this.game.toString();
        }else{
            gameString = "null";
        };

        if(this.player!=null){
            playerString = this.player.toString();
        }else{
            playerString = "null";
        };

        if(this.team!=null){
            teamString = this.team.toString();
        }else{
            teamString = "null";
        };

        if(this.previousQuestion!=null){
            previousQuestionString = this.previousQuestion.getQuestion();
        }else{
            previousQuestionString = "null";
        };


        errorString = Boolean.toString(this.error);


        if(this.errorMessage!=null){
            errorMessageString = this.errorMessage;
        }else{
            errorMessageString = "null";
        };

        if(this.id!=null){
            idString = this.id.toString();
        }else{
            idString = "null";
        };

        return
                "Question: "  + questionString + "\n"+
                "Response: "  +  responseString + "\n"+
                "TemporalContext: "  +  temporalContextString + "\n"+
                "Sport: "  +  sportString+ "\n"+
                "Game: "  +  gameString+ "\n"+
                "Player: "  +  playerString+ "\n"+
                "Team: "  +  teamString+ "\n"+
                "previousQuestion: "  +  previousQuestionString+ "\n"+
                "Error: "  +  errorString+ "\n"+
                "ErrorMessage: "  +  errorMessageString+ "\n"+
                "ID: "  +  idString+ "\n";

    }
}
