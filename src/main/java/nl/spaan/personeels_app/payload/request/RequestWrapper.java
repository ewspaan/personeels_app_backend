package nl.spaan.personeels_app.payload.request;

import nl.spaan.personeels_app.model.Day;

import java.util.List;

public class RequestWrapper {

    Day day;
    List<RosterRequest> rosterRequestList;

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public List<RosterRequest> getRosterRequestList() {
        return rosterRequestList;
    }

    public void setRosterRequestList(List<RosterRequest> rosterRequestList) {
        this.rosterRequestList = rosterRequestList;
    }
}
