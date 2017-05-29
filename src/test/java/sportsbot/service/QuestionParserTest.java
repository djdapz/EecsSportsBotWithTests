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
import sportsbot.exception.AmbiguousTeamException;
import sportsbot.exception.TeamNotFoundException;
import sportsbot.model.City;
import sportsbot.model.Player;
import sportsbot.model.QuestionContext;
import sportsbot.model.Team;
import sportsbot.model.position.Position;

import static org.junit.Assert.assertEquals;

/**
 * Created by devondapuzzo on 5/10/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionParserTest {

    @Autowired
    private RosterService rosterService;

    @Autowired
    private QuestionParser questionParser;

    @Autowired
    private PositionsService positionsService;


    private Team cubs;
    private Team whitesox;
    private Team blackhawks;
    private Team bears;
    private Team bulls;
    private Team rockies;
    private Team yankees;
    private City chicago;
    private Player anthonyRizzo;
    private QuestionContext questionContext;
    private Position pitcher;
    private Position firstBase;


    @Before
    public void Setup(){
        questionContext = new QuestionContext();
        questionContext.setQuestion("");
        cubs = rosterService.getTeam(Sport.BASEBALL, "CHC");
        whitesox = rosterService.getTeam(Sport.BASEBALL, "CWS");
        blackhawks = rosterService.getTeam(Sport.HOCKEY, "CHI");
        bears = rosterService.getTeam(Sport.FOOTBALL, "CHI");
        bulls = rosterService.getTeam(Sport.BASKETBALL, "CHI");
        rockies = rosterService.getTeam(Sport.BASEBALL, "COL");
        yankees = rosterService.getTeam(Sport.BASEBALL, "NYY");
        anthonyRizzo = cubs.getPlayers().get("Rizzo, Anthony");
        chicago = rosterService.getCity("Chicago");
        pitcher = positionsService.findPosition(Sport.BASEBALL, "pitcher");
        firstBase = positionsService.findPosition(Sport.BASEBALL, "first base");
    }

    @Test
    public void easyParseTemporal() throws Exception {
        String question = "cubs today";

        QuestionContext questionContext = new QuestionContext();
        questionContext.setQuestion(question);

        questionParser.determineTemporalContest(questionContext);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TODAY);

    }

    @Test
    public void easyParseJustTeam() throws Exception {
        String question = "cubs today";

        QuestionContext questionContext = new QuestionContext();
        questionContext.setQuestion(question);

        Team cubs = rosterService.getTeam(Sport.BASEBALL, "CHC");

        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);

    }

    @Test
    public void easyParseCityAndTeam() throws Exception {
        String question = "chicago cubs today";

        QuestionContext questionContext = new QuestionContext();
        questionContext.setQuestion(question);

        Team cubs = rosterService.getTeam(Sport.BASEBALL, "CHC");

        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);

    }

    @Test
    public void getsAllChicagoTeamsWithCityName() throws Exception {
        questionContext = new QuestionContext();
        questionContext.setQuestion("chicago cubs");
        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);

        questionContext = new QuestionContext();
        questionContext.setQuestion("chicago blackhawks");
        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.HOCKEY);
        assertEquals(questionContext.getTeam(), blackhawks);

        questionContext = new QuestionContext();
        questionContext.setQuestion("chicago white sox");
        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), whitesox);

        questionContext = new QuestionContext();
        questionContext.setQuestion("chicago bears");
        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.FOOTBALL);
        assertEquals(questionContext.getTeam(), bears);

        questionContext = new QuestionContext();
        questionContext.setQuestion("chicago bulls");
        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASKETBALL);
        assertEquals(questionContext.getTeam(), bulls);
    }


    @Test
    public void getsAllChicagoTeamsWithoutCityName() throws Exception {
        questionContext.setQuestion("cubs");
        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);

        questionContext = new QuestionContext();
        questionContext.setQuestion("blackhawks");
        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.HOCKEY);
        assertEquals(questionContext.getTeam(), blackhawks);

        questionContext = new QuestionContext();
        questionContext.setQuestion("white sox");
        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), whitesox);

        questionContext = new QuestionContext();
        questionContext.setQuestion("bears");
        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.FOOTBALL);
        assertEquals(questionContext.getTeam(), bears);

        questionContext = new QuestionContext();
        questionContext.setQuestion("bulls");
        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASKETBALL);
        assertEquals(questionContext.getTeam(), bulls);
    }



    @Test
    public void getsAllChicagoTeamsWithJustCityNameThatArePossible() throws Exception {
        questionContext = new QuestionContext();
        questionContext.setQuestion("chicago");
        questionContext.setSport(Sport.HOCKEY);
        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.HOCKEY);
        assertEquals(questionContext.getTeam(), blackhawks);

        questionContext = new QuestionContext();
        questionContext.setQuestion("chicago");
        questionContext.setSport(Sport.FOOTBALL);
        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.FOOTBALL);
        assertEquals(questionContext.getTeam(), bears);

        questionContext = new QuestionContext();
        questionContext.setQuestion("chicago");
        questionContext.setSport(Sport.BASKETBALL);
        questionParser.determineTeamAndSport(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASKETBALL);
        assertEquals(questionContext.getTeam(), bulls);
    }

    @Test(expected = AmbiguousTeamException.class)
    public void searchingJustChicagoBaseballIsAmbiguous() throws Exception{
        questionContext.setQuestion("chicago");
        questionContext.setSport(Sport.BASEBALL);
        try {
            questionParser.determineTeamAndSport(questionContext);
        }catch(AmbiguousTeamException e){
            assertEquals(e.getClass(), AmbiguousTeamException.class);
            assertEquals(cubs.getCity(), e.getCity());
            throw e;
        }
    }

    @Test(expected = TeamNotFoundException.class)
    public void whenTeamNeverIncludedThowsTeamNotFoundException() throws Exception{
        questionContext.setQuestion("what about tomorrow");
        questionParser.parse(questionContext);

    }

    public void whenTeamInContextButNotQuestionItCanFigureItOut() throws Exception{
        questionContext.setQuestion("what about tomorrow");
        questionContext.setTeam(cubs);
        questionContext.setTemporalContext(TemporalContext.TODAY);

        questionParser.parse(questionContext);

        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TOMORROW);
    }

    @Test
    public void whenSportAndCityInContextButNotQuestionItCanFigureItOut() throws Exception{
        questionContext.setQuestion("what about tomorrow");
        questionContext.setTemporalContext(TemporalContext.TODAY);
        questionContext.setSport(Sport.HOCKEY);
        questionContext.setCity(chicago);

        questionParser.parse(questionContext);

        assertEquals(questionContext.getSport(), Sport.HOCKEY);
        assertEquals(questionContext.getTeam(), blackhawks);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TOMORROW);
    }


    @Test
    public void canDetermineWhenAskingAboutPlayer() throws Exception{
        questionContext.setQuestion("How did the Cubs do?");

        questionParser.parse(questionContext);

        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TODAY);

        questionContext.setQuestion("How did Rizzo play?");
        questionParser.parse(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TODAY);
        assertEquals(questionContext.getQuestionType(), QuestionType.PLAYER_PERFORMANCE);
        assertEquals(questionContext.getPlayer(), anthonyRizzo);


    }

    @Test
    public void switchFromPlayerToAnotherTeam() throws Exception{
        questionContext.setQuestion("How did the Cubs do?");

        questionParser.parse(questionContext);

        questionContext.setQuestion("How did Rizzo play?");
        questionParser.parse(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TODAY);
        assertEquals(questionContext.getQuestionType(), QuestionType.PLAYER_PERFORMANCE);
        assertEquals(questionContext.getPlayer(), anthonyRizzo);

        questionContext.setQuestion("What about the Yankees?");
        questionParser.parse(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), yankees);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TODAY);
        assertEquals(questionContext.getQuestionType(), QuestionType.GAME_SCORE);
        assertEquals(questionContext.getPlayer(), null);

    }

    @Test
    public void switchFromPlayerToAnotherTeamViaCityName() throws Exception{
        questionContext.setQuestion("How did the Cubs do?");

        questionParser.parse(questionContext);

        questionContext.setQuestion("How did Rizzo play?");
        questionParser.parse(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TODAY);
        assertEquals(questionContext.getQuestionType(), QuestionType.PLAYER_PERFORMANCE);
        assertEquals(questionContext.getPlayer(), anthonyRizzo);

        questionContext.setQuestion("What about Colorado?");
        questionParser.parse(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), rockies);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TODAY);
        assertEquals(questionContext.getQuestionType(), QuestionType.GAME_SCORE);
        assertEquals(questionContext.getPlayer(), null);

    }

    @Test
    public void switchFromPlayerToAnotherTime() throws Exception{
        questionContext.setQuestion("How did the Cubs do?");

        questionParser.parse(questionContext);

        questionContext.setQuestion("How did Rizzo play?");
        questionParser.parse(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TODAY);
        assertEquals(questionContext.getQuestionType(), QuestionType.PLAYER_PERFORMANCE);
        assertEquals(questionContext.getPlayer(), anthonyRizzo);

        questionContext.setQuestion("What about Yesterday?");
        questionParser.parse(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.YESTERDAY);
        assertEquals(questionContext.getQuestionType(), QuestionType.PLAYER_PERFORMANCE);
        assertEquals(questionContext.getPlayer(), anthonyRizzo);
    }

    @Test
    public void askQuestionAboutPosition() throws Exception{
        questionContext.setQuestion("How did the Cubs do?");

        questionParser.parse(questionContext);

        questionContext.setQuestion("Who pitched?");
        questionParser.parse(questionContext);
        assertEquals(questionContext.getSport(), Sport.BASEBALL);
        assertEquals(questionContext.getTeam(), cubs);
        assertEquals(questionContext.getTemporalContext(), TemporalContext.TODAY);
        assertEquals(questionContext.getQuestionType(), QuestionType.POSITION_INFORMATION);
        assertEquals(questionContext.getPosition(), pitcher);
    }

}