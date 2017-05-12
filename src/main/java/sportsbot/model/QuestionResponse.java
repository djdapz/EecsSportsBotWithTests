package sportsbot.model;

import sportsbot.enums.Sport;
import sportsbot.enums.TemporalContext;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * Created by devondapuzzo on 4/25/17.
 */
public class QuestionResponse {

    private String question;
    private String response;
    private TemporalContext temporalContext;
    private Sport sport;
    private int gameID;
    private String teamAbbr;
    private boolean error = false;
    private String errorMessage;
    private int conversationId;

    public QuestionResponse(QuestionContext questionContext) {
        this.question = questionContext.getQuestion();
        this.response = questionContext.getResponse();
        this.temporalContext = questionContext.getTemporalContext();
        this.sport = questionContext.getSport();
        this.error = questionContext.isError();
        this.errorMessage = questionContext.getErrorMessage();
        this.conversationId = questionContext.getId();

        if(questionContext.getGame() !=null){
            this.gameID = questionContext.getGame().getId();
        }

        if(questionContext.getTeam() !=null){
            this.teamAbbr = questionContext.getTeam().getAbbreviation();
        }

    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
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

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getTeamAbbr() {
        return teamAbbr;
    }

    public void setTeamAbbr(String teamAbbr) {
        this.teamAbbr = teamAbbr;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
