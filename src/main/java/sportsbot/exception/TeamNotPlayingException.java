package sportsbot.exception;

import sportsbot.enums.TemporalContext;
import sportsbot.model.QuestionContext;
import sportsbot.model.Team;

/**
 * Created by devondapuzzo on 5/10/17.
 */
public class TeamNotPlayingException extends SportsBotException {
    private Team team;

    public TeamNotPlayingException(Team team){
        this.team = team;
    }

    @Override
    public void setErrorMessage(QuestionContext questionContext){
        if(questionContext.getTemporalContext() == TemporalContext.TODAY){
            questionContext.setResponse("It looks like the "+questionContext.getTeam().getName()+" aren't playing today.");
        }else if(questionContext.getTemporalContext() == TemporalContext.YESTERDAY){
            questionContext.setResponse("It looks like the "+questionContext.getTeam().getName()+" didn't play yesterday.");
        }else if(questionContext.getTemporalContext() == TemporalContext.TOMORROW) {
            questionContext.setResponse("It looks like the "+questionContext.getTeam().getName()+" aren't playing tomorrow.");
        }else {
            questionContext.setResponse("It looks like the "+questionContext.getTeam().getName()+" aren't playing on " + questionContext.getTemporalContext().getDateString() + ".");
        }
    }

    public Team getTeam() {
        return team;
    }
}
