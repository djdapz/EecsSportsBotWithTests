package sportsbot.exception;

import sportsbot.model.QuestionContext;

/**
 * Created by devondapuzzo on 5/10/17.
 */
public abstract class SportsBotException extends Exception {
    public abstract void setErrorMessage(QuestionContext questionContext);
}
