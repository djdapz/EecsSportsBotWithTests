package sportsbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportsbot.enums.Sport;
import sportsbot.exception.AmbiguousTeamException;
import sportsbot.exception.TeamNotFoundException;
import sportsbot.model.City;
import sportsbot.model.QuestionContext;
import sportsbot.model.Roster;
import sportsbot.model.Team;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Created by devondapuzzo on 4/24/17.
 */

@Service
public class RosterService {

    private Roster roster;

    private HashMap<Sport, Roster> rosters;

    private HashMap<String, City> cities;
    private HashSet<Team> allTeams;

    @Autowired
    private SportsApiService requester;

    public RosterService(){
        rosters = new HashMap<>();
        cities = new HashMap<>();
        allTeams = new HashSet<>();

        for(Sport sport: Sport.class.getEnumConstants()){
            rosters.put(sport, createRoster(sport));
            for(Team team: rosters.get(sport).getTeams().values()){
                allTeams.add(team);
            }
        }
    }


    private Roster createRoster(Sport sport){
        Roster roster = new Roster();
        ArrayList<LinkedHashMap> rawRosters = requester.getRostersFromAPI(sport);
        roster.build(rawRosters, sport, cities);
        return roster;
    }

    public Roster getRoster(Sport sport) {
        return rosters.get(sport);
    }

    public Team getTeam(Sport sport, String teamAbbr){
        return rosters.get(sport).getTeams().get(teamAbbr);
    }

    public Team findTeam(String query, QuestionContext questionContext) throws TeamNotFoundException, AmbiguousTeamException {
        City cityInQuestion = null;
        query = query.toLowerCase();

        for(City city: cities.values()){
            if(query.contains(city.getName().toLowerCase())){
                cityInQuestion = city;
                break;
            }
        }

        ArrayList<Team> teamsToSearch;
        if(cityInQuestion == null){
            if(questionContext.getCity() != null){
                cityInQuestion = questionContext.getCity();
                teamsToSearch = cityInQuestion.getTeams();
            }else{
                teamsToSearch =  new ArrayList<>(allTeams);
            }
        }else{
            teamsToSearch = cityInQuestion.getTeams();
        }

        ArrayList<Team> teamsFound = new ArrayList<>();

        if(teamsToSearch.size() == 1){
            return teamsToSearch.iterator().next();
        }else{
            for(Team team: teamsToSearch){
                if(query.contains(team.getName().toLowerCase())){
                    teamsFound.add(team);
                }
            }
            if(teamsFound.size() == 0){
                //if we already have a team and haven't found a new city and no new information, run with it
                if(questionContext.getTeam()!= null && cityInQuestion == questionContext.getCity()){
                    return questionContext.getTeam();
                }
                //See if we have a sport context to go off of
                if(questionContext.getSport() != null){
                    return cityInQuestion.findTeam(questionContext.getSport());
                }

                if(cityInQuestion == null){
                    throw new TeamNotFoundException();
                }else{
                    throw new AmbiguousTeamException(cityInQuestion);
                }
            }else if(teamsFound.size() ==1){
                return teamsFound.get(0);
            }else{
                throw new AmbiguousTeamException(cityInQuestion);
            }

        }
    }

    public City getCity(String cityName){
        return cities.get(cityName.toLowerCase());
    }

}
