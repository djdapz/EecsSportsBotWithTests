package sportsbot.service;

import org.springframework.stereotype.Service;
import sportsbot.model.QuestionContext;
import sportsbot.model.Story;

/**
 * Created by devondapuzzo on 5/15/17.
 */
@Service
public class NewsService
{
    public Story getNewsStory(QuestionContext qc){
        assert(qc.getTeam() != null);
        assert(qc.getSport() != null);
        assert(qc.getTemporalContext() != null);
        assert(qc.getQuestion() != null);



        return new Story();

    }
}
