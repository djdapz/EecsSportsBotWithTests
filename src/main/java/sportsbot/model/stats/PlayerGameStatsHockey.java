package sportsbot.model.stats;

import sportsbot.enums.StatCategory;
import sportsbot.model.Player;

import java.util.ArrayList;

/**
 * Created by devondapuzzo on 5/28/17.
 */
public class PlayerGameStatsHockey extends PlayerGameStatsAbstract{
    public PlayerGameStatsHockey(Player player) {
        super(player);
    }

    @Override
    public String getString(ArrayList<StatCategory> categoryArrayList) {
        return null;
    }

    @Override
    public String getOffensiveString() {
        return null;
    }

    @Override
    public String getDefensiveString() {
        return null;
    }
}
