package sportsbot.enums;

import java.util.Calendar;
import java.util.Locale;
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
        if(this ==TODAY || this == TOMORROW || this == YESTERDAY){
            return this.toString().toLowerCase();
        }


        Locale locale = Locale.getDefault();
        Calendar calendar = getCalendar();

        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        String day = Integer.toString(calendar.get(Calendar.DATE));
        String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale);

        StringBuilder sb = new StringBuilder();

        return sb.append(dayOfWeek)
                .append(" ")
                .append(month)
                .append(" ")
                .append(day)
                .append(", ")
                .append(year).toString();
    }

    private Calendar getCalendar(){
        TimeZone.setDefault(TimeZone.getTimeZone("America/Chicago"));
        // create a calendar
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DAY_OF_MONTH, offset);
        return cal;
    }


    public String getDateURLString(){
        Calendar cal = getCalendar();

        // get the value of all the calendar date fields.
        Integer year =  cal.get(Calendar.YEAR);
        Integer month =  cal.get(Calendar.MONTH);
        Integer day =  cal.get(Calendar.DATE);
        month++;

        if(cal.get(Calendar.HOUR_OF_DAY) < 3) {
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
