package sportsbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportsbot.enums.QuestionType;
import sportsbot.exception.*;
import sportsbot.model.*;

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

    @Autowired
    private NewsService newsService;

    @Autowired
    private SportsApiService sportsApiRequester;

    private Roster roster;
    private HashMap<String, Team> teams;


    public QuestionContext answer(QuestionContext questionContext) throws AmbiguousTeamException{
        //update every time?

        try {
            questionParser.parse(questionContext);
        } catch (AmbiguousTeamException e) {
            //TODO ACTUALY FOLLOW UP!!!
            throw e;
        } catch (TeamNotFoundException e) {
            questionContext.setResponse("I couldn't find the team you were asking about");
            return questionContext;
        }

        LinkedHashMap todaysGameMap = null;




        if(questionContext.getQuestionType() == QuestionType.NEWS){
            Story story = newsService.getNewsStory(questionContext);
            questionContext.setResponse(story.toString());
            return questionContext;
        }else{
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
    }


    public QuestionProcessor() {
    }
}
