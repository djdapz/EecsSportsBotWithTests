package sportsbot.model;

import sportsbot.enums.QuestionType;
import sportsbot.enums.Sport;
import sportsbot.enums.TemporalContext;
import sportsbot.model.position.Position;

import java.util.Random;

/**
 * Created by devondapuzzo on 4/19/17.
 */
public class QuestionContext {
    //buying tix
    //standings stuff

    //Information Hierarchy
    private Sport sport;
    private City city;
    private Team team;
    private Player player;
    private Game game;
    private Position position;

    //Other

    private String question;
    private TemporalContext temporalContext = TemporalContext.TODAY;
    private String response;
    private QuestionContext previousQuestion;
    private final Integer id;
    private boolean clarification;
    private QuestionType questionType = QuestionType.GAME_SCORE;
    private String source;



    public void setCity(City city) {
        this.city = city;

        if(this.getTeam() != null && this.getTeam().getCity() != city){
            this.setTeam(null);
        }
    }

    public void setSport(Sport sport) {
        this.sport = sport;

        if(this.getTeam() != null && this.getTeam().getSport() != sport){
            this.setTeam(null);
        }
    }

    public void setTeam(Team team) {
        if(team == null){
            this.team = null;
            this.player = null;
        }else{
            if(this.team != team){
                this.player = null;
            }
            this.team = team;
            this.sport = team.getSport();

        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGame(Game game){
        this.game = game;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void clearSource() {
        this.source = null;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public boolean isClarification() {
        return clarification;
    }

    public void setClarification(boolean clarification) {
        this.clarification = clarification;
    }

    public City getCity() {
        return city;
    }

    public QuestionContext(Integer id) {
        this.id = id;
    }

    public QuestionContext() {
        this.id = Math.abs(new Random().nextInt());
    }

    public Integer getId() {
        return id;
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


    public Game getGame() {
        return game;
    }


    public Player getPlayer() {
        return player;
    }


    public Team getTeam() {
        return team;
    }


    public void clearTeam() {
        this.team = null;
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
                "ID: "  +  idString+ "\n";

    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
