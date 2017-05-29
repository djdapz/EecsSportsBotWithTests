package sportsbot.model.stats;

import sportsbot.enums.StatCategory;
import sportsbot.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by devondapuzzo on 5/28/17.
 */
public interface PlayerGameStats {
    Player getPlayer();

    void setPlayer(Player player);

    HashMap<String, HashMap<String, PlayerStat>> getCategorizedStats();

    void addStat(String stat, LinkedHashMap statItem);

    String getString(ArrayList<StatCategory> categoryArrayList);
}
