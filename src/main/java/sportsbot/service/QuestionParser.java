package sportsbot.service;

import com.joestelmach.natty.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportsbot.enums.QuestionType;
import sportsbot.enums.StatCategory;
import sportsbot.enums.TemporalContext;
import sportsbot.exception.AmbiguousTeamException;
import sportsbot.exception.PositionNotFoundException;
import sportsbot.exception.TeamNotFoundException;
import sportsbot.model.City;
import sportsbot.model.QuestionContext;
import sportsbot.model.Team;
import sportsbot.model.position.Position;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
// import regex


/**
 * Created by devondapuzzo on 5/10/17.
 */

@Service
public class QuestionParser {

    @Autowired
    private  RosterService rosterService;

    @Autowired
    private PositionsService positionsService;

    public  void parse(QuestionContext questionContext) throws AmbiguousTeamException, TeamNotFoundException, PositionNotFoundException {
        determineTemporalContext(questionContext);
        try {
            determineTeamAndSport(questionContext);
            searchForPlayer(questionContext);
            determineQuestionType(questionContext);
            if(questionContext.getQuestionType() == QuestionType.POSITION_INFORMATION){
                determinePosition(questionContext);
            }
        } catch (AmbiguousTeamException e) {
            throw e;
        } catch (TeamNotFoundException e) {
            if(questionContext.getTeam() == null){
                throw e;
            }
        } catch (PositionNotFoundException e){
            questionContext.setQuestionType(QuestionType.GAME_SCORE);
        }
    }

    // should run after parse the question and figure out the team, player and city
    private  void determineQuestionType(QuestionContext questionContext) {
        if(questionContext.getQuestionType() == QuestionType.POSITION_INFORMATION) {
            //keep question type if asking about new position
            Position oldPosition = questionContext.getPosition();
            try {
                determinePosition(questionContext);
                if (oldPosition != questionContext.getPosition()) {
                    return;
                }
            } catch (PositionNotFoundException ignored) {
            }
        }

        if (questionContext.getQuestion().toLowerCase().contains("more") || questionContext.getQuestion().toLowerCase().contains("story")) {
            if (questionContext.getTeam() != null || questionContext.getPreviousQuestion().getTeam() != null) {
                questionContext.setQuestionType(QuestionType.NEWS);
            }
        }else if(questionContext.getQuestion().toLowerCase().contains("who")){
            if(questionContext.containsCityOrTeam()) {
                questionContext.setQuestionType(QuestionType.GAME_SCORE);
            }else{
                questionContext.setQuestionType(QuestionType.POSITION_INFORMATION);
            }
        }else if(questionContext.getPlayer() != null){
            questionContext.setQuestionType(QuestionType.PLAYER_PERFORMANCE);
        }else{
            questionContext.setQuestionType(QuestionType.GAME_SCORE);
        }
    }

    private static Date dateReformat(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }

    private static int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public  void determineTemporalContext(QuestionContext questionContext){
        TimeZone.setDefault(TimeZone.getTimeZone("America/Chicago"));
        String question = questionContext.getQuestion().toLowerCase();
        Parser parser = new Parser();
        Date today = parser.parse("today").get(0).getDates().get(0);

        today = dateReformat(today);
        Date date = null;
        try {
            date = parser.parse(question).get(0).getDates().get(0);
            date = dateReformat(date);
        } catch (Exception e) {
            if(questionContext.getTemporalContext() == null){
                questionContext.setTemporalContext(TemporalContext.TODAY);
            }
            return;
        }
        int offset = daysBetween(today, date);
        if(offset == 0){
            questionContext.setTemporalContext(TemporalContext.TODAY);
        }else if(offset == -1){
            questionContext.setTemporalContext(TemporalContext.YESTERDAY);
        }else if(offset == 1){
            questionContext.setTemporalContext(TemporalContext.TOMORROW);
        }else if(offset > 1){
            questionContext.setTemporalContext(TemporalContext.FUTURE);
            questionContext.getTemporalContext().setOffset(offset);
        }else if(offset < -1){
            questionContext.setTemporalContext(TemporalContext.PAST);
            questionContext.getTemporalContext().setOffset(offset);
        }else if(questionContext.getTemporalContext() == null){
            questionContext.setTemporalContext(TemporalContext.TODAY);
        }
    }

    public  void determineTeamAndSport(QuestionContext questionContext) throws AmbiguousTeamException, TeamNotFoundException {
        String question = questionContext.getQuestion().toLowerCase();

        try{
            Team team = rosterService.findTeam(question, questionContext);
            questionContext.setTeam(team);
            questionContext.setCity(team.getCity());
        }catch(AmbiguousTeamException e){
            City city = e.getCity();
            if(questionContext.getSport() == null){
                //If no context to off of
                throw e;
            }else{
                //if we have a context to go off of, we try and find the team.
                try {
                    questionContext.setTeam(city.findTeam(questionContext.getSport()));
                    questionContext.setCity(city);
                } catch (Exception ee) {
                    //IF it's an Unclear Team Exception
                        // Example --> Sport = Baseball ==> How did new york do?
                    //IF it's a TeamNotFoundException
                        //Example --> Sport = Baseball ==> How did Oklahoma City do?
                        //CAN THIS BE REACHED?
                    throw ee;
                }
            }
        }catch(TeamNotFoundException e){
            throw e;
        }
    }

    private void searchForPlayer(QuestionContext questionContext) {
        rosterService.findPlayer(questionContext);
    }

    public  ArrayList<StatCategory> determineStatCategories(QuestionContext questionContext) {
        ArrayList<StatCategory> statCategories = new ArrayList<>();
        String question = questionContext.getQuestion().toLowerCase();

        if(question.contains(" bat") || question.contains(" offense") || question.contains(" run")){
            statCategories.add(StatCategory.OFFENSIVE);
        }

        if(question.contains(" field") || question.contains(" defense") || question.contains(" pitch") || question.contains(" out")){
            statCategories.add(StatCategory.DEFENSIVE);
        }


        return statCategories;
    }

    public void determinePosition(QuestionContext questionContext) throws PositionNotFoundException {
        questionContext.setPosition(positionsService.findPosition(questionContext));
    }
}
