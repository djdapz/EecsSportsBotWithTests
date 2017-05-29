package sportsbot.exception;

import sportsbot.model.QuestionContext;

/**
 * Created by devondapuzzo on 5/29/17.
 */
public class PositionNotFoundException extends SportsBotException {
    @Override
    public void setErrorMessage(QuestionContext questionContext) {
        questionContext.setResponse("We couldn't find the position you were looking for, sorry!");
    }
}
