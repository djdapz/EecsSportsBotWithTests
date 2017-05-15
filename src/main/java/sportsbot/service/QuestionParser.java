package sportsbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportsbot.enums.TemporalContext;
import sportsbot.exception.AmbiguousTeamException;
import sportsbot.exception.TeamNotFoundException;
import sportsbot.model.*;

/**
 * Created by devondapuzzo on 5/10/17.
 */

@Service
public class QuestionParser {

    @Autowired
    private RosterService rosterService;

    public void parse(QuestionContext questionContext) throws AmbiguousTeamException, TeamNotFoundException{
        this.determineTemporalContest(questionContext);
        try {
            this.determineTeamAndSport(questionContext);
            this.determineQuestionType(questionContext);
        } catch (AmbiguousTeamException e) {
            throw e;
        } catch (TeamNotFoundException e) {
            if(questionContext.getTeam() == null){
                throw e;
            }
        }

    }

    private void determineQuestionType(QuestionContext questionContext) {
        //TODO - IMPLEMENT
    }

    public void determineTemporalContest(QuestionContext questionContext){
        String question = questionContext.getQuestion().toLowerCase();

        if(question.contains("today")){
            questionContext.setTemporalContext(TemporalContext.TODAY);
        }else if(question.contains("yesterday")){
            questionContext.setTemporalContext(TemporalContext.YESTERDAY);
        }else if(question.contains("tomorrow")){
            questionContext.setTemporalContext(TemporalContext.TOMORROW);
        }else if(questionContext.getTemporalContext() == null){
            questionContext.setTemporalContext(TemporalContext.TODAY);
        }
    }

    public void determineTeamAndSport(QuestionContext questionContext) throws AmbiguousTeamException, TeamNotFoundException {
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


}