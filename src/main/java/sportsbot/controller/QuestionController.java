package sportsbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportsbot.enums.Sport;
import sportsbot.enums.TemporalContext;
import sportsbot.exception.AmbiguousTeamException;
import sportsbot.model.Game;
import sportsbot.model.QuestionContext;
import sportsbot.model.QuestionResponse;
import sportsbot.model.Team;
import sportsbot.service.ConversationService;
import sportsbot.service.QuestionProcessor;
import sportsbot.service.RosterService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;

/**
 * Created by devondapuzzo on 4/19/17.
 */

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionProcessor questionProcessor;

    @Autowired
    private RosterService rosterService;

    @Autowired
    private ConversationService conversationService;

    @RequestMapping(method = RequestMethod.GET)
    public QuestionResponse addItem(@RequestParam(value = "query", required = true) String query,
                                    @RequestParam(value = "conversationId", required = false, defaultValue = "-1") Integer conversationId) {

        QuestionContext questionContext = conversationService.getConversation(conversationId);

        questionContext.setQuestion(query);
        try {
            questionContext = questionProcessor.answer(questionContext);
        } catch (AmbiguousTeamException e) {
            questionContext.setClarification(true);
            if(e.getCity() != null){
                String clarification = "Are you asking about the ";
                ArrayList<Team> teams;
                if(questionContext.getSport() != null){
                    teams = e.getCity().getTeams(questionContext.getSport());
                }else{

                    teams = e.getCity().getTeams();
                }


                for(int i = 0; i < teams.size(); i ++){
                    if(i != 0){
                        if(i == teams.size()-1){
                            clarification += " or the ";
                        }else{
                            clarification += ", ";
                        }

                    }
                    clarification += teams.get(i).getName();
                }
                clarification += "?";
                questionContext.setResponse(clarification);
            }
        }


        QuestionResponse qr = new QuestionResponse(questionContext);

        return qr;
    }

}
