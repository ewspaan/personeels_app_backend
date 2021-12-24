package nl.spaan.personeels_app.payload.response;

import java.util.List;

public class DayResponse {

    private String day;
    private List<StandardRosterResponseSmall> times;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<StandardRosterResponseSmall> getTimes() {
        return times;
    }

    public void setTimes(List<StandardRosterResponseSmall> times) {
        this.times = times;
    }
}
