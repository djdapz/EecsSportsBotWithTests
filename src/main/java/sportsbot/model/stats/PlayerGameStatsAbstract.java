package sportsbot.model.stats;

import sportsbot.enums.StatCategory;
import sportsbot.model.Player;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by devondapuzzo on 5/28/17.
 */
public abstract class PlayerGameStatsAbstract implements PlayerGameStats {
    private Player player;
    private HashMap<String, HashMap<String, PlayerStat>> categorizedStats = new HashMap<>();

    public PlayerGameStatsAbstract(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public HashMap<String, HashMap<String, PlayerStat>> getCategorizedStats() {
        return categorizedStats;
    }

    public void addStat(String stat, LinkedHashMap statItem){

        PlayerStat ps = new PlayerStat(
                stat,
                (String) statItem.get("@abbreviation"),
                (String) statItem.get("#text")
        );


        categorizedStats.computeIfAbsent((String) statItem.get("@category"), k -> new HashMap<String, PlayerStat>());
        categorizedStats.get(statItem.get("@category")).put(ps.getAbbreviation(), ps);
    }

    @Override
    public String getString(ArrayList<StatCategory> categoryArrayList) {
        if(categoryArrayList.size() == 0 || categoryArrayList.contains(StatCategory.OVERALL)){
            return getOffensiveString() + getDefensiveString();
        }else if(categoryArrayList.contains(StatCategory.OFFENSIVE)){
            return getOffensiveString();
        }else if(categoryArrayList.contains(StatCategory.DEFENSIVE)){
            return getDefensiveString();
        }else{
            throw new NotImplementedException();
        }
    }

    public abstract String getOffensiveString();

    public abstract String getDefensiveString();
}
