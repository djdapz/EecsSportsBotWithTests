package sportsbot.model.position;

import sportsbot.exception.PositionNotFoundException;

import java.util.ArrayList;

/**
 * Created by devondapuzzo on 5/28/17.
 */
public interface Positions {
    Position searchQueryForPosition(String query) throws PositionNotFoundException;
    ArrayList<Position> getPositions();
}
