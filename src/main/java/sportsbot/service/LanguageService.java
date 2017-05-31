package sportsbot.service;

import org.springframework.stereotype.Service;
import sportsbot.model.QuestionContext;

/**
 * Created by devondapuzzo on 5/29/17.
 */
@Service
public class LanguageService {

    public void answerPlayerPosition(QuestionContext questionContext){
        assert(questionContext.getPlayer() != null);
        assert(questionContext.getPosition() != null);

        String response = questionContext.getPlayer().getName() + " " + questionContext.getPosition().getAction(questionContext);

        questionContext.setResponse(response);
    }


}
