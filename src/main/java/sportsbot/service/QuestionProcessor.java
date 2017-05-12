package sportsbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportsbot.exception.*;
import sportsbot.model.Game;
import sportsbot.model.QuestionContext;
import sportsbot.model.Roster;
import sportsbot.model.Team;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by devondapuzzo on 4/24/17.
 */

@Service
public class QuestionProcessor {


    @Autowired
    private RosterService rosterService;

    @Autowired
    private QuestionParser questionParser;

    private SportsApiService sportsApiRequester = new SportsApiService();

    private Roster roster;
    private HashMap<String, Team> teams;

    public QuestionContext answer(QuestionContext questionContext) {
        //update every time?

        try {
            questionParser.parse(questionContext);
        } catch (AmbiguousTeamException e) {
            //TODO ACTUALY FOLLOW UP!!!
            questionContext.setResponse("Sorry, I'm not sure which team from " + e.getCity().getName() + " you're asking about");
        } catch (TeamNotFoundException e) {
            questionContext.setResponse("I couldn't find the team you were asking about");
            return questionContext;
        }

        LinkedHashMap todaysGameMap = null;

        try {
            todaysGameMap = sportsApiRequester.getTodaysGame(questionContext);
        } catch (SportsBotException e) {
            e.setErrorMessage(questionContext);
            return questionContext;
        }

        Game thisGame = new Game();

        roster = rosterService.getRoster(questionContext.getSport());
        thisGame.buildFromScoreboard(todaysGameMap, roster);

        questionContext.setResponse(thisGame.generateGameStatusString(questionContext));

        return questionContext;

    }


    public QuestionProcessor() {
    }
}
