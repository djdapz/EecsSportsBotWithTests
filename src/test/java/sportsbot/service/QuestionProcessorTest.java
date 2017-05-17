package sportsbot.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sportsbot.enums.Sport;
import sportsbot.enums.TemporalContext;
import sportsbot.model.City;
import sportsbot.model.QuestionContext;
import sportsbot.model.Roster;
import sportsbot.model.Team;

import static org.junit.Assert.*;

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


    private Team cubs;

    private Team yankees;
    private Team rockies;
    private City chicago;
    private City colorado;
    private City newYork;
    private QuestionContext questionContext;

    @Before
    public void Setup(){
        questionContext = new QuestionContext();
        questionContext.setQuestion("");
        cubs = rosterService.getTeam(Sport.BASEBALL, "CHC");
        rockies = rosterService.getTeam(Sport.BASEBALL, "COL");
        yankees = rosterService.getTeam(Sport.BASEBALL, "NYY");
        chicago = rosterService.getCity("Chicago");
        colorado = rosterService.getCity("Colorado");
        newYork = rosterService.getCity("new york");
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

//    @Test
//    public void switchingCitiesPersistsSportAndTimeWithClarification() throws Exception {
//        QuestionContext questionContext = new QuestionContext();
//
//        questionContext.setQuestion("Cubs today");
//        questionProcessor.answer(questionContext);
//        assertEquals(questionContext.getSport(), Sport.BASEBALL);
//        assertEquals(questionContext.getTemporalContext(), TemporalContext.TODAY);
//        assertEquals(questionContext.getCity(), chicago);
//        assertEquals(questionContext.getTeam(), cubs);
//
//        questionContext.setQuestion("What about tomorrow?");
//        questionProcessor.answer(questionContext);
//        assertEquals(questionContext.getSport(), Sport.BASEBALL);
//        assertEquals(questionContext.getTemporalContext(), TemporalContext.TOMORROW);
//        assertEquals(questionContext.getCity(), chicago);
//        assertEquals(questionContext.getTeam(), cubs);
//
//        questionContext.setQuestion("What about New York?");
//        try{
//
//        }
//
//        assertEquals(questionContext.getSport(), Sport.BASEBALL);
//        assertEquals(questionContext.getTemporalContext(), TemporalContext.TOMORROW);
//        assertEquals(questionContext.getCity(), newYork);
//        assertEquals(questionContext.isClarification(), true);
//
//        questionContext.setQuestion("Yankees");
//        questionProcessor.answer(questionContext);
//        assertEquals(questionContext.getSport(), Sport.BASEBALL);
//        assertEquals(questionContext.getTemporalContext(), TemporalContext.TOMORROW);
//        assertEquals(questionContext.getCity(), newYork);
//        assertEquals(questionContext.getTeam(), yankees);
//        assertEquals(questionContext.isClarification(), true);
//    }

}