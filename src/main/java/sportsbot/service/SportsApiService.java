package sportsbot.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sportsbot.enums.Sport;
import sportsbot.exception.PlayerHasntPlayedException;
import sportsbot.exception.ServerContactException;
import sportsbot.exception.TeamNotPlayingException;
import sportsbot.model.Param;
import sportsbot.model.QuestionContext;
import sportsbot.model.Team;

import java.util.*;


/**
 * Created by devondapuzzo on 4/12/17.
 */

@Component
public class SportsApiService {


    private static HttpHeaders createHeaders(final String username, final String password ){
        HttpHeaders headers =  new HttpHeaders(){
            {
                String auth = username + ":" + password;
                Base64.Encoder encoder = Base64.getEncoder();
                byte[] encodedAuth = encoder.encode(auth.getBytes());
                String authHeader = "Basic " + new String( encodedAuth );
                set( "Authorization", authHeader );
            }
        };
        headers.add("Content-Type", "application/xml");
        headers.add("Accept", "application/xml");

        return headers;
    }

    private static ResponseEntity<Object> contactApi(Sport sport, String requestType, Integer offset, ArrayList<Param> params){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = createHeaders("djdapz", "goCats");

        StringBuilder url = new StringBuilder();

        String dateString = getToday(offset);

        String seasonString = sport.getSeasonString();

        String sportString = sport.getURLstring();

        url.append("https://www.mysportsfeeds.com/api/feed/pull/")
                .append(sportString)
                .append("/")
                .append(seasonString)
                .append("/")
                .append(requestType)
                .append(".json?fordate=")
                .append(dateString)
                .append("&force=true");
        if(params != null){
            for(Param param: params){
                url.append("&")
                        .append(param.getTitle())
                        .append("=")
                        .append(param.getValue());
            }
        }
        return restTemplate.exchange(url.toString(), HttpMethod.GET, new HttpEntity<Object>(httpHeaders), Object.class);
    }

    private static ResponseEntity<Object> contactApi(Sport sport, String requestType, Integer offset){
        return contactApi(sport, requestType, offset, null);
    }

    private static ResponseEntity<Object> contactApi(Sport sport, String requestType){
        return contactApi(sport, requestType, 0, null);
    }

    public static ArrayList<LinkedHashMap> getRostersFromAPI(Sport sport){

        ResponseEntity<Object> response = contactApi(sport, "roster_players");

        LinkedHashMap<String, LinkedHashMap> roster = (LinkedHashMap<String, LinkedHashMap>) response.getBody();
        return (ArrayList<LinkedHashMap>) roster.get("rosterplayers").get("playerentry");
    }



    public static LinkedHashMap getGame(QuestionContext questionContext) throws TeamNotPlayingException, ServerContactException {

        Team team = questionContext.getTeam();
        Integer offset = questionContext.getTemporalContext().getOffset();

        ResponseEntity<Object> response = contactApi(questionContext.getSport(),"scoreboard", offset);

        if(response.getStatusCode().value() != 200){
            throw new ServerContactException();
        }

        LinkedHashMap<String, LinkedHashMap> responseResult = (LinkedHashMap<String, LinkedHashMap>) response.getBody();
        ArrayList<LinkedHashMap>  games = (ArrayList<LinkedHashMap>) responseResult.get("scoreboard").get("gameScore");

        if(games != null){
            for(int i = 0; i < games.size(); i++){
                LinkedHashMap todaysScore = games.get(i);
                LinkedHashMap todaysGame = (LinkedHashMap) todaysScore.get("game");
                LinkedHashMap homeTeamMap = (LinkedHashMap) todaysGame.get("awayTeam");
                LinkedHashMap awayTeamMap = (LinkedHashMap) todaysGame.get("homeTeam");

                String homeTeamAbbr = (String) homeTeamMap.get("Abbreviation");
                String awayTeamAbbr = (String) awayTeamMap.get("Abbreviation");

                if(team.getAbbreviation().equals(homeTeamAbbr) ||team.getAbbreviation().equals(awayTeamAbbr)){
                    return todaysScore;
                }
            }
        }


        throw new TeamNotPlayingException(team);
    }

    public static String getToday(){
        return getToday(0);
    }

    public static String getToday(Integer offset){
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("America/Chicago"));

        Integer year = now.get(now.YEAR);
        Integer month = now.get(now.MONTH)+1;
        Integer day = now.get(now.DATE) + offset;

        if(now.get(now.HOUR_OF_DAY) < 3) {
            day--;
        }


        String yearS = Integer.toString(year);
        String monthS = Integer.toString(month);
        String dayS = Integer.toString(day);

        if(monthS.length() == 1){
            monthS = "0" + monthS;
        }

        if(dayS.length() == 1){
            dayS = "0" + dayS;
        }

        return yearS + monthS + dayS;

    }

    public static ArrayList<LinkedHashMap> getPlayerStats(QuestionContext questionContext) throws ServerContactException, PlayerHasntPlayedException {

        ArrayList<Param> params = new ArrayList<>();
        params.add(new Param("player", questionContext.getPlayer().getUrlParam()));

        ResponseEntity<Object> response = contactApi(questionContext.getSport(), "daily_player_stats", questionContext.getTemporalContext().getOffset(), params);

        if(response.getStatusCode().value() != 200){
            throw new ServerContactException();
        }

        LinkedHashMap<String, LinkedHashMap> responseResult = (LinkedHashMap<String, LinkedHashMap>) response.getBody();

        ArrayList<LinkedHashMap> playerStats = (ArrayList<LinkedHashMap>) responseResult.get("dailyplayerstats").get("playerstatsentry");
        if(playerStats == null){
            throw new PlayerHasntPlayedException();
        }else{
            return playerStats;
        }


    }

    public static ArrayList<LinkedHashMap<String, HashMap>> getStartingLineup(QuestionContext questionContext) throws ServerContactException {
        assert(questionContext.getGame() != null);

        ArrayList<Param> params = new ArrayList<>();
        params.add(new Param("gameid", questionContext.getGame().getUrlParam()));


        ResponseEntity<Object> response = contactApi(questionContext.getSport(), "game_startinglineup", questionContext.getTemporalContext().getOffset(), params);

        if(response.getStatusCode().value() != 200){
            throw new ServerContactException();
        }

        LinkedHashMap<String, LinkedHashMap> responseResult = (LinkedHashMap<String, LinkedHashMap>) response.getBody();
        return (ArrayList<LinkedHashMap<String, HashMap>>) responseResult.get("gamestartinglineup").get("teamLineup");
    }
}
