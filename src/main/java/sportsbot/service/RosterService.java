package sportsbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportsbot.enums.GameStatus;
import sportsbot.enums.Sport;
import sportsbot.enums.StatCategory;
import sportsbot.exception.*;
import sportsbot.model.*;
import sportsbot.model.stats.PlayerGameStats;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

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

    @Autowired
    private QuestionParser questionParser;

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
        Team teamInQuestion = null;
        query = query.toLowerCase();

        for(City city: cities.values()){
            if(query.contains(city.getName().toLowerCase())){
                cityInQuestion = city;
                break;
            }
        }

        for(Team team: allTeams){
            if(query.contains(team.getName().toLowerCase())){
                teamInQuestion = team;
                break;
            }
        }


        if(teamInQuestion != null){
            return teamInQuestion;
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

    public void findPlayer(QuestionContext questionContext) {
        assert(questionContext.getSport() != null);
        assert(questionContext.getTeam() != null);
        assert(questionContext.getCity() != null);

        ArrayList<Player> players = new ArrayList<>();
        String question = questionContext.getQuestion().toLowerCase();
        Team thisTeam = rosters.get(questionContext.getSport()).getTeams().get(questionContext.getTeam().getAbbreviation());

        for(Player player: thisTeam.getPlayers().values()){
            if(question.contains(player.getFirstName().toLowerCase()) || question.contains(player.getLastName().toLowerCase())){
                players.add(player);
            }
        }

        if(players.size() == 1){
            questionContext.setPlayer(players.get(0));
        }else if(players.size() >1){
            int bestMatchLength = 0;
            Player bestMatch = null;
            for(Player player: players){
                if(question.contains(player.getLastName().toLowerCase()) && question.contains(player.getFirstName().toLowerCase())){
                    questionContext.setPlayer(player);
                    break;
                }else if(question.contains(player.getLastName().toLowerCase())){
                    if(player.getLastName().length() > bestMatchLength){
                        bestMatchLength = player.getLastName().length();
                        bestMatch = player;
                    }
                }else if(question.contains(player.getFirstName().toLowerCase())) {
                    if(player.getFirstName().length() > bestMatchLength){
                        bestMatchLength = player.getFirstName().length();
                        bestMatch = player;
                    }
                }
            }

            assert(bestMatch != null);
            questionContext.setPlayer(bestMatch);
        }
    }

    public Player findPlayer(QuestionContext questionContext, Integer playerId){
        assert(questionContext.getSport() != null);
        return rosters.get(questionContext.getSport()).getPlayer(playerId);
    }

    public void findPosition(QuestionContext questionContext) {
        assert(questionContext.getSport() != null);
        assert(questionContext.getTeam() != null);
        assert(questionContext.getCity() != null);

        throw new NotImplementedException();

    }

    public String determinePlayerPerformance(QuestionContext questionContext) throws ServerContactException, PlayerHasntPlayedException {
        PlayerGameStats pgs = getPlayerStatsObject(questionContext);
        ArrayList<StatCategory> statCategories = questionParser.determineStatCategories(questionContext);
        return pgs.getString(statCategories);
    }

    public void determinePositionInformation(QuestionContext questionContext) throws ServerContactException, PositionNotFoundException, GameNotFoundException {
        assertNotNull(questionContext.getPosition());

        ArrayList<LinkedHashMap> startingLineUp = getStartingLineup(questionContext);
        Integer playerId = null;

        for (LinkedHashMap position : startingLineUp) {
            if (position.get("position").equals(questionContext.getPosition().getAbbreviation())) {
                LinkedHashMap playerMap = (LinkedHashMap) position.get("player");
                playerId = Integer.parseInt((String) playerMap.get("ID"));
            }
        }

        if (playerId == null) {
            throw new PositionNotFoundException();
        } else {
            questionContext.setPlayer(findPlayer(questionContext, playerId));
        }
    }

    private ArrayList<LinkedHashMap> getStartingLineup(QuestionContext questionContext) throws ServerContactException, GameNotFoundException {
        assert(questionContext.getGame() != null);
        assert(questionContext.getGame().getId() != null);
        assert(questionContext.getTeam() != null);
        assert(questionContext.getTeam().getID() != null);

        ArrayList<LinkedHashMap<String, HashMap>>startingLineup = SportsApiService.getStartingLineup(questionContext);
        ArrayList lineupInQuestion = null;

        for(LinkedHashMap<String, HashMap> teamLineup: startingLineup){

            Integer myID = questionContext.getTeam().getID();
            Integer thisID = Integer.parseInt((String) teamLineup.get("team").get("ID"));

            if(Objects.equals(myID, thisID)){
                if(questionContext.getGame().getGameStatus() == GameStatus.SCHEDULED){
                    lineupInQuestion = (ArrayList) teamLineup.get("expected").get("starter");
                    break;
                }else{
                    lineupInQuestion = (ArrayList) teamLineup.get("actual").get("starter");
                    break;
                }
            }
        }

        if(lineupInQuestion == null){
            throw new GameNotFoundException();
        }
        return lineupInQuestion;
    }

    public PlayerGameStats getPlayerStatsObject(QuestionContext questionContext) throws ServerContactException, PlayerHasntPlayedException {
        assert(questionContext.getGame() != null);
        assert(questionContext.getGame().getId() != null);
        assert(questionContext.getPlayer() != null);
        assert(questionContext.getPlayer().getID() != null);

        ArrayList<LinkedHashMap> statsArray = SportsApiService.getPlayerStats(questionContext);
        LinkedHashMap<String, LinkedHashMap> stats = null;

        for(LinkedHashMap statsObject: statsArray){
            LinkedHashMap player = (LinkedHashMap) statsObject.get("player");

            if(Integer.parseInt((String) player.get("ID")) == questionContext.getPlayer().getID()){
                stats = (LinkedHashMap) statsObject.get("stats");
            }
        }

        PlayerGameStats pgs = questionContext.getSport().makeStat(questionContext.getPlayer());

        if(stats == null){
        }else{
            for(String stat: stats.keySet()){
                pgs.addStat(stat, (LinkedHashMap) stats.get(stat));
            }
        }

        return pgs;
    }
}
