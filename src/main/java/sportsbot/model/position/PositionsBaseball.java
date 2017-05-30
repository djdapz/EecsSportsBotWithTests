package sportsbot.model.position;

import sportsbot.enums.GameStatus;
import sportsbot.exception.PositionNotFoundException;

/**
 * Created by devondapuzzo on 5/28/17.
 */
public class PositionsBaseball extends PositionsAbstract {

    public PositionsBaseball(){
        Position position = new Position("SS");
        position.addName("shortstop");
        position.addName("short stop");
        position.addName("short-stop");
        positions.add(position);

        position = new Position("RF");
        position.addName("right field");
        positions.add(position);

        position = new Position("LF");
        position.addName("left field");
        positions.add(position);

        position = new Position("P");
        position.addName("pitcher");
        position.addName("pitching");
        position.addName("pitched");
        position.addActionPhrase(GameStatus.SCHEDULED, "is pitching");
        position.addActionPhrase(GameStatus.INPROGRESS, "is pitching");
        position.addActionPhrase(GameStatus.COMPLETED, "pitched");
        position.setUsesFirstNameInPhrase(false);
        positions.add(position);

        position = new Position("C");
        position.addName("catcher");
        position.addName("is catching");
        position.addName("catched");
        position.addActionPhrase(GameStatus.SCHEDULED, "is catching");
        position.addActionPhrase(GameStatus.INPROGRESS, "is catching");
        position.addActionPhrase(GameStatus.COMPLETED, "pitching");
        position.setUsesFirstNameInPhrase(false);
        positions.add(position);

        position = new Position("3B");
        position.addName("third base");
        position.addName("third");
        position.addName("3rd");
        position.addName("3rd base");
        positions.add(position);

        position = new Position("CF");
        position.addName("center field");
        positions.add(position);

        position = new Position("OF");
        position.addName("outfield");
        position.addName("out field");
        positions.add(position);

        position = new Position("DH");
        position.addName("designated hitter");
        position.addName("dh");
        positions.add(position);

        position = new Position( "1B");
        position.addName("first base");
        position.addName("first");
        position.addName("1st base");
        position.addName("1st");
        positions.add(position);

        position = new Position("2B");
        position.addName("second base");
        position.addName("second");
        position.addName("2nd base");
        position.addName("2nd");
        positions.add(position);
    }


    @Override
    public Position searchQueryForPosition(String query) throws PositionNotFoundException{
        for(Position position: positions){
            if(position.isPositionInQuery(query)){
                return position;
            }
        }

        throw new PositionNotFoundException();
    }


}
