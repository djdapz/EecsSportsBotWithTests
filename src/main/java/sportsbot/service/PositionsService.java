package sportsbot.service;

import org.springframework.stereotype.Service;
import sportsbot.enums.Sport;
import sportsbot.exception.PositionNotFoundException;
import sportsbot.model.QuestionContext;
import sportsbot.model.position.Position;
import sportsbot.model.position.Positions;
import sportsbot.model.position.PositionsBaseball;

import java.util.HashMap;

/**
 * Created by devondapuzzo on 5/28/17.
 */

@Service
public class PositionsService {
    private HashMap<Sport, Positions> positionsHashMap = new HashMap<>();

    public PositionsService(){
        positionsHashMap.put(Sport.BASEBALL, new PositionsBaseball());
    }

    public Position findPosition(QuestionContext  questionContext)  throws PositionNotFoundException{
        return positionsHashMap.get(questionContext.getSport()).searchQueryForPosition(questionContext.getQuestion().toLowerCase());
    }

    public Position findPosition(Sport sport, String query) throws PositionNotFoundException {
        return positionsHashMap.get(sport).searchQueryForPosition(query);
    }
}
