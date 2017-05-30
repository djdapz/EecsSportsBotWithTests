package sportsbot.enums;

import java.util.Calendar;
import java.util.TimeZone;

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

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getDateString() {
        return "hey";
    }


    public String getDateURLString(){
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
