package sportsbot.exception;

import sportsbot.model.QuestionContext;

/**
 * Created by devondapuzzo on 5/29/17.
 */
public class PositionNotFoundException extends SportsBotException {
    @Override
    public void setErrorMessage(QuestionContext questionContext) {
        if(questionContext.getTemporalContext().getOffset() > 2){
            questionContext.setResponse("That game is " + questionContext.getTemporalContext().getOffset()+" days away. Check back closer to the game for information!");

        }
        questionContext.setResponse("We couldn't find the position you were looking for, sorry!");
    }
}
