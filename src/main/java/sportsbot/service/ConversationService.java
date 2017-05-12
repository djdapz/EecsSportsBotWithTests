package sportsbot.service;

import org.springframework.stereotype.Service;
import sportsbot.model.QuestionContext;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by devondapuzzo on 5/10/17.
 */
@Service
public class ConversationService {
    private HashMap<Integer, QuestionContext> conversations = new HashMap<>();

    private QuestionContext newConversation(){
        Integer conversationId = Math.abs(new Random().nextInt());
        QuestionContext qc = new QuestionContext(conversationId);
        conversations.put(conversationId, qc);
        return qc;
    }

    public QuestionContext getConversation(Integer id){
        if(id == -1 || conversations.get(id) == null){
            return newConversation();
        }else{
            return conversations.get(id);
        }
    }
}
