package sportsbot.enums;

import sportsbot.model.Player;
import sportsbot.model.stats.PlayerGameStats;
import sportsbot.model.stats.PlayerGameStatsBaseball;
import sun.util.calendar.BaseCalendar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by devondapuzzo on 4/25/17.
 */
public enum Sport {
    BASEBALL{
        @Override
        public PlayerGameStats makeStat(Player player) {
            return new PlayerGameStatsBaseball(player);
        }
    },
    HOCKEY {
        @Override
        public PlayerGameStats makeStat(Player player) {
            return null;
        }
    },
    FOOTBALL {
        @Override
        public PlayerGameStats makeStat(Player player) {
            return null;
        }
    },
    BASKETBALL {
        @Override
        public PlayerGameStats makeStat(Player player) {
            return null;
        }
    };

    private String URLstring;
    private String SeasonString;

    static {
        BASEBALL.URLstring = "mlb";
        HOCKEY.URLstring = "nhl";
        FOOTBALL.URLstring = "nfl";
        BASKETBALL.URLstring = "nba";

        BASEBALL.SeasonString = "current";
        HOCKEY.SeasonString = "2016-regular";
        FOOTBALL.SeasonString = "2016-regular";
        BASKETBALL.SeasonString = "current";

    }

    public String getURLstring() {
        return URLstring;
    }

    public String getSeasonString() {
        return SeasonString;
    }

    public abstract PlayerGameStats makeStat(Player player);
}
