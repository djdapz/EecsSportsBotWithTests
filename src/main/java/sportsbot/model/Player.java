package sportsbot.model;

import sportsbot.enums.Sport;

/**
 * Created by devondapuzzo on 4/12/17.
 */
public class Player {
    private int ID;
    private String LastName;
    private String FirstName;
    private int JerseyNumber;
    private String Position;
    private int Age;
    private boolean isRookie;
    private Team team;
    private Sport sport;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public int getJerseyNumber() {
        return JerseyNumber;
    }

    public void setJerseyNumber(int jerseyNumber) {
        JerseyNumber = jerseyNumber;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public boolean isRookie() {
        return isRookie;
    }

    public void setRookie(boolean rookie) {
        isRookie = rookie;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam(){
        return this.team;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Sport getSport() {
        return sport;
    }

    public String toString(){
        return getLastName() + ", " + getFirstName();
    }
}
