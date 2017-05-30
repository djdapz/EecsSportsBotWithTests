package sportsbot.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sportsbot.enums.QuestionType;
import sportsbot.enums.Sport;
import sportsbot.enums.TemporalContext;
import sportsbot.model.City;
import sportsbot.model.Player;
import sportsbot.model.QuestionContext;
import sportsbot.model.Team;
import sportsbot.model.position.Position;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by devondapuzzo on 5/17/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionProcessorTest {

    @Autowired
    QuestionProcessor questionProcessor;

    @Autowired
    RosterService rosterService;

    @Autowired
    PositionsService positionsService;


    private Team cubs;

    private Team yankees;
    private Team rockies;
    private City chicago;
    private City colorado;
    private City newYork;
    private QuestionContext questionContext;
    private Player anthonyRizzo;
    private Position pitcher;
    private Position firstBase;

    @Before
    public void Setup() throws Exception{
        questionContext = new QuestionContext();
        questionContext.setQuestion("");
        cubs = rosterService.getTeam(Sport.BASEBALL, "CHC");
        rockies = rosterService.getTeam(Sport.BASEBALL, "COL");
        yankees = rosterService.getTeam(Sport.BASEBALL, "NYY");
        chicago = rosterService.getCity("Chicago");
        colorado = rosterService.getCity("Colorado");
        newYork = rosterService.getCity("new york");
        anthonyRizzo = cubs.getPlayers().get("Rizzo, Anthony");
        pitcher = positionsService.findPosition(Sport.BASEBALL, "pitcher");
        firstBase = positionsService.findPosition(Sport.BASEBALL, "first base");
    }

    @Test
    public void switchingCitiesPersistsSportAndTime() throws Exception {
        QuestionContext questionContext = new QuestionContext();

        questionContext.setQuestion("Cubs today");
        questionProcessor.answer(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TODAY);
        assertEquals(questionContext.getCity(), chicago);
        assertEquals(questionContext.getTeam(), cubs);

        questionContext.setQuestion("What about tomorrow?");
        questionProcessor.answer(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TOMORROW);
        assertEquals(questionContext.getCity(), chicago);
        assertEquals(questionContext.getTeam(), cubs);

        questionContext.setQuestion("What about Colorado?");
        questionProcessor.answer(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TOMORROW);
        assertEquals(questionContext.getCity(), colorado);
        assertEquals(questionContext.getTeam(), rockies);
    }

    @Test
    public void canDetermineWhenAskingAboutPlayer() throws Exception{
        questionContext.setQuestion("How did the Cubs do?");

        questionProcessor.answer(questionContext);
        questionContext.setQuestion("How did Rizzo play?");
        questionProcessor.answer(questionContext);
        
        
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TODAY);
        assertEquals(questionContext.getPlayer(), anthonyRizzo);

    }

    @Test
    public void canDetermineWhenAskingAboutPlayerYesterday() throws Exception{
        questionContext.setQuestion("How did the Cubs do yesterday?");

        questionProcessor.answer(questionContext);
        questionContext.setQuestion("How did Rizzo play?");
        questionProcessor.answer(questionContext);


        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.YESTERDAY);
        assertEquals(questionContext.getPlayer(), anthonyRizzo);

    }

    @Test
    public void canDetermineWhoPlayedWhatPosition() throws Exception{
        questionContext.setQuestion("How did the Cubs do yesterday?");
        questionProcessor.answer(questionContext);

        questionContext.setQuestion("Who pitched");
        questionProcessor.answer(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.YESTERDAY);
        assertEquals(questionContext.getQuestionType(), QuestionType.POSITION_INFORMATION);
        assertEquals(questionContext.getPosition(), pitcher);


        questionContext.setQuestion("Who played first");
        questionProcessor.answer(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.YESTERDAY);
        assertEquals(questionContext.getQuestionType(), QuestionType.POSITION_INFORMATION);
        assertEquals(questionContext.getPosition(), firstBase);
    }

    @Test
    public void canDetermineVariousTypesOfFirstBase() throws Exception{
        questionContext.setQuestion("How did the Cubs do yesterday?");
        questionProcessor.answer(questionContext);
        questionContext.setQuestion("Who played first");
        questionProcessor.answer(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.YESTERDAY);
        assertEquals(questionContext.getQuestionType(), QuestionType.POSITION_INFORMATION);
        assertEquals(questionContext.getPosition(), firstBase);


        questionContext = new QuestionContext();
        questionContext.setQuestion("How did the Cubs do yesterday?");
        questionProcessor.answer(questionContext);
        questionContext.setQuestion("Who played 1st");
        questionProcessor.answer(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.YESTERDAY);
        assertEquals(questionContext.getQuestionType(), QuestionType.POSITION_INFORMATION);
        assertEquals(questionContext.getPosition(), firstBase);


        questionContext = new QuestionContext();
        questionContext.setQuestion("How did the Cubs do yesterday?");
        questionProcessor.answer(questionContext);
        questionContext.setQuestion("Who played first base");
        questionProcessor.answer(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.YESTERDAY);
        assertEquals(questionContext.getQuestionType(), QuestionType.POSITION_INFORMATION);
        assertEquals(questionContext.getPosition(), firstBase);

    }

    @Test
    public void canDetermineGameInFuture() throws Exception{
        questionContext.setQuestion("Who are the cubs playing on 7/2?");
        questionProcessor.answer(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.FUTURE);
        assertEquals(questionContext.getQuestionType(), QuestionType.GAME_SCORE);
        assertTrue(questionContext.getResponse().toLowerCase().contains("reds"));

        questionContext.setQuestion("Who are the cubs playing on may 7th?");
        questionProcessor.answer(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.PAST);
        assertEquals(questionContext.getQuestionType(), QuestionType.GAME_SCORE);
        assertTrue(questionContext.getResponse().toLowerCase().contains("yankees"));

    }


}