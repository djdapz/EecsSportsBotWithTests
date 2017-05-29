package sportsbot.model;

/**
 * Created by devondapuzzo on 5/15/17.
 */
public class Story {
    private String storyString;
    private String description;
    private String link;
//    public Story () {
//        this.storyString = null;
//        this.link = null;
//    }
    public Story (String storyString, String description, String link) {
        this.storyString = storyString;
        this.description = description;
        this.link = link;
    }
    public String getStoryString() {
        return storyString;
    }

    public void setStoryString(String storyString) {
        this.storyString = storyString;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString(){
        return storyString + "Source: " + link;
    }
}
