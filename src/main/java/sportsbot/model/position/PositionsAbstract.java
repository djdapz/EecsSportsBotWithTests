package sportsbot.model.position;

import sportsbot.exception.PositionNotFoundException;

import java.util.ArrayList;

/**
 * Created by devondapuzzo on 5/28/17.
 */
public abstract class PositionsAbstract implements Positions {

    protected ArrayList<Position> positions = new ArrayList<>();


    @Override
    public ArrayList<Position> getPositions() {
        return positions;
    }

    public abstract Position searchQueryForPosition(String query) throws PositionNotFoundException;
}
