package sportsbot.exception;

import sportsbot.model.City;
import sportsbot.model.QuestionContext;

/**
 * Created by devondapuzzo on 5/10/17.
 */
public class AmbiguousTeamException extends SportsBotException{
    //We were able to parse out a city, but not able to decide which team is in question
    //Possibiliies
        //How did chicago do? --> no sport context
        //How did New York do? --> sports context == baseball  (unclear if Mets or Yankees)

    City city;

    @Override
    public void setErrorMessage(QuestionContext questionContext) {
        questionContext.setResponse("AmbiguousTeamException");
    }

    public AmbiguousTeamException(City city){
        this.city = city;
    }

    public City getCity() {
        return city;
    }
}
