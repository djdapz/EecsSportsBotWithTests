package sportsbot.model.position;

import sportsbot.enums.GameStatus;
import sportsbot.model.QuestionContext;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by devondapuzzo on 5/28/17.
 */
public class Position {

    private String abbreviation;
    private ArrayList<String> names = new ArrayList();
    private HashMap<GameStatus, String> actionPhrases = new HashMap<>();
    private boolean usesFirstNameInPhrase = true;

    public Position(String abbreviation) {
        this.abbreviation = abbreviation;
        addActionPhrase(GameStatus.SCHEDULED, "is set to play");
        addActionPhrase(GameStatus.INPROGRESS, "is currently playing");
        addActionPhrase(GameStatus.COMPLETED, "played");
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void addName(String newName) {
        names.add(newName);
    }

    public void addActionPhrase(GameStatus gs, String newPhrase) {
        actionPhrases.put(gs, newPhrase);
    }

    public boolean isUsesFirstNameInPhrase() {
        return usesFirstNameInPhrase;
    }

    public void setUsesFirstNameInPhrase(boolean usesFirstNameInPhrase) {
        this.usesFirstNameInPhrase = usesFirstNameInPhrase;
    }

    public boolean isPositionInQuery(String query){
        for(String name: names){
            if(query.contains(name)){
                return true;
            }
        }
        return false;
    }

    public String getAction(QuestionContext questionContext) {

        if(usesFirstNameInPhrase){
            return actionPhrases.get(questionContext.getGame().getGameStatus()) + " " + names.get(0);
        }else{
            return actionPhrases.get(questionContext.getGame().getGameStatus());
        }

    }
}
