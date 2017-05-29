package sportsbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportsbot.enums.QuestionType;
import sportsbot.exception.AmbiguousTeamException;
import sportsbot.exception.PositionNotFoundException;
import sportsbot.exception.SportsBotException;
import sportsbot.exception.TeamNotFoundException;
import sportsbot.model.Game;
import sportsbot.model.QuestionContext;
import sportsbot.model.Roster;
import sportsbot.model.Story;

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

    @Autowired
    private LanguageService languageService;

    public QuestionContext answer(QuestionContext questionContext) throws AmbiguousTeamException {
        //update every time?

        try {
            questionParser.parse(questionContext);
        } catch (AmbiguousTeamException e) {
            //TODO ACTUALY FOLLOW UP!!!
            throw e;
        } catch (TeamNotFoundException | PositionNotFoundException e) {
            e.setErrorMessage(questionContext);
            return questionContext;
        }

        LinkedHashMap gameMap;

        if (questionContext.getQuestionType() == QuestionType.NEWS) {
            Story story = newsService.getNewsStory(questionContext);
            if (story == null) {
                questionContext.setResponse("Sorry, I couldn't find the story you're looking for. Please try again");
                questionContext.setSource(null);
                return questionContext;
<<<<<<< Updated upstream
            } else {
                questionContext.setResponse(story.getStoryString());
=======
            }else{
                questionContext.setResponse(story.getDescription());
>>>>>>> Stashed changes
                questionContext.setSource(story.getLink());
                return questionContext;
            }
        } else {
            questionContext.clearSource();

            try {
                gameMap = sportsApiRequester.getGame(questionContext);
            } catch (SportsBotException e) {
                e.setErrorMessage(questionContext);
                return questionContext;
            }

            Game thisGame = new Game();

            Roster roster = rosterService.getRoster(questionContext.getSport());
            thisGame.buildFromScoreboard(gameMap, roster);
            questionContext.setGame(thisGame);

            try {
                if (questionContext.getQuestionType() == QuestionType.PLAYER_PERFORMANCE) {
                    questionContext.setResponse(rosterService.determinePlayerPerformance(questionContext));

                } else if (questionContext.getQuestionType() == QuestionType.POSITION_INFORMATION) {
                    rosterService.determinePositionInformation(questionContext);
                    languageService.answerPlayerPerformance(questionContext);
                } else {
                    questionContext.setResponse(thisGame.generateGameStatusString(questionContext));
                }
            } catch (SportsBotException e) {
                e.printStackTrace();
            }
            return questionContext;
        }
    }
}
