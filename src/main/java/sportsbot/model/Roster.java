package sportsbot.model;

import sportsbot.enums.Sport;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

/**
 * Created by devondapuzzo on 4/19/17.
 */
public class Roster {
    private HashMap<String, Player> players;
    private HashMap<String, Team> teams;

    private boolean isInitialized = false;

    public Roster(){
        this.players = new HashMap<String, Player>();
        this.teams = new HashMap<String, Team>();
    }

    public void build(ArrayList<LinkedHashMap> rosterResponse, Sport sport, HashMap<String, City> cities){
        for(int i = 0; i < rosterResponse.size(); i ++){
            LinkedHashMap<String, LinkedHashMap> playerTeamObject = (LinkedHashMap<String, LinkedHashMap>) rosterResponse.get(i);
            LinkedHashMap<String, String> playerResult = (LinkedHashMap<String, String>) playerTeamObject.get("player");
            LinkedHashMap<String, String> teamResult = (LinkedHashMap<String, String>) playerTeamObject.get("team");

            Player player = new Player();
            player.setID(Integer.parseInt(playerResult.get("ID")));
            player.setFirstName(playerResult.get("FirstName"));
            player.setLastName(playerResult.get("LastName"));
            player.setPosition(playerResult.get("Position"));
            player.setRookie(Boolean.getBoolean(playerResult.get("Rookie")));
            player.setSport(sport);

            playerResult.computeIfPresent("JerseyNumber",
                    (key,value) -> {player.setJerseyNumber(Integer.parseInt(value)); return null;});


            String teamAbbreviation = teamResult.get("Abbreviation");

            teams.computeIfAbsent(teamAbbreviation, k -> buildTeam(teamResult, sport, cities));
            player.setTeam(teams.get(teamAbbreviation));

            teams.get(teamResult.get("Abbreviation")).addPlayer(player);
            players.put(player.getLastName()+", "+player.getFirstName(), player);
        }

        this.isInitialized = true;
    }

    private Team buildTeam(LinkedHashMap<String, String> teamResult, Sport sport, HashMap<String, City> cities){
        Team team = new Team();
        team.setName(teamResult.get("Name"));
        team.setAbbreviation(teamResult.get("Abbreviation"));
        team.setID(Integer.parseInt(teamResult.get("ID")));
        team.setSport(sport);

        String cityName = teamResult.get("City");

        if(cities.get(cityName.toLowerCase()) == null){
            City newCity = new City(cityName);
            cities.put(cityName.toLowerCase(), newCity);
        }

        team.setCity(cities.get(cityName.toLowerCase()));
        cities.get(cityName.toLowerCase()).addTeam(team);

        return team;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public HashMap<String, Team> getTeams() {
        return teams;
    }


}
