package sportsbot.exception;

import sportsbot.model.QuestionContext;

/**
 * Created by devondapuzzo on 5/28/17.
 */
public class GameNotFoundException extends SportsBotException {
    @Override
    public void setErrorMessage(QuestionContext questionContext) {
        questionContext.setResponse("We couldn't find the game you're looking for.");
    }
}
