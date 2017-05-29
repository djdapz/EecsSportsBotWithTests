package sportsbot.enums;

/**
 * Created by devondapuzzo on 4/25/17.
 */
public enum TemporalContext {
    TODAY(0),
    YESTERDAY(-1),
    TOMORROW(1),
    PAST,
    FUTURE;

    private int offset;

    TemporalContext(Integer offset){
        this.offset = offset;
    }

    TemporalContext(){
        this.offset = 0;
    }

    public int getOffset() {
        return offset;
    }
}
