package sportsbot.exception;

import sportsbot.enums.GameStatus;
import sportsbot.enums.TemporalContext;
import sportsbot.model.QuestionContext;

/**
 * Created by devondapuzzo on 5/28/17.
 */
public class PlayerHasntPlayedException extends SportsBotException {


    @Override
    public void setErrorMessage(QuestionContext questionContext) {
        if(questionContext.getGame() == null ){
            questionContext.setResponse("We couldn't find the player or game you're referring to, Sorry!");
        }

        GameStatus gs = questionContext.getGame().getGameStatus();
        String playerName = questionContext.getPlayer().getName();

        if(questionContext.getTemporalContext() == TemporalContext.TODAY){
            if(gs == GameStatus.SCHEDULED){
                questionContext.setResponse("It looks like " + playerName + " hasn't played yet today.");
            }else if(gs == GameStatus.INPROGRESS){
                questionContext.setResponse("It looks like " + playerName + " hasn't played yet today.");
            }else if(gs == GameStatus.COMPLETED){
                questionContext.setResponse("It looks like " + playerName + " didn't play today.");
            }
        }else if(questionContext.getTemporalContext() == TemporalContext.TOMORROW || questionContext.getTemporalContext() == TemporalContext.FUTURE){
            questionContext.setResponse("It looks like " + playerName + " isn't set to play on " + questionContext.getTemporalContext().getDateString());
        }else{
            questionContext.setResponse("It looks like " + playerName + " didn't play on " + questionContext.getTemporalContext().getDateString());
        }
    }
}
