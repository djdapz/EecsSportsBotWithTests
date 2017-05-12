package sportsbot.service;

import java.util.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sportsbot.enums.Sport;
import sportsbot.enums.TemporalContext;
import sportsbot.exception.ServerContactException;
import sportsbot.exception.TeamNotPlayingException;
import sportsbot.model.Game;
import sportsbot.model.QuestionContext;
import sportsbot.model.Team;
import sun.awt.image.ImageWatched;


/**
 * Created by devondapuzzo on 4/12/17.
 */

@Component
public class SportsApiService {


    private HttpHeaders createHeaders(final String username, final String password ){
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

    private ResponseEntity<Object> contactApi(Sport sport, String requestType, Integer offset){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = this.createHeaders("djdapz", "goCats");

        String dateString = this.getToday(offset);

        String seasonString = sport.getSeasonString();

        String sportString = sport.getURLstring();

        String urlString = "https://www.mysportsfeeds.com/api/feed/pull/"+sportString+"/"+seasonString+"/"+requestType+".json?fordate="+dateString+"&force=true";
        return restTemplate.exchange(urlString, HttpMethod.GET, new HttpEntity<Object>(httpHeaders), Object.class);
    }

    private ResponseEntity<Object> contactApi(Sport sport, String requestType){
        return contactApi(sport, requestType, 0);
    }

    public ArrayList<LinkedHashMap> getRostersFromAPI(Sport sport){

        ResponseEntity<Object> response = this.contactApi(sport, "roster_players");

        LinkedHashMap<String, LinkedHashMap> roster = (LinkedHashMap<String, LinkedHashMap>) response.getBody();
        return (ArrayList<LinkedHashMap>) roster.get("rosterplayers").get("playerentry");
    }



    public LinkedHashMap getTodaysGame(QuestionContext questionContext) throws TeamNotPlayingException, ServerContactException {

        Team team = questionContext.getTeam();
        TemporalContext temporalContext = questionContext.getTemporalContext();
        ResponseEntity<Object> response;

        Integer offset = 0;

        if(questionContext.getTemporalContext() == TemporalContext.YESTERDAY){
            offset = -1;
            response = this.contactApi(questionContext.getSport(), "scoreboard", offset);
        }else {
            if(questionContext.getTemporalContext() == TemporalContext.TOMORROW){
                offset = 1;
            }
            response = this.contactApi(questionContext.getSport(),"scoreboard", offset);
        }


        if(response.getStatusCode().value() != 200){
            throw new ServerContactException();
        }

        LinkedHashMap<String, LinkedHashMap> responseResult = (LinkedHashMap<String, LinkedHashMap>) response.getBody();
        ArrayList<LinkedHashMap>  games = (ArrayList<LinkedHashMap>) responseResult.get("scoreboard").get("gameScore");

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

        throw new TeamNotPlayingException(team);
    }

    public String getToday(){
        return getToday(0);
    }

    public String getToday(Integer offset){
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
}
