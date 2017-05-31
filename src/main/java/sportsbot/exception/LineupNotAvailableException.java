package sportsbot.exception;

import sportsbot.model.QuestionContext;

/**
 * Created by devondapuzzo on 5/31/17.
 */
public class LineupNotAvailableException extends SportsBotException {
    @Override
    public void setErrorMessage(QuestionContext questionContext) {
        questionContext.setResponse("Sorry, our data source does not have this information.");
    }
}
