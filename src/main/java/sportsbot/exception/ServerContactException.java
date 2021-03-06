package sportsbot.exception;

import sportsbot.model.QuestionContext;

/**
 * Created by devondapuzzo on 5/10/17.
 */
public class ServerContactException extends SportsBotException {
    @Override
    public void setErrorMessage(QuestionContext questionContext) {
        questionContext.setResponse("ERROR contacting our data source. Try again later.");
    }
}
