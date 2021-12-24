package nl.spaan.personeels_app.payload.response;

import java.util.List;

public class WeekRosterResponse {

    private Long id;
    private int year;
    private int week;
    private List<WeekDayResponse> weekDayResponses;


    public void setId(Long id) {
        this.id = id;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void setWeekDayResponses(List<WeekDayResponse> weekDayResponses) {
        this.weekDayResponses = weekDayResponses;
    }

    public Long getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public int getWeek() {
        return week;
    }

    public List<WeekDayResponse> getWeekDayResponses() {
        return weekDayResponses;
    }
}
