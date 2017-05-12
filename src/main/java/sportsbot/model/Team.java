package sportsbot.model;

import sportsbot.enums.Sport;

import java.util.HashMap;

/**
 * Created by devondapuzzo on 4/19/17.
 */
public class Team {

    private int ID;
    private City city;
    private String name;
    private String Abbreviation;
    private HashMap<String, Player> players;
    private Sport sport;

    public Team() {
        players = new HashMap<String, Player>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAbbreviation() {
        return Abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        Abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public HashMap getPlayers(){
        return players;
    }

    public void addPlayer(Player player){
        String key = player.getLastName()+", "+player.getFirstName();
        if(!players.containsKey(key)){
            players.put(key, player);
        };
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Sport getSport() {
        return sport;
    }

    public String toString(){
        return city.getName() + " " + name;
    }
}
