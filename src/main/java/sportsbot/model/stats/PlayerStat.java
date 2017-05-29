package sportsbot.model.stats;

/**
 * Created by devondapuzzo on 5/28/17.
 */
public class PlayerStat {
    private String abbreviation;
    private String name;
    private Integer value;

    public PlayerStat(String name, String abbreviation, Integer value) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.value = value;
    }

    public PlayerStat(String name, String abbreviation, String value) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.value = Math.round(Float.parseFloat(value));
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = Integer.parseInt(value);

    }

    public String getName() {
        return name;
    }
}
