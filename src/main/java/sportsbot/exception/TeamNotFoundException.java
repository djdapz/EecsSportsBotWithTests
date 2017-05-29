package sportsbot.exception;

import sportsbot.model.QuestionContext;

/**
 * Created by devondapuzzo on 5/10/17.
 */
public class TeamNotFoundException extends SportsBotException {
    @Override
    public void setErrorMessage(QuestionContext questionContext) {
        questionContext.setResponse("I couldn't find the team you were asking about");
    }
}
