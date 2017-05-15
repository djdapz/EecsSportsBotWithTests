package sportsbot.model;

import sportsbot.enums.Sport;
import sportsbot.exception.AmbiguousTeamException;
import sportsbot.exception.TeamNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by devondapuzzo on 5/10/17.
 */
public class City {
    private final String name;
    private ArrayList<Team> teams;
    private HashMap<Sport, ArrayList<Team>> teamsBySport;

    public City(String name){
        this.teams = new ArrayList<>();
        this.teamsBySport = new HashMap<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public ArrayList<Team> getTeams(Sport sport) {
        return teamsBySport.get(sport);
    }

    public void addTeam(Team team){
        teams.add(team);
        teamsBySport.computeIfAbsent(team.getSport(),  k -> new ArrayList<>());
        teamsBySport.get(team.getSport()).add(team);
    }



    public Team findTeam(Sport sport) throws TeamNotFoundException, AmbiguousTeamException {
        int numberOfTeams =teamsBySport.get(sport).size();


        if(numberOfTeams == 0){
            throw new TeamNotFoundException();
        }else if(numberOfTeams == 1){
            return teamsBySport.get(sport).get(0);
        }else{
            throw new AmbiguousTeamException(this);
        }

    }
}
